package com.timenw.pinganyixia.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timenw.pinganyixia.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToContacts: () -> Unit,
    onNavigateToMedicine: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val medicines by viewModel.medicines.collectAsStateWithLifecycle()
    val todayCheckin by viewModel.todayCheckin.collectAsStateWithLifecycle()
    val todayLogs by viewModel.todayLogs.collectAsStateWithLifecycle()
    val message by viewModel.lastMessage.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(hostState = snackbarHostState)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("平安一下", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text("给长辈的极简安全关怀", fontSize = 18.sp, color = Color.Gray)
            Spacer(Modifier.height(16.dp))
            StatusCard(
                checkedIn = todayCheckin != null,
                medicineCount = medicines.size,
                takenCount = todayLogs.size
            )
            Spacer(Modifier.height(24.dp))
            LargeActionButton("✓", "我今天很好", Color(0xFF2E7D32), todayCheckin == null) { viewModel.checkin() }
            Spacer(Modifier.height(16.dp))
            LargeActionButton("💊", "已吃药", Color(0xFF1565C0), true) { onNavigateToMedicine() }
            Spacer(Modifier.height(16.dp))
            LargeActionButton("🆘", "紧急求助", Color(0xFFC62828), true) { viewModel.triggerEmergency() }
            Spacer(Modifier.height(24.dp))
            QuickLinkRow("👥", "联系人", onNavigateToContacts)
            QuickLinkRow("💊", "用药计划", onNavigateToMedicine)
            QuickLinkRow("📋", "历史记录", onNavigateToHistory)
            QuickLinkRow("⚙️", "设置", onNavigateToSettings)
        }
    }
}

@Composable
private fun StatusCard(checkedIn: Boolean, medicineCount: Int, takenCount: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("今日状态", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            StatusRow("报平安", if (checkedIn) "已完成" else "未完成", if (checkedIn) Color(0xFF2E7D32) else Color(0xFFC62828))
            StatusRow("用药打卡", "$takenCount / $medicineCount", Color(0xFF1565C0))
        }
    }
}

@Composable
private fun StatusRow(label: String, value: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 20.sp)
        Text(value, fontSize = 20.sp, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun LargeActionButton(icon: String, text: String, color: Color, enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(86.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White)
    ) {
        Text("$icon  $text", fontSize = 28.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun QuickLinkRow(icon: String, label: String, onClick: () -> Unit) {
    Text(
        text = "$icon  $label",
        fontSize = 22.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(18.dp)
    )
}
