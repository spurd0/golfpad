package com.roman.data

import android.content.Context
import com.roman.model.GolfHoleData
import kotlinx.serialization.json.Json

class HoleRepository(private val context: Context) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun getCourseData(fileName: String): List<GolfHoleData> {
        // TODO: may add different sources (local, remote, etc..)
        return try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            json.decodeFromString<List<GolfHoleData>>(jsonString)
        } catch (e: Exception) {
            //TODO: handle exception, at least log it
            emptyList()
        }
    }
}