package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.EcosystemViewModel
import com.example.viewmodel.JournalEntry

@Composable
fun WellnessScreen(viewModel: EcosystemViewModel) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val journals by viewModel.journalEntries.collectAsState()
    val rawText by viewModel.currentJournalText.collectAsState()
    val activeMoodIndex by viewModel.currentMoodIndex.collectAsState()

    // Breathing guidance active timers
    var breathCycleCount by remember { mutableStateOf(0) }
    var breathPhase by remember { mutableStateOf("Inhale") } // Inhale -> Hold -> Exhale
    var cycleTrigger by remember { mutableStateOf(0) }

    // Auto timing cycles
    LaunchedEffect(cycleTrigger) {
        breathPhase = "Inhale"
        kotlinx.coroutines.delay(4000)
        breathPhase = "Hold"
        kotlinx.coroutines.delay(4000)
        breathPhase = "Exhale"
        kotlinx.coroutines.delay(4000)
        breathCycleCount += 1
        cycleTrigger += 1 // Repeat loop infinitely
    }

    // Expanding Pulse Animation linked to Breathing Phase
    val infiniteTransition = rememberInfiniteTransition(label = "breath")
    val breathProgress by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathProgress"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBgDark)
    ) {
        // Calm calming green radial glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentWellness.copy(alpha = 0.04f), Color.Transparent),
                        radius = 800f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { viewModel.goBack() },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BrandCardLight.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = TextLight
                    )
                }

                Text(
                    text = "SOULRESET STUDIO",
                    color = AccentWellness,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                // Placeholder quiet mode toggle button
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Quiet Mode activated. All system notifications muted.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BrandCardLight.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.DoNotDisturbOn,
                        contentDescription = "Do not disturb",
                        tint = AccentWellness
                    )
                }
            }

            // Scrollable Workspace Layout
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Intro Panel
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Wellness & Community Hub".uppercase(),
                            color = TextGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Calm Alignment Spaces",
                            color = TextLight,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Take a moment to align your creative energies. Engage with our synchronized breathing ring or log reflections in your local secure timeline ledger.",
                            color = TextGray,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                // --- 1. DYNAMIC BREATH RING CANVAS WIDGET ---
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BrandCardDark, RoundedCornerShape(24.dp))
                            .border(1.dp, BorderDark, RoundedCornerShape(24.dp))
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "BREATH ALIGNMENT SPHERE",
                            color = AccentWellness,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Canvas Vector Sphere
                        Box(
                            modifier = Modifier
                                .size(170.dp)
                                .drawBehind {
                                    val progressRadius = (size.width * 0.45f) * (if (breathPhase == "Inhale") breathProgress else if (breathPhase == "Exhale") (1.5f - breathProgress).coerceIn(0.5f, 1.0f) else 1.0f)

                                    // Outer tranquil glowing ring
                                    drawCircle(
                                        color = AccentWellness.copy(alpha = 0.05f),
                                        radius = progressRadius * 1.3f
                                    )

                                    // Mid pulsating ring
                                    drawCircle(
                                        color = AccentWellness.copy(alpha = 0.12f),
                                        radius = progressRadius * 1.1f
                                    )

                                    // Core structure border
                                    drawCircle(
                                        color = AccentWellness,
                                        radius = progressRadius,
                                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                                            width = 1.5.dp.toPx()
                                        )
                                    )

                                    // Central anchors
                                    drawCircle(
                                        color = BrandCardLight,
                                        radius = 24.dp.toPx()
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = breathPhase.uppercase(),
                                    color = TextLight,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = when (breathPhase) {
                                        "Inhale" -> "Expand"
                                        "Hold" -> "Absorb"
                                        else -> "Release"
                                    },
                                    color = TextGray,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Animated narrative texts
                        Text(
                            text = when (breathPhase) {
                                "Inhale" -> "Inhale the golden brand alignments..."
                                "Hold" -> "Rest in the quiet spacious silence..."
                                else -> "Exhale old constraints & design slop..."
                            },
                            color = TextLight,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Cycles Completed today: $breathCycleCount",
                            color = TextGray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // --- 2. THE LOCAL SECURE TIMELINE LEDGER ---
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BrandCardDark, RoundedCornerShape(24.dp))
                            .border(1.dp, BorderDark, RoundedCornerShape(24.dp))
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "DAILY REFLECTION LOGS",
                            color = AccentWellness,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Express Your Focus Intention",
                            color = TextLight,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Mood choice icons row
                        Text(text = "How is your creative energy?", color = TextGray, fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val moods = listOf(
                                "✨" to "Energetic",
                                "📐" to "Focused",
                                "⚖️" to "Balanced",
                                "🌊" to "Calm",
                                "🌙" to "Dreamy"
                            )

                            moods.forEachIndexed { idx, pair ->
                                val score = idx + 1
                                val isSelected = activeMoodIndex == score
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) AccentWellness.copy(alpha = 0.15f) else Color.Transparent)
                                        .clickable { viewModel.setMoodIndex(score) }
                                        .padding(6.dp)
                                ) {
                                    Text(text = pair.first, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = pair.second, color = if (isSelected) AccentWellness else TextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Journal Notes Text Field
                        OutlinedTextField(
                            value = rawText,
                            onValueChange = { viewModel.setJournalText(it) },
                            placeholder = { Text("What projects are we designing list, or what focus is aligned?", color = TextGray, fontSize = 12.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextLight,
                                unfocusedTextColor = TextLight,
                                focusedBorderColor = AccentWellness,
                                unfocusedBorderColor = BorderDark
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (rawText.isNotBlank()) {
                                    viewModel.saveJournalEntry()
                                    keyboardController?.hide()
                                    Toast.makeText(context, "Journal entry logged successfully!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Please type a short note first!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentWellness),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = "", tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Commit Reflections to Ledger", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Header for saved notes
                item {
                    Text(
                        text = "PAST TIMELINE ENTRIES",
                        color = TextGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }

                // Render Logs
                if (journals.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = BrandCardDark),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, BorderDark)
                        ) {
                            Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "No private reflections saved yet. Capture your first design focus above!",
                                    color = TextGray,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(journals) { article ->
                        TimelineLogCard(entry = article) {
                            viewModel.deleteJournalEntry(article)
                            Toast.makeText(context, "Reflection removed successfully.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineLogCard(entry: JournalEntry, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderDark, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = BrandCardDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mood tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AccentWellness.copy(alpha = 0.08f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = entry.mood,
                        color = AccentWellness,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = entry.date,
                        color = TextGray,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = "Delete log entry",
                            tint = Color.Red.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = BorderDark)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = entry.note,
                color = TextLight,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}
