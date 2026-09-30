package com.example.healthscanai.ui

import android.Manifest
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.healthscanai.HealthScanApplication
import com.example.healthscanai.data.local.VitalMeasurement
import com.example.healthscanai.screening.SafetyRules
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.*
import java.io.File

@Composable
fun HealthScanApp() {
    var tab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    "Home" to Icons.Default.Home,
                    "Scan" to Icons.Default.CameraAlt,
                    "Vitals" to Icons.Default.Favorite,
                    "Symptoms" to Icons.Default.MedicalServices,
                    "Labs" to Icons.Default.Description,
                    "Settings" to Icons.Default.Settings
                ).forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = tab == index,
                        onClick = { tab = index },
                        icon = { Icon(item.second, contentDescription = item.first) },
                        label = { Text(item.first) }
                    )
                }
            }
        }
    ) { padding ->
        when (tab) {
            0 -> Dashboard(Modifier.padding(padding))
            1 -> CameraScanScreen(Modifier.padding(padding))
            2 -> VitalsScreen(Modifier.padding(padding))
            3 -> SymptomsScreen(Modifier.padding(padding))
            4 -> LabsScreen(Modifier.padding(padding))
            5 -> SettingsScreen(Modifier.padding(padding))
        }
    }
}


@Composable
private fun CameraScanScreen(modifier: Modifier) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var cameraReady by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Position the body area clearly in the frame.") }

    val hasPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Body / Skin Scan", style = MaterialTheme.typography.headlineSmall)
        Text(
            "This captures an image for future validated screening models. " +
                "It does not diagnose disease.",
            modifier = Modifier.padding(12.dp)
        )

        if (!hasPermission) {
            Text("Camera permission is required. Enable it in Settings.")
        } else {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                update = { previewView ->
                    val future = ProcessCameraProvider.getInstance(context)
                    future.addListener({
                        val provider = future.get()
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }
                        val capture = ImageCapture.Builder().build()
                        imageCapture = capture
                        try {
                            provider.unbindAll()
                            provider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                capture
                            )
                            cameraReady = true
                        } catch (_: Exception) {
                            cameraReady = false
                            message = "Unable to start the camera."
                        }
                    }, ContextCompat.getMainExecutor(context))
                }
            )
        }

        Text(message, modifier = Modifier.padding(8.dp))
        Button(
            enabled = cameraReady,
            onClick = {
                val capture = imageCapture ?: return@Button
                val file = File(
                    context.filesDir,
                    "scan_${System.currentTimeMillis()}.jpg"
                )
                val output = ImageCapture.OutputFileOptions.Builder(file).build()
                capture.takePicture(
                    output,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(
                            outputFileResults: ImageCapture.OutputFileResults
                        ) {
                            message = "Saved offline: ${file.name}"
                            Toast.makeText(
                                context,
                                "Image saved locally",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onError(exception: ImageCaptureException) {
                            message = "Capture failed: ${exception.message ?: "unknown error"}"
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { Text("Capture image") }
    }
}

@Composable
private fun Dashboard(modifier: Modifier) {
    val context = LocalContext.current
    val app = context.applicationContext as HealthScanApplication
    val vitals by app.repository.vitals().collectAsState(initial = emptyList())
    val findings by app.repository.findings().collectAsState(initial = emptyList())

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("HealthScan AI", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Offline-first health screening and monitoring",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        item {
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Important", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "This app records and screens health information. It does not diagnose disease."
                    )
                }
            }
        }
        item {
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Recent vitals", style = MaterialTheme.typography.titleMedium)
                    Text("${vitals.size} measurements stored locally")
                }
            }
        }
        item {
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("Screening findings", style = MaterialTheme.typography.titleMedium)
                    Text("${findings.size} findings stored locally")
                }
            }
        }
    }
}

