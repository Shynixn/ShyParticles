package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticleModifier as ParticleModifierContract
import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.enumeration.ParticleAxisType
import com.github.shynixn.shyparticles.impl.VectorUtil
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
        val coordinateModifier = VectorUtil.applyDirectionalOffsets(
            Vector(modifier.x, modifier.y, modifier.z),
            modifier.forward,
            modifier.sideward,
            modifier.upward,
            modifier.ignorePitch,
            baseLocation
        )

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
}