package com.github.shynixn.shyparticles

import com.github.shynixn.mccoroutine.folia.*
import com.github.shynixn.mcutils.common.ChatColor
import com.github.shynixn.mcutils.common.CoroutinePlugin
import com.github.shynixn.mcutils.common.Version
import com.github.shynixn.mcutils.common.checkIfFoliaIsLoadable
import com.github.shynixn.mcutils.common.di.DependencyInjectionModule
import com.github.shynixn.mcutils.common.language.reloadTranslation
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderServiceImpl
import com.github.shynixn.shyparticles.entity.ShyParticlesSettings
import com.github.shynixn.shyparticles.enumeration.PlaceHolder
import com.github.shynixn.shyparticles.impl.commandexecutor.ShyParticlesCommandExecutor
import com.github.shynixn.shyparticles.impl.listener.ShyParticlesListener
import java.util.logging.Level
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Job
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.plugin.java.JavaPlugin

class ShyParticlesPlugin : JavaPlugin(), CoroutinePlugin {
    private val prefix: String = ChatColor.BLUE.toString() + "[ShyParticles] " + ChatColor.WHITE
    private var module: DependencyInjectionModule? = null

    companion object {
        private val areLegacyVersionsIncluded: Boolean by lazy {
            try {
                Class.forName("com.github.shynixn.shyparticles.lib.com.github.shynixn.mcutils.packet.nms.v1_8_R3.PacketSendServiceImpl")
                true
            } catch (e: ClassNotFoundException) {
                false
            }
        }
    }

    override fun onEnable() {
        Bukkit.getServer().consoleSender.sendMessage(prefix + ChatColor.GREEN + "Loading ShyParticles ...")
        this.saveDefaultConfig()
        this.reloadConfig()
        val versions = if (areLegacyVersionsIncluded) {
            listOf(
                Version.VERSION_1_8_R3,
                Version.VERSION_1_9_R2,
                Version.VERSION_1_10_R1,
                Version.VERSION_1_11_R1,
                Version.VERSION_1_12_R1,
                Version.VERSION_1_13_R1,
                Version.VERSION_1_13_R2,
                Version.VERSION_1_14_R1,
                Version.VERSION_1_15_R1,
                Version.VERSION_1_16_R1,
                Version.VERSION_1_16_R2,
                Version.VERSION_1_16_R3,
                Version.VERSION_1_17_R1,
                Version.VERSION_1_18_R1,
                Version.VERSION_1_18_R2,
                Version.VERSION_1_19_R1,
                Version.VERSION_1_19_R2,
                Version.VERSION_1_19_R3,
                Version.VERSION_1_20_R1,
                Version.VERSION_1_20_R2,
                Version.VERSION_1_20_R3,
                Version.VERSION_1_20_R4,
                Version.VERSION_1_21_R1,
                Version.VERSION_1_21_R2,
                Version.VERSION_1_21_R3,
                Version.VERSION_1_21_R4,
                Version.VERSION_1_21_R5,
                Version.VERSION_1_21_R6,
            )
        } else {
            listOf(Version.VERSION_1_21_R6)
        }

        if (!Version.serverVersion.isCompatible(*versions.toTypedArray())) {
            logger.log(Level.SEVERE, "================================================")
            logger.log(Level.SEVERE, "ShyParticles does not support your server version")
            logger.log(Level.SEVERE, "Install v" + versions[0].from + " - v" + versions[versions.size - 1].to)
            logger.log(Level.SEVERE, "Need support for a particular version? Go to https://www.patreon.com/Shynixn")
            logger.log(Level.SEVERE, "Plugin gets now disabled!")
            logger.log(Level.SEVERE, "================================================")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        logger.log(Level.INFO, "Loaded NMS version ${Version.serverVersion}.")

        if (mcCoroutineConfiguration.isFoliaLoaded && !checkIfFoliaIsLoadable()) {
            logger.log(Level.SEVERE, "================================================")
            logger.log(Level.SEVERE, "ShyParticles for Folia requires ShyParticles-Premium-Folia.jar")
            logger.log(Level.SEVERE, "Go to https://www.patreon.com/Shynixn to download it.")
            logger.log(Level.SEVERE, "Plugin gets now disabled!")
            logger.log(Level.SEVERE, "================================================")
            Bukkit.getPluginManager().disablePlugin(this)
            return
        }

        // Register Plugin Channel
        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")

        // Register Language
        val language = ShyParticlesLanguageImpl()
        reloadTranslation(language)
        logger.log(Level.INFO, "Loaded language file.")

        // Module
        val plugin = this
        val settings = ShyParticlesSettings { settings ->
        }
        settings.reload()
        val placeHolderService = PlaceHolderServiceImpl(this)
        this.module = ShyParticlesDependencyInjectionModule(
            this,
            settings,
            language,
            placeHolderService
        ).build()

        // Register PlaceHolders
        PlaceHolder.registerAll(
            this,
            this.module!!.getService<PlaceHolderService>(),
        )

        // Register Listeners
        Bukkit.getPluginManager().registerEvents(module!!.getService<ShyParticlesListener>(), this)

        // Register CommandExecutor
        module!!.getService<ShyParticlesCommandExecutor>()
        plugin.launch {
            Bukkit.getServer().consoleSender.sendMessage(prefix + ChatColor.GREEN + "Enabled ShyParticles " + plugin.description.version + " by Shynixn")
        }
    }

    override fun execute(coroutineContext: CoroutineContext, f: suspend () -> Unit): Job {
        return launch(coroutineContext) { f.invoke() }
    }

    override fun execute(f: suspend () -> Unit): Job {
        return launch { f.invoke() }
    }

    override fun fetchEntityDispatcher(entity: Entity): CoroutineContext {
        return entityDispatcher(entity)
    }

    override fun fetchGlobalRegionDispatcher(): CoroutineContext {
        return globalRegionDispatcher
    }

    override fun fetchLocationDispatcher(location: Location): CoroutineContext {
        return regionDispatcher(location)
    }

    override fun onDisable() {
        if (module == null) {
            return
        }

        module!!.close()
        module = null
    }
}
