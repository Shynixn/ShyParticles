package com.github.shynixn.shyparticles

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.shyparticles.contract.ShyParticlesLanguage

class ShyParticlesLanguageImpl : ShyParticlesLanguage {
    override val names: List<String>
        get() = listOf("en_us")
        
    override var playerNotFoundMessage = LanguageItem("[&9ShyParticles&f] &cPlayer %shyparticles_param_1% not found.")
    override var noPermissionCommand = LanguageItem("[&9ShyParticles&f] &cYou do not have permission to execute this command.")
    override var reloadCommandHint = LanguageItem("Reloads all particle effects and configuration.")
    override var reloadMessage = LanguageItem("[&9ShyParticles&f] Reloaded all particle effects and configuration.")
    override var commonErrorMessage = LanguageItem("[&9ShyParticles&f] &cA problem occurred. Check the console log for details.")
    override var commandSenderHasToBePlayer = LanguageItem("[&9ShyParticles&f] The command sender has to be a player.")
    override var commandUsage = LanguageItem("[&9ShyParticles&f] Use /shyparticles help to see more info about the plugin.")
    override var commandDescription = LanguageItem("[&9ShyParticles&f] All commands for the ShyParticles plugin.")
    override var effectNotFoundMessage = LanguageItem("[&9ShyParticles&f] &cParticle effect %shyparticles_param_1% not found.")
    override var effectPlayMessage = LanguageItem("[&9ShyParticles&f] Playing particle effect %shyparticles_param_1%.")
    override var effectStopMessage = LanguageItem("[&9ShyParticles&f] Stopped particle effect %shyparticles_param_1%.")
    override var effectStopAllMessage = LanguageItem("[&9ShyParticles&f] Stopped all particle effects.")
    override var effectListMessage = LanguageItem("[&9ShyParticles&f] Available effects: %shyparticles_param_1%")
    override var playCommandHint = LanguageItem("Plays a particle effect at your location or the specified location.")
    override var stopCommandHint = LanguageItem("Stops a running particle effect.")
    override var listCommandHint = LanguageItem("Lists all available particle effects.")
    override var createCommandHint = LanguageItem("Creates a new particle effect.")
    override var maxEffectsReached = LanguageItem("[&9ShyParticles&f] &cMaximum number of effects reached. Stop some effects first.")
    override var invalidLocation = LanguageItem("[&9ShyParticles&f] &cInvalid location specified.")
    override var effectCreated = LanguageItem("[&9ShyParticles&f] Created particle effect %shyparticles_param_1%.")
}