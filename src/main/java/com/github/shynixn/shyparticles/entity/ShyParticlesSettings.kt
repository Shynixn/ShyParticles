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
    )

    /**
     * Reloads the config.
     */
    fun reload() {
        reloadFun.invoke(this)
    }
}