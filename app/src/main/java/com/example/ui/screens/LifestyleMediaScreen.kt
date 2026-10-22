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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.EcosystemViewModel

data class EditorialPost(
    val title: String,
    val date: String,
    val readTime: String,
    val category: String,
    val snippet: String,
    val body: String
)

@Composable
fun LifestyleMediaScreen(viewModel: EcosystemViewModel) {
    val context = LocalContext.current

    val isPlaying by viewModel.isPlayingPodcast.collectAsState()
    val progress by viewModel.podcastProgress.collectAsState()
    val activeTrack by viewModel.currentPodcastTitle.collectAsState()

    var activeArticleDetail by remember { mutableStateOf<EditorialPost?>(null) }

    // Animated Bouncing Waveform values when Playing
    val infiniteTransition = rememberInfiniteTransition(label = "audioWave")
    val waveHeightPercent1 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "w1"
    )
    val waveHeightPercent2 by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 0.1f,
        animationSpec = infiniteRepeatable(tween(350, easing = LinearEasing), RepeatMode.Reverse),
        label = "w2"
    )
    val waveHeightPercent3 by infiniteTransition.animateFloat(
        initialValue = 0.1f, targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(500, easing = FastOutLinearInEasing), RepeatMode.Reverse),
        label = "w3"
    )
    val waveHeightPercent4 by infiniteTransition.animateFloat(
        initialValue = 0.7f, targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(400, easing = EaseInOutBounce), RepeatMode.Reverse),
        label = "w4"
    )

    // Curated Editorial Magazine Database
    val articles = listOf(
        EditorialPost(
            title = "Aesthetic Alignment: The Art of Brand Serenity",
            date = "June 2026",
            readTime = "5 min read",
            category = "Creative Ethos",
            snippet = "Discover why high-end digital spaces lean towards generous negative space and sand-neutral color schemes to evoke trust and premium authority.",
            body = "In an era saturated with strobe-light visual assets and over-engineered digital designs, silence has become the ultimate luxury. Luxury brand director Enid Kimberly explores how minimalism is not merely an aesthetic rule, but a profound canvas that respects client focus. \n\nBy leveraging muted earth tones like sand beiges, soft gold highlights, and tranquil slate greys, a brand can position itself with an aura of deliberate serenity. When applied to digital storefront architectures, these cohesive gradients allow products like stickers and mockups to speak clearly without visual clutter."
        ),
        EditorialPost(
            title = "Luxe Lightroom Formula: Balancing Organic Warmth",
            date = "May 2026",
            readTime = "4 min read",
            category = "Preset Guide",
            snippet = "An analytical breakdown of highlight curves and golden-hour exposures that formulate our proprietary Luxe Champagne collection.",
            body = "Professional photographers and creators understand that color is a narrative channel. Our 'Luxe Champagne' Lightroom mobile curves are engineered to gently slide color temperatures towards warm golden fields, while retaining clean cream-white highlights. This creates a soft, editorial texture that elevates flat smartphone imagery into a cinematic design asset. \n\nTo recreate this premium look manually, reduce green saturation by 15%, increase exposure levels by +0.3 EV, and apply a gentle warm-radial gradient to emphasize the focal content."
        ),
        EditorialPost(
            title = "The Integrated Workspace: From Custom Badges to Habits",
            date = "April 2026",
            readTime = "3 min read",
            category = "Cohesive Living",
            snippet = "How physical name plates and local tracker sheets build a lifestyle routine centered on creative growth and community loops.",
            body = "A creative professional's environment directly conditions their output. Combining custom physical elements (such as double-layered etched name tags or hand-stained walnut clipboards) with digital routines ensures synchronization. Our 'Soul Reset Habit Tracker' sheets allow entrepreneurs to monitor their breathing cycles, review brand identity spreadsheets, and log creative intentions in a cohesive loop. Physicality and digital utility exist in perfect unity."
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBgDark)
    ) {
        // Decorative Earth Sunset Glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentLifestyle.copy(alpha = 0.04f), Color.Transparent),
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
                    text = "LIFESTYLE AND MEDIA",
                    color = AccentLifestyle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                // Dummy volume feedback button
                IconButton(
                    onClick = {
                        Toast.makeText(context, "Haptic media feedback sync established.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BrandCardLight.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Audio feedback toggle",
                        tint = AccentLifestyle
                    )
                }
            }

            // Scrollable Content
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Section Title Intro
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Aesthetic Media & Editorial".uppercase(),
                            color = TextGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Luxe Curation Portal",
                            color = TextLight,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tune in to Enid Kimberly's executive audio sessions or explore her design-theory articles. The meeting place of digital mastery and lifestyle curation.",
                            color = TextGray,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                // --- 1. PREMIUM COHESIVE PODCAST INTERACTION STATION ---
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BrandCardDark, RoundedCornerShape(24.dp))
                            .border(1.dp, BorderDark, RoundedCornerShape(24.dp))
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "ENID KIMBERLY PODCAST",
                            color = AccentLifestyle,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Creative Directors Broadcast",
                            color = TextLight,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Selected Podcast card layout
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BrandCardLight.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                .border(1.dp, BorderDark, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Disk Icon with wave glows
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(AccentLifestyle.copy(alpha = 0.12f))
                                    .border(1.dp, AccentLifestyle.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Podcasts,
                                    contentDescription = "",
                                    tint = AccentLifestyle,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = activeTrack,
                                    color = TextLight,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (isPlaying) "Playing Podcast..." else "Paused",
                                    color = if (isPlaying) AccentLifestyle else TextGray,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Audio Waveform Bouncing Bars (Responsive feedback details)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                                .background(BrandCardLight.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Waveform Feed:",
                                color = TextGray,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(end = 6.dp)
                            )

                            // Bouncing Waveforms loops
                            val numBars = 16
                            for (i in 0 until numBars) {
                                val factor = when (i % 4) {
                                    0 -> if (isPlaying) waveHeightPercent1 else 0.2f
                                    1 -> if (isPlaying) waveHeightPercent2 else 0.4f
                                    2 -> if (isPlaying) waveHeightPercent3 else 0.15f
                                    else -> if (isPlaying) waveHeightPercent4 else 0.35f
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(factor)
                                        .clip(RoundedCornerShape(30.dp))
                                        .background(
                                            if (isPlaying) AccentLifestyle else TextGray.copy(alpha = 0.3f)
                                        )
                                )
                            }
                        }

                        // Progress Slider
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val elapsedMinutes = (progress * 25.0f).toInt()
                            val seconds = ((progress * 25.0f - elapsedMinutes) * 60.0f).toInt()
                            Text(
                                text = "%02d:%02d".format(elapsedMinutes, seconds),
                                color = TextGray,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.width(36.dp)
                            )

                            Slider(
                                value = progress,
                                onValueChange = { viewModel.updatePodcastProgress(it) },
                                colors = SliderDefaults.colors(
                                    thumbColor = AccentLifestyle,
                                    activeTrackColor = AccentLifestyle,
                                    inactiveTrackColor = BorderDark
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "25:00",
                                color = TextGray,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.width(36.dp),
                                textAlign = TextAlign.End
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Controls buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                val backVal = (progress - 0.05f).coerceAtLeast(0.0f)
                                viewModel.updatePodcastProgress(backVal)
                            }) {
                                Icon(Icons.Default.Replay5, contentDescription = "Rewind 5s", tint = TextLight)
                            }

                            Spacer(modifier = Modifier.width(18.dp))

                            // Glowing play circular frame
                            IconButton(
                                onClick = { viewModel.togglePodcast() },
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(AccentLifestyle, AccentLifestyle.copy(alpha = 0.7f))
                                        )
                                    )
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play podcast toggle",
                                    tint = Color.Black,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(18.dp))

                            IconButton(onClick = {
                                val forwardVal = (progress + 0.05f).coerceAtMost(1.0f)
                                viewModel.updatePodcastProgress(forwardVal)
                            }) {
                                Icon(Icons.Default.Forward5, contentDescription = "Fast-forward 5s", tint = TextLight)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = BorderDark)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Other episode listings
                        Text(text = "Choose Premium Broadcast Episode", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        val episodes = listOf(
                            "EP 12: Crafting Space for Luxury Digital Assets",
                            "EP 11: Shopify Storefront Foundations with Printify",
                            "EP 10: The Calm Creative: Building Brand Serenity"
                        )

                        episodes.forEach { ep ->
                            val isCurrent = ep == activeTrack
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectPodcastTrack(ep) }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isCurrent) Icons.Default.MusicNote else Icons.Default.PlayArrow,
                                    contentDescription = "",
                                    tint = if (isCurrent) AccentLifestyle else TextGray,
                                    modifier = Modifier.size(14.dp)
                                )

                                Text(
                                    text = ep,
                                    color = if (isCurrent) AccentLifestyle else TextLight,
                                    fontSize = 11.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // --- 2. THE EDITORIAL DIGITAL MAGAZINE ---
                item {
                    Text(
                        text = "SOULFUL PERSPECTIVES JOURNAL",
                        color = TextGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }

                // Redundant lists of visual blogs
                items(articles) { blog ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
                            .clickable { activeArticleDetail = blog },
                        colors = CardDefaults.cardColors(containerColor = BrandCardDark),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(AccentLifestyle.copy(alpha = 0.08f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = blog.category.uppercase(),
                                        color = AccentLifestyle,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Text(
                                    text = "${blog.date} • ${blog.readTime}",
                                    color = TextGray,
                                    fontSize = 10.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = blog.title,
                                color = TextLight,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = blog.snippet,
                                color = TextGray,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Read Full Editorial",
                                    color = AccentLifestyle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "",
                                    tint = AccentLifestyle,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // EDITORIAL READ OVERLAY DIALOG/SHEET
        if (activeArticleDetail != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f))
                    .clickable { activeArticleDetail = null },
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                        .background(BrandCardDark, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .border(1.dp, BorderDark, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .clickable(enabled = false) {}
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .clip(CircleShape)
                                .background(BorderDark)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = activeArticleDetail!!.category.uppercase(),
                            color = AccentLifestyle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        IconButton(onClick = { activeArticleDetail = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close reading modal", tint = TextLight)
                        }
                    }

                    Text(
                        text = activeArticleDetail!!.title,
                        color = TextLight,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Published ${activeArticleDetail!!.date} • ${activeArticleDetail!!.readTime}",
                        color = TextGray,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Divider(color = BorderDark)

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = activeArticleDetail!!.body,
                            color = TextLight,
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Enid Kimberly Publications © 2026",
                            color = AccentLifestyle.copy(alpha = 0.5f),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
