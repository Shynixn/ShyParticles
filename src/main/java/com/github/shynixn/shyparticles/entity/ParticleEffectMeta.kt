package com.github.shynixn.shyparticles.entity

import com.github.shynixn.mcutils.common.repository.Comment
import com.github.shynixn.mcutils.common.repository.Element

/**
 * Metadata for a particle effect.
 */
@Comment(
    "###############",
    "",
    "This is the configuration for one particle effect.",
    "",
    "###############"
)
class ParticleEffectMeta : Element {
    @Comment("Unique identifier of the particle effect")
    override var name: String = ""
    
    @Comment("Duration of the effect in seconds (0 = infinite)")
    var duration: Int = 0
    
    @Comment("Whether the effect should repeat after finishing")
    var repeat: Boolean = false
    
    @Comment("Layers of particles that make up the effect")
    var layers: List<ParticleLayer> = ArrayList()
    
    @Comment("Conditions for when this effect can play")
    var conditions: ParticleConditions = ParticleConditions()
    
    @Comment("Sound effects to play with the particle effect")
    var sounds: List<SoundEffect> = ArrayList()
}