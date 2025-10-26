package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

class ParticleLineShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            for (i in 0 until pointCount) {
                val progress = i.toDouble() / pointCount
                val x = options.width * progress
                yield(Vector(x - options.width / 2, 0.0, 0.0))
            }
        }
    }
}
