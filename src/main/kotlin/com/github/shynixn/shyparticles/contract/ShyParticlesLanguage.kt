package com.github.shynixn.shyparticles.contract

/**
 * Language interface for ShyParticles.
 */
interface ShyParticlesLanguage {
    /**
     * Player not found message.
     */
    var playerNotFoundMessage: String
    
    /**
     * No permission message.
     */
    var noPermissionCommand: String
    
    /**
     * Reload command hint.
     */
    var reloadCommandHint: String
    
    /**
     * Reload message.
     */
    var reloadMessage: String
    
    /**
     * Common error message.
     */
    var commonErrorMessage: String
    
    /**
     * Command sender has to be player message.
     */
    var commandSenderHasToBePlayer: String
    
    /**
     * Command usage message.
     */
    var commandUsage: String
    
    /**
     * Command description.
     */
    var commandDescription: String
    
    /**
     * Effect not found message.
     */
    var effectNotFoundMessage: String
    
    /**
     * Effect play message.
     */
    var effectPlayMessage: String
    
    /**
     * Effect stop message.
     */
    var effectStopMessage: String
    
    /**
     * Effect stop all message.
     */
    var effectStopAllMessage: String
    
    /**
     * Effect list message.
     */
    var effectListMessage: String
    
    /**
     * Play command hint.
     */
    var playCommandHint: String
    
    /**
     * Stop command hint.
     */
    var stopCommandHint: String
    
    /**
     * List command hint.
     */
    var listCommandHint: String
    
    /**
     * Create command hint.
     */
    var createCommandHint: String
    
    /**
     * Max effects reached message.
     */
    var maxEffectsReached: String
    
    /**
     * Invalid location message.
     */
    var invalidLocation: String
    
    /**
     * Effect created message.
     */
    var effectCreated: String
}