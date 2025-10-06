package com.github.shynixn.shyparticles.entity

import com.github.shynixn.mcutils.common.repository.Comment
import com.github.shynixn.mcutils.common.repository.Element

/**
 * Configuration for particle effect settings.
 */
@Comment(
    "###############",
    "",
    "This is the configuration for particle engine settings.",
    "",
    "###############"
)
class ParticleEffectSettings : Element {
    @Comment("Unique identifier")
    override var name: String = "settings"
    
    @Comment("Maximum number of particles that can be displayed at once per effect")
    var maxParticlesPerEffect: Int = 1000
    
    @Comment("Maximum number of concurrent effects per player")
    var maxEffectsPerPlayer: Int = 5
    
    @Comment("Maximum number of concurrent effects globally")
    var maxGlobalEffects: Int = 50
    
    @Comment("Update frequency in ticks (20 ticks = 1 second)")
    var updateFrequency: Int = 1
    
    @Comment("Maximum render distance for particles")
    var renderDistance: Double = 64.0
    
    @Comment("Automatically reduce particle density when TPS is low")
    var adaptivePerformance: Boolean = true
    
    @Comment("TPS threshold below which particles are reduced")
    var tpsThreshold: Double = 18.0
    
    @Comment("Particle reduction factor when TPS is low (0.5 = 50% particles)")
    var reductionFactor: Double = 0.5
}