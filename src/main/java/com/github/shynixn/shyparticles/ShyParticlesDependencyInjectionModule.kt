package com.github.shynixn.shyparticles

import com.github.shynixn.fasterxml.jackson.core.type.TypeReference
import com.github.shynixn.mcutils.common.ConfigurationService
import com.github.shynixn.mcutils.common.ConfigurationServiceImpl
import com.github.shynixn.mcutils.common.CoroutinePlugin
import com.github.shynixn.mcutils.common.chat.ChatMessageService
import com.github.shynixn.mcutils.common.command.CommandService
import com.github.shynixn.mcutils.common.command.CommandServiceImpl
import com.github.shynixn.mcutils.common.di.DependencyInjectionModule
import com.github.shynixn.mcutils.common.item.ItemService
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.mcutils.common.repository.CachedRepositoryImpl
import com.github.shynixn.mcutils.common.repository.Repository
import com.github.shynixn.mcutils.common.repository.YamlFileRepositoryImpl
import com.github.shynixn.mcutils.packet.api.MaterialService
import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.impl.service.ChatMessageServiceImpl
import com.github.shynixn.mcutils.packet.impl.service.ItemServiceImpl
import com.github.shynixn.mcutils.packet.impl.service.MaterialServiceImpl
import com.github.shynixn.mcutils.packet.impl.service.PacketServiceImpl
import com.github.shynixn.shyparticles.contract.ParticleEffectFactory
import com.github.shynixn.shyparticles.contract.ParticleEffectService
import com.github.shynixn.shyparticles.contract.ShyParticlesLanguage
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import com.github.shynixn.shyparticles.entity.ShyParticlesSettings
import com.github.shynixn.shyparticles.impl.commandexecutor.ShyParticlesCommandExecutor
import com.github.shynixn.shyparticles.impl.listener.ShyParticlesListener
import com.github.shynixn.shyparticles.impl.service.ParticleEffectFactoryImpl
import com.github.shynixn.shyparticles.impl.service.ParticleEffectServiceImpl
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.ServicePriority

class ShyParticlesDependencyInjectionModule(
    private val plugin: Plugin,
    private val settings: ShyParticlesSettings,
    private val language: ShyParticlesLanguage,
    private val placeHolderService: PlaceHolderService
) {
    fun build(): DependencyInjectionModule {
        val module = DependencyInjectionModule()

        // Params
        module.addService<Plugin>(plugin)
        module.addService<CoroutinePlugin>(plugin)
        module.addService<ShyParticlesLanguage>(language)
        module.addService<ShyParticlesSettings>(settings)
        module.addService<PlaceHolderService>(placeHolderService)

        // Repositories
        val effectRepositoryImpl = YamlFileRepositoryImpl<ParticleEffectMeta>(
            plugin,
            "effects",
            plugin.dataFolder.toPath().resolve("effects"),
            settings.defaultParticles,
            emptyList(),
            object : TypeReference<ParticleEffectMeta>() {}
        )
        val cacheEffectRepository = CachedRepositoryImpl(effectRepositoryImpl)
        module.addService<Repository<ParticleEffectMeta>>(cacheEffectRepository)
        module.addService<CacheRepository<ParticleEffectMeta>>(cacheEffectRepository)

        // Services
        module.addService<ShyParticlesCommandExecutor> {
            ShyParticlesCommandExecutor(
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService(),
                module.getService()
            )
        }
        module.addService<ShyParticlesListener> {
            ShyParticlesListener(module.getService(), module.getService())
        }
        module.addService<ParticleEffectFactory> {
            ParticleEffectFactoryImpl(module.getService(), module.getService(), module.getService(), module.getService(), module.getService())
        }
        module.addService<ParticleEffectService> {
            ParticleEffectServiceImpl(
                module.getService(),
                module.getService(),
            )
        }
        module.addService<ConfigurationService> { ConfigurationServiceImpl(module.getService()) }
        module.addService<PacketService> { PacketServiceImpl(module.getService()) }
        module.addService<ItemService> { ItemServiceImpl() }
        module.addService<MaterialService> { MaterialServiceImpl() }
        module.addService<CommandService> { CommandServiceImpl(module.getService()) }
        module.addService<ChatMessageService> {
            ChatMessageServiceImpl(module.getService(), module.getService())
        }

        // Developer Api.
        Bukkit.getServicesManager()
            .register(
                ParticleEffectService::class.java,
                module.getService<ParticleEffectService>(),
                plugin,
                ServicePriority.Normal
            )
        Bukkit.getServicesManager()
            .register(
                ParticleEffectFactory::class.java,
                module.getService<ParticleEffectFactory>(),
                plugin,
                ServicePriority.Normal
            )

        return module
    }
}
