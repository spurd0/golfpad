package com.roman.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoPoint(
    val lat: Double,
    val lon: Double
)

@Serializable
data class Green(
    val front: GeoPoint,
    val middle: GeoPoint,
    val back: GeoPoint
)

@Serializable
data class GolfHoleData(
    val holeNumber: Int,
    val par: Int,
    val green: Green
)