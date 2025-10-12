package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticleModifier as ParticleModifierContract
import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.enumeration.ParticleAxisType
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class ParticleModifierTranslateImpl : ParticleModifierContract {
    override fun apply(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
        val angle = tickCount * modifier.speed * 0.05

        return when (modifier.axis) {
            ParticleAxisType.X  -> {
                // Orbit around X axis
                val yOffset = modifier.y * cos(angle)
                val zOffset = modifier.z * sin(angle)
                point.clone().add(Vector(modifier.x, yOffset, zOffset))
            }

            ParticleAxisType.Y -> {
                // Orbit around Y axis
                val xOffset = modifier.x * cos(angle)
                val zOffset = modifier.z * sin(angle)
                point.clone().add(Vector(xOffset, modifier.y, zOffset))
            }

            ParticleAxisType.Z  -> {
                // Orbit around Z axis
                val xOffset = modifier.x * cos(angle)
                val yOffset = modifier.y * sin(angle)
                point.clone().add(Vector(xOffset, yOffset, modifier.z))
            }

            ParticleAxisType.ALL -> {
                // Complex orbital motion using all three axes
                val xOffset = modifier.x * cos(angle)
                val yOffset = modifier.y * sin(angle * 1.3)
                val zOffset = modifier.z * cos(angle * 0.7)
                point.clone().add(Vector(xOffset, yOffset, zOffset))
            }

            else -> point
        }
    }
}