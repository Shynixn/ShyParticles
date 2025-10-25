package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleCircleShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            for (i in 0 until pointCount) {
                val angle = (2 * PI * i / pointCount) + (tickCount * 0.05)
                val x = options.radius * cos(angle) + options.x
                val z = options.radius * sin(angle) + options.z
                yield(Vector(x, options.y, z))
            }
        }
    }
}