package com.example.habitstracker.utils

object BMIUtils {
    fun calculateBMI(
        weight: Float,
        heightCm: Float
    ): Float {
        val heightM = heightCm / 100f
        return weight / (heightM * heightM)
    }
    fun getBMIStatus(
        bmi: Float
    ): String {
        return when {
            bmi < 18.5f ->
                "Underweight"
            bmi < 25f ->
                "Normal"
            bmi < 30f ->
                "Overweight"
            else ->
                "Obese"
        }
    }
}