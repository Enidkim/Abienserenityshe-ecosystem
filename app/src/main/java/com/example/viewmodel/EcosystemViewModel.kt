package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiClient
import com.example.data.Product
import com.example.data.ProductCategory
import com.example.data.ProductRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Screen {
    HOME,
    CREATIVE_LAB,
    BOUTIQUE,
    WELLNESS,
    LIFESTYLE
}

data class JournalEntry(
    val date: String,
    val mood: String,
    val moodValue: Int,
    val note: String
)

data class CartItem(
    val product: Product,
    val quantity: Int
)

class EcosystemViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPrefs = application.getSharedPreferences("enid_kimberly_prefs", Context.MODE_PRIVATE)
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    // Screen State
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Navigation Stack for simple back transitions
    private val navigationStack = mutableListOf<Screen>()

    // Creative Lab (AbienserenityAI) states
    private val _aiResult = MutableStateFlow("")
    val aiResult: StateFlow<String> = _aiResult.asStateFlow()

    private val _aiLoading = MutableStateFlow(false)
    val aiLoading: StateFlow<Boolean> = _aiLoading.asStateFlow()

    private val _aiHistory = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val aiHistory: StateFlow<List<Pair<String, String>>> = _aiHistory.asStateFlow()

    // Boutique Storefront States
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ProductCategory.STICKERS)
    val selectedCategory: StateFlow<ProductCategory> = _selectedCategory.asStateFlow()

    private val _selectedProductForDetail = MutableStateFlow<Product?>(null)
    val selectedProductForDetail: StateFlow<Product?> = _selectedProductForDetail.asStateFlow()

    // Interactive Customizer - Name Badge
    private val _customBadgeName = MutableStateFlow("Enid Kimberly")
    val customBadgeName: StateFlow<String> = _customBadgeName.asStateFlow()

    private val _customBadgeMaterial = MutableStateFlow("Mirror Gold Acrylic")
    val customBadgeMaterial: StateFlow<String> = _customBadgeMaterial.asStateFlow()

    // Interactive Customizer - Photo Preset
    private val _activePresetPreview = MutableStateFlow("Luxe Champagne")
    val activePresetPreview: StateFlow<String> = _activePresetPreview.asStateFlow()

    // Interactive Customizer - Habit Grid
    private val _habitGrid = MutableStateFlow(
        mapOf(
            "Morning Coffee & Intention" to true,
            "10 Minute Deep Inhale Rest" to false,
            "Review Luxe Preset Highlights" to false,
            "Journal Branded Reflections" to true,
            "Audit Storefront Order Sync" to false
        )
    )
    val habitGrid: StateFlow<Map<String, Boolean>> = _habitGrid.asStateFlow()

    // Wellness check-in & Journal states
    private val _journalEntries = MutableStateFlow<List<JournalEntry>>(emptyList())
    val journalEntries: StateFlow<List<JournalEntry>> = _journalEntries.asStateFlow()

    private val _currentJournalText = MutableStateFlow("")
    val currentJournalText: StateFlow<String> = _currentJournalText.asStateFlow()

    private val _currentMoodIndex = MutableStateFlow(4) // 4 = Calm Serene
    val currentMoodIndex: StateFlow<Int> = _currentMoodIndex.asStateFlow()

    // Audio/Podcast State
    private val _isPlayingPodcast = MutableStateFlow(false)
    val isPlayingPodcast: StateFlow<Boolean> = _isPlayingPodcast.asStateFlow()

    private val _podcastProgress = MutableStateFlow(0.35f)
    val podcastProgress: StateFlow<Float> = _podcastProgress.asStateFlow()

    private val _currentPodcastTitle = MutableStateFlow("EP 12: Crafting Space for Luxury Digital Assets")
    val currentPodcastTitle: StateFlow<String> = _currentPodcastTitle.asStateFlow()

    init {
        loadJournalData()
        loadCartData()
    }

    // Navigation Controls
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            navigationStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun goBack(): Boolean {
        if (navigationStack.isNotEmpty()) {
            _currentScreen.value = navigationStack.removeAt(navigationStack.size - 1)
            return true
        }
        return false
    }

    // Creative Lab AI Trigger
    fun queryAbienserenityAI(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            _aiLoading.value = true
            _aiResult.value = ""
            val reply = GeminiClient.generateCreativeResponse(prompt)
            _aiResult.value = reply
            _aiHistory.value = _aiHistory.value + (prompt to reply)
            _aiLoading.value = false
        }
    }

    fun clearAIChat() {
        _aiResult.value = ""
        _aiHistory.value = emptyList()
    }

    // Cart Management
    fun addToCart(product: Product, quantity: Int = 1) {
        val currentList = _cart.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val oldItem = currentList[index]
            currentList[index] = oldItem.copy(quantity = oldItem.quantity + quantity)
        } else {
            currentList.add(CartItem(product, quantity))
        }
        _cart.value = currentList
        saveCartData()
    }

    fun removeFromCart(product: Product) {
        val currentList = _cart.value.toMutableList()
        currentList.removeAll { it.product.id == product.id }
        _cart.value = currentList
        saveCartData()
    }

    fun incrementCartItem(product: Product) {
        val currentList = _cart.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            currentList[index] = currentList[index].copy(quantity = currentList[index].quantity + 1)
            _cart.value = currentList
            saveCartData()
        }
    }

    fun decrementCartItem(product: Product) {
        val currentList = _cart.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val item = currentList[index]
            if (item.quantity > 1) {
                currentList[index] = item.copy(quantity = item.quantity - 1)
            } else {
                currentList.removeAt(index)
            }
            _cart.value = currentList
            saveCartData()
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
        saveCartData()
    }

    // Category Selector
    fun selectCategory(category: ProductCategory) {
        _selectedCategory.value = category
    }

    fun viewProductDetail(product: Product?) {
        _selectedProductForDetail.value = product
    }

    // Name Tag Customizer
    fun updateBadgeName(name: String) {
        _customBadgeName.value = name
    }

    fun updateBadgeMaterial(material: String) {
        _customBadgeMaterial.value = material
    }

    // Preset Preview Select
    fun updatePresetPreview(preset: String) {
        _activePresetPreview.value = preset
    }

    // Habit Clicker
    fun toggleHabit(habit: String) {
        val updated = _habitGrid.value.toMutableMap()
        updated[habit] = !(updated[habit] ?: false)
        _habitGrid.value = updated
    }

    // Journal & Mood Setup
    fun setJournalText(text: String) {
        _currentJournalText.value = text
    }

    fun setMoodIndex(index: Int) {
        _currentMoodIndex.value = index
    }

    fun saveJournalEntry() {
        val note = _currentJournalText.value
        if (note.isBlank()) return

        val moodIcon = when (_currentMoodIndex.value) {
            1 -> "Energetic ✨"
            2 -> "Focused 📐"
            3 -> "Balanced ⚖️"
            4 -> "Calm Serene 🌊"
            5 -> "Dreamy 🌙"
            else -> "Mindful 🌱"
        }

        val sdf = SimpleDateFormat("MMM d, yyyy - hh:mm a", Locale.getDefault())
        val dateString = sdf.format(Date())

        val newEntry = JournalEntry(
            date = dateString,
            mood = moodIcon,
            moodValue = _currentMoodIndex.value,
            note = note
        )

        _journalEntries.value = listOf(newEntry) + _journalEntries.value
        _currentJournalText.value = ""
        saveJournalData()
    }

    fun deleteJournalEntry(entry: JournalEntry) {
        _journalEntries.value = _journalEntries.value.filter { it != entry }
        saveJournalData()
    }

    // Podcast controls
    fun togglePodcast() {
        _isPlayingPodcast.value = !_isPlayingPodcast.value
    }

    fun updatePodcastProgress(progress: Float) {
        _podcastProgress.value = progress
    }

    fun selectPodcastTrack(title: String) {
        _currentPodcastTitle.value = title
        _podcastProgress.value = 0.0f
        _isPlayingPodcast.value = true
    }

    // --- SharedPreferences State Storage ---
    private fun saveJournalData() {
        try {
            val listType = Types.newParameterizedType(List::class.java, JournalEntry::class.java)
            val adapter = moshi.adapter<List<JournalEntry>>(listType)
            val json = adapter.toJson(_journalEntries.value)
            sharedPrefs.edit().putString("journals_json", json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadJournalData() {
        val json = sharedPrefs.getString("journals_json", null)
        if (json != null) {
            try {
                val listType = Types.newParameterizedType(List::class.java, JournalEntry::class.java)
                val adapter = moshi.adapter<List<JournalEntry>>(listType)
                val list = adapter.fromJson(json)
                if (list != null) {
                    _journalEntries.value = list
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            // Prepopulate an elegant first journal entry from Enid Kimberly
            _journalEntries.value = listOf(
                JournalEntry(
                    date = "Jun 7, 2026 - 10:30 AM",
                    mood = "Calm Serene 🌊",
                    moodValue = 4,
                    note = "Welcome to the SoulResetStudio space. Today, I am establishing Enid's branding ecosystem. The synergy between digital products, active design feedback, creative prompting, and daily moments of quiet is our foundational heartbeat. Take a deep breath."
                )
            )
        }
    }

    private fun saveCartData() {
        try {
            val listType = Types.newParameterizedType(List::class.java, CartItem::class.java)
            val adapter = moshi.adapter<List<CartItem>>(listType)
            val json = adapter.toJson(_cart.value)
            sharedPrefs.edit().putString("cart_json", json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadCartData() {
        val json = sharedPrefs.getString("cart_json", null)
        if (json != null) {
            try {
                val listType = Types.newParameterizedType(List::class.java, CartItem::class.java)
                val adapter = moshi.adapter<List<CartItem>>(listType)
                val list = adapter.fromJson(json)
                if (list != null) {
                    _cart.value = list
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
