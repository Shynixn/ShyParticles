package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticleModifier as ParticleModifierContract
import com.github.shynixn.shyparticles.entity.ParticleModifier
import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.sin

class ParticleModifierWaveImpl : ParticleModifierContract {
    override fun apply(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
        val waveOffset = modifier.amplitude * sin(tickCount * modifier.frequency * 0.1 * modifier.speed)
        return point.clone().add(Vector(0.0, waveOffset, 0.0))
    }
}