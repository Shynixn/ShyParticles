package com.github.shynixn.shyparticles.impl.commandexecutor

import com.github.shynixn.mcutils.common.CoroutinePlugin
import com.github.shynixn.mcutils.common.chat.ChatMessageService
import com.github.shynixn.mcutils.common.command.CommandService
import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService
import com.github.shynixn.shyparticles.contract.ParticleEffectService
import com.github.shynixn.shyparticles.contract.ShyParticlesLanguage
import com.github.shynixn.shyparticles.enumeration.Permission
import kotlinx.coroutines.launch
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class ParticleCommandExecutor(
    private val plugin: Plugin,
    private val coroutinePlugin: CoroutinePlugin,
    private val language: ShyParticlesLanguage,
    private val chatMessageService: ChatMessageService,
    private val placeHolderService: PlaceHolderService,
    private val commandService: CommandService,
    private val particleService: ParticleEffectService
) : CommandExecutor, TabCompleter {
    
    init {
        commandService.registerCommand(plugin, "shyparticles", this, this)
    }
    
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        coroutinePlugin.launch {
            handleCommand(sender, args)
        }
        return true
    }
    
    private suspend fun handleCommand(sender: CommandSender, args: Array<out String>) {
        if (args.isEmpty()) {
            chatMessageService.sendChatMessage(language.commandUsage, sender)
            return
        }
        
        when (args[0].lowercase()) {
            "help" -> handleHelpCommand(sender)
            "reload" -> handleReloadCommand(sender)
            "play" -> handlePlayCommand(sender, args)
            "stop" -> handleStopCommand(sender, args)
            "list" -> handleListCommand(sender)
            "create" -> handleCreateCommand(sender, args)
            else -> chatMessageService.sendChatMessage(language.commandUsage, sender)
        }
    }
    
    private suspend fun handleHelpCommand(sender: CommandSender) {
        if (!hasPermission(sender, Permission.COMMAND)) return
        
        chatMessageService.sendChatMessage(language.commandDescription, sender)
        chatMessageService.sendChatMessage("&e/shyparticles play <effect> [x] [y] [z] &7- ${language.playCommandHint}", sender)
        chatMessageService.sendChatMessage("&e/shyparticles stop <effectId|all> &7- ${language.stopCommandHint}", sender)
        chatMessageService.sendChatMessage("&e/shyparticles list &7- ${language.listCommandHint}", sender)
        chatMessageService.sendChatMessage("&e/shyparticles reload &7- ${language.reloadCommandHint}", sender)
    }
    
    private suspend fun handleReloadCommand(sender: CommandSender) {
        if (!hasPermission(sender, Permission.RELOAD)) return
        
        particleService.reload()
        chatMessageService.sendChatMessage(language.reloadMessage, sender)
    }
    
    private suspend fun handlePlayCommand(sender: CommandSender, args: Array<out String>) {
        if (!hasPermission(sender, Permission.PLAY)) return
        
        if (args.size < 2) {
            chatMessageService.sendChatMessage("&cUsage: /shyparticles play <effect> [x] [y] [z]", sender)
            return
        }
        
        val effectName = args[1]
        val location = if (args.size >= 5) {
            // Parse coordinates
            try {
                val world = (sender as? Player)?.world ?: plugin.server.worlds.first()
                val x = args[2].toDouble()
                val y = args[3].toDouble()
                val z = args[4].toDouble()
                org.bukkit.Location(world, x, y, z)
            } catch (e: NumberFormatException) {
                chatMessageService.sendChatMessage(language.invalidLocation, sender)
                return
            }
        } else {
            // Use player location
            if (sender !is Player) {
                chatMessageService.sendChatMessage(language.commandSenderHasToBePlayer, sender)
                return
            }
            sender.location
        }
        
        val success = particleService.playEffect(effectName, location, sender as? Player)
        if (success) {
            val message = placeHolderService.replacePlaceHolders(
                language.effectPlayMessage,
                sender,
                mapOf("shyparticles_param_1" to effectName)
            )
            chatMessageService.sendChatMessage(message, sender)
        } else {
            val message = placeHolderService.replacePlaceHolders(
                language.effectNotFoundMessage,
                sender,
                mapOf("shyparticles_param_1" to effectName)
            )
            chatMessageService.sendChatMessage(message, sender)
        }
    }
    
    private suspend fun handleStopCommand(sender: CommandSender, args: Array<out String>) {
        if (!hasPermission(sender, Permission.STOP)) return
        
        if (args.size < 2) {
            chatMessageService.sendChatMessage("&cUsage: /shyparticles stop <effectId|all>", sender)
            return
        }
        
        if (args[1] == "all") {
            val stopped = particleService.stopAllEffects(sender as? Player)
            if (stopped > 0) {
                chatMessageService.sendChatMessage(language.effectStopAllMessage, sender)
            } else {
                chatMessageService.sendChatMessage("&cNo effects to stop.", sender)
            }
        } else {
            val effectId = args[1]
            val success = particleService.stopEffect(effectId)
            if (success) {
                val message = placeHolderService.replacePlaceHolders(
                    language.effectStopMessage,
                    sender,
                    mapOf("shyparticles_param_1" to effectId)
                )
                chatMessageService.sendChatMessage(message, sender)
            } else {
                chatMessageService.sendChatMessage("&cEffect not found or already stopped.", sender)
            }
        }
    }
    
    private suspend fun handleListCommand(sender: CommandSender) {
        if (!hasPermission(sender, Permission.COMMAND)) return
        
        val effects = particleService.getAvailableEffects()
        if (effects.isEmpty()) {
            chatMessageService.sendChatMessage("&cNo particle effects available.", sender)
        } else {
            val effectList = effects.joinToString(", ")
            val message = placeHolderService.replacePlaceHolders(
                language.effectListMessage,
                sender,
                mapOf("shyparticles_param_1" to effectList)
            )
            chatMessageService.sendChatMessage(message, sender)
        }
    }
    
    private suspend fun handleCreateCommand(sender: CommandSender, args: Array<out String>) {
        if (!hasPermission(sender, Permission.CREATE)) return
        
        chatMessageService.sendChatMessage("&eParticle effect creation via commands is not yet implemented.", sender)
        chatMessageService.sendChatMessage("&eCreate effects by adding YAML files to the /plugins/ShyParticles/effects/ folder.", sender)
    }
    
    private suspend fun hasPermission(sender: CommandSender, permission: Permission): Boolean {
        if (!sender.hasPermission(permission.permission)) {
            chatMessageService.sendChatMessage(language.noPermissionCommand, sender)
            return false
        }
        return true
    }
    
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        if (args.size == 1) {
            return listOf("help", "reload", "play", "stop", "list", "create")
                .filter { it.startsWith(args[0], ignoreCase = true) }
        }
        
        if (args.size == 2) {
            when (args[0].lowercase()) {
                "play" -> {
                    // Return available effects
                    coroutinePlugin.launch {
                        return@launch particleService.getAvailableEffects()
                            .filter { it.startsWith(args[1], ignoreCase = true) }
                    }
                }
                "stop" -> {
                    return listOf("all").filter { it.startsWith(args[1], ignoreCase = true) }
                }
            }
        }
        
        return emptyList()
    }
}