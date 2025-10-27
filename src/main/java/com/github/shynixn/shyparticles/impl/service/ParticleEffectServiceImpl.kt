package com.github.shynixn.shyparticles.impl.service

import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.shyparticles.contract.ParticleEffect
import com.github.shynixn.shyparticles.contract.ParticleEffectFactory
import com.github.shynixn.shyparticles.contract.ParticleEffectService
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import org.bukkit.Location
import org.bukkit.entity.Player

class ParticleEffectServiceImpl(
    private val factory: ParticleEffectFactory,
    private val repository: CacheRepository<ParticleEffectMeta>
) : ParticleEffectService {
    // All global particle effects
    private val activeParticleEffects = HashMap<String, ParticleEffect>()

    // All player particle effects
    private val playerParticleEffects = HashMap<Player, ParticleEffect>()

    private val effectMetaDataCache = HashMap<String, ParticleEffectMeta>()

    /** Plays a particle effect at the given location. */
    override fun startEffect(
        meta: ParticleEffectMeta,
        location: () -> Location,
        player: Player?
    ): String {
        val effect = factory.createEffect(meta, location, player)

        if (player != null) {
            stopPlayerEffects(player)
            playerParticleEffects[player] = effect
        }

        activeParticleEffects[effect.id] = effect
        return effect.id
    }

    override fun getSessionIds(): List<String> {
        return activeParticleEffects.keys.toList().sortedDescending()
    }

    /**
     * Tries to get an effect meta data.
     */
    override fun getEffectMetaFromName(name: String): ParticleEffectMeta? {
        return effectMetaDataCache[name]
    }

    /** Stops a running particle effect. */
    override fun stopEffect(effectId: String) {
        val globalEffect = activeParticleEffects.remove(effectId) ?: return
        val player = globalEffect.player
        if (player != null) {
            stopPlayerEffects(player)
        } else {
            globalEffect.close()
        }
    }

    /** Stops all running effects for a player. */
    override fun stopPlayerEffects(player: Player) {
        val playerEffect = playerParticleEffects.remove(player) ?: return
        playerEffect.close()
    }

    /** Reloads all effects from disk. */
    override suspend fun reload() {
        repository.clearCache()
        effectMetaDataCache.clear()
        for (element in repository.getAll()) {
            effectMetaDataCache[element.name] = element
        }
        for (globalParticleEffect in activeParticleEffects.values) {
            globalParticleEffect.close()
        }
        for (playerParticleEffect in playerParticleEffects.values) {
            playerParticleEffect.close()
        }

        activeParticleEffects.clear()
        playerParticleEffects.clear()
    }

    override fun close() {
        for (globalParticleEffect in activeParticleEffects.values) {
            globalParticleEffect.close()
        }
        for (playerParticleEffect in playerParticleEffects.values) {
            playerParticleEffect.close()
        }

        effectMetaDataCache.clear()
        activeParticleEffects.clear()
        playerParticleEffects.clear()
    }
}