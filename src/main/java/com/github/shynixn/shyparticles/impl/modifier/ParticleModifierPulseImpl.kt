package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticleModifier as ParticleModifierContract
import com.github.shynixn.shyparticles.entity.ParticleModifier
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.sin

class ParticleModifierPulseImpl : ParticleModifierContract {
    override fun apply(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
        val pulseValue = sin(tickCount * modifier.speed * 0.1)
        val scale = modifier.minScale + (modifier.maxScale - modifier.minScale) * (pulseValue + 1) / 2
        return point.clone().multiply(scale)
    }
}
