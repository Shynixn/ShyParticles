package com.github.shynixn.shyparticles.entity

import com.github.shynixn.shyparticles.enumeration.Permission

class ShyParticlesSettings(private val reloadFun: (ShyParticlesSettings) -> Unit) {
    /**
     * Base Command.
     */
    var baseCommand: String = "shyparticles"

    /**
     * Command aliases.
     */
    var commandAliases: List<String> = ArrayList()

    var commandPermission: String = Permission.COMMAND.text

    var listPermission: String = Permission.LIST.text

    var reloadPermission: String = Permission.RELOAD.text

    var defaultParticles: List<Pair<String, String>> = listOf(
        "effects/ash_fall.yml" to "ash_fall.yml",
        "effects/blue_circle.yml" to "blue_circle.yml",
        "effects/blue_line.yml" to "blue_line.yml",
        "effects/celestial_dance.yml" to "celestial_dance.yml",
        "effects/electric_ring.yml" to "electric_ring.yml",
        "effects/enchant_table.yml" to "enchant_table.yml",
        "effects/ender_vortex.yml" to "ender_vortex.yml",
        "effects/explosion_burst.yml" to "explosion_burst.yml",
        "effects/fire_smoke_tornado.yml" to "fire_smoke_tornado.yml",
        "effects/green_spiral.yml" to "green_spiral.yml",
        "effects/growing_flower.yml" to "growing_flower.yml",
        "effects/lava_ring.yml" to "lava_ring.yml",
        "effects/love_circle.yml" to "love_circle.yml",
        "effects/magic_portal.yml" to "magic_portal.yml",
        "effects/moving_circle.yml" to "moving_circle.yml",
        "effects/note_trail.yml" to "note_trail.yml",
        "effects/orbital_dance.yml" to "orbital_dance.yml",
        "effects/purple_dust_ring.yml" to "purple_dust_ring.yml",
        "effects/purple_sphere.yml" to "purple_sphere.yml",
        "effects/rain_drops.yml" to "rain_drops.yml",
        "effects/slime_bounce.yml" to "slime_bounce.yml",
        "effects/smoke_cloud.yml" to "smoke_cloud.yml",
        "effects/sparkle_fountain.yml" to "sparkle_fountain.yml",
        "effects/white_dots.yml" to "white_dots.yml",
        "effects/yellow_star.yml" to "yellow_star.yml"
    )

    /**
     * Reloads the config.
     */
    fun reload() {
        reloadFun.invoke(this)
    }
}