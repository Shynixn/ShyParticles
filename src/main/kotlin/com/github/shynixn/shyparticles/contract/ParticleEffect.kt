package com.github.shynixn.shyparticles.contract

import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

/**
 * Represents a running particle effect instance.
 */
interface ParticleEffect {
    /**
     * Unique identifier for this effect instance.
     */
    val id: String
    
    /**
     * Name of the effect template.
     */
    val name: String
    
    /**
     * Location where the effect is playing.
     */
    val location: Location
    
    /**
     * Player who can see this effect (null = all players).
     */
    val player: Player?
    
    /**
     * Whether this effect is currently running.
     */
    val isRunning: Boolean
    
    /**
     * Start time of the effect.
     */
    val startTime: Long
    
    /**
     * Starts the effect.
     */
    suspend fun start()
    
    /**
     * Stops the effect.
     */
    suspend fun stop()
    
    /**
     * Updates the effect (called every tick).
     */
    suspend fun update()
    
    /**
     * Checks if the effect should be removed.
     */
    fun shouldRemove(): Boolean
}