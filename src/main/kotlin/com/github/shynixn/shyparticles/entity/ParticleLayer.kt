package com.github.shynixn.shyparticles.entity

/**
 * Represents a layer of particles in an effect.
 */
class ParticleLayer {
    /**
     * Type of particle (e.g., FLAME, CLOUD, etc.).
     */
    var particle: String = "CLOUD"
    
    /**
     * Shape of the particle arrangement.
     */
    var shape: String = "circle"
    
    /**
     * Options for the shape and particle display.
     */
    var options: ParticleOptions = ParticleOptions()
    
    /**
     * Modifiers that affect the particles over time.
     */
    var modifiers: List<ParticleModifier> = ArrayList()
}