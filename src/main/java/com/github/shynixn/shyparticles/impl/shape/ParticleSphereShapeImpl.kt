package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleSphereShapeImpl {
    fun sphereShape(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            val phiSteps = (pointCount * 0.5).toInt().coerceAtLeast(2)
            val thetaSteps = pointCount / phiSteps
            for (i in 0 until phiSteps) {
                val phi = PI * i / phiSteps
                for (j in 0 until thetaSteps) {
                    val theta = 2 * PI * j / thetaSteps
                    val x = options.radius * sin(phi) * cos(theta) + options.offsetX
                    val y = options.radius * cos(phi) + options.offsetY
                    val z = options.radius * sin(phi) * sin(theta) + options.offsetZ
                    yield(Vector(x, y, z))
                }
            }
        }
    }
}
