package com.github.shynixn.shyparticles.impl

import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

object VectorUtil {
    /**
     * Applies directional offsets to the given vector based on the location's orientation.
     */
    fun applyDirectionalOffsets(
        vector: Vector,
        forward: Double,
        sideward: Double,
        upward: Double,
        ignorePitch: Boolean,
        location: Location
    ): Vector {
        if (forward == 0.0 && sideward == 0.0 && upward == 0.0) {
            return vector
        }

        val yaw = Math.toRadians(location.yaw.toDouble())
        val pitch = Math.toRadians(location.pitch.toDouble())

        if (ignorePitch) {
            // All offsets operate in horizontal plane when ignoring pitch
            val forwardX = -sin(yaw) * forward
            val forwardZ = cos(yaw) * forward

            val sidewardX = cos(yaw) * sideward
            val sidewardZ = sin(yaw) * sideward

            val upwardY = upward

            return vector.clone().add(
                Vector(
                    forwardX + sidewardX,
                    upwardY,
                    forwardZ + sidewardZ
                )
            )
        } else {
            // All offsets use full 3D orientation when not ignoring pitch

            // Forward direction: straight ahead in look direction
            val forwardHorizontal = cos(pitch) * forward
            val forwardX = -sin(yaw) * forwardHorizontal
            val forwardY = -sin(pitch) * forward
            val forwardZ = cos(yaw) * forwardHorizontal

            // Sideward direction: perpendicular to look direction (right/left)
            // This is the cross product of the forward direction and world up vector
            val sidewardHorizontal = cos(pitch) * sideward
            val sidewardX = cos(yaw) * sidewardHorizontal
            val sidewardY = 0.0 // Sideward stays horizontal to the pitch plane
            val sidewardZ = sin(yaw) * sidewardHorizontal

            // Upward direction: perpendicular to look direction (up relative to view)
            // This is essentially the pitch-rotated up vector
            val upwardHorizontal = -sin(pitch) * upward
            val upwardX = -sin(yaw) * upwardHorizontal
            val upwardY = cos(pitch) * upward
            val upwardZ = cos(yaw) * upwardHorizontal

            return vector.clone().add(
                Vector(
                    forwardX + sidewardX + upwardX,
                    forwardY + sidewardY + upwardY,
                    forwardZ + sidewardZ + upwardZ
                )
            )
        }
    }
}