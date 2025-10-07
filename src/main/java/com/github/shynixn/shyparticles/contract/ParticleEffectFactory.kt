package com.github.shynixn.shyparticles.contract

import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * Factory for creating particle effects.
 */
interface ParticleEffectFactory {
    /**
     * Creates a new particle effect instance from meta.
     */
    fun createEffect(meta: ParticleEffectMeta, location: () -> Location, player: Player? = null): ParticleEffect
}