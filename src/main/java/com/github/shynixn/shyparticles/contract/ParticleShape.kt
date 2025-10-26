package com.github.shynixn.shyparticles.contract

import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

interface ParticleShape {
    fun apply(density: Double, pointCount: Int, options: ParticleOptions): Sequence<Vector>
}