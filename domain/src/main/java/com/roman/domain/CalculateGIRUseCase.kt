package com.roman.domain

import com.roman.model.GolfHoleData

private const val PUTTS_RESERVE = 2
private const val HOLED_OUT_THRESHOLD_METERS = 0.5f
private const val INDEX_NOT_FOUND = -1

class CalculateGIRUseCase(private val distanceProvider: DistanceProvider) {
    fun isGIRAchieved(hole: GolfHoleData): Boolean {
        if (hole.shots.isEmpty()) return false

        val greenRadius = distanceProvider.distanceBetween(
            hole.green.middle,
            hole.green.front
        )

        val firstShotOnGreenIndex = hole.shots.indexOfFirst { shot ->
            distanceProvider.distanceBetween(shot, hole.green.middle) <= greenRadius
        }

        if (firstShotOnGreenIndex == INDEX_NOT_FOUND) return false

        val shotsToReachGreen = firstShotOnGreenIndex + 1
        val girThreshold = hole.par - PUTTS_RESERVE

        return shotsToReachGreen <= girThreshold
    }

    fun isHoledOut(hole: GolfHoleData): Boolean {
        if (hole.shots.isEmpty()) return false
        val lastShot = hole.shots.last()
        val distanceToPin = distanceProvider.distanceBetween(lastShot, hole.green.middle)
        // gps is not so accurate
        return distanceToPin <= HOLED_OUT_THRESHOLD_METERS
    }
}