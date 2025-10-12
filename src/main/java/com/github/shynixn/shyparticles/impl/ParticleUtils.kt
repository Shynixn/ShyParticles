package com.github.shynixn.shyparticles.impl

import com.github.shynixn.shyparticles.entity.ParticleModifier
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

/**
 * Utility class for particle-related calculations and transformations.
 */
object ParticleUtils {

    /**
     * Applies relative transformation based on the base location's yaw and pitch.
     * This allows modifiers to work with relative directions (forward, sideward, updown)
     * based on the orientation of the base location.
     *
     * @param modifier The particle modifier containing transformation parameters
     * @param tickCount Current tick count for time-based calculations
     * @param baseLocation The base location with yaw and pitch orientation
     * @return Vector representing the relative transformation offset
     */
    fun applyRelativeTransformAbsolute(
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
        // Calculate the time progress for animation
        val timeProgress = tickCount * modifier.speed * 0.05

        // Get yaw and pitch from the base location in radians
        val yaw = Math.toRadians(baseLocation.yaw.toDouble())
        val pitch = if (modifier.ignorePitch) 0.0 else Math.toRadians(baseLocation.pitch.toDouble())

        // Calculate forward vector (direction the location is facing)
        // In Minecraft: -sin(yaw) for X, -sin(pitch) for Y, cos(yaw) for Z
        val forwardX = -sin(yaw) * cos(pitch)
        val forwardY = -sin(pitch)
        val forwardZ = cos(yaw) * cos(pitch)

        // Calculate right vector (perpendicular to forward, for sideward movement)
        // Right vector is 90 degrees clockwise from forward on the horizontal plane
        val rightX = -cos(yaw)
        val rightY = 0.0 // Right vector has no vertical component
        val rightZ = -sin(yaw)

        // Calculate up vector (perpendicular to both forward and right)
        // This is the true "up" direction relative to the pitch
        val upX = sin(yaw) * sin(pitch)
        val upY = cos(pitch)
        val upZ = -cos(yaw) * sin(pitch)

        // Apply relative offsets using the calculated direction vectors
        val totalX = (modifier.x * timeProgress) +
                    (modifier.forward * timeProgress * forwardX) +
                    (modifier.sideward * timeProgress * rightX) +
                    (modifier.updown * timeProgress * upX)

        val totalY = (modifier.y * timeProgress) +
                    (modifier.forward * timeProgress * forwardY) +
                    (modifier.sideward * timeProgress * rightY) +
                    (modifier.updown * timeProgress * upY)

        val totalZ = (modifier.z * timeProgress) +
                    (modifier.forward * timeProgress * forwardZ) +
                    (modifier.sideward * timeProgress * rightZ) +
                    (modifier.updown * timeProgress * upZ)

        return Vector(totalX, totalY, totalZ)
    }
}
