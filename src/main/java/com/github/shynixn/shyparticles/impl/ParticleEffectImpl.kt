package com.github.shynixn.shyparticles.impl

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.api.packet.PacketOutParticle
import com.github.shynixn.shyparticles.contract.ParticleEffect
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import com.github.shynixn.shyparticles.entity.ParticleLayer
import com.github.shynixn.shyparticles.entity.ParticleModifier
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

        // Apply transform_absolute modifiers to the base location
        var effectiveBaseLocation = baseLocation.clone()
        for (modifier in layer.modifiers) {
            if (modifier.type.lowercase() == "transform_absolute") {
                val offset = applyTransformAbsolute(modifier, tickCount)
                effectiveBaseLocation.add(offset)
            }
            else if (modifier.type.lowercase() == "reltransform_absolute") {
                val offset = ParticleUtils.applyRelativeTransformAbsolute(modifier, tickCount, baseLocation)
                effectiveBaseLocation.add(offset)
            }
        }

        val points = generateShapePoints(shape, options, tickCount)

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
            when (modifier.type.lowercase()) {
                "rotate" -> {
                    modifiedPoint = applyRotation(modifiedPoint, modifier, tickCount)
                }
                "wave" -> {
                    modifiedPoint = applyWave(modifiedPoint, modifier, tickCount)
                }
                "pulse" -> {
                    modifiedPoint = applyPulse(modifiedPoint, modifier, tickCount)
                }
                "offset" -> {
                    modifiedPoint = applyOffset(modifiedPoint, modifier, tickCount)
                }
                "random" -> {
                    modifiedPoint = applyRandom(modifiedPoint, modifier)
                }
                "transform" -> {
                    modifiedPoint = applyTransform(modifiedPoint, modifier, tickCount)
                }
                "reltransform" -> {
                    modifiedPoint = ParticleRelTransform.applyRelativeTransform(modifiedPoint, modifier, tickCount, locationRef())
                }
            }
        }

        return modifiedPoint
    }

    private fun applyRotation(point: Vector, modifier: ParticleModifier, tickCount: Long): Vector {
        val angle = tickCount * 0.05 * modifier.speed

        return when (modifier.axis.uppercase()) {
            "X" -> {
                val y = point.y * cos(angle) - point.z * sin(angle)
                val z = point.y * sin(angle) + point.z * cos(angle)
                Vector(point.x, y, z)
            }
            "Y" -> {
                val x = point.x * cos(angle) - point.z * sin(angle)
                val z = point.x * sin(angle) + point.z * cos(angle)
                Vector(x, point.y, z)
            }
            "Z" -> {
                val x = point.x * cos(angle) - point.y * sin(angle)
                val y = point.x * sin(angle) + point.y * cos(angle)
                Vector(x, y, point.z)
            }
            else -> point
        }
    }

    private fun applyWave(point: Vector, modifier: ParticleModifier, tickCount: Long): Vector {
        val waveOffset = modifier.amplitude * sin(tickCount * modifier.frequency * 0.1 * modifier.speed)
        return point.clone().add(Vector(0.0, waveOffset, 0.0))
    }

    private fun applyPulse(point: Vector, modifier: ParticleModifier, tickCount: Long): Vector {
        val pulseValue = sin(tickCount * modifier.speed * 0.1)
        val scale = modifier.minScale + (modifier.maxScale - modifier.minScale) * (pulseValue + 1) / 2
        return point.clone().multiply(scale)
    }

    private fun applyOffset(point: Vector, modifier: ParticleModifier, tickCount: Long): Vector {
        val timeProgress = tickCount * modifier.speed * 0.05
        return point.clone().add(Vector(
            modifier.x * timeProgress,
            modifier.y * timeProgress,
            modifier.z * timeProgress
        ))
    }

    private fun applyRandom(point: Vector, modifier: ParticleModifier): Vector {
        val randomX = (Math.random() - 0.5) * modifier.strength
        val randomY = (Math.random() - 0.5) * modifier.strength
        val randomZ = (Math.random() - 0.5) * modifier.strength
        return point.clone().add(Vector(randomX, randomY, randomZ))
    }

    private fun applyTransform(point: Vector, modifier: ParticleModifier, tickCount: Long): Vector {
        val angle = tickCount * modifier.speed * 0.05

        return when (modifier.axis.uppercase()) {
            "X" -> {
                // Orbit around X axis
                val yOffset = modifier.y * cos(angle)
                val zOffset = modifier.z * sin(angle)
                point.clone().add(Vector(modifier.x, yOffset, zOffset))
            }
            "Y" -> {
                // Orbit around Y axis
                val xOffset = modifier.x * cos(angle)
                val zOffset = modifier.z * sin(angle)
                point.clone().add(Vector(xOffset, modifier.y, zOffset))
            }
            "Z" -> {
                // Orbit around Z axis
                val xOffset = modifier.x * cos(angle)
                val yOffset = modifier.y * sin(angle)
                point.clone().add(Vector(xOffset, yOffset, modifier.z))
            }
            "ALL" -> {
                // Complex orbital motion using all three axes
                val xOffset = modifier.x * cos(angle)
                val yOffset = modifier.y * sin(angle * 1.3)
                val zOffset = modifier.z * cos(angle * 0.7)
                point.clone().add(Vector(xOffset, yOffset, zOffset))
            }
            else -> point
        }
    }

    private fun applyTransformAbsolute(modifier: ParticleModifier, tickCount: Long): Vector {
        // Calculate the absolute transform offset
        val timeProgress = tickCount * modifier.speed * 0.05
        return Vector(
            modifier.x * timeProgress,
            modifier.y * timeProgress,
            modifier.z * timeProgress
        )
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
        options: com.github.shynixn.shyparticles.entity.ParticleOptions,
        modifiers: List<ParticleModifier> = emptyList(),
        tickCount: Long = 0
    ) {
        // Calculate fade modifier effect on alpha if present
        var effectiveAlpha = options.alpha
        for (modifier in modifiers) {
            if (modifier.type.lowercase() == "fade") {
                val fadeProgress = (tickCount * 0.05 / modifier.fadeTime).coerceIn(0.0, 1.0)
                effectiveAlpha = (modifier.startAlpha + (modifier.endAlpha - modifier.startAlpha) * fadeProgress * 255).toInt()
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