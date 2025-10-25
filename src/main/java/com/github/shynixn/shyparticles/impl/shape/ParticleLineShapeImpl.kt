package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

class ParticleLineShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            for (i in 0 until pointCount) {
                val progress = i.toDouble() / pointCount
                val y = options.height * progress + options.y
                yield(Vector(options.x, y, options.z))
            }
        }
    }
}
// TODO: SUpport dirfferent direction.