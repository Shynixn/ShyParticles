package com.github.shynixn.shyparticles.impl.commandexecutor

import com.github.shynixn.mccoroutine.folia.globalRegionDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import com.github.shynixn.mcutils.common.CoroutinePlugin
import com.github.shynixn.mcutils.common.chat.ChatMessageService
import com.github.shynixn.mcutils.common.command.CommandBuilder
import com.github.shynixn.mcutils.common.command.Validator
import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.mcutils.common.language.reloadTranslation
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.mcutils.common.repository.CacheRepository
import com.github.shynixn.shyparticles.contract.ParticleEffectService
import com.github.shynixn.shyparticles.contract.ShyParticlesLanguage
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import com.github.shynixn.shyparticles.entity.ShyParticlesSettings
import com.github.shynixn.shyparticles.enumeration.Permission
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.UUID

class ShyParticlesCommandExecutor(
    private val settings: ShyParticlesSettings,
    private val plugin: CoroutinePlugin,
    private val particleService: ParticleEffectService,
    private val language: ShyParticlesLanguage,
    private val chatMessageService: ChatMessageService,
    private val repository: CacheRepository<ParticleEffectMeta>,
    private val placeHolderService: PlaceHolderService
) {
    
    private val senderHasToBePlayer: () -> String = {
        language.commandSenderHasToBePlayer.text
    }

    private val effectTabs: (CommandSender) -> List<String> = {
        repository.getCache()?.map { e -> e.name } ?: emptyList()
    }

    private val effectMustExist = object : Validator<ParticleEffectMeta> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): ParticleEffectMeta? {
            return repository.getAll().firstOrNull { e -> e.name.equals(openArgs[0], true) }
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return placeHolderService.resolvePlaceHolder(
                language.effectNotFoundMessage.text,
                sender as? Player,
                mapOf("shyparticles_param_1" to openArgs[0])
            )
        }
    }

    private val onlinePlayerTabs: ((CommandSender) -> List<String>) = {
        Bukkit.getOnlinePlayers().map { e -> e.name }
    }

    private val playerMustExist = object : Validator<Player> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): Player? {
            try {
                val playerId = openArgs[0]
                val player = Bukkit.getPlayer(playerId)

                if (player != null) {
                    return player
                }
                return Bukkit.getPlayer(UUID.fromString(playerId))
            } catch (e: Exception) {
                return null
            }
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return placeHolderService.resolvePlaceHolder(
                language.playerNotFoundMessage.text,
                sender as? Player,
                mapOf("shyparticles_param_1" to openArgs[0])
            )
        }
    }

    private val coordinateValidator = object : Validator<Double> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): Double? {
            return try {
                openArgs[0].toDouble()
            } catch (e: NumberFormatException) {
                null
            }
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return language.invalidLocation.text
        }
    }

    init {
        CommandBuilder(plugin, "shyparticles", chatMessageService) {
            usage(language.commandUsage.text)
            description(language.commandDescription.text)
            aliases(listOf("sparticles", "sp"))
            permission(Permission.COMMAND.permission)
            permissionMessage(language.noPermissionCommand.text)
            
            subCommand("play") {
                permission(Permission.PLAY.permission)
                toolTip { language.playCommandHint.text }
                builder().argument("effect").validator(effectMustExist)
                    .tabs(effectTabs).executePlayer(senderHasToBePlayer) { player, effectMeta ->
                        plugin.launch {
                            playEffect(player, effectMeta, player.location)
                        }
                    }.argument("x").validator(coordinateValidator)
                    .argument("y").validator(coordinateValidator)
                    .argument("z").validator(coordinateValidator)
                    .executePlayer(senderHasToBePlayer) { player, effectMeta, x, y, z ->
                        plugin.launch {
                            val world = player.world
                            val location = Location(world, x, y, z)
                            playEffect(player, effectMeta, location)
                        }
                    }
            }
            
            subCommand("stop") {
                permission(Permission.STOP.permission)
                toolTip { language.stopCommandHint.text }
                builder().argument("effectId").tabs({ listOf("all", "[effectId]") })
                    .execute { sender, effectId ->
                        plugin.launch {
                            stopEffect(sender, effectId)
                        }
                    }
            }
            
            subCommand("list") {
                permission(Permission.COMMAND.permission)
                toolTip { language.listCommandHint.text }
                builder().execute { sender ->
                    plugin.launch {
                        listEffects(sender)
                    }
                }
            }
            
            subCommand("reload") {
                permission(Permission.RELOAD.permission)
                toolTip { language.reloadCommandHint.text }
                builder().execute { sender ->
                    plugin.launch {
                        reloadEffects(sender)
                    }
                }
            }
            
            subCommand("create") {
                permission(Permission.CREATE.permission)
                toolTip { language.createCommandHint.text }
                builder().argument("name").execute { sender, name ->
                    plugin.launch {
                        createEffect(sender, name)
                    }
                }
            }.helpCommand()
        }.build()
    }

    private suspend fun playEffect(sender: CommandSender, effectMeta: ParticleEffectMeta, location: Location) {
        val success = particleService.playEffect(effectMeta.name, location, sender as? Player)
        if (success) {
            sender.sendLanguageMessage(language.effectPlayMessage, effectMeta.name)
        } else {
            sender.sendLanguageMessage(language.maxEffectsReached)
        }
    }

    private suspend fun stopEffect(sender: CommandSender, effectId: String) {
        if (effectId == "all") {
            val stopped = particleService.stopAllEffects(sender as? Player)
            if (stopped > 0) {
                sender.sendLanguageMessage(language.effectStopAllMessage)
            } else {
                chatMessageService.sendChatMessage("&cNo effects to stop.", sender)
            }
        } else {
            val success = particleService.stopEffect(effectId)
            if (success) {
                sender.sendLanguageMessage(language.effectStopMessage, effectId)
            } else {
                chatMessageService.sendChatMessage("&cEffect not found or already stopped.", sender)
            }
        }
    }

    private suspend fun listEffects(sender: CommandSender) {
        val effects = particleService.getAvailableEffects()
        if (effects.isEmpty()) {
            chatMessageService.sendChatMessage("&cNo particle effects available.", sender)
        } else {
            val effectList = effects.joinToString(", ")
            sender.sendLanguageMessage(language.effectListMessage, effectList)
        }
    }

    private suspend fun reloadEffects(sender: CommandSender) {
        particleService.reload()
        (plugin as Plugin).reloadTranslation(language)
        sender.sendLanguageMessage(language.reloadMessage)
    }

    private suspend fun createEffect(sender: CommandSender, name: String) {
        chatMessageService.sendChatMessage("&eParticle effect creation via commands is not yet implemented.", sender)
        chatMessageService.sendChatMessage("&eCreate effects by adding YAML files to the /plugins/ShyParticles/effects/ folder.", sender)
    }

    private fun CommandSender.sendLanguageMessage(languageItem: LanguageItem, vararg args: String) {
        val sender = this
        plugin.launch(plugin.globalRegionDispatcher) {
            chatMessageService.sendLanguageMessage(sender, languageItem, *args)
        }
    }
}
}