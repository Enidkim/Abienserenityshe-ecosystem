package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.geometry.CornerRadius
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
import com.example.data.Product
import com.example.data.ProductCategory
import com.example.data.ProductRepository
import com.example.ui.theme.*
import com.example.viewmodel.EcosystemViewModel
import com.example.viewmodel.CartItem
import com.example.viewmodel.Screen
import androidx.compose.foundation.BorderStroke

@Composable
fun BoutiqueStoreScreen(viewModel: EcosystemViewModel) {
    val context = LocalContext.current

    val currentCategory by viewModel.selectedCategory.collectAsState()
    val cartItems by viewModel.cart.collectAsState()
    val selectedProduct by viewModel.selectedProductForDetail.collectAsState()

    // Interactive customizers states
    val badgeName by viewModel.customBadgeName.collectAsState()
    val badgeMaterial by viewModel.customBadgeMaterial.collectAsState()
    val activePresetPreview by viewModel.activePresetPreview.collectAsState()
    var exposureSliderValue by remember { mutableStateOf(0.0f) } // -1.0f to 1.0f

    // Toggle states
    var isCheckingOut by remember { mutableStateOf(false) }
    var isCartOpen by remember { mutableStateOf(false) }
    var showCheckoutSuccess by remember { mutableStateOf(false) }

    // Filter Repository items
    val filteredProducts = remember(currentCategory) {
        ProductRepository.items.filter { it.category == currentCategory }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandBgDark)
    ) {
        // Soft golden glow inside background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentBoutique.copy(alpha = 0.03f), Color.Transparent),
                        radius = 800f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Header Bar
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
                    text = "ABIENSERENITY BOUTIQUE",
                    color = AccentBoutique,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )

                // Dynamic Shopping Cart Badge Icon
                IconButton(
                    onClick = { isCartOpen = true },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BrandCardLight.copy(alpha = 0.5f))
                ) {
                    BadgedBox(
                        badge = {
                            if (cartItems.isNotEmpty()) {
                                Badge(containerColor = AccentBoutique) {
                                    val count = cartItems.sumOf { it.quantity }
                                    Text(
                                        text = count.toString(),
                                        color = Color.Black,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingCart,
                            contentDescription = "Open cart basket",
                            tint = TextLight
                        )
                    }
                }
            }

            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Intro
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Shopify + Printify Storefront".uppercase(),
                        color = TextGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Branded Digital Curation",
                        color = TextLight,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Browse physical stickers, premium etched name tags, custom Lightroom mobile presets, website CSS mockups, and client sheets.",
                        color = TextGray,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }

                // Interactive Customizers Sections (High engagement elements)
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = BorderDark, modifier = Modifier.padding(horizontal = 24.dp))
                Spacer(modifier = Modifier.height(18.dp))

                InteractiveCustomizersPanel(
                    activePreset = activePresetPreview,
                    exposure = exposureSliderValue,
                    onExposureChange = { exposureSliderValue = it },
                    onPresetUpdate = { viewModel.updatePresetPreview(it) },
                    badgeName = badgeName,
                    badgeMaterial = badgeMaterial,
                    onBadgeNameChange = { viewModel.updateBadgeName(it) },
                    onMaterialSelect = { viewModel.updateBadgeMaterial(it) },
                    onAddTagToCart = {
                        val product = Product(
                            id = "custom_tag_ordered",
                            name = "Custom Acrylic Badge ($badgeName)",
                            category = ProductCategory.NAME_TAGS,
                            price = 18.00,
                            description = "Your custom etched executive acrylic badge styled on mirror $badgeMaterial backing frame. Created dynamically.",
                            features = listOf("Personalized to: '$badgeName'", "Backing: $badgeMaterial", "Heavy-duty apparel backing magnets")
                        )
                        viewModel.addToCart(product)
                        Toast.makeText(context, "Added Customized Name Tag to Cart Workspace!", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = BorderDark, modifier = Modifier.padding(horizontal = 24.dp))
                Spacer(modifier = Modifier.height(20.dp))

                // Product Category Navigation Tabs
                Text(
                    text = "COLLECTIONS CATALOG",
                    color = TextGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(ProductCategory.values()) { cat ->
                        val isSelected = cat == currentCategory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(30.dp))
                                .background(if (isSelected) AccentBoutique else BrandCardDark)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) AccentBoutique else BorderDark,
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .clickable { viewModel.selectCategory(cat) }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = cat.displayName,
                                color = if (isSelected) Color.Black else TextLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Products Display Shelf
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    filteredProducts.forEach { item ->
                        StoreItemRow(product = item) {
                            viewModel.viewProductDetail(item)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // 1. PRODUCT DETAIL DIALOG
        if (selectedProduct != null) {
            ProductDetailsOverlay(
                product = selectedProduct!!,
                onDismiss = { viewModel.viewProductDetail(null) },
                onAddToCart = {
                    viewModel.addToCart(selectedProduct!!)
                    viewModel.viewProductDetail(null)
                    Toast.makeText(context, "Added to shopping basket!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // 2. ACTIVE SHOPPING BASKET DRAWER OVERLAY
        if (isCartOpen) {
            CartDrawerOverlay(
                cartItems = cartItems,
                onDismiss = { isCartOpen = false },
                onAdd = { viewModel.incrementCartItem(it) },
                onMinus = { viewModel.decrementCartItem(it) },
                onDelete = { viewModel.removeFromCart(it) },
                onCheckoutPay = {
                    isCartOpen = false
                    isCheckingOut = true
                }
            )
        }

        // 3. CHECKOUT PAYMENT STREAM LOADER
        if (isCheckingOut) {
            CheckoutSimulationOverlay(
                cartItems = cartItems,
                onSuccessTrigger = {
                    isCheckingOut = false
                    viewModel.clearCart()
                    showCheckoutSuccess = true
                },
                onCancel = { isCheckingOut = false }
            )
        }

        // 4. CHECKOUT SUCCESS SPLASH
        if (showCheckoutSuccess) {
            ConfettiSuccessOverlay {
                showCheckoutSuccess = false
            }
        }
    }
}

@Composable
fun StoreItemRow(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = BrandCardDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Soft category visual backing
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentBoutique.copy(alpha = 0.08f))
                    .border(1.dp, AccentBoutique.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (product.category) {
                        ProductCategory.STICKERS -> Icons.Default.FilterVintage
                        ProductCategory.PRESETS -> Icons.Default.Brush
                        ProductCategory.MOCKUPS -> Icons.Default.Layers
                        ProductCategory.NAME_TAGS -> Icons.Default.AccountBox
                        ProductCategory.TRACKERS -> Icons.Default.ListAlt
                        ProductCategory.SHEETS -> Icons.Default.Description
                    },
                    contentDescription = "",
                    tint = AccentBoutique,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    color = TextLight,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.description,
                    color = TextGray,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${"%.2f".format(product.price)}",
                    color = TextGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = AccentBoutique,
                        modifier = Modifier.size(10.dp)
                    )
                    Text(
                        text = " ${product.rating}",
                        color = TextGray,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun InteractiveCustomizersPanel(
    activePreset: String,
    exposure: Float,
    onExposureChange: (Float) -> Unit,
    onPresetUpdate: (String) -> Unit,
    badgeName: String,
    badgeMaterial: String,
    onBadgeNameChange: (String) -> Unit,
    onMaterialSelect: (String) -> Unit,
    onAddTagToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // --- 1. LIGHTROOM PRESET PREVIEWER CANVAS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandCardDark, RoundedCornerShape(20.dp))
                .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Text(
                text = "LIGHTROOM PRESET SIMULATOR",
                color = AccentBoutique,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Preset Curation Live Preview",
                color = TextLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // The Render Box Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .drawBehind {
                        // Apply custom color filters dynamically linked to State exposure coefficients
                        val filterColor = when (activePreset) {
                            "Luxe Champagne" -> Color(0xFFF7E9CE)
                            "Serene Sage" -> Color(0xFFD5ECE0)
                            "Vintage Editorial" -> Color(0xFFD4C8BC)
                            else -> Color.White
                        }

                        // Exposure coefficient calculation - translates to transparency levels
                        val alphaFactor = (0.55f + (exposure * 0.35f)).coerceIn(0.1f, 0.95f)

                        // Outer gradient representing photo background
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF2C2F36), Color(0xFF131417))
                            )
                        )

                        // Blending color overlay simulating exposure adjustments on a photo
                        drawRect(
                            color = filterColor.copy(alpha = alphaFactor)
                        )

                        // Render a stylized visual mock landscape layout in the canvas
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.3f),
                            topLeft = Offset(size.width * 0.1f, size.height * 0.2f),
                            size = androidx.compose.ui.geometry.Size(
                                size.width * 0.4f,
                                size.height * 0.6f
                            ),
                            cornerRadius = CornerRadius(12f)
                        )

                        drawCircle(
                            color = AccentBoutique.copy(alpha = 0.4f),
                            radius = size.height * 0.25f,
                            center = Offset(size.width * 0.75f, size.height * 0.5f)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Branded Aesthetic Subject".uppercase(),
                        color = Color.Black.copy(alpha = 0.75f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Active Preset: $activePreset",
                        color = Color.Black.copy(alpha = 0.60f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Preset Selection Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Luxe Champagne", "Serene Sage", "Vintage Editorial").forEach { p ->
                    val isSelected = activePreset == p
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) AccentBoutique.copy(alpha = 0.15f) else BrandCardLight)
                            .border(
                                1.dp,
                                if (isSelected) AccentBoutique else BorderDark,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onPresetUpdate(p) }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(p.replace(" ", " \n"), color = if (isSelected) AccentBoutique else TextLight, fontSize = 9.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Exposure Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.LightMode, contentDescription = "", tint = TextGray, modifier = Modifier.size(16.dp))
                Slider(
                    value = exposure,
                    onValueChange = onExposureChange,
                    valueRange = -1.0f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = AccentBoutique,
                        activeTrackColor = AccentBoutique,
                        inactiveTrackColor = BorderDark
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${if (exposure >= 0) "+" else ""}${"%.1f".format(exposure)} EV",
                    color = TextLight,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.width(42.dp)
                )
            }
        }

        // --- 2. EXECUTIVE CUSTOM NAME EMBOSSER ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandCardDark, RoundedCornerShape(20.dp))
                .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
                .padding(18.dp)
        ) {
            Text(
                text = "PRINTIFY PERSONAL CUSTOMIZER",
                color = AccentBoutique,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Mirror Gold Acrylic Badge",
                color = TextLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Live Customized Plate Render Graphic
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when (badgeMaterial) {
                            "Mirror Gold Acrylic" -> Brush.verticalGradient(
                                listOf(Color(0xFFEADBBE), Color(0xFF8C733E))
                            )
                            "Space Black Leather" -> Brush.verticalGradient(
                                listOf(Color(0xFF282A30), Color(0xFF111113))
                            )
                            "Bamboo Natural" -> Brush.verticalGradient(
                                listOf(Color(0xFFE9E0CD), Color(0xFFC7B18E))
                            )
                            else -> Brush.verticalGradient(listOf(Color(0xFF904E35), Color(0xFF421C0F)))
                        }
                    )
                    .border(
                        2.dp,
                        if (badgeMaterial == "Mirror Gold Acrylic") Color(0xFFFFF0D0) else Color.DarkGray,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = badgeName.uppercase(),
                        color = if (badgeMaterial == "Mirror Gold Acrylic" || badgeMaterial == "Bamboo Natural") Color(0xFF1A1A1A) else TextLight,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "MEMBER BRAND ALLIANCE".uppercase(),
                        color = if (badgeMaterial == "Mirror Gold Acrylic" || badgeMaterial == "Bamboo Natural") Color(0xFF4A4A4A) else TextGray,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Form Fields
            OutlinedTextField(
                value = badgeName,
                onValueChange = onBadgeNameChange,
                label = { Text("Name to Engrave", color = TextGray, fontSize = 11.sp) },
                singleLine = true,
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextLight,
                    unfocusedTextColor = TextLight,
                    focusedBorderColor = AccentBoutique,
                    unfocusedBorderColor = BorderDark
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Backing Material Choice
            Text(text = "Choose Plate Finish", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                listOf("Mirror Gold Acrylic", "Space Black Leather", "Bamboo Natural").forEach { mat ->
                    val isSel = badgeMaterial == mat
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSel) AccentBoutique.copy(alpha = 0.15f) else BrandCardLight)
                            .border(1.dp, if (isSel) AccentBoutique else BorderDark, RoundedCornerShape(6.dp))
                            .clickable { onMaterialSelect(mat) }
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(mat, color = if (isSel) AccentBoutique else TextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Add Custom Tag to Cart Button
            Button(
                onClick = onAddTagToCart,
                colors = ButtonDefaults.buttonColors(containerColor = AccentBoutique),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "", tint = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Order Personalized Plate - $18.00", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProductDetailsOverlay(
    product: Product,
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandCardDark, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .border(1.dp, BorderDark, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .clickable(enabled = false) {}
                .padding(24.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = product.category.displayName.uppercase(),
                        color = AccentBoutique,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = product.name,
                        color = TextLight,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Text(
                    text = "$${"%.2f".format(product.price)}",
                    color = TextGold,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = product.description,
                color = TextGray,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Key Specifications:".uppercase(),
                color = TextGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Specs
            product.features.forEach { spec ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "",
                        tint = AccentBoutique,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = spec,
                        color = TextLight,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action CTAs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextLight),
                    border = BorderStroke(1.dp, BorderDark)
                ) {
                    Text("Browse More", fontSize = 13.sp)
                }

                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBoutique)
                ) {
                    Text("Add to Shopify Basket", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CartDrawerOverlay(
    cartItems: List<CartItem>,
    onDismiss: () -> Unit,
    onAdd: (Product) -> Unit,
    onMinus: (Product) -> Unit,
    onDelete: (Product) -> Unit,
    onCheckoutPay: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.85f)
                .background(BrandCardDark)
                .border(1.dp, BorderDark, RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp))
                .clickable(enabled = false) {}
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = "", tint = AccentBoutique)
                    Text(
                        text = "Shopify Cart",
                        color = TextLight,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close Cart drawer", tint = TextLight)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = BorderDark)
            Spacer(modifier = Modifier.height(14.dp))

            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.ShoppingCart, contentDescription = "", tint = TextGray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Ecosystem Cart is Empty", color = TextLight, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Add items from the store above", color = TextGray, fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }
            } else {
                // Cart Product list
                Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                    for (item in cartItems) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .background(BrandCardLight.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .border(1.dp, BorderDark, RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.product.name,
                                    color = TextLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "$${"%.2f".format(item.product.price)} each",
                                    color = TextGold,
                                    fontSize = 10.sp
                                )
                            }

                            // Quantities Incremental Panel
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                IconButton(
                                    onClick = { onMinus(item.product) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Minus count", tint = TextLight, modifier = Modifier.size(12.dp))
                                }

                                Text(
                                    text = item.quantity.toString(),
                                    color = TextLight,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(
                                    onClick = { onAdd(item.product) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Plus count", tint = TextLight, modifier = Modifier.size(12.dp))
                                }

                                IconButton(
                                    onClick = { onDelete(item.product) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete product", tint = Color.Red, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = BorderDark)
                Spacer(modifier = Modifier.height(14.dp))

                // Price total count
                val totalPrice = cartItems.sumOf { it.product.price * it.quantity }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Price:", color = TextGray, fontSize = 13.sp)
                    Text("$${"%.2f".format(totalPrice)}", color = TextGold, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onCheckoutPay,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentBoutique),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Pay via ShopPay / Printify Gateway", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CheckoutSimulationOverlay(
    cartItems: List<CartItem>,
    onSuccessTrigger: () -> Unit,
    onCancel: () -> Unit
) {
    var loadingStep by remember { mutableStateOf(1) }
    val totalPrice = cartItems.sumOf { it.product.price * it.quantity }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1200)
        loadingStep = 2 // verifying Printify stock
        kotlinx.coroutines.delay(1200)
        loadingStep = 3 // securing payment gateway
        kotlinx.coroutines.delay(1000)
        onSuccessTrigger() // Complete payment!
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .background(BrandCardDark, RoundedCornerShape(20.dp))
                .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = AccentBoutique, strokeWidth = 3.dp)
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "SHOPIFY SYSTEM CHECK",
                color = AccentBoutique,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = when (loadingStep) {
                    1 -> "Securing secure checkout handshake..."
                    2 -> "Verifying automated Printify inventory sync..."
                    else -> "Routing to Visa / ShopPay API..."
                },
                color = TextLight,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Processing purchase of: $${"%.2f".format(totalPrice)}",
                color = TextGray,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = onCancel) {
                Text("Cancel Transaction", color = Color.Red, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun ConfettiSuccessOverlay(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            // Success Glowing Circle icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AccentBoutique.copy(alpha = 0.15f))
                    .border(2.dp, AccentBoutique, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Celebration,
                    contentDescription = "Success Confetti",
                    tint = AccentBoutique,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "SHOPIFY STORES SYNC SUCCESS",
                color = AccentBoutique,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Thank you for shopping Enid Kimberly's curated catalog! Your digital presets, spreadsheet sheets, and Mockup files are routed to your email inbox, while custom name tags are transmitted for laser carving in her Boutique & Design Agency.",
                color = TextLight,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 280.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = AccentBoutique),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Acknowledge & Continue", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
