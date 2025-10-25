package com.github.shynixn.shyparticles.entity

import com.github.shynixn.mcutils.common.item.Item

/**
 * Options for particle shapes and display.
 */
class ParticleOptions {
    // region Packet Options - All options provided by the Minecraft Protocol.
    /**
     * Range of random spread around the location.
     */
    var spreadOffSetX: Double = 0.0

    /**
     * Range of random spread around the location.
     */
    var spreadOffSetY: Double = 0.0

    /**
     * Range of random spread around the location.
     */
    var spreadOffSetZ: Double = 0.0

    /**
     * Speed of the particle.
     */
    var speed: Double = 0.0

    /**
     * Amount of actually spawning particle with a single packet.
     */
    var count: Int = 1

    /**
     * Red component of particle color (0-255).
     */
    @Suppress("unused") // Use for de-serialization.
    var red: Int
        get() {
            return fromRed
        }
        set(value) {
            fromRed = value
        }

    /**
     * Green component of particle color (0-255).
     */
    @Suppress("unused") // Use for de-serialization.
    var green: Int
        get() {
            return fromGreen
        }
        set(value) {
            fromGreen = value
        }

    /**
     * Blue component of particle color (0-255).
     */
    @Suppress("unused") // Use for de-serialization.
    var blue: Int
        get() {
            return fromBlue
        }
        set(value) {
            fromBlue = value
        }

    /**
     * Alpha component of particle color (0-255).
     */
    @Suppress("unused") // Use for de-serialization.
    var alpha: Int
        get() {
            return fromAlpha
        }
        set(value) {
            fromAlpha = value
        }

    /**
     * Red component of particle color (0-255).
     */
    var fromRed: Int = 255

    /**
     * Green component of particle color (0-255).
     */
    var fromGreen: Int = 255

    /**
     * Blue component of particle color (0-255).
     */
    var fromBlue: Int = 255

    /**
     * Alpha component of particle color (0-255).
     */
    var fromAlpha: Int = 255

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
     * Item for BlockStates and items.
     */
    var item: Item? = null

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

    // endregion

    // region Additional Options - All options provided additionally by the API.

    /**
     * Density of particles (0.0 to 1.0).
     */
    var density: Double = 1.0

    /**
     * Number of particles to spawn per update.
     */
    var particleCount: Int = 1

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
     * Width for rectangular shapes.
     */
    var width: Double = 1.0

    /**
     * Length for rectangular shapes.
     */
    var length: Double = 1.0

    /**
     * Depth for rectangular shapes.
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
     * Forward offset.
     */
    var forwardOffset: Double = 0.0

    /**
     * Forward offset.
     */
    var sidewardOffset: Double = 0.0

    // endregion
}