package com.roman.golfpad.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.roman.domain.DistanceProvider
import com.roman.golfpad.GolfViewModel
import com.roman.golfpad.HoleUiState
import com.roman.golfpad.util.GolfTextFormatter
import com.roman.model.GolfHoleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GolfCourseScreen(
    modifier: Modifier = Modifier,
    distanceProvider: DistanceProvider,
    viewModel: GolfViewModel = hiltViewModel()
) {
    val holes by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAndCalculate()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PRO GIR TRACKER", fontWeight = FontWeight.Black) }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF2F2F7)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(holes) { holeState ->
                HoleCard(holeState, distanceProvider)
            }
        }
    }
}

@Composable
fun HoleCard(state: HoleUiState, distanceProvider: DistanceProvider) {
    var expanded by remember { mutableStateOf(false) }
    val statusColor = if (state.isGIR) Color(0xFF2E7D32) else Color(0xFFD32F2F)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            HoleHeader(state, statusColor)
            if (expanded) {
                HoleDetails(state.data, distanceProvider)
            }
        }
    }
}

@Composable
fun HoleHeader(state: HoleUiState, statusColor: Color) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = statusColor.copy(alpha = 0.1f),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "${state.data.holeNumber}",
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "PAR ${state.data.par}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = GolfTextFormatter.formatCompletionStatus(state.isHoledOut),
                style = MaterialTheme.typography.labelSmall,
                color = if (state.isHoledOut) Color.Gray else Color(0xFFD32F2F),
                fontWeight = FontWeight.Bold
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = GolfTextFormatter.formatGirStatus(state.isGIR),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = statusColor
            )
            Icon(
                imageVector = if (state.isGIR) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun HoleDetails(hole: GolfHoleData, distanceProvider: DistanceProvider) {
    val greenRadius = distanceProvider.distanceBetween(hole.green.middle, hole.green.front)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .background(Color(0xFFF8F9FA), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = "SHOT TRACKER",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Black,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        hole.shots.forEachIndexed { index, shot ->
            val distToPin = distanceProvider.distanceBetween(shot, hole.green.middle)
            val isInGreen = distToPin <= greenRadius

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = GolfTextFormatter.formatShotLabel(index, isInGreen),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = GolfTextFormatter.formatDistanceLabel(distToPin, greenRadius),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = if (isInGreen) Color(0xFF2E7D32) else Color.Black
                )
            }
        }
    }
}

@Composable
fun CourseSummaryCard(holes: List<HoleUiState>) {
    val girCount = holes.count { it.isGIR }
    val dnfCount = holes.count { !it.isHoledOut }
    val girPercent = if (holes.isNotEmpty()) (girCount * 100 / holes.size) else 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("GIR RATE", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                Text(
                    "$girPercent%",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("ROUND STATS", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                Text(
                    "$girCount/${holes.size} GIR",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                if (dnfCount > 0) {
                    Text(
                        "$dnfCount DNF",
                        color = Color(0xFFE57373),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}