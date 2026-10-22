package com.example.api

import android.util.Log
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    // Data classes matching Moshi specs for direct JSON serialization
    data class RequestPayload(
        val contents: List<Content>,
        val systemInstruction: Content? = null
    )

    data class Content(
        val parts: List<Part>
    )

    data class Part(
        val text: String
    )

    data class ResponsePayload(
        val candidates: List<Candidate>?
    )

    data class Candidate(
        val content: Content?
    )

    suspend fun generateCreativeResponse(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.e(TAG, "API Key is missing or default placeholder.")
            return@withContext "Welcome to AbienserenityAI, Enid's Creative Lab playground. Inside the live app environment, you can access her custom branding brain! Please add a valid GEMINI_API_KEY in the AI Studio Secrets panel to enable real-time replies. \n\n[Alternative Creative Prompt Idea]: Try selecting one of our inspiration options below for a pre-loaded creative breakdown!"
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val systemInstructionText = """
            You are AbienserenityAI, the custom creative intelligence system of Enid Kimberly.
            You help design and brand boutique entrepreneurs, designers, and creatives searching for modern luxury aesthetics.
            
             Tone of Voice:
            - Sophisticated, serene, and authoritative yet warm and deeply inspiring.
            - Focus on chic design curation, elegant typography hints, earthy or muted luxury color palettes, and minimal layout ideas.
            - Incorporate themes of 'Soul Reset' and 'Mindfulness' into branding structures.
            
            Format your responses beautifully with Markdown headings, neat bullet points, and high line spacing or short paragraphs. Avoid long blocks of flat text.
        """.trimIndent()

        val payload = RequestPayload(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = Content(parts = listOf(Part(text = systemInstructionText)))
        )

        try {
            val adapter = moshi.adapter(RequestPayload::class.java)
            val jsonString = adapter.toJson(payload)

            val requestBody = jsonString.toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: ""
                    Log.e(TAG, "API error: ${response.code} $errorBody")
                    return@withContext "My cognitive server is taking a gentle breath. Let's try once more, or use our premium local checklist tools!"
                }
                val bodyText = response.body?.string()
                if (bodyText == null) {
                    return@withContext "Empty response returned from the server."
                }
                
                val responseAdapter = moshi.adapter(ResponsePayload::class.java)
                val parsed = responseAdapter.fromJson(bodyText)
                val textResult = parsed?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                
                return@withContext textResult ?: "I could not evoke an elegant thought for this prompt. Try a slightly different word pairing!"
            }
        } catch (e: IOException) {
            Log.e(TAG, "Network connection issue", e)
            return@withContext "Network connection timed out. I am unable to touch the digital creative ether. Please check your network connection."
        } catch (e: Exception) {
            Log.e(TAG, "General parse issue", e)
            return@withContext "An unexpected layout error occurred in the brain server. Fortunately, our local catalogs are still fully operational!"
        }
    }
}
