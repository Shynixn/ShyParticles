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

    // Color properties for DUST, ENTITY_EFFECT, DUST_COLOR_TRANSITION particles
    /**
     * Red component of particle color (0-255).
     */
    var red: Int = 255

    /**
     * Green component of particle color (0-255).
     */
    var green: Int = 255

    /**
     * Blue component of particle color (0-255).
     */
    var blue: Int = 255

    /**
     * Alpha component of particle color (0-255).
     */
    var alpha: Int = 255

    /**
     * To-color red component for transition particles (0-255).
     */
    var toRed: Int = 255

    /**
     * To-color green component for transition particles (0-255).
     */
    var toGreen: Int = 255

    /**
     * To-color blue component for transition particles (0-255).
     */
    var toBlue: Int = 255

    /**
     * To-color alpha component for transition particles (0-255).
     */
    var toAlpha: Int = 255

    /**
     * Scale/size for dust particles.
     */
    var scale: Double = 1.0

    /**
     * Roll value for certain particles.
     */
    var roll: Int = 0

    /**
     * Delay for shriek particles.
     */
    var delay: Int = 0

    /**
     * Vibration ticks for vibration particles.
     */
    var vibrationTicks: Int = 20
}