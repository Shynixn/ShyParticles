package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticleModifier as ParticleModifierContract
import com.github.shynixn.shyparticles.entity.ParticleModifier
import org.bukkit.Location
import org.bukkit.util.Vector

class ParticleModifierOffsetImpl : ParticleModifierContract {
    override fun apply(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
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
