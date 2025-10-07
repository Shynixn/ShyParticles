package com.github.shynixn.shyparticles.impl.listener

import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.shyparticles.contract.ParticleEffectService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

class ShyParticlesListener(
    private val plugin: Plugin,
    private val particleService: ParticleEffectService
) : Listener {
    
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        plugin.launch {
            particleService.stopPlayerEffects(event.player)
        }
    }
}