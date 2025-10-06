package com.github.shynixn.shyparticles.entity

/**
 * Represents a time range for particle effect conditions.
 */
class TimeRange {
    /**
     * Start time in ticks (0-24000).
     */
    var start: Int = 0
    
    /**
     * End time in ticks (0-24000).
     */
    var end: Int = 24000
}