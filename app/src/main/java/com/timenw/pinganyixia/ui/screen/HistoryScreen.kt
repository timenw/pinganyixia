package com.timenw.pinganyixia.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timenw.pinganyixia.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: MainViewModel) {
    val checkin by viewModel.todayCheckin.collectAsStateWithLifecycle()
    val logs by viewModel.todayLogs.collectAsStateWithLifecycle()
    val emergencies by viewModel.recentEmergencies.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TopAppBar(title = { Text("历史记录", fontSize = 24.sp, fontWeight = FontWeight.Bold) })
        RecordCard("今日报平安") {
            Text(if (checkin != null) "✓ ${formatTime(checkin!!.checkedInAt)}" else "✗ 未报平安", fontSize = 18.sp, color = if (checkin != null) Color(0xFF2E7D32) else Color(0xFFC62828))
        }
        RecordCard("今日用药") {
            if (logs.isEmpty()) Text("暂无用药记录", fontSize = 18.sp, color = Color.Gray) else logs.forEach { Text("✓ ${it.medicineName} - ${formatTime(it.takenAt)}", fontSize = 16.sp) }
        }
        RecordCard("紧急求助记录") {
            if (emergencies.isEmpty()) Text("暂无紧急记录", fontSize = 18.sp, color = Color.Gray) else emergencies.forEach { Text("🆘 ${it.contactName} ${formatTime(it.createdAt)}", fontSize = 16.sp) }
        }
    }
}

@Composable
private fun RecordCard(title: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

private fun formatTime(timestamp: Long): String = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(Date(timestamp))
