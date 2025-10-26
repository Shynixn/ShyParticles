package com.github.shynixn.shyparticles.entity

import com.github.shynixn.shyparticles.enumeration.ParticleAxisType
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
    var axis: ParticleAxisType = ParticleAxisType.Y

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
     * X offset from center.
     */
    var x: Double = 0.0

    /**
     * Y offset from center.
     */
    var y: Double = 0.0

    /**
     * Z offset from center.
     */
    var z: Double = 0.0

    /**
     * Forward offset.
     */
    var forward: Double = 0.0

    /**
     * Forward offset.
     */
    var sideward: Double = 0.0

    /**
     * Upwards offset.
     */
    var upward: Double = 0.0

    /**
     * Ignore pitch.
     */
    var ignorePitch: Boolean = false

    /**
     * When to start applying the modifier in ticks.
     */
    var startTick: Int = 0

    /**
     * Angle in degrees.
     */
    var angle: Double = 0.0

    /**
     * Aligns the rotation to yaw.
     */
    var yawOrigin: Boolean = false

    /**
     * Aligns the rotation to pitch.
     */
    var pitchOrigin: Boolean = false

    /**
     * When to end applying the modifier in ticks.
     */
    var endTick: Int = Int.MAX_VALUE
}