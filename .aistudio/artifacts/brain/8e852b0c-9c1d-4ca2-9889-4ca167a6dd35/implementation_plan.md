# SoulReset Studio & Enid Kimberly Ecosystem Expansion

Integrate the newly uploaded SoulReset Studio digital shop assets, curated books, carousel collections, INR-to-KSH/UGX currency remittance rates, and the signature Burgundy, Deep Teal, and Warm Gold color palette into a cohesive Android experience with an informational crisis care directory.

## User Review & Critical Decisions

> [!IMPORTANT]
> The following directions were confirmed based on your selections:
> - **Primary Feature Focus**: Digital Shop expansion prioritizing Books (eBooks & Haunted House Metaphor), Carousels (e.g., The Mother Tree story asset), and Currency/Remittance Rate Tables (INR to KSH & UGX).
> - **Visual Identity**: Official SoulReset Studio palette featuring Burgundy (`#730C18`), Deep Teal (`#004D4D`), Warm Gold (`#F4B942`), and Soft Cream (`#FFF8F4`).
> - **Crisis Support Experience**: An empathetic, informational directory of crisis lines (Kiran, 112, 108, 181), mental health guidelines, grounding techniques (5-4-3-2-1), and self-care checklists without invasive full-screen alerts.

---

## 1. Overview & Core Concept

- **What It Does**: Unifies Enid Kimberly's multi-studio ecosystem with SoulReset Studio's digital shop, literature, and mindfulness resources. Users can explore curated digital books ("No One Can Heal You", "The Haunted House Metaphor"), swipe through visual carousels like "The Mother Tree", calculate instant remittance and exchange rates between INR, KSH, and UGX, browse wellness tools, and access gentle crisis support guidelines.
- **Target Audience / Persona**: Creative entrepreneurs, digital art buyers, and individuals seeking mindful healing, therapeutic essays, and aesthetic brand assets.
- **Key Value**: Replaces disconnected standalone web pages with a unified, high-performance Jetpack Compose app featuring rich visual typography, tactile shopping carts, instant currency lookups, and empathetic reading experiences.

---

## 2. User Experience & Visual Design

### Key User Flows

1. **Brand Portal Hub**:
   - Dynamic top greeting reflecting SoulReset Studio by E.K.
   - Quick studio switchers: **Digital Shop**, **The Boutique**, **Creative Lab (AI)**, **Wellness & Care**, and **Directory / Crisis Guide**.
   - Featured highlight card for *"The Mother Tree"* with its botanical wisdom narrative.

2. **Expanded Digital Shop**:
   - **Bookshelf Shelf**: Filterable catalog featuring *"No One Can Heal You — eBook"* (₹399), *"The Haunted House Metaphor — PDF"* (₹249), with preview excerpts, author quotes, and direct Add-to-Cart / Wishlist actions.
   - **Carousels Section**: Interactive swipeable slide preview of visual carousel sets, including *"The Mother Tree"* and social design assets.
   - **Remittance & Rate Calculator**: Live currency lookup tool comparing INR to KSH (Kenyan Shillings) and UGX (Ugandan Shillings) with tier rates from the provided rate sheets (e.g. 500 INR = 725 KSH, 3,000 INR = 4,200 KSH).
   - **Unified Shopping Basket**: Displays items with INR currency formatting (₹), quantity adjusters, and payment checkout flow.

3. **Torchbearer & Haunted House Reader**:
   - Dedicated interactive reader for *"The Haunted House Metaphor"* and *"The Torchbearer's Checklist"*, with 10 practical rules for holding space for loved ones suffering in silence.

4. **Informational Crisis & Self-Care Directory**:
   - Clean, non-intrusive safety hub: 5-4-3-2-1 Sensory Grounding step-by-step card, 4-4-6 calming breath cycle, and national helpline directory (Kiran 1800-599-0019, 112, 108, 181) with copy-to-clipboard functionality.

### Visual Identity & Theme

- **Color Tokens**:
  - `SoulBurgundy`: `#730C18` (Primary brand accent, button fills, highlights)
  - `SoulTeal`: `#004D4D` (Secondary brand anchor, headers, active tabs)
  - `SoulGold`: `#F4B942` (Warm accent, badges, ratings, quote highlights)
  - `SoulCream`: `#FFF8F4` (Background canvas for clean readability)
  - `SoulCardBg`: `#FFFFFF` (Surface cards with subtle border tint)
  - `SoulTextDark`: `#1E1E1E` (Crisp typographic contrast)
  - `SoulTextMuted`: `#686868` (Secondary descriptions and metadata)
