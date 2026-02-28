package com.roman.golfpad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roman.data.HoleRepository
import com.roman.domain.CalculateGIRUseCase
import com.roman.model.GolfHoleData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val COURSE_DATA_FILENAME = "course_data.json"

data class HoleUiState(
    val data: GolfHoleData,
    val isGIR: Boolean
)

@HiltViewModel
class GolfViewModel @Inject constructor(
    private val repository: HoleRepository,
    private val calculateGIRUseCase: CalculateGIRUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<HoleUiState>>(emptyList())
    val uiState = _uiState.asStateFlow()

    fun loadAndCalculate() {
        viewModelScope.launch {
            val course = repository.getCourseData(COURSE_DATA_FILENAME)

            val results = course.map { hole ->
                HoleUiState(hole, calculateGIRUseCase(hole))
            }

            _uiState.value = results
        }
    }
}