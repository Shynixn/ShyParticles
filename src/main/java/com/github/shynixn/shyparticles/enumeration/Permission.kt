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
     * Permission to play particle effects.
     */
    PLAY("shyparticles.play"),
    
    /**
     * Permission to stop particle effects.
     */
    STOP("shyparticles.stop")
}