package com.github.shynixn.shyparticles.contract

import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * Service for managing particle effects.
 */
interface ParticleEffectService {
    /**
     * Plays a particle effect at the given location.
     */
    suspend fun playEffect(effectName: String, location: Location, player: Player? = null): Boolean
    
    /**
     * Stops a running particle effect.
     */
    suspend fun stopEffect(effectId: String): Boolean
    
    /**
     * Stops all running effects for a player.
     */
    suspend fun stopAllEffects(player: Player? = null): Int
    
    /**
     * Gets all available effect names.
     */
    suspend fun getAvailableEffects(): List<String>
    
    /**
     * Gets a specific effect meta by name.
     */
    suspend fun getEffect(name: String): ParticleEffectMeta?
    
    /**
     * Gets all running effects.
     */
    suspend fun getRunningEffects(): Map<String, ParticleEffect>
    
    /**
     * Reloads all effects from disk.
     */
    suspend fun reload()
}