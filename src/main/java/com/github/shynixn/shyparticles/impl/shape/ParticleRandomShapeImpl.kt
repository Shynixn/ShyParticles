package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

class ParticleRandomShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            for (i in 0 until pointCount) {
                val x = (Math.random() - 0.5) * 2 * options.radius + options.x
                val y = (Math.random() - 0.5) * 2 * options.height + options.y
                val z = (Math.random() - 0.5) * 2 * options.radius + options.z
                yield(Vector(x, y, z))
            }
        }
    }
}
