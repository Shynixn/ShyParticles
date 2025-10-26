package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

class ParticleRandomShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int,  options: ParticleOptions): Sequence<Vector> {
        return sequence {
            for (i in 0 until pointCount) {
                val x = (Math.random() - 0.5) * 2 * options.radius
                val y = (Math.random() - 0.5) * 2 * options.height
                val z = (Math.random() - 0.5) * 2 * options.radius
                yield(Vector(x, y, z))
            }
        }
    }
}
