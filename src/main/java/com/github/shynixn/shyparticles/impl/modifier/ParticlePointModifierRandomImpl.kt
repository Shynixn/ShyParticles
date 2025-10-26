package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticlePointModifier
import com.github.shynixn.shyparticles.entity.ParticleModifier
import org.bukkit.Location
import org.bukkit.util.Vector

class ParticlePointModifierRandomImpl : ParticlePointModifier {
    override fun apply(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
        val randomX = (Math.random() - 0.5) * modifier.strength
        val randomY = (Math.random() - 0.5) * modifier.strength
        val randomZ = (Math.random() - 0.5) * modifier.strength
        return point.clone().add(Vector(randomX, randomY, randomZ))
    }
}
