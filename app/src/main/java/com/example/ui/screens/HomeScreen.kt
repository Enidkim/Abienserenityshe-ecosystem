package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.EcosystemViewModel
import com.example.viewmodel.Screen

@Composable
fun HomeScreen(viewModel: EcosystemViewModel) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBgDark)
            .verticalScroll(scrollState)
    ) {
        // Aesthetic diagonal glow behind cards
        CanvasBackgroundGlow()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 64.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Elegant studio prefix
            Text(
                text = "Studio Directory & Portal".uppercase(),
                color = AccentBoutique,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Main Brand Title
            Text(
                text = "ENID KIMBERLY",
                color = TextLight,
                fontSize = 32.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Brand Ethos Subtext
            Text(
                text = "A high-fidelity multi-studio hub curating modern visual arts, luxury storefront assets, holistic community circles, and premium design media.",
                color = TextGray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.widthIn(max = 340.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 4-Quadrant Adaptive Hub
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // TOP PAIR (Creative Lab & Boutique storefront)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        QuadrantCard(
                            title = "Creative Lab",
                            subtitle = "AbienserenityAI",
                            icon = Icons.Default.AutoAwesome,
                            accentColor = AccentCreative,
                            description = "Custom cognitive brand playground formulating prompt templates.",
                            onClick = { viewModel.navigateTo(Screen.CREATIVE_LAB) }
                        )
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        QuadrantCard(
                            title = "The Boutique",
                            subtitle = "Abienserenity",
                            icon = Icons.Default.Sell,
                            accentColor = AccentBoutique,
                            description = "Printify & Shopify storefront for premium stickers, presets, and name tags.",
                            onClick = { viewModel.navigateTo(Screen.BOUTIQUE) }
                        )
                    }
                }

                // BOTTOM PAIR (Wellness & Lifestyle)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        QuadrantCard(
                            title = "Wellness",
                            subtitle = "SoulResetStudio-EK",
                            icon = Icons.Default.Spa,
                            accentColor = AccentWellness,
                            description = "Daily journal logs, calm alignments, and breathing rhythms.",
                            onClick = { viewModel.navigateTo(Screen.WELLNESS) }
                        )
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        QuadrantCard(
                            title = "Lifestyle Hub",
                            subtitle = "Media & Curation",
                            icon = Icons.Default.Audiotrack,
                            accentColor = AccentLifestyle,
                            description = "Curated luxury blogs, podcast streams, and high design files.",
                            onClick = { viewModel.navigateTo(Screen.LIFESTYLE) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Interactive Bottom Overview Card
            BrandManifestoCard()
        }
    }
}

@Composable
fun QuadrantCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    description: String,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .shadow(12.dp, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(BrandCardDark)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(accentColor.copy(alpha = 0.5f), BorderDark)
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick)
            .drawBehind {
                // Subtle radial light highlighting inside the card corner
                drawCircle(
                    color = accentColor.copy(alpha = 0.06f),
                    radius = size.width * 0.5f,
                    center = Offset(size.width * 0.8f, size.height * 0.2f)
                )
            }
            .padding(20.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Glowing Icon Frame
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.12f))
                        .border(1.dp, accentColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "$title Icon",
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Tiny Go Arrow
                Icon(
                    imageVector = Icons.Default.ArrowOutward,
                    contentDescription = "Navigate to $title",
                    tint = TextGray.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    color = TextLight,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = subtitle.uppercase(),
                    color = accentColor.copy(alpha = 0.85f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    color = TextGray,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
fun BrandManifestoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderDark, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = BrandCardLight.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.FilterVintage,
                contentDescription = "Brand Crest Motif",
                tint = AccentBoutique.copy(alpha = 0.7f),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "THE ECOSYSTEM COHESION",
                color = TextGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We design ecosystems where business, design mechanics, cognitive tools, and wellness check-ins flow in unison. This landing client bridges our Shopify store, local meditation breath rhythms, podcast updates, and our generative AbienserenityAI in one pristine interface.",
                color = TextGray,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 290.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = BorderDark, thickness = 1.dp, modifier = Modifier.width(60.dp))

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Enid Kimberly • Creative Director © 2026",
                color = TextGray.copy(alpha = 0.6f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun CanvasBackgroundGlow() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                // Glowing radial spheres to represent luxury ambient lighting
                drawCircle(
                    color = AccentCreative.copy(alpha = 0.05f),
                    radius = size.width * 0.7f,
                    center = Offset(-size.width * 0.2f, size.height * 0.15f)
                )

                drawCircle(
                    color = AccentWellness.copy(alpha = 0.04f),
                    radius = size.width * 0.8f,
                    center = Offset(size.width * 1.2f, size.height * 0.5f)
                )

                drawCircle(
                    color = AccentBoutique.copy(alpha = 0.04f),
                    radius = size.width * 0.6f,
                    center = Offset(size.width * 0.1f, size.height * 0.9f)
                )
            }
    )
}
