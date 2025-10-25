package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticleModifier as ParticleModifierContract
import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.entity.ParticleOptions
import com.github.shynixn.shyparticles.enumeration.ParticleAxisType
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class ParticleModifierOscillateImpl : ParticleModifierContract {
    override fun apply(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
        val angle = tickCount * modifier.speed * 0.05
        val coordinateModifier = applyDirectionalOffsets(Vector(modifier.x, modifier.y, modifier.z), modifier, baseLocation)

        return when (modifier.axis) {
            ParticleAxisType.X -> {
                // Orbit around X axis
                val yOffset = coordinateModifier.y * cos(angle)
                val zOffset = coordinateModifier.z * sin(angle)
                point.clone().add(Vector(coordinateModifier.x, yOffset, zOffset))
            }

            ParticleAxisType.Y -> {
                // Orbit around Y axis
                val xOffset = coordinateModifier.x * cos(angle)
                val zOffset = coordinateModifier.z * sin(angle)
                point.clone().add(Vector(xOffset, coordinateModifier.y, zOffset))
            }

            ParticleAxisType.Z -> {
                // Orbit around Z axis
                val xOffset = coordinateModifier.x * cos(angle)
                val yOffset = coordinateModifier.y * sin(angle)
                point.clone().add(Vector(xOffset, yOffset, coordinateModifier.z))
            }

            ParticleAxisType.ALL -> {
                // Complex orbital motion using all three axes
                val xOffset = coordinateModifier.x * cos(angle)
                val yOffset = coordinateModifier.y * sin(angle * 1.3)
                val zOffset = coordinateModifier.z * cos(angle * 0.7)
                point.clone().add(Vector(xOffset, yOffset, zOffset))
            }

            else -> point
        }
    }

    private fun applyDirectionalOffsets(vector: Vector, options: ParticleModifier, location: Location): Vector {
        if (options.forward == 0.0 && options.sideward == 0.0 && options.upward == 0.0) {
            return vector
        }

        val yaw = Math.toRadians(location.yaw.toDouble())
        val pitch = Math.toRadians(location.pitch.toDouble())

        if (options.ignorePitch) {
            // All offsets operate in horizontal plane when ignoring pitch
            val forwardX = -sin(yaw) * options.forward
            val forwardZ = cos(yaw) * options.forward

            val sidewardX = cos(yaw) * options.sideward
            val sidewardZ = sin(yaw) * options.sideward

            val upwardY = options.upward

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
            val forwardHorizontal = cos(pitch) * options.forward
            val forwardX = -sin(yaw) * forwardHorizontal
            val forwardY = -sin(pitch) * options.forward
            val forwardZ = cos(yaw) * forwardHorizontal

            // Sideward direction: perpendicular to look direction (right/left)
            // This is the cross product of the forward direction and world up vector
            val sidewardHorizontal = cos(pitch) * options.sideward
            val sidewardX = cos(yaw) * sidewardHorizontal
            val sidewardY = 0.0 // Sideward stays horizontal to the pitch plane
            val sidewardZ = sin(yaw) * sidewardHorizontal

            // Upward direction: perpendicular to look direction (up relative to view)
            // This is essentially the pitch-rotated up vector
            val upwardHorizontal = -sin(pitch) * options.upward
            val upwardX = -sin(yaw) * upwardHorizontal
            val upwardY = cos(pitch) * options.upward
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