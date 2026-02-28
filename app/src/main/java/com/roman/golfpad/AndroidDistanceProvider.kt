package com.roman.golfpad

import android.location.Location
import com.roman.domain.DistanceProvider
import com.roman.model.GeoPoint

class AndroidDistanceProvider : DistanceProvider {
    override fun distanceBetween(p1: GeoPoint, p2: GeoPoint): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            p1.lat, p1.lon,
            p2.lat, p2.lon,
            results
        )
        return results[0]
    }
}