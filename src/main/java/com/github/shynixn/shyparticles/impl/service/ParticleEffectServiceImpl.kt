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
    private val ownedParticleEffects = HashMap<Player, ParticleEffect>()

    private val effectMetaDataCache = HashMap<String, ParticleEffectMeta>()

    /** Plays a particle effect at the given location. */
    override fun startEffect(
        meta: ParticleEffectMeta,
        location: () -> Location,
        owner: Player?,
        visible: Player?
    ): String {
        val effect = factory.createEffect(meta, location, owner, visible)

        if (owner != null) {
            stopPlayerEffects(owner)
            ownedParticleEffects[owner] = effect
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
        val player = globalEffect.ownerPlayer
        if (player != null) {
            stopPlayerEffects(player)
        }
    }

    /** Stops all running effects for a player. */
    override fun stopPlayerEffects(player: Player) {
        val playerEffect = ownedParticleEffects.remove(player) ?: return
        playerEffect.close()
        activeParticleEffects.remove(playerEffect.id)
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
        for (playerParticleEffect in ownedParticleEffects.values) {
            playerParticleEffect.close()
        }

        activeParticleEffects.clear()
        ownedParticleEffects.clear()
    }

    override fun close() {
        for (globalParticleEffect in activeParticleEffects.values) {
            globalParticleEffect.close()
        }
        for (playerParticleEffect in ownedParticleEffects.values) {
            playerParticleEffect.close()
        }

        effectMetaDataCache.clear()
        activeParticleEffects.clear()
        ownedParticleEffects.clear()
    }
}