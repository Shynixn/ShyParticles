package com.github.shynixn.shyparticles.enumeration

/**
 * Supported particle shapes.
 */
enum class ParticleShape(val shapeName: String) {
    /**
     * Circular arrangement of particles.
     */
    CIRCLE("circle"),
    
    /**
     * Spherical arrangement of particles.
     */
    SPHERE("sphere"),
    
    /**
     * Spiral arrangement of particles.
     */
    SPIRAL("spiral"),
    
    /**
     * Line arrangement of particles.
     */
    LINE("line"),
    
    /**
     * Rectangle arrangement of particles.
     */
    RECTANGLE("rectangle"),
    
    /**
     * Cube arrangement of particles.
     */
    CUBE("cube"),
    
    /**
     * Random arrangement of particles.
     */
    RANDOM("random"),
    
    /**
     * Single point particle.
     */
    POINT("point"),
    
    /**
     * Heart shape arrangement.
     */
    HEART("heart"),
    
    /**
     * Star shape arrangement.
     */
    STAR("star");
    
    companion object {
        /**
         * Gets a shape by name.
         */
        fun fromName(name: String): ParticleShape? {
            return values().find { it.shapeName.equals(name, ignoreCase = true) }
        }
    }
}