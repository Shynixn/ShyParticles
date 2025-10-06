package com.github.shynixn.shyparticles.impl.service

import com.github.shynixn.shyparticles.contract.ParticleEffect
import com.github.shynixn.shyparticles.contract.ParticleEffectFactory
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import com.github.shynixn.shyparticles.entity.ParticleEffectSettings
import com.github.shynixn.shyparticles.impl.ParticleEffectImpl
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

class ParticleEffectFactoryImpl(
    private val settings: ParticleEffectSettings,
    private val plugin: Any
) : ParticleEffectFactory {
    
    override fun createEffect(meta: ParticleEffectMeta, location: Location, player: Player?): ParticleEffect {
        val id = UUID.randomUUID().toString()
        return ParticleEffectImpl(id, meta.name, location, player, meta, settings)
    }
}