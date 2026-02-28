package com.roman.domain

import com.roman.model.GolfHoleData

private const val PUTTS_RESERVE = 2
private const val INDEX_INVALID = -1

class CalculateGIRUseCase(private val distanceProvider: DistanceProvider) {
    operator fun invoke(hole: GolfHoleData): Boolean {
        val greenRadius = distanceProvider.distanceBetween(
            hole.green.middle,
            hole.green.front
        )

        val firstShotOnGreenIndex = hole.shots.indexOfFirst { shot ->
            distanceProvider.distanceBetween(shot, hole.green.middle) <= greenRadius
        }

        if (firstShotOnGreenIndex == INDEX_INVALID) return false

        val shotsToReachGreen = firstShotOnGreenIndex + 1
        val girThreshold = hole.par - PUTTS_RESERVE

        return shotsToReachGreen <= girThreshold
    }
}