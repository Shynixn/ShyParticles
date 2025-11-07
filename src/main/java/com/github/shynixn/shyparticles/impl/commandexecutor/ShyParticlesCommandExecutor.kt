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
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

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
        language.shyParticlesCommandSenderHasToBePlayer.text
    }

    private val effectTabs: (CommandSender) -> List<String> = {
        repository.getCache()?.map { e -> e.name } ?: emptyList()
    }

    private val sessionIdTabs: (CommandSender) -> List<String> = {
        particleService.getSessionIds()
    }

    private val effectMustExist = object : Validator<ParticleEffectMeta> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): ParticleEffectMeta? {
            return repository.getAll().firstOrNull { e -> e.name.equals(openArgs[0], true) }
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return placeHolderService.resolvePlaceHolder(
                language.shyParticlesEffectNotFoundMessage.text,
                sender as? Player,
                mapOf("0" to openArgs[0])
            )
        }
    }

    private val worldMustExist = object : Validator<World> {
        override suspend fun transform(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): World? {
            try {
                return Bukkit.getWorld(openArgs[0])
            } catch (e: Exception) {
                return null
            }
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return placeHolderService.resolvePlaceHolder(
                language.shyParticlesWorldNotFoundMessage.text,
                sender as? Player,
                mapOf("0" to openArgs[0])
            )
        }
    }

    private val coordinateValue = object : Validator<String> {
        override suspend fun transform(
            sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>
        ): String? {
            val value = openArgs[0]
            if (value.toDoubleOrNull() != null) {
                return value
            }

            if (value.startsWith("~") && value.substring(1).toDoubleOrNull() != null) {
                return value
            }

            return null
        }

        override suspend fun message(sender: CommandSender, prevArgs: List<Any>, openArgs: List<String>): String {
            return placeHolderService.resolvePlaceHolder(
                language.shyParticlesCoordinateValueMessage.text,
                sender as? Player,
                mapOf("0" to openArgs[0])
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
                language.shyParticlesPlayerNotFoundMessage.text,
                sender as? Player,
                mapOf("0" to openArgs[0])
            )
        }
    }

    init {
        CommandBuilder(plugin, settings.baseCommand, chatMessageService) {
            usage(language.shyParticlesCommandUsage.text)
            description(language.shyParticlesCommandDescription.text)
            aliases(settings.commandAliases)
            permission(settings.commandPermission)
            permissionMessage(language.shyParticlesNoPermissionCommand.text)
            subCommand("play") {
                permission(settings.playPermission)
                toolTip { language.shyParticlesPlayCommandHint.text }
                builder().argument("effect").validator(effectMustExist)
                    .tabs(effectTabs).executePlayer(senderHasToBePlayer) { player, effectMeta ->
                        playEffect(player, effectMeta, player.location)
                    }.argument("x").validator(coordinateValue)
                    .executePlayer(senderHasToBePlayer) { player, effectMeta, xRaw ->
                        val location = player.location.clone()
                        setXRaw(location, xRaw)
                        playEffect(player, effectMeta, location)
                    }.argument("y").validator(coordinateValue)
                    .executePlayer(senderHasToBePlayer) { player, effectMeta, xRaw, yRaw ->
                        val location = player.location.clone()
                        setXRaw(location, xRaw)
                        setYRaw(location, yRaw)
                        playEffect(player, effectMeta, location)
                    }.argument("z").validator(coordinateValue)
                    .executePlayer(senderHasToBePlayer) { player, effectMeta, xRaw, yRaw, zRaw ->
                        val location = player.location.clone()
                        setXRaw(location, xRaw)
                        setYRaw(location, yRaw)
                        setZRaw(location, zRaw)
                        playEffect(player, effectMeta, location)
                    }.argument("world").validator(worldMustExist)
                    .execute { sender, effectMeta, xRaw, yRaw, zRaw, world ->
                        if (xRaw.toDoubleOrNull() != null && yRaw.toDoubleOrNull() != null && zRaw.toDoubleOrNull() != null) {
                            val location = Location(world, xRaw.toDouble(), yRaw.toDouble(), zRaw.toDouble())
                            playEffect(sender, effectMeta, location)
                        }
                    }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs)
                    .execute { sender, effectMeta, xRaw, yRaw, zRaw, world, player ->
                        if (xRaw.toDoubleOrNull() != null && yRaw.toDoubleOrNull() != null && zRaw.toDoubleOrNull() != null) {
                            val location = Location(world, xRaw.toDouble(), yRaw.toDouble(), zRaw.toDouble())
                            playEffect(sender, effectMeta, location, player)
                        }
                    }
            }
            subCommand("follow") {
                permission(settings.followPermission)
                toolTip { language.shyParticlesPlayCommandHint.text }
                builder().argument("effect").validator(effectMustExist)
                    .tabs(effectTabs).executePlayer(senderHasToBePlayer) { player, effectMeta ->
                        playFollowEffect(player, effectMeta, player)
                    }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs)
                    .execute { sender, effect, player ->
                        if (sender.hasPermission(settings.followOtherPermission)) {
                            playFollowEffect(sender, effect, player)
                        } else {
                            sender.sendLanguageMessage(language.shyParticlesNoPermissionCommand)
                        }
                    }
            }
            subCommand("stop") {
                permission(settings.stopPermission)
                toolTip { language.shyParticlesStopCommandHint.text }
                builder().argument("sessionId").tabs(sessionIdTabs).execute { sender, sessionId ->
                    stopEffects(sender, sessionId)
                }
            }
            subCommand("stopFollow") {
                permission(settings.stopFollowPermission)
                toolTip { language.shyParticlesStopCommandHint.text }
                builder().executePlayer(senderHasToBePlayer) { player ->
                    stopFollowEffect(player, player)
                }.argument("player").validator(playerMustExist).tabs(onlinePlayerTabs).execute { sender, player ->
                    if (sender.hasPermission(settings.stopFollowOtherPermission)) {
                        stopFollowEffect(sender, player)
                    } else {
                        sender.sendLanguageMessage(language.shyParticlesNoPermissionCommand)
                    }
                }
            }
            subCommand("list") {
                permission(settings.listPermission)
                toolTip { language.shyParticlesListCommandHint.text }
                builder().execute { sender ->
                    listEffects(sender)
                }
            }
            subCommand("reload") {
                permission(settings.reloadPermission)
                toolTip { language.shyParticlesReloadCommandHint.text }
                builder().execute { sender ->
                    plugin.saveDefaultConfig()
                    plugin.reloadConfig()
                    plugin.reloadTranslation(language)
                    particleService.reload()
                    sender.sendLanguageMessage(language.shyParticlesReloadMessage)
                }
            }.helpCommand()
        }.build()
    }

    private fun playFollowEffect(sender: CommandSender, effectMeta: ParticleEffectMeta, player: Player) {
        if (!sender.hasPermission(settings.effectStartPermission + effectMeta.name.lowercase(Locale.ENGLISH))) {
            sender.sendLanguageMessage(language.shyParticlesNoPermissionCommand)
            return
        }

        val sessionId = particleService.startEffect(effectMeta, { player.location }, player)
        sender.sendLanguageMessage(language.shyParticlesEffectPlayMessage, effectMeta.name, sessionId)
    }

    private fun stopFollowEffect(sender: CommandSender, player: Player) {
        particleService.stopPlayerEffects(player)
        sender.sendLanguageMessage(language.shyParticlesEffectStopAllMessage)
    }

    private fun playEffect(
        sender: CommandSender,
        effectMeta: ParticleEffectMeta,
        location: Location,
        visible: Player? = null
    ) {
        if (!sender.hasPermission(settings.effectStartPermission + effectMeta.name.lowercase(Locale.ENGLISH))) {
            sender.sendLanguageMessage(language.shyParticlesNoPermissionCommand)
            return
        }

        val sessionId = particleService.startEffect(effectMeta, { location }, null, visible)
        sender.sendLanguageMessage(language.shyParticlesEffectPlayMessage, effectMeta.name, sessionId)
    }

    private fun stopEffects(sender: CommandSender, sessionId: String) {
        particleService.stopEffect(sessionId)
        sender.sendLanguageMessage(language.shyParticlesEffectStopMessage, sessionId)
    }

    private suspend fun listEffects(sender: CommandSender) {
        val effects = repository.getAll()
        val effectList = effects.sortedBy { e -> e.name.lowercase(Locale.ENGLISH) }.joinToString(", ") { e -> e.name }
        sender.sendLanguageMessage(language.shyParticlesEffectListMessage, effectList)
    }

    private fun CommandSender.sendLanguageMessage(languageItem: LanguageItem, vararg args: String) {
        val sender = this
        plugin.launch(plugin.globalRegionDispatcher) {
            chatMessageService.sendLanguageMessage(sender, languageItem, *args)
        }
    }

    private fun setXRaw(location: Location, raw: String) {
        if (raw.startsWith("~")) {
            location.x += raw.substring(1).toDouble()
        } else {
            location.x = raw.toDouble()
        }
    }

    private fun setYRaw(location: Location, raw: String) {
        if (raw.startsWith("~")) {
            location.y += raw.substring(1).toDouble()
        } else {
            location.y = raw.toDouble()
        }
    }

    private fun setZRaw(location: Location, raw: String) {
        if (raw.startsWith("~")) {
            location.z += raw.substring(1).toDouble()
        } else {
            location.z = raw.toDouble()
        }
    }
}