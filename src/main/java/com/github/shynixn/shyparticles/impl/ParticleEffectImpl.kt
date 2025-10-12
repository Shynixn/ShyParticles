package com.github.shynixn.shyparticles.impl

import com.github.shynixn.mcutils.packet.api.PacketService
import com.github.shynixn.mcutils.packet.api.packet.PacketOutParticle
import com.github.shynixn.shyparticles.contract.ParticleEffect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.util.Vector
import kotlin.String

class ParticleEffectImpl(
    override val id: String,
    override val name: String,
    val locationRef: () -> Location,
    override val player: Player?,
    private val plugin: Plugin,
    private val packetService: PacketService
) : ParticleEffect {

    /** Location where the effect is playing. */
    override val location: Location
        get() = TODO("Not yet implemented")

    /** Whether this effect is currently running. */
    override val isRunning: Boolean
        get() = TODO("Not yet implemented")

    /** Start time of the effect. */
    override val startTime: Long
        get() = TODO("Not yet implemented")

    fun play() {
        packetService.sendPacketOutParticle(
            play(), PacketOutParticle(var name : String,
            var location : Location,
        var offset: Vector = Vector(0.0, 0.0, 0.0),
        var speed: Double = 1.0,
        var count: Int = 1,
        // For BlockState particles
        var material: Material? = null,
        // For Dust particles
        var fromAlpha: Int = 255,
        var fromRed: Int? = null,
        var fromGreen: Int? = null,
        var fromBlue: Int? = null,
        var toAlpha: Int = 255,
        var toRed: Int? = null,
        var toGreen: Int? = null,
        var toBlue: Int? = null,
        var scale: Double = 1.0,
        // Roll
        var roll: Int? = null,
        // Item,
        var item: ItemStack? = null,
        // Time in Ticks before display.
        var delay: Int? = null,
        // Vibration
        var vibrationSourceName: String? = null,
        var vibrationLocation: Location? = null,
        var vibrationTicks: Int? = null))
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}