package com.github.shynixn.shyparticles.entity

/**
 * Sound effect configuration for particle effects.
 */
class SoundEffect {
    /**
     * Sound to play.
     */
    var sound: String = ""
    
    /**
     * Volume of the sound (0.0 to 1.0).
     */
    var volume: Float = 1.0f
    
    /**
     * Pitch of the sound (0.5 to 2.0).
     */
    var pitch: Float = 1.0f

    /**
     * When to start applying the modifier in ticks.
     */
    var startTick: Int = 0
}