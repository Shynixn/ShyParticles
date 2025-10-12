package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

class ParticleRectangleShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            val perimeter = 2 * (options.width + options.length)
            val spacing = perimeter / pointCount
            var distance = 0.0
            for (i in 0 until pointCount) {
                val (x, z) = when {
                    distance < options.width -> Pair(distance - options.width / 2, -options.length / 2)
                    distance < options.width + options.length -> Pair(
                        options.width / 2,
                        distance - options.width - options.length / 2
                    )
                    distance < 2 * options.width + options.length -> Pair(
                        options.width / 2 - (distance - options.width - options.length),
                        options.length / 2
                    )
                    else -> Pair(
                        -options.width / 2,
                        options.length / 2 - (distance - 2 * options.width - options.length)
                    )
                }
                yield(Vector(x + options.offsetX, options.offsetY, z + options.offsetZ))
                distance += spacing
            }
        }
    }
}
