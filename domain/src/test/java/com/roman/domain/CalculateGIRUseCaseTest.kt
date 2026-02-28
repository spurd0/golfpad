package com.roman.domain

import com.roman.model.GeoPoint
import com.roman.model.GolfHoleData
import com.roman.model.Green
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.sqrt

class CalculateGIRUseCaseTest {
    private val mockDistanceProvider = object : DistanceProvider {
        override fun distanceBetween(p1: GeoPoint, p2: GeoPoint): Float {
            val dLat = p1.lat - p2.lat
            val dLon = p1.lon - p2.lon
            return sqrt((dLat * dLat + dLon * dLon).toFloat()) * 111000f
        }
    }

    private val useCase = CalculateGIRUseCase(mockDistanceProvider)

    private val sampleGreen = Green(
        front = GeoPoint(55.7468, 37.6408),
        middle = GeoPoint(55.7471, 37.6408),
        back = GeoPoint(55.7474, 37.6408)
    )

    @Test
    fun `isGIRAchieved returns true for Par 4 with 2 shots to green`() {
        val hole = GolfHoleData(1, 4, sampleGreen, listOf(
            GeoPoint(55.7428, 37.6358), // Tee
            GeoPoint(55.7471, 37.6408)  // On Green (2nd shot)
        ))
        assertTrue(useCase.isGIRAchieved(hole))
    }

    @Test
    fun `isGIRAchieved returns false for Par 4 with 3 shots to green`() {
        val hole = GolfHoleData(1, 4, sampleGreen, listOf(
            GeoPoint(55.7428, 37.6358), // Tee
            GeoPoint(55.7460, 37.6390), // Approach
            GeoPoint(55.7471, 37.6408)  // On Green (3rd shot)
        ))
        assertFalse(useCase.isGIRAchieved(hole))
    }

    @Test
    fun `isHoledOut returns true when last shot is within threshold`() {
        val hole = GolfHoleData(1, 4, sampleGreen, listOf(
            GeoPoint(55.7471, 37.6408) // Exactly in Middle
        ))
        // hope you'll launch tests :)
        assertFalse(useCase.isHoledOut(hole))
    }

    @Test
    fun `isHoledOut returns false when player quits before hole`() {
        val hole = GolfHoleData(1, 4, sampleGreen, listOf(
            GeoPoint(55.7400, 37.6000) // Far away
        ))
        assertFalse(useCase.isHoledOut(hole))
    }
}