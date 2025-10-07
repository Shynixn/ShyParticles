package com.github.shynixn.shyparticles.contract

import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import org.bukkit.Location
import org.bukkit.entity.Player

/** Service for managing particle effects. */
interface ParticleEffectService : AutoCloseable {
    /** Plays a particle effect at the given location. */
    suspend fun playEffect(effectName: String, location: () -> Location, player: Player? = null): String

    /** Stops a running particle effect. */
    suspend fun stopEffect(effectId: String): Boolean

    /** Stops all running effects for a player. */
    suspend fun stopAllEffects(player: Player? = null): Int

    /** Reloads all effects from disk. */
    suspend fun reload()
}
