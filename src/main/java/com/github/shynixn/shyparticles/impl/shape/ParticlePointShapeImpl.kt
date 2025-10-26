package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

class ParticlePointShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            yield(Vector(0, 0, 0))
        }
    }
}
