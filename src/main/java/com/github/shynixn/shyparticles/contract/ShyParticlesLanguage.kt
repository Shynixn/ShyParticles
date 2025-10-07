package com.github.shynixn.shyparticles.contract

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.mcutils.common.language.LanguageProvider

interface ShyParticlesLanguage : LanguageProvider {
  var shyParticlesPlayerNotFoundMessage: LanguageItem

  var shyParticlesNoPermissionCommand: LanguageItem

  var shyParticlesReloadCommandHint: LanguageItem

  var shyParticlesReloadMessage: LanguageItem

  var shyParticlesCommonErrorMessage: LanguageItem

  var shyParticlesCommandSenderHasToBePlayer: LanguageItem

  var shyParticlesCommandUsage: LanguageItem

  var shyParticlesCommandDescription: LanguageItem

  var shyParticlesEffectNotFoundMessage: LanguageItem

  var shyParticlesEffectPlayMessage: LanguageItem

  var shyParticlesEffectStopMessage: LanguageItem

  var shyParticlesEffectStopAllMessage: LanguageItem

  var shyParticlesEffectListMessage: LanguageItem

  var shyParticlesPlayCommandHint: LanguageItem

  var shyParticlesStopCommandHint: LanguageItem

  var shyParticlesListCommandHint: LanguageItem
}
