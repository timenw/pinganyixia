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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timenw.pinganyixia.data.local.entity.ContactEntity
import com.timenw.pinganyixia.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(viewModel: MainViewModel) {
    val contacts by viewModel.contacts.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(title = { Text("联系人", fontSize = 24.sp, fontWeight = FontWeight.Bold) }, actions = {
            IconButton(onClick = { showAddDialog = true }) { Icon(Icons.Default.Add, contentDescription = "添加") }
        })
        if (contacts.isEmpty()) {
            Text("暂无联系人，点击右上角添加", fontSize = 20.sp, modifier = Modifier.padding(24.dp))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(contacts.size) { index -> ContactCard(contacts[index], viewModel) }
            }
        }
    }
    if (showAddDialog) {
        AddContactDialog(onDismiss = { showAddDialog = false }) { name, relation, phone, emergency ->
            viewModel.addContact(name, relation, phone, emergency)
            showAddDialog = false
        }
    }
}

@Composable
private fun ContactCard(contact: ContactEntity, viewModel: MainViewModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(contact.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    if (contact.isEmergency) Icon(Icons.Default.Star, contentDescription = "紧急", tint = Color(0xFFFFC107))
                }
                Text("${contact.relation} · ${contact.phone}", fontSize = 18.sp, color = Color.Gray)
            }
            IconButton(onClick = { viewModel.deleteContact(contact.id) }) { Icon(Icons.Default.Delete, contentDescription = "删除") }
        }
    }
}

@Composable
private fun AddContactDialog(onDismiss: () -> Unit, onConfirm: (String, String, String, Boolean) -> Unit) {
    var name by remember { mutableStateOf("") }
    var relation by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var emergency by remember { mutableStateOf(true) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("添加联系人") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(value = name, onValueChange = { name = it }, label = { Text("姓名") }, singleLine = true)
                TextField(value = relation, onValueChange = { relation = it }, label = { Text("关系") }, singleLine = true)
                TextField(value = phone, onValueChange = { phone = it }, label = { Text("手机号") }, singleLine = true)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("设为紧急联系人", fontSize = 18.sp)
                    Switch(checked = emergency, onCheckedChange = { emergency = it })
                }
            }
        },
        confirmButton = { Button(onClick = { onConfirm(name, relation, phone, emergency) }) { Text("确定") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } }
    )
}
