package com.example.healthscanai.screening

import com.example.healthscanai.data.local.ScreeningFinding
import com.example.healthscanai.data.local.VitalMeasurement

object SafetyRules {
    fun evaluate(v: VitalMeasurement): ScreeningFinding? {
        val type = v.type.lowercase()
        return when {
            type == "spo2" && v.value1 < 90.0 -> ScreeningFinding(
                category = "VITAL",
                title = "Low oxygen saturation reading",
                description = "The entered SpO₂ value is below 90%. Recheck the measurement and consider urgent medical assessment, especially if symptoms are present.",
                level = "URGENT",
                recommendation = "Recheck promptly. Seek urgent medical care if the reading remains low or you have breathing difficulty, chest pain, confusion, or severe symptoms."
            )
            type == "temperature_c" && v.value1 >= 39.4 -> ScreeningFinding(
                category = "VITAL",
                title = "High temperature reading",
                description = "The recorded temperature is high.",
                level = "EVALUATE",
                recommendation = "Consider medical advice, especially if persistent or accompanied by concerning symptoms."
            )
            else -> null
        }
    }
}
