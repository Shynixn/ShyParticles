package com.github.shynixn.shyparticles.entity

/**
 * Sound effect configuration for particle effects.
 */
class SoundEffect {
    /**
     * Sound to play.
     */
    var name: String = ""
    
    /**
     * Volume of the sound (0.0 to 1.0).
     */
    var volume: Float = 1.0f
    
    /**
     * Pitch of the sound (0.5 to 2.0).
     */
    var pitch: Float = 1.0f

    /**
     * When to start sending the sound.
     */
    var start: Int = 0

    /**
     * How often this sound is played.
     */
    var interval : Int = 20

    /**
     * When to end sending the sound.
     */
    var end: Int = Int.MAX_VALUE
}