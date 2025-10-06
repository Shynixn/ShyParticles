package com.github.shynixn.shyparticles.entity

/**
 * Options for particle shapes and display.
 */
class ParticleOptions {
    /**
     * Radius for circular shapes.
     */
    var radius: Double = 1.0
    
    /**
     * Height for vertical shapes like spirals or cylinders.
     */
    var height: Double = 1.0
    
    /**
     * Number of turns for spiral shapes.
     */
    var turns: Int = 1
    
    /**
     * Density of particles (0.0 to 1.0).
     */
    var density: Double = 1.0
    
    /**
     * Number of particles to spawn per update.
     */
    var particleCount: Int = 1
    
    /**
     * Width for rectangular shapes.
     */
    var width: Double = 1.0
    
    /**
     * Length for rectangular shapes.
     */
    var length: Double = 1.0
    
    /**
     * Depth for 3D shapes.
     */
    var depth: Double = 1.0
    
    /**
     * X offset from center.
     */
    var offsetX: Double = 0.0
    
    /**
     * Y offset from center.
     */
    var offsetY: Double = 0.0
    
    /**
     * Z offset from center.
     */
    var offsetZ: Double = 0.0
    
    /**
     * Extra data for the particle (color, size, etc.).
     */
    var extra: Double = 0.0
    
    /**
     * Speed of the particles.
     */
    var speed: Double = 0.0
}