package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

class ParticleCubeShapeImpl {
    fun cubeShape(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            val pointsPerEdge = (pointCount / 12.0).toInt().coerceAtLeast(1)
            // Bottom face edges
            for (i in 0 until pointsPerEdge) {
                val t = i.toDouble() / pointsPerEdge
                yield(Vector(-options.width / 2 + t * options.width, -options.height / 2, -options.depth / 2))
                yield(Vector(-options.width / 2 + t * options.width, -options.height / 2, options.depth / 2))
                yield(Vector(-options.width / 2, -options.height / 2, -options.depth / 2 + t * options.depth))
                yield(Vector(options.width / 2, -options.height / 2, -options.depth / 2 + t * options.depth))
            }
        }
    }
}

