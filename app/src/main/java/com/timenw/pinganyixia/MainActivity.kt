package com.timenw.pinganyixia

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.timenw.pinganyixia.navigation.AppNavGraph
import com.timenw.pinganyixia.ui.theme.PingAnYiXiaTheme
import com.timenw.pinganyixia.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PingAnYiXiaTheme {
                val vm: MainViewModel = viewModel(factory = MainViewModel.factory(LocalContext.current))
                val settings by vm.settings.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                AppNavGraph(
                    viewModel = vm,
                    settings = settings
                )
            }
        }
    }
}
