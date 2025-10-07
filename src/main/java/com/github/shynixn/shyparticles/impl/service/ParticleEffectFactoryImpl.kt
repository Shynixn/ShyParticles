package com.github.shynixn.shyparticles.impl.service

import com.github.shynixn.shyparticles.contract.ParticleEffect
import com.github.shynixn.shyparticles.contract.ParticleEffectFactory
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import com.github.shynixn.shyparticles.impl.ParticleEffectImpl
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class ParticleEffectFactoryImpl(
    private val plugin: Plugin
) : ParticleEffectFactory {
    private var counter = 1

    /**
     * Creates a new particle effect instance from meta.
     */
    override fun createEffect(
        meta: ParticleEffectMeta,
        location: () -> Location,
        player: Player?
    ): ParticleEffect {
        val particleId = "particle_${counter}"
        counter++
        return ParticleEffectImpl(particleId, meta.name, location, player, plugin)
    }
}