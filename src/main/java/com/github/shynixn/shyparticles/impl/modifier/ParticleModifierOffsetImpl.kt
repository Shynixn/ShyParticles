package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.entity.ParticleModifier
import org.bukkit.util.Vector

class ParticleModifierOffsetImpl {
    fun applyOffset(point: Vector, modifier: ParticleModifier, tickCount: Long): Vector {
        val timeProgress = tickCount * modifier.speed * 0.05
        return point.clone().add(
            Vector(
                modifier.x * timeProgress,
                modifier.y * timeProgress,
                modifier.z * timeProgress
            )
        )
    }
}

