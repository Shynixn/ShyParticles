package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleCircleShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            for (i in 0 until pointCount) {
                val angle = (2 * PI * i / pointCount)
                val x = options.radius * cos(angle)
                val z = options.radius * sin(angle)
                yield(Vector(x, 0.0, z))
            }
        }
    }
}