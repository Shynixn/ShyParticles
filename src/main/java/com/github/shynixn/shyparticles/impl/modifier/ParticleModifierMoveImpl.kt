package com.github.shynixn.shyparticles.impl.modifier

import com.github.shynixn.shyparticles.contract.ParticleModifier as ParticleModifierContract
import com.github.shynixn.shyparticles.entity.ParticleModifier
import com.github.shynixn.shyparticles.impl.VectorUtil
import org.bukkit.Location
import org.bukkit.util.Vector

class ParticleModifierMoveImpl() : ParticleModifierContract {
    private var lastTickCount = -1L
    private val offset = Vector()

    override fun apply(
        point: Vector,
        modifier: ParticleModifier,
        tickCount: Long,
        baseLocation: Location
    ): Vector {
        // Only apply modifier if its delay has elapsed
        if (tickCount >= modifier.startTick && tickCount <= modifier.endTick && lastTickCount != tickCount) {
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
                modifier.ignorePitch,
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
