package com.github.shynixn.shyparticles

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.shyparticles.contract.ShyParticlesLanguage

class ShyParticlesLanguageImpl : ShyParticlesLanguage {
 override val names: List<String>
  get() = listOf("en_us")
 override var shyParticlesPlayerNotFoundMessage = LanguageItem("[&9ShyParticles&f] &cPlayer %shyparticles_param_1% not found.")

 override var shyParticlesNoPermissionCommand = LanguageItem("[&9ShyParticles&f] &cYou do not have permission to execute this command.")

 override var shyParticlesReloadCommandHint = LanguageItem("Reloads all particle effects and configuration.")

 override var shyParticlesReloadMessage = LanguageItem("[&9ShyParticles&f] Reloaded all particle effects and configuration.")

 override var shyParticlesCommonErrorMessage = LanguageItem("[&9ShyParticles&f] &cA problem occurred. Check the console log for details.")

 override var shyParticlesCommandSenderHasToBePlayer = LanguageItem("[&9ShyParticles&f] The command sender has to be a player.")

 override var shyParticlesCommandUsage = LanguageItem("[&9ShyParticles&f] Use /shyparticles help to see more info about the plugin.")

 override var shyParticlesCommandDescription = LanguageItem("[&9ShyParticles&f] All commands for the ShyParticles plugin.")

 override var shyParticlesEffectNotFoundMessage = LanguageItem("[&9ShyParticles&f] &cParticle effect %shyparticles_param_1% not found.")

 override var shyParticlesEffectPlayMessage = LanguageItem("[&9ShyParticles&f] Playing particle effect %shyparticles_param_1% with session %shyparticles_param_2%.")

 override var shyParticlesEffectStopMessage = LanguageItem("[&9ShyParticles&f] Stopped particle effect %shyparticles_param_1%.")

 override var shyParticlesEffectStopAllMessage = LanguageItem("[&9ShyParticles&f] Stopped all particle effects.")

 override var shyParticlesEffectListMessage = LanguageItem("[&9ShyParticles&f] Available effects: %shyparticles_param_1%")

 override var shyParticlesPlayCommandHint = LanguageItem("Plays a particle effect at your location or the specified location.")

 override var shyParticlesStopCommandHint = LanguageItem("Stops a running particle effect.")

 override var shyParticlesListCommandHint = LanguageItem("Lists all available particle effects.")

 override var shyParticlesCoordinateValueMessage = LanguageItem("[&9ShyParticles&f] &cThe value %shyparticles_param_1% has to be a number with the following supported formats: 2, 2.0, -3.0, ~2.2 ~-2.3")

 override var shyParticlesWorldNotFoundMessage = LanguageItem("[&9ShyParticles&f] &cWorld %shyparticles_param_1% not found.")
}
