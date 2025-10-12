package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleStarShapeImpl {
    fun starShape(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            for (i in 0 until pointCount) {
                val angle = (2 * PI * i / pointCount) + (tickCount * 0.05)
                val radius = if (i % 2 == 0) options.radius else options.radius / 2
                val x = radius * cos(angle) + options.offsetX
                val z = radius * sin(angle) + options.offsetZ
                yield(Vector(x, options.offsetY, z))
            }
        }
    }
}

