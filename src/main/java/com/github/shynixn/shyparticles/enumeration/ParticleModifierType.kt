package com.github.shynixn.shyparticles.enumeration

enum class ParticleModifierType {
    NONE,

    /**
     * Moves the entire particle effect layer by the specified x, y, z offsets back and forth oscillating around its origin position.
     */
    TRANSFORM,
    /**
     * Moves the entire particle effect layer by the specified x, y, z offsets relative to its origin position.
     */
    TRANSFORM_ABSOLUTE,

    /**
     * Moves the entire particle effect layer by the specified forward, sideward, updown offsets relative to its origin position.
     */
    RELATIVE_TRANSFORM_ABSOLUTE,


    RELATIVE_TRANSFORM,

    ROTATE,


    WAVE,

    PULSE,

    OFFSET,

    RANDOM,

    FADE

}