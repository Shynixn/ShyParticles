package com.github.shynixn.shyparticles.enumeration

import com.github.shynixn.mcutils.common.placeholder.PlaceHolderService

/**
 * PlaceHolder enumeration for ShyParticles.
 */
enum class PlaceHolder(val fullPlaceHolder: String) {
    /**
     * Player name placeholder.
     */
    PLAYER_NAME("shyparticles_player_name"),
    
    /**
     * Parameter placeholder.
     */
    PARAM_1("shyparticles_param_1"),
    PARAM_2("shyparticles_param_2"),
    PARAM_3("shyparticles_param_3");
    
    companion object {
        /**
         * Registers all placeholders.
         */
        fun registerAll(
            plugin: Any,
            placeHolderService: PlaceHolderService
        ) {
            // Register placeholders here if needed
        }
    }
}