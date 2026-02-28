package com.roman.data

import android.content.Context
import com.roman.model.GolfHoleData
import kotlinx.serialization.json.Json

class HoleRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getHoleData(fileName: String): GolfHoleData {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        return json.decodeFromString(jsonString)
    }
}