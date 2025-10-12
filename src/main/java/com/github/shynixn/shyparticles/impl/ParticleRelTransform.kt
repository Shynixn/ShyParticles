package com.github.shynixn.shyparticles.impl

import com.github.shynixn.shyparticles.entity.ParticleModifier
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

/**
 * Utility class for relative particle transformations.
 * Provides transformation capabilities relative to a base location's orientation.
 */
object ParticleRelTransform {

    /**
     * Applies relative transformation to a particle point based on the base location's yaw and pitch.
     * This is equivalent to the transform modifier but uses relative direction values.
     *
     * @param point The original particle point
     * @param modifier The particle modifier containing transformation parameters
     * @param tickCount Current tick count for time-based calculations
     * @param baseLocation The base location with yaw and pitch orientation
     * @return Vector representing the transformed particle point
     */
    fun applyRelativeTransform(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
        val angle = tickCount * modifier.speed * 0.05

        // Get yaw and pitch from the base location in radians
        val yaw = Math.toRadians(baseLocation.yaw.toDouble())
        val pitch = Math.toRadians(baseLocation.pitch.toDouble())

        // Calculate direction vectors based on the base location's orientation
        val forwardX = -sin(yaw) * cos(pitch)
        val forwardY = -sin(pitch)
        val forwardZ = cos(yaw) * cos(pitch)

        val rightX = -cos(yaw)
        val rightY = 0.0
        val rightZ = -sin(yaw)

        val upX = sin(yaw) * sin(pitch)
        val upY = cos(pitch)
        val upZ = -cos(yaw) * sin(pitch)

        return when (modifier.axis.uppercase()) {
            "X" -> {
                // Relative orbit around X axis with directional offsets
                val yOffset = modifier.y * cos(angle) +
                             (modifier.forward * forwardY) +
                             (modifier.sideward * rightY) +
                             (modifier.updown * upY)
                val zOffset = modifier.z * sin(angle) +
                             (modifier.forward * forwardZ) +
                             (modifier.sideward * rightZ) +
                             (modifier.updown * upZ)
                val xOffset = modifier.x +
                             (modifier.forward * forwardX) +
                             (modifier.sideward * rightX) +
                             (modifier.updown * upX)
                point.clone().add(Vector(xOffset, yOffset, zOffset))
            }
            "Y" -> {
                // Relative orbit around Y axis with directional offsets
                val xOffset = modifier.x * cos(angle) +
                             (modifier.forward * forwardX) +
                             (modifier.sideward * rightX) +
                             (modifier.updown * upX)
                val zOffset = modifier.z * sin(angle) +
                             (modifier.forward * forwardZ) +
                             (modifier.sideward * rightZ) +
                             (modifier.updown * upZ)
                val yOffset = modifier.y +
                             (modifier.forward * forwardY) +
                             (modifier.sideward * rightY) +
                             (modifier.updown * upY)
                point.clone().add(Vector(xOffset, yOffset, zOffset))
            }
            "Z" -> {
                // Relative orbit around Z axis with directional offsets
                val xOffset = modifier.x * cos(angle) +
                             (modifier.forward * forwardX) +
                             (modifier.sideward * rightX) +
                             (modifier.updown * upX)
                val yOffset = modifier.y * sin(angle) +
                             (modifier.forward * forwardY) +
                             (modifier.sideward * rightY) +
                             (modifier.updown * upY)
                val zOffset = modifier.z +
                             (modifier.forward * forwardZ) +
                             (modifier.sideward * rightZ) +
                             (modifier.updown * upZ)
                point.clone().add(Vector(xOffset, yOffset, zOffset))
            }
            "ALL" -> {
                // Complex relative orbital motion with directional offsets
                val xOffset = modifier.x * cos(angle) +
                             (modifier.forward * forwardX) +
                             (modifier.sideward * rightX) +
                             (modifier.updown * upX)
                val yOffset = modifier.y * sin(angle * 1.3) +
                             (modifier.forward * forwardY) +
                             (modifier.sideward * rightY) +
                             (modifier.updown * upY)
                val zOffset = modifier.z * cos(angle * 0.7) +
                             (modifier.forward * forwardZ) +
                             (modifier.sideward * rightZ) +
                             (modifier.updown * upZ)
                point.clone().add(Vector(xOffset, yOffset, zOffset))
            }
            else -> {
                // Default: just apply directional offsets without orbital motion
                val xOffset = modifier.x +
                             (modifier.forward * forwardX) +
                             (modifier.sideward * rightX) +
                             (modifier.updown * upX)
                val yOffset = modifier.y +
                             (modifier.forward * forwardY) +
                             (modifier.sideward * rightY) +
                             (modifier.updown * upY)
                val zOffset = modifier.z +
                             (modifier.forward * forwardZ) +
                             (modifier.sideward * rightZ) +
                             (modifier.updown * upZ)
                point.clone().add(Vector(xOffset, yOffset, zOffset))
            }
        }
    }
}
