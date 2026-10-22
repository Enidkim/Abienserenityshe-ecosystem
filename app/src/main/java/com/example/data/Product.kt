package com.example.data

enum class ProductCategory(val displayName: String) {
    STICKERS("Stickers"),
    PRESETS("Presets"),
    MOCKUPS("Mockups"),
    NAME_TAGS("Name Tags"),
    TRACKERS("Trackers"),
    SHEETS("Sheets")
}

data class Product(
    val id: String,
    val name: String,
    val category: ProductCategory,
    val price: Double,
    val description: String,
    val features: List<String>,
    val rating: Float = 4.9f,
    val reviewCount: Int = 24
)

object ProductRepository {
    val items = listOf(
        // Stickers
        Product(
            id = "s1",
            name = "EK Holo Serenity Sticker Pack",
            category = ProductCategory.STICKERS,
            price = 6.99,
            description = "High-quality, water-resistant holographic die-cut sticker pack with custom therapeutic designs, featuring gold alignments and calming brand motifs.",
            features = listOf("Holographic premium vinyl finish", "Waterproof & UV resistant", "Pack of 5 distinct luxury motifs", "Designed by Enid Kimberly")
        ),
        Product(
            id = "s2",
            name = "Soul Reset Matte Decals",
            category = ProductCategory.STICKERS,
            price = 5.49,
            description = "Elegant matte decals optimized for minimalist journaling, planners, and tech casing. Imprinted with daily mindfulness prompts.",
            features = listOf("Soft-touch matte textured coating", "Eco-friendly printing materials", "Perfect for laptop or journal surfaces", "Includes therapeutic journaling guide")
        ),
        // Presets
        Product(
            id = "p1",
            name = "Luxe Champagne Lightroom Presets",
            category = ProductCategory.PRESETS,
            price = 24.99,
            description = "Our signature preset formulation. Infuses high-end editorial warmth, luminous cream tones, and soft champagne highlights. Elevate photos instantly.",
            features = listOf("Includes 5 Lightroom Mobile/Desktop presets (.dng / .xmp)", "Premium warm editorial golden hues", "Optimized for skin-tones and luxury lifestyle shoots", "Step-by-step styling guide included")
        ),
        Product(
            id = "p2",
            name = "Serene Sage Presets Pack",
            category = ProductCategory.PRESETS,
            price = 19.99,
            description = "An earthy, calming palette preset. Reduces harsh greens to soothing sage tones, emphasizes organic shadows, and highlights gentle sand accents.",
            features = listOf("Includes 4 unique mobile presets", "Deep tranquil green and beige balancing", "Perfect for outdoor wellness, plants, and interiors", "One-click professional optimization")
        ),
        Product(
            id = "p3",
            name = "Vintage Editorial Filter Pack",
            category = ProductCategory.PRESETS,
            price = 22.00,
            description = "Timed to recreate the grain, structure, and low contrast of luxury printed media from retro Europe. High sophistication.",
            features = listOf("Inspired by mid-century French fashion journals", "Custom grain overlays and fade profiles", "Ideal for lifestyle storytelling and content creators", "Flexible contrast ratios")
        ),
        // Mockups
        Product(
            id = "m1",
            name = "Minimalist Brand PSD Mockup",
            category = ProductCategory.MOCKUPS,
            price = 14.99,
            description = "Photorealistic high-resolution stationary brand mockup. Features luxury organic shadows, linen-textured backing sheets, and debossed logo vectors.",
            features = listOf("4500 x 3000px high fidelity Photoshop file", "Fully active smart object layers", "Interchangeable concrete/clay shadows", "Commercial brand presentation layout")
        ),
        Product(
            id = "m2",
            name = "Organic Canvas Tote Mockup Pack",
            category = ProductCategory.MOCKUPS,
            price = 12.99,
            description = "A gorgeous lifestyle mockup of heavy weaved organic cotton tote bags suspended in warm studio sunlight with botanical shadow highlights.",
            features = listOf("Includes 3 different isometric angles", "Adjustable fabric color tones & logo placements", "Beautiful palm leaf natural shadows", "Ready for Shop/Instagram banner promotion")
        ),
        // Name Tags
        Product(
            id = "n1",
            name = "Engraved Gold Acrylic Name Tag",
            category = ProductCategory.NAME_TAGS,
            price = 18.00,
            description = "Our flagship physical boutique item. Double-layered, mirror gold acrylic finish custom etched with your studio title or name. Ultra premium.",
            features = listOf("Custom laser-etched name and position", "High-reflection dual-layer gold mirror surface", "Includes extra strength clothing magnet backing", "Handcrafted in the Boutique and Design Agency")
        ),
        Product(
            id = "n2",
            name = "Branded Matte Black Leather Badge",
            category = ProductCategory.NAME_TAGS,
            price = 16.50,
            description = "A striking, debossed pure leather name tag with modern typography. Gives a rustic yet incredibly premium agency presentation.",
            features = listOf("100% full-grain tanned leather badge", "Hot-press debossed personalization", "Available with premium copper safety pin or magnetic clip", "Durable and gains character over time")
        ),
        // Trackers
        Product(
            id = "t1",
            name = "Soul Reset Habit & Mindfulness Tracker",
            category = ProductCategory.TRACKERS,
            price = 9.99,
            description = "An interactive, beautifully stylized progress tracker. Helps you map out daily focus, sleep quality, water reset ratios, and mindfulness streaks.",
            features = listOf("Printable custom artwork PDFs (A4, A5, Letter size)", "Interactive fillable PDF version for iPads or mobile notes", "Color-keyed weekly streak tracking", "Coupled with 10 wellness journal templates")
        ),
        Product(
            id = "t2",
            name = "Branding Launch Roadmap Tracker",
            category = ProductCategory.TRACKERS,
            price = 14.50,
            description = "A step-by-step checklist tracker built specifically for startups, designer clients, and product boutiques mapping their brand debut.",
            features = listOf("Ultimate 60-stage milestone checklist", "Includes resource links for sourcing & package printouts", "Interactive status charts for tracking budgets", "Lifetime updates with brand iterations")
        ),
        // Sheets
        Product(
            id = "sh1",
            name = "Aesthetic Brand Identity Google Sheet",
            category = ProductCategory.SHEETS,
            price = 19.99,
            description = "A gorgeous, fully customized spreadsheet that organizes your brand assets, hex palettes, typographic styles, and social schedule with an aesthetic dark mode layout.",
            features = listOf("Interactive color palette dashboard with live dynamic visuals", "Typographic pairing planner matrix", "Automated asset categorization formulas", "No spreadsheet knowledge needed — pre-formatted & styled")
        ),
        Product(
            id = "sh2",
            name = "Creative Income & Storefront Ledger",
            category = ProductCategory.SHEETS,
            price = 24.00,
            description = "A financial planner built for digital shop owners, designers, and creatives. Tracks Printify and Shopify profit margins, fees, and revenue elegantly.",
            features = listOf("Custom visual charts with monthly breakdown bars", "Printify price calculator auto-formulated", "Interactive inventory alerts", "Compatible with Google Sheets & Microsoft Excel")
        )
    )
}
