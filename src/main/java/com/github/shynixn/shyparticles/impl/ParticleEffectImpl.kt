package com.github.shynixn.shyparticles.impl

import com.github.shynixn.shyparticles.contract.ParticleEffect
import com.github.shynixn.shyparticles.entity.ParticleEffectMeta
import com.github.shynixn.shyparticles.entity.ParticleLayer
import com.github.shynixn.shyparticles.enumeration.ParticleShape
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import kotlin.math.*

class ParticleEffectImpl(
    override val id: String,
    override val name: String,
    override val location: Location,
    override val player: Player?,
    private val meta: ParticleEffectMeta,
    private val plugin : Plugin
) : ParticleEffect {
    
    override var isRunning: Boolean = false
        private set
    
    override val startTime: Long = System.currentTimeMillis()
    
    private var ticks: Long = 0
    private var layerStates = mutableMapOf<Int, LayerState>()
    
    private data class LayerState(
        var rotation: Double = 0.0,
        var fadeProgress: Double = 0.0,
        var pulsePhase: Double = 0.0,
        var wavePhase: Double = 0.0
    )
    
    override suspend fun start() {
        isRunning = true
        // Initialize layer states
        meta.layers.forEachIndexed { index, _ ->
            layerStates[index] = LayerState()
        }
    }
    
    override suspend fun stop() {
        isRunning = false
    }
    
    override suspend fun update() {
        if (!isRunning) return
        
        ticks++
        
        // Check if effect should finish
        if (meta.duration > 0) {
            val elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0
            if (elapsedSeconds >= meta.duration) {
                if (meta.repeat) {
                    // Reset effect
                    ticks = 0
                    layerStates.clear()
                    meta.layers.forEachIndexed { index, _ ->
                        layerStates[index] = LayerState()
                    }
                } else {
                    stop()
                    return
                }
            }
        }
        
        // Update each layer
        meta.layers.forEachIndexed { layerIndex, layer ->
            updateLayer(layer, layerIndex)
        }
    }
    
    private fun updateLayer(layer: ParticleLayer, layerIndex: Int) {
        val state = layerStates[layerIndex] ?: return
        val options = layer.options
        
        // Apply modifiers
        layer.modifiers.forEach { modifier ->
            when (modifier.type.lowercase()) {
                "rotate" -> {
                    state.rotation += modifier.speed / 20.0 // Convert to degrees per tick
                }
                "fade" -> {
                    state.fadeProgress += 1.0 / (modifier.fadeTime * 20.0)
                    if (state.fadeProgress > 1.0) state.fadeProgress = 1.0
                }
                "pulse" -> {
                    state.pulsePhase += modifier.speed / 20.0 * 2 * PI
                }
                "wave" -> {
                    state.wavePhase += modifier.frequency / 20.0 * 2 * PI
                }
            }
        }
        
        // Generate particles based on shape
        val particleType = try {
            Particle.valueOf(layer.particle.uppercase())
        } catch (e: Exception) {
            Particle.CLOUD
        }
        
        val shape = ParticleShape.fromName(layer.shape) ?: ParticleShape.CIRCLE
        val particles = generateParticles(shape, options, state)
        
        // Spawn particles
        particles.forEach { particleLocation ->
            spawnParticle(particleType, particleLocation, options, state)
        }
    }
    
    private fun generateParticles(shape: ParticleShape, options: com.github.shynixn.shyparticles.entity.ParticleOptions, state: LayerState): List<Location> {
        val particles = mutableListOf<Location>()
        val baseLocation = location.clone()
        
        when (shape) {
            ParticleShape.CIRCLE -> {
                val angleStep = 2 * PI / options.particleCount
                for (i in 0 until options.particleCount) {
                    val angle = i * angleStep + Math.toRadians(state.rotation)
                    val x = cos(angle) * options.radius
                    val z = sin(angle) * options.radius
                    particles.add(baseLocation.clone().add(x, options.offsetY, z))
                }
            }
            
            ParticleShape.SPIRAL -> {
                val totalSteps = (options.turns * options.particleCount).toInt()
                val angleStep = 2 * PI * options.turns / totalSteps
                val heightStep = options.height / totalSteps
                
                for (i in 0 until totalSteps) {
                    val angle = i * angleStep + Math.toRadians(state.rotation)
                    val height = i * heightStep
                    val radius = options.radius * (1.0 - height / options.height * 0.2) // Slight taper
                    
                    val x = cos(angle) * radius
                    val z = sin(angle) * radius
                    particles.add(baseLocation.clone().add(x, height + options.offsetY, z))
                }
            }
            
            ParticleShape.SPHERE -> {
                repeat(options.particleCount) {
                    val theta = 2 * PI * Random().nextDouble()
                    val phi = acos(1 - 2 * Random().nextDouble())
                    
                    val x = options.radius * sin(phi) * cos(theta)
                    val y = options.radius * sin(phi) * sin(theta)
                    val z = options.radius * cos(phi)
                    
                    particles.add(baseLocation.clone().add(x, y + options.offsetY, z))
                }
            }
            
            ParticleShape.LINE -> {
                val step = options.height / options.particleCount
                for (i in 0 until options.particleCount) {
                    val y = i * step
                    particles.add(baseLocation.clone().add(options.offsetX, y + options.offsetY, options.offsetZ))
                }
            }
            
            ParticleShape.HEART -> {
                val angleStep = 2 * PI / options.particleCount
                for (i in 0 until options.particleCount) {
                    val t = i * angleStep
                    val scale = options.radius / 16.0
                    
                    // Heart equation: x = 16sin³(t), y = 13cos(t) - 5cos(2t) - 2cos(3t) - cos(4t)
                    val x = 16 * sin(t).pow(3.0) * scale
                    val y = (13 * cos(t) - 5 * cos(2 * t) - 2 * cos(3 * t) - cos(4 * t)) * scale
                    
                    particles.add(baseLocation.clone().add(x, y + options.offsetY, options.offsetZ))
                }
            }
            
            ParticleShape.STAR -> {
                val angleStep = 2 * PI / (options.particleCount * 2) // Double for inner and outer points
                for (i in 0 until options.particleCount * 2) {
                    val angle = i * angleStep + Math.toRadians(state.rotation)
                    val radius = if (i % 2 == 0) options.radius else options.radius * 0.5
                    
                    val x = cos(angle) * radius
                    val z = sin(angle) * radius
                    particles.add(baseLocation.clone().add(x, options.offsetY, z))
                }
            }
            
            ParticleShape.POINT -> {
                particles.add(baseLocation.clone().add(options.offsetX, options.offsetY, options.offsetZ))
            }
            
            else -> {
                // Default to circle
                val angleStep = 2 * PI / options.particleCount
                for (i in 0 until options.particleCount) {
                    val angle = i * angleStep
                    val x = cos(angle) * options.radius
                    val z = sin(angle) * options.radius
                    particles.add(baseLocation.clone().add(x, options.offsetY, z))
                }
            }
        }
        
        return particles
    }
    
    private fun spawnParticle(
        particle: Particle, 
        particleLocation: Location, 
        options: com.github.shynixn.shyparticles.entity.ParticleOptions, 
        state: LayerState
    ) {
        val world = particleLocation.world ?: return
        
        if (player != null) {
            // Show to specific player
            if (player.location.distance(particleLocation) <= settings.renderDistance) {
                player.spawnParticle(
                    particle,
                    particleLocation,
                    1,
                    options.offsetX,
                    options.offsetY,
                    options.offsetZ,
                    options.speed
                )
            }
        } else {
            // Show to all nearby players
            world.spawnParticle(
                particle,
                particleLocation,
                1,
                options.offsetX,
                options.offsetY,
                options.offsetZ,
                options.speed
            )
        }
    }
    
    override fun shouldRemove(): Boolean {
        if (!isRunning) return true
        
        if (meta.duration > 0 && !meta.repeat) {
            val elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000.0
            return elapsedSeconds >= meta.duration
        }
        
        return false
    }
}