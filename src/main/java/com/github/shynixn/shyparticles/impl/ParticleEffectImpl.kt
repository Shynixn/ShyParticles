package com.github.shynixn.shyparticles.impl

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.api.packet.PacketOutParticle
import com.github.shynixn.shyparticles.contract.ParticleEffect
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import com.github.shynixn.shyparticles.entity.ParticleLayer
import com.github.shynixn.shyparticles.enumeration.ParticleShape
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
        val shape = try {
            ParticleShape.valueOf(layer.shape.uppercase())
        } catch (e: Exception) {
            ParticleShape.CIRCLE
        }

        val points = generateShapePoints(shape, options, tickCount)

        for (point in points) {
            val particleLocation = baseLocation.clone().add(point)
            spawnParticle(layer.particle, particleLocation, options)
        }
    }

    private fun generateShapePoints(
        shape: ParticleShape,
        options: com.github.shynixn.shyparticles.entity.ParticleOptions,
        tickCount: Long
    ): List<Vector> {
        val points = mutableListOf<Vector>()
        val density = options.density.coerceIn(0.1, 1.0)
        val pointCount = (options.particleCount * density).toInt().coerceAtLeast(1)

        when (shape) {
            ParticleShape.CIRCLE -> {
                for (i in 0 until pointCount) {
                    val angle = (2 * PI * i / pointCount) + (tickCount * 0.05)
                    val x = options.radius * cos(angle) + options.offsetX
                    val z = options.radius * sin(angle) + options.offsetZ
                    points.add(Vector(x, options.offsetY, z))
                }
            }

            ParticleShape.SPHERE -> {
                val phiSteps = (pointCount * 0.5).toInt().coerceAtLeast(2)
                val thetaSteps = pointCount / phiSteps
                for (i in 0 until phiSteps) {
                    val phi = PI * i / phiSteps
                    for (j in 0 until thetaSteps) {
                        val theta = 2 * PI * j / thetaSteps
                        val x = options.radius * sin(phi) * cos(theta) + options.offsetX
                        val y = options.radius * cos(phi) + options.offsetY
                        val z = options.radius * sin(phi) * sin(theta) + options.offsetZ
                        points.add(Vector(x, y, z))
                    }
                }
            }

            ParticleShape.SPIRAL -> {
                val totalPoints = pointCount * options.turns
                for (i in 0 until totalPoints) {
                    val angle = (2 * PI * options.turns * i / totalPoints) + (tickCount * 0.05)
                    val heightProgress = options.height * i / totalPoints
                    val x = options.radius * cos(angle) + options.offsetX
                    val y = heightProgress + options.offsetY
                    val z = options.radius * sin(angle) + options.offsetZ
                    points.add(Vector(x, y, z))
                }
            }

            ParticleShape.LINE -> {
                for (i in 0 until pointCount) {
                    val progress = i.toDouble() / pointCount
                    val y = options.height * progress + options.offsetY
                    points.add(Vector(options.offsetX, y, options.offsetZ))
                }
            }

            ParticleShape.RECTANGLE -> {
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
                    points.add(Vector(x + options.offsetX, options.offsetY, z + options.offsetZ))
                    distance += spacing
                }
            }

            ParticleShape.CUBE -> {
                val pointsPerEdge = (pointCount / 12.0).toInt().coerceAtLeast(1)
                // Bottom face edges
                for (i in 0 until pointsPerEdge) {
                    val t = i.toDouble() / pointsPerEdge
                    points.add(Vector(-options.width / 2 + t * options.width, -options.height / 2, -options.depth / 2))
                    points.add(Vector(-options.width / 2 + t * options.width, -options.height / 2, options.depth / 2))
                    points.add(Vector(-options.width / 2, -options.height / 2, -options.depth / 2 + t * options.depth))
                    points.add(Vector(options.width / 2, -options.height / 2, -options.depth / 2 + t * options.depth))
                }
            }

            ParticleShape.HEART -> {
                for (i in 0 until pointCount) {
                    val t = 2 * PI * i / pointCount
                    val x = options.radius * 16 * sin(t) * sin(t) * sin(t) / 16 + options.offsetX
                    val y =
                        options.radius * (13 * cos(t) - 5 * cos(2 * t) - 2 * cos(3 * t) - cos(4 * t)) / 16 + options.offsetY
                    points.add(Vector(x, y, options.offsetZ))
                }
            }

            ParticleShape.STAR -> {
                for (i in 0 until pointCount) {
                    val angle = (2 * PI * i / pointCount) + (tickCount * 0.05)
                    val radius = if (i % 2 == 0) options.radius else options.radius / 2
                    val x = radius * cos(angle) + options.offsetX
                    val z = radius * sin(angle) + options.offsetZ
                    points.add(Vector(x, options.offsetY, z))
                }
            }

            ParticleShape.POINT -> {
                points.add(Vector(options.offsetX, options.offsetY, options.offsetZ))
            }

            ParticleShape.RANDOM -> {
                for (i in 0 until pointCount) {
                    val x = (Math.random() - 0.5) * 2 * options.radius + options.offsetX
                    val y = (Math.random() - 0.5) * 2 * options.height + options.offsetY
                    val z = (Math.random() - 0.5) * 2 * options.radius + options.offsetZ
                    points.add(Vector(x, y, z))
                }
            }
        }

        return points
    }

    private fun spawnParticle(
        particleName: String,
        location: Location,
        options: com.github.shynixn.shyparticles.entity.ParticleOptions
    ) {
        val packet = PacketOutParticle(
            name = particleName,
            location = location,
            offset = Vector(0.1, 0.1, 0.1),  // Small offset for particle spread
            speed = options.speed,
            count = 1,
            // Color properties for DUST, ENTITY_EFFECT, DUST_COLOR_TRANSITION
            fromRed = options.red,
            fromGreen = options.green,
            fromBlue = options.blue,
            fromAlpha = options.alpha,
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