package com.example.healthscanai.ml

import com.example.healthscanai.data.local.ScreeningFinding

/**
 * Safe extension point for validated medical ML models.
 *
 * Do not replace this with an unvalidated model and market the result
 * as a diagnosis. Models should output structured observations that
 * are then handled by the safety/rules layer.
 */
class ScreeningEngine {

    fun analyzeImage(category: String): ScreeningFinding {
        return ScreeningFinding(
            category = category,
            title = "Image captured",
            description = "The image was captured successfully. No validated disease model is installed in this build.",
            level = "MONITOR",
            confidence = null,
            recommendation = "Use a clinically validated screening module or seek professional evaluation when appropriate."
        )
    }
}
