package com.github.shynixn.shyparticles.contract

import com.github.shynixn.shyparticles.entity.ParticleModifier
import org.bukkit.Location
import org.bukkit.util.Vector

interface ParticleModifier {
    fun apply(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector
}