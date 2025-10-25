package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ParticleSphereShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            // Use Fibonacci sphere algorithm for uniform distribution
            val goldenRatio = (1 + sqrt(5.0)) / 2
            val angleIncrement = 2 * PI * goldenRatio

            for (i in 0 until pointCount) {
                // Calculate theta (azimuthal angle)
                val theta = angleIncrement * i

                // Calculate phi (polar angle) using normalized index
                // Map i from [0, pointCount-1] to [-1, 1] for even distribution
                val t = (2.0 * i + 1.0) / pointCount - 1.0
                val phi = acos(t)

                // Convert spherical coordinates to Cartesian
                val x = options.radius * sin(phi) * cos(theta) + options.x
                val y = options.radius * cos(phi) + options.y
                val z = options.radius * sin(phi) * sin(theta) + options.z

                yield(Vector(x, y, z))
            }
        }
    }
}
