package com.github.shynixn.shyparticles.entity

/**
 * Conditions for when a particle effect can play.
 */
class ParticleConditions {
    /**
     * Minimum TPS required to play this effect.
     */
    var minTps: Double = 0.0
    
    /**
     * Maximum number of this effect that can run simultaneously.
     */
    var maxInstances: Int = -1  // -1 = unlimited
    
    /**
     * Biomes where this effect can play (empty = all biomes).
     */
    var allowedBiomes: List<String> = ArrayList()
    
    /**
     * Worlds where this effect can play (empty = all worlds).
     */
    var allowedWorlds: List<String> = ArrayList()
    
    /**
     * Weather conditions where this effect can play (empty = any weather).
     */
    var allowedWeather: List<String> = ArrayList()
    
    /**
     * Time ranges when this effect can play (empty = any time).
     */
    var allowedTimes: List<TimeRange> = ArrayList()
}