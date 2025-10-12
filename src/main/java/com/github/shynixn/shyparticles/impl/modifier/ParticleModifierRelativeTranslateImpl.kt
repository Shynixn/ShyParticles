package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.enumeration.ParticleAxisType
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class ParticleModifierRelativeTranslateImpl {
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
        val pitch = if (modifier.ignorePitch) 0.0 else Math.toRadians(baseLocation.pitch.toDouble())

        // Calculate orbital motion (same as regular transform)
        val orbitalVector = when (modifier.axis) {
            ParticleAxisType.X -> {
                // Orbit around X axis
                val yOffset = modifier.y * cos(angle)
                val zOffset = modifier.z * sin(angle)
                Vector(modifier.x, yOffset, zOffset)
            }
            ParticleAxisType.Y -> {
                // Orbit around Y axis
                val xOffset = modifier.x * cos(angle)
                val zOffset = modifier.z * sin(angle)
                Vector(xOffset, modifier.y, zOffset)
            }
            ParticleAxisType.Z -> {
                // Orbit around Z axis
                val xOffset = modifier.x * cos(angle)
                val yOffset = modifier.y * sin(angle)
                Vector(xOffset, yOffset, modifier.z)
            }
            ParticleAxisType.ALL -> {
                // Complex orbital motion using all three axes
                val xOffset = modifier.x * cos(angle)
                val yOffset = modifier.y * sin(angle * 1.3)
                val zOffset = modifier.z * cos(angle * 0.7)
                Vector(xOffset, yOffset, zOffset)
            }
            else -> Vector(0.0, 0.0, 0.0)
        }

        // Calculate relative directional offset with time-based oscillation
        val relativeVector = Vector(
            modifier.sideward * sin(angle),      // Oscillate left/right
            modifier.updown * cos(angle * 1.1),  // Oscillate up/down with slightly different frequency
            modifier.forward * cos(angle)        // Oscillate forward/backward
        )

        // Rotate both vectors by the base location's orientation
        rotateVector(orbitalVector, yaw, pitch)
        rotateVector(relativeVector, yaw, pitch)

        // Combine orbital motion with relative offset and add to the original point
        return point.clone().add(orbitalVector).add(relativeVector)
    }

    /**
     * Rotates a vector by the given yaw and pitch angles.
     * This applies the orientation transformation to align with the base location's direction.
     *
     * @param vector The vector to rotate (modified in place)
     * @param yaw The yaw angle in radians
     * @param pitch The pitch angle in radians
     * @return The rotated vector (same instance)
     */
    private fun rotateVector(vector: Vector, yaw: Double, pitch: Double): Vector {
        // First rotate around Y axis by yaw
        val cosYaw = cos(yaw)
        val sinYaw = sin(yaw)
        val tempX = vector.x * cosYaw - vector.z * sinYaw
        val tempZ = vector.x * sinYaw + vector.z * cosYaw

        // Then rotate around X axis by pitch
        val cosPitch = cos(pitch)
        val sinPitch = sin(pitch)
        val finalY = vector.y * cosPitch - tempZ * sinPitch
        val finalZ = vector.y * sinPitch + tempZ * cosPitch

        vector.x = tempX
        vector.y = finalY
        vector.z = finalZ

        return vector
    }
}