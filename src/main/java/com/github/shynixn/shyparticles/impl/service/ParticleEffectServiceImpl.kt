package com.github.shynixn.shyparticles.impl.service

import com.github.shynixn.mcutils.common.CoroutinePlugin
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.shyparticles.contract.ParticleEffect
import com.github.shynixn.shyparticles.contract.ParticleEffectFactory
import com.github.shynixn.shyparticles.contract.ParticleEffectService
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.concurrent.ConcurrentHashMap

class ParticleEffectServiceImpl(
    private val plugin: Plugin,
    private val coroutinePlugin: CoroutinePlugin,
    private val settings: ParticleEffectSettings,
    private val factory: ParticleEffectFactory,
    private val repository: CacheRepository<ParticleEffectMeta>
) : ParticleEffectService {
    
    private val runningEffects = ConcurrentHashMap<String, ParticleEffect>()
    private val playerEffectCounts = ConcurrentHashMap<String, Int>()
    private var isRunning = false
    
    init {
        startUpdateTask()
    }
    
    override suspend fun playEffect(effectName: String, location: Location, player: Player?): Boolean {
        val meta = repository.getByName(effectName) ?: return false
        
        // Check global effect limit
        if (runningEffects.size >= settings.maxGlobalEffects) {
            return false
        }
        
        // Check per-player effect limit
        if (player != null) {
            val playerEffectCount = playerEffectCounts[player.name] ?: 0
            if (playerEffectCount >= settings.maxEffectsPerPlayer) {
                return false
            }
        }
        
        // Check conditions
        if (!checkConditions(meta, location)) {
            return false
        }
        
        // Create and start effect
        val effect = factory.createEffect(meta, location, player)
        runningEffects[effect.id] = effect
        
        // Update player effect count
        if (player != null) {
            playerEffectCounts[player.name] = (playerEffectCounts[player.name] ?: 0) + 1
        }
        
        effect.start()
        return true
    }
    
    override suspend fun stopEffect(effectId: String): Boolean {
        val effect = runningEffects.remove(effectId) ?: return false
        effect.stop()
        
        // Update player effect count
        if (effect.player != null) {
            val currentCount = playerEffectCounts[effect.player.name] ?: 0
            if (currentCount > 1) {
                playerEffectCounts[effect.player.name] = currentCount - 1
            } else {
                playerEffectCounts.remove(effect.player.name)
            }
        }
        
        return true
    }
    
    override suspend fun stopAllEffects(player: Player?): Int {
        var stopped = 0
        val effectsToStop = if (player != null) {
            runningEffects.values.filter { it.player == player }
        } else {
            runningEffects.values.toList()
        }
        
        effectsToStop.forEach { effect ->
            if (stopEffect(effect.id)) {
                stopped++
            }
        }
        
        return stopped
    }
    
    override suspend fun getAvailableEffects(): List<String> {
        return repository.getAll().map { it.name }
    }
    
    override suspend fun getEffect(name: String): ParticleEffectMeta? {
        return repository.getByName(name)
    }
    
    override suspend fun getRunningEffects(): Map<String, ParticleEffect> {
        return runningEffects.toMap()
    }
    
    override suspend fun reload() {
        // Stop all running effects
        stopAllEffects()
        
        // Clear caches
        playerEffectCounts.clear()
        
        // Reload repository
        repository.clearCache()
    }
    
    private fun checkConditions(meta: ParticleEffectMeta, location: Location): Boolean {
        val conditions = meta.conditions
        val world = location.world ?: return false
        
        // Check TPS
        if (conditions.minTps > 0) {
            val tps = getTPS()
            if (tps < conditions.minTps) {
                return false
            }
        }
        
        // Check world
        if (conditions.allowedWorlds.isNotEmpty()) {
            if (!conditions.allowedWorlds.contains(world.name)) {
                return false
            }
        }
        
        // Check biome
        if (conditions.allowedBiomes.isNotEmpty()) {
            val biome = world.getBiome(location.blockX, location.blockY, location.blockZ)
            if (!conditions.allowedBiomes.contains(biome.name)) {
                return false
            }
        }
        
        // Check max instances
        if (conditions.maxInstances > 0) {
            val currentInstances = runningEffects.values.count { it.name == meta.name }
            if (currentInstances >= conditions.maxInstances) {
                return false
            }
        }
        
        return true
    }
    
    private fun getTPS(): Double {
        // Simple TPS calculation - in a real implementation you might want to use a more sophisticated method
        return try {
            val server = Bukkit.getServer()
            val recentTps = server.javaClass.getMethod("getTPS").invoke(server) as DoubleArray
            recentTps[0]
        } catch (e: Exception) {
            20.0 // Default to 20 TPS if we can't get the actual value
        }
    }
    
    private fun startUpdateTask() {
        if (isRunning) return
        isRunning = true
        
        coroutinePlugin.launch {
            val updateInterval = settings.updateFrequency
            var lastUpdate = System.currentTimeMillis()
            
            while (isRunning) {
                val now = System.currentTimeMillis()
                if (now - lastUpdate >= updateInterval * 50) { // Convert ticks to milliseconds
                    updateEffects()
                    lastUpdate = now
                }
                
                // Sleep for a short time to prevent busy waiting
                kotlinx.coroutines.delay(10)
            }
        }
    }
    
    private suspend fun updateEffects() {
        val effectsToRemove = mutableListOf<String>()
        
        runningEffects.forEach { (id, effect) ->
            if (effect.shouldRemove()) {
                effectsToRemove.add(id)
            } else {
                try {
                    effect.update()
                } catch (e: Exception) {
                    // Log error and mark for removal
                    plugin.logger.warning("Error updating particle effect $id: ${e.message}")
                    effectsToRemove.add(id)
                }
            }
        }
        
        // Remove finished effects
        effectsToRemove.forEach { id ->
            stopEffect(id)
        }
    }
}