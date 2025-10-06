package com.github.shynixn.shyparticles.contract

import com.github.shynixn.mcutils.common.language.LanguageItem
import com.github.shynixn.mcutils.common.language.LanguageProvider

/**
 * Language interface for ShyParticles.
 */
interface ShyParticlesLanguage : LanguageProvider {
    /**
     * Player not found message.
     */
    var playerNotFoundMessage: LanguageItem
    
    /**
     * No permission message.
     */
    var noPermissionCommand: LanguageItem
    
    /**
     * Reload command hint.
     */
    var reloadCommandHint: LanguageItem
    
    /**
     * Reload message.
     */
    var reloadMessage: LanguageItem
    
    /**
     * Common error message.
     */
    var commonErrorMessage: LanguageItem
    
    /**
     * Command sender has to be player message.
     */
    var commandSenderHasToBePlayer: LanguageItem
    
    /**
     * Command usage message.
     */
    var commandUsage: LanguageItem
    
    /**
     * Command description.
     */
    var commandDescription: LanguageItem
    
    /**
     * Effect not found message.
     */
    var effectNotFoundMessage: LanguageItem
    
    /**
     * Effect play message.
     */
    var effectPlayMessage: LanguageItem
    
    /**
     * Effect stop message.
     */
    var effectStopMessage: LanguageItem
    
    /**
     * Effect stop all message.
     */
    var effectStopAllMessage: LanguageItem
    
    /**
     * Effect list message.
     */
    var effectListMessage: LanguageItem
    
    /**
     * Play command hint.
     */
    var playCommandHint: LanguageItem
    
    /**
     * Stop command hint.
     */
    var stopCommandHint: LanguageItem
    
    /**
     * List command hint.
     */
    var listCommandHint: LanguageItem
    
    /**
     * Create command hint.
     */
    var createCommandHint: LanguageItem
    
    /**
     * Max effects reached message.
     */
    var maxEffectsReached: LanguageItem
    
    /**
     * Invalid location message.
     */
    var invalidLocation: LanguageItem
    
    /**
     * Effect created message.
     */
    var effectCreated: LanguageItem
}