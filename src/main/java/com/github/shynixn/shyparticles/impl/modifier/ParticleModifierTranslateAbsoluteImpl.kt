package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.entity.ParticleModifier
import org.bukkit.util.Vector

class ParticleModifierTranslateAbsoluteImpl {
    fun applyTranslateAbsolute(modifier: ParticleModifier, tickCount: Long): Vector {
        // Calculate the absolute transform offset
        val timeProgress = tickCount * modifier.speed * 0.05
        return Vector(
            modifier.x * timeProgress,
            modifier.y * timeProgress,
            modifier.z * timeProgress
        )
    }
}