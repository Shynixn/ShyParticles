package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticlePointModifier
import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.enumeration.ParticleAxisType
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class ParticlePointModifierRotationImpl : ParticlePointModifier {
    private var angle = 0.0

    override fun apply(
        point: Vector, modifier: ParticleModifier, tickCount: Long, baseLocation: Location
    ): Vector {
        // Only apply modifier if its delay has elapsed
        if (tickCount >= modifier.start && tickCount <= modifier.end) {
            val elapsedTicks = (tickCount - modifier.start + 1).toDouble() // +1 to include current tick
            // modifier.angle is in degrees; compute accumulated degrees and convert to radians
            if (modifier.yawOrigin) {
                angle = Math.toRadians(baseLocation.yaw + modifier.angle)
            } else if (modifier.pitchOrigin) {
                angle = Math.toRadians(baseLocation.pitch + modifier.angle)
            } else {
                angle = Math.toRadians(elapsedTicks * modifier.speed * modifier.angle)
            }
        }

        return when (modifier.axis) {
            ParticleAxisType.X -> {
                val y = point.y * cos(angle) - point.z * sin(angle)
                val z = point.y * sin(angle) + point.z * cos(angle)
                Vector(point.x, y, z)
            }

            ParticleAxisType.Y -> {
                val x = point.x * cos(angle) - point.z * sin(angle)
                val z = point.x * sin(angle) + point.z * cos(angle)
                Vector(x, point.y, z)
            }

            ParticleAxisType.Z -> {
                val x = point.x * cos(angle) - point.y * sin(angle)
                val y = point.x * sin(angle) + point.y * cos(angle)
                Vector(x, y, point.z)
            }

            else -> point
        }
    }
}