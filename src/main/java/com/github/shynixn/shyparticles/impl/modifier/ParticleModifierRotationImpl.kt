package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticleModifier as ParticleModifierContract
import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.enumeration.ParticleAxisType
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class ParticleModifierRotationImpl : ParticleModifierContract {
    override fun apply(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
        val angle = tickCount * 0.05 * modifier.speed

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