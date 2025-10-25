package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleSpiralShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            val totalPoints = pointCount * options.turns
            for (i in 0 until totalPoints) {
                val angle = (2 * PI * options.turns * i / totalPoints) + (tickCount * 0.05)
                val heightProgress = options.height * i / totalPoints
                val x = options.radius * cos(angle) + options.x
                val y = heightProgress + options.y
                val z = options.radius * sin(angle) + options.z
                yield(Vector(x, y, z))
            }
        }
    }
}
