package com.timenw.pinganyixia.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timenw.pinganyixia.data.local.entity.MedicineEntity
import com.timenw.pinganyixia.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineScreen(viewModel: MainViewModel) {
    val medicines by viewModel.medicines.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(title = { Text("用药提醒", fontSize = 24.sp, fontWeight = FontWeight.Bold) }, actions = {
            IconButton(onClick = { showAddDialog = true }) { Icon(Icons.Default.Add, contentDescription = "添加") }
        })
        if (medicines.isEmpty()) {
            Text("暂无药品，点击右上角添加", fontSize = 20.sp, modifier = Modifier.padding(24.dp))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(medicines.size) { index ->
                    MedicineCard(medicines[index], viewModel)
                }
            }
        }
    }
    if (showAddDialog) {
        AddMedicineDialog(onDismiss = { showAddDialog = false }) { name, dosage ->
            viewModel.addMedicine(name, dosage)
            showAddDialog = false
        }
    }
}

@Composable
private fun MedicineCard(medicine: MedicineEntity, viewModel: MainViewModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(medicine.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("用法：${medicine.dosage}", fontSize = 18.sp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { viewModel.markMedicineTaken(medicine) }) { Icon(Icons.Default.Check, contentDescription = "已吃") }
                IconButton(onClick = { viewModel.deleteMedicine(medicine.id) }) { Icon(Icons.Default.Delete, contentDescription = "删除") }
            }
        }
    }
}

@Composable
private fun AddMedicineDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加药品") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(value = name, onValueChange = { name = it }, label = { Text("药品名称") }, singleLine = true)
                TextField(value = dosage, onValueChange = { dosage = it }, label = { Text("剂量/用法") }, singleLine = true)
            }
        },
        confirmButton = { Button(onClick = { onConfirm(name, dosage) }) { Text("确定") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } }
    )
}
