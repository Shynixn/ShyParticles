package com.github.shynixn.shyparticles.impl

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.api.packet.PacketOutParticle
import com.github.shynixn.shyparticles.contract.ParticleEffect
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import com.github.shynixn.shyparticles.entity.ParticleLayer
import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.enumeration.ParticleModifierType
import com.github.shynixn.shyparticles.enumeration.ParticleShapeType
import com.github.shynixn.shyparticles.impl.modifier.ParticleModifierOffsetImpl
import com.github.shynixn.shyparticles.impl.modifier.ParticleModifierPulseImpl
import com.github.shynixn.shyparticles.impl.modifier.ParticleModifierRandomImpl
import com.github.shynixn.shyparticles.impl.modifier.ParticleModifierRelativeTranslateAbsoluteImpl
import com.github.shynixn.shyparticles.impl.modifier.ParticleModifierRelativeTranslateImpl
import com.github.shynixn.shyparticles.impl.modifier.ParticleModifierRotationImpl
import com.github.shynixn.shyparticles.impl.modifier.ParticleModifierTranslateImpl
import com.github.shynixn.shyparticles.impl.modifier.ParticleModifierTranslateAbsoluteImpl
import com.github.shynixn.shyparticles.impl.modifier.ParticleModifierWaveImpl
import com.github.shynixn.shyparticles.impl.shape.ParticleCircleShapeImpl
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleEffectImpl(
    override val id: String,
    private val effectMeta: ParticleEffectMeta,
    val locationRef: () -> Location,
    override val player: Player?,
    private val plugin: Plugin,
    private val packetService: PacketService
) : ParticleEffect {

    private var job: Job? = null
    private var running = false
    private val modifierRotation = ParticleModifierRotationImpl()
    private val modifierPulse = ParticleModifierPulseImpl()
    private val modifierRandom = ParticleModifierRandomImpl()
    private val modifierOffSet = ParticleModifierOffsetImpl()
    private val modifierWave = ParticleModifierWaveImpl()
    private val modifierTranslate = ParticleModifierTranslateImpl()
    private val modifierTranslateAbsolute = ParticleModifierTranslateAbsoluteImpl()
    private val modifierRelativeTranslateAbsolute = ParticleModifierRelativeTranslateAbsoluteImpl()
    private val modifierRelativeTranslate = ParticleModifierRelativeTranslateImpl()

    // Shapes
    private val shapeCircle = ParticleCircleShapeImpl()
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
        val durationMillis = if (effectMeta.duration > 0) effectMeta.duration * 1000L else Long.MAX_VALUE

        // Play sounds at start
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
            }
        }
    }

    private fun renderLayer(layer: ParticleLayer, baseLocation: Location, tickCount: Long) {
        val options = layer.options

        // Apply transform_absolute modifiers to the base location
        val effectiveBaseLocation = baseLocation.clone()
        for (modifier in layer.modifiers) {
            if (modifier.type == ParticleModifierType.TRANSFORM_ABSOLUTE) {
                val offset = modifierTranslateAbsolute.applyTranslateAbsolute(modifier, tickCount)
                effectiveBaseLocation.add(offset)
            } else if (modifier.type == ParticleModifierType.RELATIVE_TRANSFORM_ABSOLUTE) {
                val offset =
                    modifierRelativeTranslateAbsolute.applyRelativeTransformAbsolute(modifier, tickCount, baseLocation)
                effectiveBaseLocation.add(offset)
            }
        }

        val points = generateShapePoints(layer.shape, options, tickCount)

        // Apply modifiers to each point (excluding transform_absolute)
        val modifiedPoints = points.map { point ->
            applyModifiers(point, layer.modifiers, tickCount, options)
        }

        for (point in modifiedPoints) {
            val particleLocation = effectiveBaseLocation.clone().add(point)
            spawnParticle(layer.particle, particleLocation, options, layer.modifiers, tickCount)
        }
    }

    private fun applyModifiers(
        point: Vector,
        modifiers: List<ParticleModifier>,
        tickCount: Long,
        options: com.github.shynixn.shyparticles.entity.ParticleOptions
    ): Vector {
        var modifiedPoint = point.clone()

        for (modifier in modifiers) {
            when (modifier.type) {
                ParticleModifierType.ROTATE -> {
                    modifiedPoint = modifierRotation.applyRotation(modifiedPoint, modifier, tickCount)
                }

                ParticleModifierType.WAVE -> {
                    modifiedPoint = modifierWave.applyWave(modifiedPoint, modifier, tickCount)
                }

                ParticleModifierType.PULSE -> {
                    modifiedPoint = modifierPulse.applyPulse(modifiedPoint, modifier, tickCount)
                }

                ParticleModifierType.OFFSET -> {
                    modifiedPoint = modifierOffSet.applyOffset(modifiedPoint, modifier, tickCount)
                }

                ParticleModifierType.RANDOM -> {
                    modifiedPoint = modifierRandom.applyRandom(modifiedPoint, modifier)
                }

                ParticleModifierType.TRANSFORM -> {
                    modifiedPoint = modifierTranslate.applyTranslate(modifiedPoint, modifier, tickCount)
                }

                ParticleModifierType.RELATIVE_TRANSFORM -> {
                    modifiedPoint = modifierRelativeTranslate.applyRelativeTransform(
                        modifiedPoint,
                        modifier,
                        tickCount,
                        locationRef()
                    )
                }

                else -> {}
            }
        }

        return modifiedPoint
    }


    private fun generateShapePoints(
        shape: ParticleShapeType,
        options: com.github.shynixn.shyparticles.entity.ParticleOptions,
        tickCount: Long
    ): Sequence<Vector> {
        return sequence {
            val density = options.density.coerceIn(0.1, 1.0)
            val pointCount = (options.particleCount * density).toInt().coerceAtLeast(1)

            when (shape) {
                ParticleShapeType.CIRCLE -> {
                    yieldAll(shapeCircle.circleShape(density, pointCount, tickCount, options))
                }

                ParticleShapeType.SPHERE -> {
                    val phiSteps = (pointCount * 0.5).toInt().coerceAtLeast(2)
                    val thetaSteps = pointCount / phiSteps
                    for (i in 0 until phiSteps) {
                        val phi = PI * i / phiSteps
                        for (j in 0 until thetaSteps) {
                            val theta = 2 * PI * j / thetaSteps
                            val x = options.radius * sin(phi) * cos(theta) + options.offsetX
                            val y = options.radius * cos(phi) + options.offsetY
                            val z = options.radius * sin(phi) * sin(theta) + options.offsetZ
                            yield(Vector(x, y, z))
                        }
                    }
                }

                ParticleShapeType.SPIRAL -> {
                    val totalPoints = pointCount * options.turns
                    for (i in 0 until totalPoints) {
                        val angle = (2 * PI * options.turns * i / totalPoints) + (tickCount * 0.05)
                        val heightProgress = options.height * i / totalPoints
                        val x = options.radius * cos(angle) + options.offsetX
                        val y = heightProgress + options.offsetY
                        val z = options.radius * sin(angle) + options.offsetZ
                        yield(Vector(x, y, z))
                    }
                }

                ParticleShapeType.LINE -> {
                    for (i in 0 until pointCount) {
                        val progress = i.toDouble() / pointCount
                        val y = options.height * progress + options.offsetY
                        yield(Vector(options.offsetX, y, options.offsetZ))
                    }
                }

                ParticleShapeType.RECTANGLE -> {
                    val perimeter = 2 * (options.width + options.length)
                    val spacing = perimeter / pointCount
                    var distance = 0.0
                    for (i in 0 until pointCount) {
                        val (x, z) = when {
                            distance < options.width -> Pair(distance - options.width / 2, -options.length / 2)
                            distance < options.width + options.length -> Pair(
                                options.width / 2,
                                distance - options.width - options.length / 2
                            )

                            distance < 2 * options.width + options.length -> Pair(
                                options.width / 2 - (distance - options.width - options.length),
                                options.length / 2
                            )

                            else -> Pair(
                                -options.width / 2,
                                options.length / 2 - (distance - 2 * options.width - options.length)
                            )
                        }
                        yield(Vector(x + options.offsetX, options.offsetY, z + options.offsetZ))
                        distance += spacing
                    }
                }

                ParticleShapeType.CUBE -> {
                    val pointsPerEdge = (pointCount / 12.0).toInt().coerceAtLeast(1)
                    // Bottom face edges
                    for (i in 0 until pointsPerEdge) {
                        val t = i.toDouble() / pointsPerEdge
                        yield(Vector(-options.width / 2 + t * options.width, -options.height / 2, -options.depth / 2))
                        yield(Vector(-options.width / 2 + t * options.width, -options.height / 2, options.depth / 2))
                        yield(Vector(-options.width / 2, -options.height / 2, -options.depth / 2 + t * options.depth))
                        yield(Vector(options.width / 2, -options.height / 2, -options.depth / 2 + t * options.depth))
                    }
                }

                ParticleShapeType.HEART -> {
                    for (i in 0 until pointCount) {
                        val t = 2 * PI * i / pointCount
                        val x = options.radius * 16 * sin(t) * sin(t) * sin(t) / 16 + options.offsetX
                        val y =
                            options.radius * (13 * cos(t) - 5 * cos(2 * t) - 2 * cos(3 * t) - cos(4 * t)) / 16 + options.offsetY
                        yield(Vector(x, y, options.offsetZ))
                    }
                }

                ParticleShapeType.STAR -> {
                    for (i in 0 until pointCount) {
                        val angle = (2 * PI * i / pointCount) + (tickCount * 0.05)
                        val radius = if (i % 2 == 0) options.radius else options.radius / 2
                        val x = radius * cos(angle) + options.offsetX
                        val z = radius * sin(angle) + options.offsetZ
                        yield(Vector(x, options.offsetY, z))
                    }
                }

                ParticleShapeType.POINT -> {
                    yield(Vector(options.offsetX, options.offsetY, options.offsetZ))
                }

                ParticleShapeType.RANDOM -> {
                    for (i in 0 until pointCount) {
                        val x = (Math.random() - 0.5) * 2 * options.radius + options.offsetX
                        val y = (Math.random() - 0.5) * 2 * options.height + options.offsetY
                        val z = (Math.random() - 0.5) * 2 * options.radius + options.offsetZ
                        yield(Vector(x, y, z))
                    }
                }
            }
        }
    }

    private fun spawnParticle(
        particleName: String,
        location: Location,
        options: com.github.shynixn.shyparticles.entity.ParticleOptions,
        modifiers: List<ParticleModifier> = emptyList(),
        tickCount: Long = 0
    ) {
        // Calculate fade modifier effect on alpha if present
        var effectiveAlpha = options.alpha
        for (modifier in modifiers) {
            if (modifier.type == ParticleModifierType.FADE) {
                val fadeProgress = (tickCount * 0.05 / modifier.fadeTime).coerceIn(0.0, 1.0)
                effectiveAlpha =
                    (modifier.startAlpha + (modifier.endAlpha - modifier.startAlpha) * fadeProgress * 255).toInt()
            }
        }

        val packet = PacketOutParticle(
            name = particleName,
            location = location,
            offset = Vector(options.extra, options.extra, options.extra),  // Use extra for particle spread
            speed = options.speed,
            count = 1,
            // Color properties for DUST, ENTITY_EFFECT, DUST_COLOR_TRANSITION
            fromRed = options.red,
            fromGreen = options.green,
            fromBlue = options.blue,
            fromAlpha = effectiveAlpha,
            toRed = options.toRed,
            toGreen = options.toGreen,
            toBlue = options.toBlue,
            toAlpha = options.toAlpha,
            // Scale for DUST particles
            scale = options.scale,
            // Roll for SCULK_CHARGE particles
            roll = options.roll,
            // Delay for SHRIEK particles
            delay = options.delay,
            // Vibration properties
            vibrationLocation = location,
            vibrationTicks = options.vibrationTicks
        )

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
            val currentLocation = locationRef()
            val world = currentLocation.world ?: continue

            if (player != null) {
                player.playSound(currentLocation, sound.sound, sound.volume, sound.pitch)
            } else {
                world.playSound(currentLocation, sound.sound, sound.volume, sound.pitch)
            }
        }
    }

    override fun close() {
        running = false
        job?.cancel()
    }
}