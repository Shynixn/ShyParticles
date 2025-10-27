package com.github.shynixn.shyparticles.enumeration

/**
 * Permission enumeration for ShyParticles.
 */
enum class Permission(val text: String) {
    /**
     * Permission to use ShyParticles commands.
     */
    COMMAND("shyparticles.command"),
    
    /**
     * Permission to reload particle effects.
     */
    RELOAD("shyparticles.reload"),

    /**
     * Permission to list particle effects.
     */
    LIST("shyparticles.list"),

    /**
     * Permission to play a particle effect at any location.
     */
    PLAY("shyparticles.play"),

    /**
     * Permission to play a particle effect at any location.
     */
    STOP("shyparticles.stop"),

    /**
     * Permission to play a follow effect.
     */
    FOLLOW("shyparticles.follow"),

    /**
     * Permission to play a follow effect for other players.
     */
    FOLLOW_OTHER("shyparticles.followother"),

    /**
     * Permission to stop a follow effect.
     */
    STOP_FOLLOW("shyparticles.stopfollow"),

    /**
     * Permission to stop a follow effect.
     */
    STOP_FOLLOW_OTHER("shyparticles.stopfollowother"),

    /**
     * Permission to access a specific particle effect or all shyparticles.effect.start.*
     */
    EFFECT_START("shyparticles.effect.start."),

    /**
     * Permission to view a specific particle effect or all shyparticles.effect.start.*
     */
    EFFECT_VISIBLE("shyparticles.effect.visible.");
}