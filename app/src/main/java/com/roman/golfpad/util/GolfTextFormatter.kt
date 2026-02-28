package com.roman.golfpad.util

object GolfTextFormatter {
    private const val METERS_TO_YARDS = 1.09361f

    fun toYards(meters: Float): String = "${(meters * METERS_TO_YARDS).toInt()} YDS"

    fun formatParLabel(par: Int): String = "PAR $par"

    fun formatGirStatus(isGIR: Boolean): String = if (isGIR) "IN REG" else "MISSED"

    fun formatCompletionStatus(isHoled: Boolean): String = if (isHoled) "HOLED OUT" else "DNF"

    fun formatShotLabel(index: Int, isInGreen: Boolean): String {
        val icon = if (isInGreen) "⛳" else "🏌️"
        return "Shot ${index + 1} $icon"
    }

    fun formatDistanceLabel(meters: Float, greenRadius: Float): String {
        val isInGreen = meters <= greenRadius
        val targetDist = if (isInGreen) meters else (meters - greenRadius)
        val suffix = if (isInGreen) "TO PIN" else "TO GREEN"
        return "${toYards(targetDist)} $suffix"
    }
}