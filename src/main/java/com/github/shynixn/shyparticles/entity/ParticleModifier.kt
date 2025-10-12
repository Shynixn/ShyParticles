package com.github.shynixn.shyparticles.entity

import com.github.shynixn.shyparticles.enumeration.ParticleModifierType

/**
 * Modifiers that affect particles over time.
 */
class ParticleModifier {
    /**
     * Type of modifier (rotate, fade, pulse, wave, etc.).
     */
    var type: ParticleModifierType = ParticleModifierType.NONE
    
    /**
     * Speed of the modifier effect.
     */
    var speed: Double = 1.0
    
    /**
     * Axis for rotation (X, Y, or Z).
     */
    var axis: String = "Y"
    
    /**
     * Fade time in seconds.
     */
    var fadeTime: Double = 1.0
    
    /**
     * Starting alpha value for fade.
     */
    var startAlpha: Double = 1.0
    
    /**
     * Ending alpha value for fade.
     */
    var endAlpha: Double = 0.0
    
    /**
     * Minimum scale for pulse effects.
     */
    var minScale: Double = 0.8
    
    /**
     * Maximum scale for pulse effects.
     */
    var maxScale: Double = 1.2
    
    /**
     * Amplitude for wave effects.
     */
    var amplitude: Double = 1.0
    
    /**
     * Frequency for wave effects.
     */
    var frequency: Double = 1.0
    
    /**
     * Strength for random effects.
     */
    var strength: Double = 1.0
    
    /**
     * X offset for offset modifiers.
     */
    var x: Double = 0.0
    
    /**
     * Y offset for offset modifiers.
     */
    var y: Double = 0.0
    
    /**
     * Z offset for offset modifiers.
     */
    var z: Double = 0.0

    /**
     * Forward offset relative to the yaw direction.
     */
    var forward: Double = 0.0

    /**
     * Sideward offset relative to the yaw direction (left/right).
     */
    var sideward: Double = 0.0

    /**
     * Up/down offset relative to the pitch direction.
     */
    var updown: Double = 0.0

    /**
     * Whether to ignore pitch when applying transformations.
     * If true, only yaw rotation will be applied.
     */
    var ignorePitch: Boolean = false
}