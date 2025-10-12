package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

class ParticlePointShapeImpl {
    fun pointShape(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            yield(Vector(options.offsetX, options.offsetY, options.offsetZ))
        }
    }
}

