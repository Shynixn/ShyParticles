package com.github.shynixn.shyparticles.impl.listener

import com.github.shynixn.shyparticles.contract.ParticleEffectService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import kotlinx.coroutines.runBlocking

class ShyParticlesListener(
    private val plugin: Plugin,
    private val particleService: ParticleEffectService
) : Listener {
    
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        // Stop all effects for the player when they quit
        runBlocking {
            particleService.stopAllEffects(event.player)
        }
    }
}