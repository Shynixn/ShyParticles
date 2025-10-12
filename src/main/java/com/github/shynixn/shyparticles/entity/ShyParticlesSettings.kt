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
        "effects/blue_line.yml" to "blue_line.yml",
        "effects/red_circle.yml" to "red_circle.yml",
    )

    /**
     * Reloads the config.
     */
    fun reload() {
        reloadFun.invoke(this)
    }
}