package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

class ParticleLineShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            for (i in 0 until pointCount) {
                val progress = i.toDouble() / pointCount

                // Determine direction based on which dimension has a value
                val vector = when {
                    options.height != 0.0 -> {
                        // Vertical line (Y-axis)
                        val y = options.height * progress + options.y
                        Vector(options.x, y, options.z)
                    }
                    options.width != 0.0 -> {
                        // Horizontal line along X-axis
                        val x = options.width * progress + options.x
                        Vector(x, options.y, options.z)
                    }
                    options.length != 0.0 -> {
                        // Horizontal line along Z-axis
                        val z = options.length * progress + options.z
                        Vector(options.x, options.y, z)
                    }
                    else -> {
                        // Default to a point if no dimension is set
                        Vector(options.x, options.y, options.z)
                    }
                }

                yield(vector)
            }
        }
    }
}