@Composable
private fun VitalsScreen(modifier: Modifier) {
    val context = LocalContext.current
    val app = context.applicationContext as HealthScanApplication
    val scope = rememberCoroutineScope()
    val vitals by app.repository.vitals().collectAsState(initial = emptyList())

    var type by remember { mutableStateOf("heart_rate") }
    var value by remember { mutableStateOf("") }
    var value2 by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("bpm") }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Vitals", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(type, { type = it }, label = { Text("Type") })
            OutlinedTextField(value, { value = it }, label = { Text("Value") })
            OutlinedTextField(value2, { value2 = it }, label = { Text("Second value (optional)") })
            OutlinedTextField(unit, { unit = it }, label = { Text("Unit") })
            Button(
                onClick = {
                    val v = value.toDoubleOrNull() ?: return@Button
                    val second = value2.toDoubleOrNull()
                    scope.launch {
                        val item = VitalMeasurement(
                            type = type,
                            value1 = v,
                            value2 = second,
                            unit = unit
                        )
                        app.repository.addVital(item)
                        SafetyRules.evaluate(item)?.let { app.repository.addFinding(it) }
                        value = ""
                        value2 = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save measurement") }
        }

        items(vitals, key = { it.id }) { v ->
            ListItem(
                headlineContent = { Text("${v.type}: ${v.value1} ${v.unit}") },
                supportingContent = {
                    Text(DateFormat.getDateTimeInstance().format(Date(v.recordedAt)))
                }
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun SymptomsScreen(modifier: Modifier) {
    val context = LocalContext.current
    val app = context.applicationContext as HealthScanApplication
    val scope = rememberCoroutineScope()
    val symptoms by app.repository.symptoms().collectAsState(initial = emptyList())

    var area by remember { mutableStateOf("General") }
    var symptom by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf("1") }
    var duration by remember { mutableStateOf("1") }
    var notes by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Symptoms", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(area, { area = it }, label = { Text("Body area") })
            OutlinedTextField(symptom, { symptom = it }, label = { Text("Symptom") })
            OutlinedTextField(severity, { severity = it }, label = { Text("Severity 1–10") })
            OutlinedTextField(duration, { duration = it }, label = { Text("Duration in days") })
            OutlinedTextField(notes, { notes = it }, label = { Text("Notes") })
            Button(
                onClick = {
                    if (symptom.isBlank()) return@Button
                    scope.launch {
                        app.repository.addSymptom(
                            com.example.healthscanai.data.local.SymptomEvent(
                                bodyArea = area,
                                symptom = symptom,
                                severity = severity.toIntOrNull()?.coerceIn(1, 10) ?: 1,
                                durationDays = duration.toIntOrNull()?.coerceAtLeast(0) ?: 0,
                                notes = notes
                            )
                        )
                        symptom = ""
                        notes = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save symptom") }
        }

        items(symptoms, key = { it.id }) {
            ListItem(
                headlineContent = { Text("${it.bodyArea}: ${it.symptom}") },
                supportingContent = { Text("Severity ${it.severity}/10 • ${it.durationDays} days") }
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun LabsScreen(modifier: Modifier) {
    val context = LocalContext.current
    val app = context.applicationContext as HealthScanApplication
    val scope = rememberCoroutineScope()
    val labs by app.repository.labs().collectAsState(initial = emptyList())

    var name by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var range by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Lab results", style = MaterialTheme.typography.headlineSmall)
            Text("Enter OCR-extracted values here until the full report parser is added.")
            OutlinedTextField(name, { name = it }, label = { Text("Test name") })
            OutlinedTextField(value, { value = it }, label = { Text("Result") })
            OutlinedTextField(unit, { unit = it }, label = { Text("Unit") })
            OutlinedTextField(range, { range = it }, label = { Text("Reference range") })
            Button(
                onClick = {
                    if (name.isBlank() || value.isBlank()) return@Button
                    scope.launch {
                        app.repository.addLab(
                            com.example.healthscanai.data.local.LabResult(
                                testName = name,
                                value = value,
                                unit = unit,
                                referenceRange = range
                            )
                        )
                        name = ""; value = ""; unit = ""; range = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save lab result") }
        }

        items(labs, key = { it.id }) {
            ListItem(
                headlineContent = { Text("${it.testName}: ${it.value} ${it.unit}") },
                supportingContent = { Text("Reference: ${it.referenceRange}") }
            )
            HorizontalDivider()
        }
    }
}

@Composable
private fun SettingsScreen(modifier: Modifier) {
    val context = LocalContext.current
    val app = context.applicationContext as HealthScanApplication
    var enabled by remember { mutableStateOf(app.syncRepository.enabled()) }
    var url by remember { mutableStateOf(app.syncRepository.baseUrl()) }
    var cameraGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { cameraGranted = it }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)

        Text("Camera: ${if (cameraGranted) "granted" else "not granted"}")
        Button(
            onClick = { cameraLauncher.launch(Manifest.permission.CAMERA) }
        ) { Text("Request camera permission") }

        HorizontalDivider()

        Text("Optional online sync", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("HTTPS API base URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Enable sync")
            Switch(
                checked = enabled,
                onCheckedChange = {
                    enabled = it
                    app.syncRepository.configure(url, enabled)
                }
            )
        }

        Button(
            onClick = {
                app.syncRepository.configure(url, enabled)
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Save sync settings") }

        Text(
            "Online synchronization is optional and disabled by default. " +
                "Use HTTPS and a properly authenticated server before storing real health data.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