- **Typography**: Poppins-inspired geometric sans-serif with bold display headers, generous line heights, and elegant letter spacing.
- **Micro-Interactions**: Smooth horizontal carousel paging, spring-based tab switches, and ripple effects on all interactive tiles.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Blended Brand Portal rather than Separate Apps**
  - *Chosen Approach*: Keep the existing Enid Kimberly high-end creative lab while embedding SoulReset Studio as the premier wellness and digital shop wing.
  - *Why*: Eliminates fragmentation and delivers the complete multi-studio vision requested by the user.
- **Decision 2: Offline-First Currency Remittance Table**
  - *Chosen Approach*: Embed the standard money mover conversion tiers directly from `INR_Table_Setup_9beee91e.csv` and `downloaded-text_ded4b7bd.txt` with a reactive slider/input calculator.
  - *Why*: Provides instant, zero-latency rate computations without relying on unreliable third-party forex APIs.
- **Decision 3: Non-Intrusive Crisis Directory**
  - *Chosen Approach*: Present helplines and self-care steps as a dignified, readily accessible reference tab rather than aggressive popups.
  - *Why*: Complies with user's explicit preference for an informational directory while providing immediate access when needed.

---

## 4. Technical Architecture & Data Strategy

### Architecture & Component Diagram

```
┌────────────────────────────────────────────────────────┐
│                      MainActivity                      │
│                 (Edge-to-Edge Scaffold)                │
└───────────────────────────┬────────────────────────────┘
                            │
               ┌────────────┴────────────┐
               ▼                         ▼
   ┌───────────────────────┐ ┌───────────────────────┐
   │  EcosystemViewModel   │ │     Theme.kt (M3)     │
   │  • Screen Navigation  │ │  • Burgundy (#730C18) │
   │  • Digital Cart State │ │  • Deep Teal (#004D4D)│
   │  • Rate Converter     │ │  • Warm Gold (#F4B942)│
   │  • Journal & Progress │ │  • Cream / Ink Surface│
   └───────────┬───────────┘ └───────────────────────┘
               │
   ┌───────────┼────────────────────────────────────────┐
   ▼           ▼                    ▼                   ▼
┌──────────┐ ┌───────────────┐ ┌───────────────┐ ┌────────────────┐
│HomeScreen│ │DigitalShop    │ │Torchbearer &  │ │Crisis &        │
│(Hub &    │ │• Books (₹)    │ │Haunted House  │ │Self-Care Hub   │
│Portal)   │ │• Carousels    │ │• 10 Checklists│ │• 5-4-3-2-1 Guide│
│          │ │• Rates (INR/  │ │• Metaphor     │ │• Hotlines (112,│
│          │ │  KSH/UGX)     │ │  Essays       │ │  Kiran)        │
└──────────┘ └───────────────┘ └───────────────┘ └────────────────┘
```

### Data Model & State Entities

- `ShopBook`: Book entity (ID, title, priceInInr, author, coverAsset, description, excerptQuote).
- `CarouselSlide`: Slide entity (title, subtitle, narrative, visualTheme, bgGradient).
- `RemittanceRate`: Money conversion tier (inrAmount, kshAmount, ugxAmount, notes).
- `CrisisResource`: Helpline listing (name, number, description, category, hours).
- `ChecklistItem`: Torchbearer guidelines (id, title, detailInstruction, category).

### Step-by-Step Implementation Sequence

1. **Color & Theme Update**: Define `SoulBurgundy`, `SoulTeal`, `SoulGold`, `SoulCream`, and `SoulDark` in `Color.kt` and update `Theme.kt`.
2. **Data Repository Extension**: Create `SoulResetRepository.kt` with books (`No One Can Heal You`, `Haunted House Metaphor`), remittance conversion rate data, and carousel slides.
3. **Digital Shop Expansion Screen (`SoulResetShopScreen.kt`)**:
   - Books shelf with quick details modal and add-to-cart.
   - Interactive Mother Tree carousel reader.
   - Live INR to KSH/UGX currency converter.
4. **Torchbearer & Crisis Directory Screen (`SoulResetCareScreen.kt`)**:
   - The 10 Torchbearer guidelines.
   - 5-4-3-2-1 Sensory Grounding and 4-4-6 breathing card.
   - Helpline directory with one-tap dial/copy.
5. **Main Navigation & Home Screen Sync**: Add intuitive tab/card entries on the home portal to seamlessly route between existing Creative Lab / Boutique and the new SoulReset modules.
6. **Compilation & Verification**: Verify build stability via `compile_applet`.
