package com.github.shynixn.shyparticles.contract

import org.bukkit.Location
import org.bukkit.entity.Player

/** Represents a running particle effect instance. */
interface ParticleEffect : AutoCloseable {
    /** Unique identifier for this effect instance. */
    val id: String

    /** Name of the effect template. */
    val name: String

    /** Location where the effect is playing. */
    val location: Location

    /** Player who can see this effect (null = all players). */
    val visiblePlayer: Player?

    /**
     *  Player who owns this effect (null = no one).
     *  If this player quits the server. This effect is automatically cleaned up.
     */
    val ownerPlayer: Player?

    /** Whether this effect is currently running. */
    val isRunning: Boolean
}
