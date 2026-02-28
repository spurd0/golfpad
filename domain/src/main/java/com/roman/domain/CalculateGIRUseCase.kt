package com.roman.domain

import com.roman.model.GeoPoint
import com.roman.model.GolfHoleData

class CalculateGIRUseCase(private val distanceProvider: DistanceProvider) {
    operator fun invoke(hole: GolfHoleData, shots: List<GeoPoint>): Boolean {
        val greenRadius = distanceProvider.distanceBetween(
            hole.green.middle,
            hole.green.front
        )

        val firstShotOnGreenIndex = shots.indexOfFirst { shot ->
            distanceProvider.distanceBetween(shot, hole.green.middle) <= greenRadius
        }

        if (firstShotOnGreenIndex == -1) return false

        val shotsCount = firstShotOnGreenIndex + 1
        return shotsCount <= (hole.par - 2)
    }
}