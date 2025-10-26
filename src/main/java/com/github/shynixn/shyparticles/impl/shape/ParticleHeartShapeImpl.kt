package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleHeartShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            for (i in 0 until pointCount) {
                val t = 2 * PI * i / pointCount
                val x = options.radius * 16 * sin(t) * sin(t) * sin(t) / 16
                val y =
                    options.radius * (13 * cos(t) - 5 * cos(2 * t) - 2 * cos(3 * t) - cos(4 * t)) / 16
                yield(Vector(x, y, 0.0))
            }
        }
    }
}
