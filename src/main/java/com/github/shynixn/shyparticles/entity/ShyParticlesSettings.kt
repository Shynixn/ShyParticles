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

    var playPermission: String = Permission.PLAY.text

    var stopPermission: String = Permission.STOP.text

    var followPermission: String = Permission.FOLLOW.text

    var followOtherPermission: String = Permission.FOLLOW_OTHER.text

    var stopFollowPermission: String = Permission.STOP_FOLLOW.text

    var stopFollowOtherPermission: String = Permission.STOP_FOLLOW_OTHER.text

    var effectStartPermission: String = Permission.EFFECT_START.text

    var effectVisiblePermission: String = Permission.EFFECT_VISIBLE.text

    var defaultParticles: List<Pair<String, String>> = listOf(
        "effects/blue_sphere.yml" to "blue_sphere.yml",
        "effects/yellow_star.yml" to "yellow_star.yml",
        "effects/box_tower.yml" to "box_tower.yml",
        "effects/rainbow_spiral.yml" to "rainbow_spiral.yml",
        "effects/pulsing_heart.yml" to "pulsing_heart.yml",
        "effects/orbital_rings.yml" to "orbital_rings.yml",
        "effects/dancing_circles.yml" to "dancing_circles.yml",
        "effects/enchanting_portal.yml" to "enchanting_portal.yml",
        "effects/soul_vortex.yml" to "soul_vortex.yml",
        "effects/flame_tornado.yml" to "flame_tornado.yml",
        "effects/electric_storm.yml" to "electric_storm.yml",
        "effects/cherry_blossom_wind.yml" to "cherry_blossom_wind.yml",
        )

    /**
     * Reloads the config.
     */
    fun reload() {
        reloadFun.invoke(this)
    }
}