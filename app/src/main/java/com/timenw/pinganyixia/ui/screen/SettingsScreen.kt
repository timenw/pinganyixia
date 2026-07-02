package com.timenw.pinganyixia.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timenw.pinganyixia.data.prefs.AppSettings
import com.timenw.pinganyixia.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: MainViewModel, settings: AppSettings) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TopAppBar(title = { Text("设置", fontSize = 24.sp, fontWeight = FontWeight.Bold) })
        SettingsSection("基础设置") {
            SettingsRow("语音播报", "完成操作时语音提示", settings.voiceEnabled, viewModel::updateVoiceEnabled)
            SettingsRow("震动反馈", "按钮点击时震动", settings.vibrationEnabled, viewModel::updateVibrationEnabled)
        }
        SettingsSection("提醒设置") {
            StaticRow("每日报平安提醒", settings.dailyCheckinReminderTime)
            StaticRow("未报平安检查", settings.missingCheckinAlertTime)
        }
        SettingsSection("无障碍") {
            SettingsRow("高对比度模式", "增强颜色对比度", settings.highContrastMode, viewModel::updateHighContrastMode)
            SettingsRow("简洁模式", "仅显示核心功能", settings.simpleMode, viewModel::updateSimpleMode)
        }
        SettingsSection("关于") {
            StaticRow("版本", "1.0.0")
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            content()
        }
    }
}

@Composable
private fun SettingsRow(title: String, subtitle: String, value: Boolean, onValueChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 20.sp)
            Text(subtitle, fontSize = 14.sp, color = Color.Gray)
        }
        Switch(checked = value, onCheckedChange = onValueChange)
    }
}

@Composable
private fun StaticRow(title: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(title, fontSize = 20.sp)
        Text(value, fontSize = 18.sp, color = Color.Gray)
    }
}
