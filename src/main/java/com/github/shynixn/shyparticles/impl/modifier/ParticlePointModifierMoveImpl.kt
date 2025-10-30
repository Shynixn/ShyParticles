package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticlePointModifier
import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.impl.VectorUtil
import org.bukkit.Location
import org.bukkit.util.Vector

class ParticlePointModifierMoveImpl() : ParticlePointModifier {
    private var lastTickCount = -1L
    private val offsetMap = HashMap<ParticleModifier, Vector>()

    override fun apply(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
        var offset = offsetMap[modifier]

        if (offset == null) {
            offset = Vector()
            offsetMap[modifier] = offset
        }

        // Only apply modifier if its delay has elapsed
        if (tickCount >= modifier.start && tickCount <= modifier.end && lastTickCount != tickCount) {
            // Apply basic x, y, z offsets multiplied by speed
            val deltaOffset = Vector(
                modifier.x * modifier.speed,
                modifier.y * modifier.speed,
                modifier.z * modifier.speed
            )

            // Apply directional offsets (forward, sideward, upward) using VectorUtil
            val directionalOffset = VectorUtil.applyDirectionalOffsets(
                Vector(0.0, 0.0, 0.0),
                modifier.forward * modifier.speed,
                modifier.sideward * modifier.speed,
                modifier.upward * modifier.speed,
                !modifier.usePitch,
                baseLocation
            )

            // Add both offsets to the accumulated offset
            offset.add(deltaOffset).add(directionalOffset)
            lastTickCount = tickCount
        }

        // The offset is added outside.
        return point.add(offset)
    }
}
