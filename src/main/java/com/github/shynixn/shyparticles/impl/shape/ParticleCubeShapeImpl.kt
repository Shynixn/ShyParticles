package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector

class ParticleCubeShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, tickCount: Long, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            val halfWidth = options.width / 2
            val halfLength = options.length / 2
            val halfHeight = options.height / 2

            // Calculate total perimeter of all 12 edges
            val edgeWidth = options.width
            val edgeLength = options.length
            val edgeHeight = options.height
            val totalPerimeter = 4 * (edgeWidth + edgeLength + edgeHeight)

            // Distribute points evenly around all edges
            for (i in 0 until pointCount) {
                val distance = (i.toDouble() / pointCount.coerceAtLeast(1)) * totalPerimeter
                var accumulated = 0.0

                // Bottom rectangle (4 edges at y = -halfHeight)
                // Front edge (along x-axis, z = -halfLength)
                if (distance < accumulated + edgeWidth) {
                    val t = (distance - accumulated) / edgeWidth
                    val x = -halfWidth + t * edgeWidth
                    yield(Vector(x + options.x, -halfHeight + options.y, -halfLength + options.z))
                    continue
                }
                accumulated += edgeWidth

                // Right edge (along z-axis, x = halfWidth)
                if (distance < accumulated + edgeLength) {
                    val t = (distance - accumulated) / edgeLength
                    val z = -halfLength + t * edgeLength
                    yield(Vector(halfWidth + options.x, -halfHeight + options.y, z + options.z))
                    continue
                }
                accumulated += edgeLength

                // Back edge (along x-axis, z = halfLength)
                if (distance < accumulated + edgeWidth) {
                    val t = (distance - accumulated) / edgeWidth
                    val x = halfWidth - t * edgeWidth
                    yield(Vector(x + options.x, -halfHeight + options.y, halfLength + options.z))
                    continue
                }
                accumulated += edgeWidth

                // Left edge (along z-axis, x = -halfWidth)
                if (distance < accumulated + edgeLength) {
                    val t = (distance - accumulated) / edgeLength
                    val z = halfLength - t * edgeLength
                    yield(Vector(-halfWidth + options.x, -halfHeight + options.y, z + options.z))
                    continue
                }
                accumulated += edgeLength

                // Top rectangle (4 edges at y = halfHeight)
                // Front edge (along x-axis, z = -halfLength)
                if (distance < accumulated + edgeWidth) {
                    val t = (distance - accumulated) / edgeWidth
                    val x = -halfWidth + t * edgeWidth
                    yield(Vector(x + options.x, halfHeight + options.y, -halfLength + options.z))
                    continue
                }
                accumulated += edgeWidth

                // Right edge (along z-axis, x = halfWidth)
                if (distance < accumulated + edgeLength) {
                    val t = (distance - accumulated) / edgeLength
                    val z = -halfLength + t * edgeLength
                    yield(Vector(halfWidth + options.x, halfHeight + options.y, z + options.z))
                    continue
                }
                accumulated += edgeLength

                // Back edge (along x-axis, z = halfLength)
                if (distance < accumulated + edgeWidth) {
                    val t = (distance - accumulated) / edgeWidth
                    val x = halfWidth - t * edgeWidth
                    yield(Vector(x + options.x, halfHeight + options.y, halfLength + options.z))
                    continue
                }
                accumulated += edgeWidth

                // Left edge (along z-axis, x = -halfWidth)
                if (distance < accumulated + edgeLength) {
                    val t = (distance - accumulated) / edgeLength
                    val z = halfLength - t * edgeLength
                    yield(Vector(-halfWidth + options.x, halfHeight + options.y, z + options.z))
                    continue
                }
                accumulated += edgeLength

                // Vertical edges (4 edges along y-axis)
                // Front-left vertical edge (x = -halfWidth, z = -halfLength)
                if (distance < accumulated + edgeHeight) {
                    val t = (distance - accumulated) / edgeHeight
                    val y = -halfHeight + t * edgeHeight
                    yield(Vector(-halfWidth + options.x, y + options.y, -halfLength + options.z))
                    continue
                }
                accumulated += edgeHeight

                // Front-right vertical edge (x = halfWidth, z = -halfLength)
                if (distance < accumulated + edgeHeight) {
                    val t = (distance - accumulated) / edgeHeight
                    val y = -halfHeight + t * edgeHeight
                    yield(Vector(halfWidth + options.x, y + options.y, -halfLength + options.z))
                    continue
                }
                accumulated += edgeHeight

                // Back-right vertical edge (x = halfWidth, z = halfLength)
                if (distance < accumulated + edgeHeight) {
                    val t = (distance - accumulated) / edgeHeight
                    val y = -halfHeight + t * edgeHeight
                    yield(Vector(halfWidth + options.x, y + options.y, halfLength + options.z))
                    continue
                }
                accumulated += edgeHeight

                // Back-left vertical edge (x = -halfWidth, z = halfLength)
                val t = (distance - accumulated) / edgeHeight
                val y = -halfHeight + t * edgeHeight
                yield(Vector(-halfWidth + options.x, y + options.y, halfLength + options.z))
            }
        }
    }
}
