package com.github.shynixn.shyparticles.impl.shape

import com.github.shynixn.shyparticles.contract.ParticleShape
import com.github.shynixn.shyparticles.entity.ParticleOptions
import org.bukkit.util.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleStarShapeImpl : ParticleShape {
    override fun apply(density: Double, pointCount: Int, options: ParticleOptions): Sequence<Vector> {
        return sequence {
            // Create a standard 5-pointed star
            val starPoints = 5  // Standard 5-pointed star
            val pointsPerEdge = (pointCount / (starPoints * 2)).coerceAtLeast(1)

            // Generate points for all 10 vertices (5 outer + 5 inner)
            val vertices = mutableListOf<Pair<Double, Double>>()
            for (i in 0 until starPoints * 2) {
                val angle = (2 * PI * i / (starPoints * 2)) - (PI / 2) // Start from top
                val radius = if (i % 2 == 0) options.radius else options.radius / 2.5
                val x = radius * cos(angle)
                val z = radius * sin(angle)
                vertices.add(Pair(x, z))
            }

            // Draw lines between consecutive vertices
            for (i in vertices.indices) {
                val (x1, z1) = vertices[i]
                val (x2, z2) = vertices[(i + 1) % vertices.size]

                // Interpolate points along the line
                for (j in 0..pointsPerEdge) {
                    val t = j.toDouble() / pointsPerEdge
                    val x = x1 + (x2 - x1) * t
                    val z = z1 + (z2 - z1) * t
                    yield(Vector(x, 0.0, z))
                }
            }
        }
    }
}

/*TODO:

        They should only be modifiers to move it.


        speed value in modifers, should instant.

        ROTATE SHOULD ROTATE IT RELATIVE TO THE PLAYER baased on forward, sideward, and upward.

        startMs should be startTick and endTick.

        --> rotate on tick 0 exactly.

        --> rotate then slowly to between tick 0 2nad 20 to 90 degrees. Then move backwards.

        --> ROPTATE_RELATIVE --> roate on tick exactl to relative positoin.

        ---> ROTATE CAN BE ALSO USED TO FLIp.

*/