package com.github.shynixn.shyparticles

import com.github.shynixn.mcutils.common.language.LanguageProvider
import com.github.shynixn.shyparticles.contract.ShyParticlesLanguage

class ShyParticlesLanguageImpl : ShyParticlesLanguage, LanguageProvider {
    override var playerNotFoundMessage: String = "shyParticlesPlayerNotFoundMessage"
    override var noPermissionCommand: String = "shyParticlesNoPermissionCommand"
    override var reloadCommandHint: String = "shyParticlesReloadCommandHint"
    override var reloadMessage: String = "shyParticlesReloadMessage"
    override var commonErrorMessage: String = "shyParticlesCommonErrorMessage"
    override var commandSenderHasToBePlayer: String = "shyParticlesCommandSenderHasToBePlayer"
    override var commandUsage: String = "shyParticlesCommandUsage"
    override var commandDescription: String = "shyParticlesCommandDescription"
    override var effectNotFoundMessage: String = "shyParticlesEffectNotFoundMessage"
    override var effectPlayMessage: String = "shyParticlesEffectPlayMessage"
    override var effectStopMessage: String = "shyParticlesEffectStopMessage"
    override var effectStopAllMessage: String = "shyParticlesEffectStopAllMessage"
    override var effectListMessage: String = "shyParticlesEffectListMessage"
    override var playCommandHint: String = "shyParticlesPlayCommandHint"
    override var stopCommandHint: String = "shyParticlesStopCommandHint"
    override var listCommandHint: String = "shyParticlesListCommandHint"
    override var createCommandHint: String = "shyParticlesCreateCommandHint"
    override var maxEffectsReached: String = "shyParticlesMaxEffectsReached"
    override var invalidLocation: String = "shyParticlesInvalidLocation"
    override var effectCreated: String = "shyParticlesEffectCreated"
}