package com.github.shynixn.shyparticles.impl

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.ticks
import com.github.shynixn.mcutils.common.item.ItemService
import com.github.shynixn.mcutils.packet.api.MaterialService
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.api.packet.PacketOutParticle
import com.github.shynixn.shyparticles.contract.ParticleEffect
import com.github.shynixn.shyparticles.contract.ParticlePointModifier
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import com.github.shynixn.shyparticles.entity.ParticleLayer
import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.entity.ParticleOptions
import com.github.shynixn.shyparticles.entity.ShyParticlesSettings
import com.github.shynixn.shyparticles.enumeration.ParticleModifierType
import com.github.shynixn.shyparticles.enumeration.ParticleShapeType
import com.github.shynixn.shyparticles.impl.modifier.*
import com.github.shynixn.shyparticles.impl.modifier.ParticlePointModifierPulseImpl
import com.github.shynixn.shyparticles.impl.shape.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector
import java.util.Locale

class ParticleEffectImpl(
    override val id: String,
    private val effectMeta: ParticleEffectMeta,
    val locationRef: () -> Location,
    override val player: Player?,
    plugin: Plugin,
    private val packetService: PacketService,
    private val materialService: MaterialService,
    private val itemService: ItemService,
    settings: ShyParticlesSettings
) : ParticleEffect {
    private val stateLessModifiers = mapOf(
        ParticleModifierType.PULSE to ParticlePointModifierPulseImpl(),
        ParticleModifierType.RANDOM to ParticlePointModifierRandomImpl(),
        ParticleModifierType.WAVE to ParticlePointModifierWaveImpl(),
        ParticleModifierType.OSCILLATE to ParticlePointModifierOscillateImpl(),
    )
    private var stateFullModifiers: Map<ParticleModifierType, ParticlePointModifier> =
        emptyMap()
    private val shapes = mapOf(
        ParticleShapeType.CIRCLE to ParticleCircleShapeImpl(),
        ParticleShapeType.CUBE to ParticleCubeShapeImpl(),
        ParticleShapeType.HEART to ParticleHeartShapeImpl(),
        ParticleShapeType.LINE to ParticleLineShapeImpl(),
        ParticleShapeType.POINT to ParticlePointShapeImpl(),
        ParticleShapeType.RANDOM to ParticleRandomShapeImpl(),
        ParticleShapeType.RECTANGLE to ParticleRectangleShapeImpl(),
        ParticleShapeType.SPHERE to ParticleSphereShapeImpl(),
        ParticleShapeType.SPIRAL to ParticleSpiralShapeImpl(),
        ParticleShapeType.STAR to ParticleStarShapeImpl(),
    )
    private val visiblePermission = settings.effectVisiblePermission + effectMeta.name.lowercase(Locale.ENGLISH)
    private var job: Job? = null
    private var running = false

    /** Name of the effect template. */
    override val name: String
        get() = effectMeta.name

    /** Location where the effect is playing. */
    override val location: Location
        get() = locationRef()

    /** Whether this effect is currently running. */
    override val isRunning: Boolean
        get() = running

    init {
        // Start the coroutine to play particles
        job = plugin.launch {
            running = true
            try {
                playEffect()
            } finally {
                running = false
            }
        }
    }

    private suspend fun playEffect() {
        val finishTicks = if (effectMeta.duration > 0) {
            effectMeta.duration
        } else {
            Long.MAX_VALUE
        }
        var tickCount = 0L
        val layersAndOptions: MutableList<Pair<ParticleLayer, ParticleOptions>> = ArrayList()
        reset(layersAndOptions)

        while (running) {
            val currentLocation = locationRef().clone()
            val players = getPlayersInRange(currentLocation)

            if (players.isNotEmpty()) {
                // Render each layer
                for (pair in layersAndOptions) {
                    if (pair.second.skip == 0 || tickCount % pair.second.skip == 0L) {
                        renderLayer(pair.first, pair.second, currentLocation, tickCount, players)
                    }
                }

                playSounds(currentLocation, tickCount)
            }

            tickCount++
            delay(1.ticks) // 1 tick = 50ms (20 ticks per second)
            // Check if effect finished and should repeat
            if (!effectMeta.repeat && tickCount >= finishTicks) {
                break
            }

            // Reset tick count if repeating
            if (effectMeta.repeat && tickCount >= finishTicks) {
                tickCount = 0
                reset(layersAndOptions)
            }
        }
    }

    private fun getPlayersInRange(baseLocation: Location): Set<Player> {
        val selection = HashSet<Player>()

        if (player != null) {
            selection.add(player)
        } else {
            val world = baseLocation.world

            if (world != null) {
                selection.addAll(world.players)
            }
        }

        val result = HashSet<Player>()

        for (player in selection) {
            if (player.location.distance(baseLocation) <= effectMeta.range) {
                if (player.hasPermission(visiblePermission)) {
                    result.add(player)
                }
            }
        }

        return result
    }

    private fun reset(layersAndOptions: MutableList<Pair<ParticleLayer, ParticleOptions>>) {
        stateFullModifiers = mapOf(
            ParticleModifierType.MOVE to ParticlePointModifierMoveImpl(),
            ParticleModifierType.ROTATE to ParticlePointModifierRotationImpl(),
        )
        layersAndOptions.clear()
        layersAndOptions.addAll(effectMeta.layers.map { Pair(it, it.options.copy()) })
    }

    private fun renderLayer(
        layer: ParticleLayer,
        options: ParticleOptions,
        baseLocation: Location,
        tickCount: Long,
        players: Set<Player>
    ) {
        val points = generateShapePoints(layer.shape, options)

        // Apply modifiers to each point (excluding transform_absolute)
        val modifiedPoints = points.map { point ->
            applyPointModifiers(point, baseLocation, options, layer.modifiers, tickCount)
        }

        for (point in modifiedPoints) {
            val particleLocation = baseLocation.clone().add(point)
            spawnParticle(layer.particle, particleLocation, options, players)
        }
    }

    private fun applyPointModifiers(
        point: Vector,
        baseLocation: Location,
        options: ParticleOptions,
        modifierActions: List<ParticleModifier>,
        tickCount: Long,
    ): Vector {
        var modifiedPoint = point.clone()
        for (modifier in modifierActions) {
            // Only apply modifier if its delay has elapsed
            if (tickCount >= modifier.start && tickCount <= modifier.end) {
                val stateLessFun = stateLessModifiers[modifier.type]
                if (stateLessFun != null) {
                    modifiedPoint = stateLessFun.apply(modifiedPoint, modifier, tickCount, baseLocation)
                }

                if (modifier.type == ParticleModifierType.OPTIONS_SET) {
                    modifier.options?.copy(options)
                } else if (modifier.type == ParticleModifierType.OPTIONS_ADD) {
                    modifier.options?.copyAdd(options)
                }
            }

            val stateFullFun = stateFullModifiers[modifier.type]
            if (stateFullFun != null) {
                modifiedPoint = stateFullFun.apply(modifiedPoint, modifier, tickCount, baseLocation)
            }
        }

        return modifiedPoint
    }

    private fun generateShapePoints(
        shapeType: ParticleShapeType,
        options: ParticleOptions,
    ): Sequence<Vector> {
        return sequence {
            val density = options.density.coerceIn(0.1, 1.0)
            val pointCount = (options.particleCount * density).toInt().coerceAtLeast(1)
            val shape = shapes[shapeType]!!
            yieldAll(shape.apply(density, pointCount, options))
        }
    }

    private fun spawnParticle(
        particleName: String,
        location: Location,
        options: ParticleOptions,
        players: Set<Player>
    ) {
        val packet = PacketOutParticle(
            name = particleName,
            location = location,
            offset = Vector(
                options.spreadX, options.spreadY, options.spreadZ
            ),  // Use extra for particle spread
            speed = options.speed,
            count = options.count,
            fromRed = options.fromRed,
            fromGreen = options.fromGreen,
            fromBlue = options.fromBlue,
            fromAlpha = options.fromAlpha,
            toRed = options.toRed,
            toGreen = options.toGreen,
            toBlue = options.toBlue,
            toAlpha = options.toAlpha,
            scale = options.scale,
            roll = options.roll,
            delay = options.delay,
            vibrationLocation = location,
            vibrationTicks = options.vibrationTicks
        )

        if (options.item != null) {
            packet.material = materialService.findMaterialFromName(options.item!!.typeName)
            packet.item = itemService.toItemStack(options.item!!)
        }

        for (player in players) {
            packetService.sendPacketOutParticle(player, packet)
        }
    }

    private fun playSounds(baseLocation: Location, tickCount: Long) {
        for (sound in effectMeta.sounds) {
            if (tickCount < sound.startTick || tickCount > sound.startTick) {
                continue
            }

            val world = baseLocation.world
            if (world != null) {
                if (player != null) {
                    player.playSound(baseLocation, sound.sound, sound.volume, sound.pitch)
                } else {
                    world.playSound(baseLocation, sound.sound, sound.volume, sound.pitch)
                }
            }
        }
    }

    override fun close() {
        running = false
        job?.cancel()
    }
}
