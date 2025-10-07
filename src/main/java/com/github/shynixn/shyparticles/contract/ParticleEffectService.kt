package com.github.shynixn.shyparticles.contract

import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import org.bukkit.Location
import org.bukkit.entity.Player

/** Service for managing particle effects. */
interface ParticleEffectService : AutoCloseable {
    /** Plays a particle effect at the given location. */
    fun startEffect(meta: ParticleEffectMeta, location: () -> Location, player: Player? = null): String

    /**
     * Tries to get an effect meta data.
     */
    fun getEffectMetaFromName(name: String): ParticleEffectMeta?

    /** Stops a running particle effect. */
    fun stopEffect(effectId: String)

    /** Stops all running effects for a player. */
    fun stopPlayerEffects(player: Player)

    /** Reloads all effects from disk. */
    suspend fun reload()
}
