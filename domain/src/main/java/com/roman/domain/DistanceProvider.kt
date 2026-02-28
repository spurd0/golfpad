package com.roman.domain

import com.roman.model.GeoPoint

interface DistanceProvider {
    fun distanceBetween(p1: GeoPoint, p2: GeoPoint): Float
}