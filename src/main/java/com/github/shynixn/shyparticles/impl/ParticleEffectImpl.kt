package com.github.shynixn.shyparticles.impl

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mccoroutine.folia.ticks
import com.github.shynixn.mcutils.common.item.ItemService
import com.github.shynixn.mcutils.packet.api.MaterialService
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.api.packet.PacketOutParticle
import com.github.shynixn.shyparticles.contract.ParticleEffect
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import com.github.shynixn.shyparticles.entity.ParticleLayer
import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.entity.ParticleOptions
import com.github.shynixn.shyparticles.enumeration.ParticleModifierType
import com.github.shynixn.shyparticles.enumeration.ParticleShapeType
import com.github.shynixn.shyparticles.impl.modifier.*
import com.github.shynixn.shyparticles.impl.modifier.ParticleModifierPulseImpl
import com.github.shynixn.shyparticles.impl.shape.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class ParticleEffectImpl(
    override val id: String,
    private val effectMeta: ParticleEffectMeta,
    val locationRef: () -> Location,
    override val player: Player?,
    private val plugin: Plugin,
    private val packetService: PacketService,
    private val materialService: MaterialService,
    private val itemService: ItemService
) : ParticleEffect {
    companion object {
        private val modifiers = mapOf(
            ParticleModifierType.ROTATE to ParticleModifierRotationImpl(),
            ParticleModifierType.PULSE to ParticleModifierPulseImpl(),
            ParticleModifierType.RANDOM to ParticleModifierRandomImpl(),
            ParticleModifierType.OFFSET to ParticleModifierOffsetImpl(),
            ParticleModifierType.WAVE to ParticleModifierWaveImpl(),
            ParticleModifierType.TRANSFORM to ParticleModifierTranslateImpl(),
            ParticleModifierType.TRANSFORM_ABSOLUTE to ParticleModifierTranslateAbsoluteImpl(),
            ParticleModifierType.RELATIVE_TRANSFORM to ParticleModifierRelativeTranslateImpl(),
            ParticleModifierType.RELATIVE_TRANSFORM_ABSOLUTE to ParticleModifierRelativeTranslateAbsoluteImpl()
        )
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
    }

    private var job: Job? = null
    private var running = false
    override val startTime: Long = System.currentTimeMillis()

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
        val startTime = System.currentTimeMillis()
        val durationMillis = if (effectMeta.durationSec > 0) effectMeta.durationSec * 1000L else Long.MAX_VALUE

        playSounds()

        var tickCount = 0L
        while (running && (System.currentTimeMillis() - startTime) < durationMillis) {
            val currentLocation = locationRef()

            // Render each layer
            for (layer in effectMeta.layers) {
                renderLayer(layer, currentLocation, tickCount)
            }

            tickCount++
            delay(50) // 1 tick = 50ms (20 ticks per second)

            // Check if effect finished and should repeat
            if (!effectMeta.repeat && (System.currentTimeMillis() - startTime) >= durationMillis) {
                break
            }

            // Reset tick count if repeating
            if (effectMeta.repeat && (System.currentTimeMillis() - startTime) >= durationMillis) {
                tickCount = 0
                playSounds()
            }
        }
    }

    private fun renderLayer(layer: ParticleLayer, baseLocation: Location, tickCount: Long) {
        val options = layer.options
        val location = locationRef()

        // Apply transform_absolute modifiers to the base location
        val effectiveBaseLocation = baseLocation.clone()
        for (modifier in layer.modifiers) {
            if (modifier.type == ParticleModifierType.TRANSFORM_ABSOLUTE) {
                val offset = modifiers[ParticleModifierType.TRANSFORM_ABSOLUTE]!!.apply(
                    Vector(),
                    modifier,
                    tickCount,
                    locationRef()
                )
                effectiveBaseLocation.add(offset)
            } else if (modifier.type == ParticleModifierType.RELATIVE_TRANSFORM_ABSOLUTE) {
                val offset = modifiers[ParticleModifierType.RELATIVE_TRANSFORM_ABSOLUTE]!!.apply(
                    Vector(),
                    modifier,
                    tickCount,
                    locationRef()
                )
                effectiveBaseLocation.add(offset)
            }
        }

        val points = generateShapePoints(layer.shape, options, tickCount, location)

        // Apply modifiers to each point (excluding transform_absolute)
        val modifiedPoints = points.map { point ->
            applyModifiers(point, layer.modifiers, tickCount)
        }

        for (point in modifiedPoints) {
            val particleLocation = effectiveBaseLocation.clone().add(point)
            spawnParticle(layer.particle, particleLocation, options)
        }
    }

    private fun applyModifiers(
        point: Vector,
        modifierActions: List<ParticleModifier>,
        tickCount: Long,
    ): Vector {
        var modifiedPoint = point.clone()
        val location = locationRef()
        for (modifier in modifierActions) {
            modifiedPoint = modifiers[modifier.type]!!.apply(modifiedPoint, modifier, tickCount, location)
        }

        return modifiedPoint
    }

    private fun generateShapePoints(
        shapeType: ParticleShapeType,
        options: com.github.shynixn.shyparticles.entity.ParticleOptions,
        tickCount: Long,
        location: Location
    ): Sequence<Vector> {
        return sequence {
            val density = options.density.coerceIn(0.1, 1.0)
            val pointCount = (options.particleCount * density).toInt().coerceAtLeast(1)
            val shape = shapes[shapeType]!!
            yieldAll(shape.apply(density, pointCount, tickCount, options).map { vector -> applyDirectionalOffsets(vector, options, location) })
        }
    }

    private fun applyDirectionalOffsets(vector: Vector, options: ParticleOptions, location: Location): Vector {
        if (options.forwardOffset == 0.0 && options.sidewardOffset == 0.0) {
            return vector
        }

        val yaw = Math.toRadians(location.yaw.toDouble())
        val forwardX = -sin(yaw) * options.forwardOffset
        val forwardZ = cos(yaw) * options.forwardOffset
        val sidewardX = cos(yaw) * options.sidewardOffset
        val sidewardZ = sin(yaw) * options.sidewardOffset

        return vector.clone().add(Vector(forwardX + sidewardX, 0.0, forwardZ + sidewardZ))
    }

    private fun spawnParticle(
        particleName: String,
        location: Location,
        options: com.github.shynixn.shyparticles.entity.ParticleOptions,
    ) {
        val packet = PacketOutParticle(
            name = particleName,
            location = location,
            offset = Vector(
                options.spreadOffSetX,
                options.spreadOffSetY,
                options.spreadOffSetZ
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

        if (player != null) {
            packetService.sendPacketOutParticle(player, packet)
        } else {
            // Send to all players in range
            val world = location.world ?: return
            for (p in world.players) {
                if (p.location.distance(location) <= 64.0) {
                    packetService.sendPacketOutParticle(p, packet)
                }
            }
        }
    }

    private fun playSounds() {
        for (sound in effectMeta.sounds) {
            plugin.launch {
                delay(sound.delayTicks.ticks)
                val currentLocation = locationRef()
                val world = currentLocation.world
                if(world != null){
                    if (player != null) {
                        player.playSound(currentLocation, sound.sound, sound.volume, sound.pitch)
                    } else {
                        world.playSound(currentLocation, sound.sound, sound.volume, sound.pitch)
                    }
                }
            }
        }
    }

    override fun close() {
        running = false
        job?.cancel()
    }
}