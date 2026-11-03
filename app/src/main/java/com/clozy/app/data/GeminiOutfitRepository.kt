package com.clozy.app.data

import com.clozy.app.BuildConfig
import com.clozy.app.ui.wardrobe.Garment
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.delay
import org.json.JSONObject

data class AiOutfitResult(
    val garmentIds: List<Int>,
    val name: String,
    val explanation: String,
)

object GeminiOutfitRepository {

    // Modelos a intentar en orden. Google apaga modelos con frecuencia, así que si uno
    // falla (modelo no encontrado, etc.) se prueba el siguiente. Se puede fijar uno
    // propio con GEMINI_MODEL en local.properties.
    private val modelNames: List<String> = listOfNotNull(
        BuildConfig.GEMINI_MODEL.takeIf { it.isNotBlank() },
        "gemini-3.8-flash",
        "gemini-3.6-flash",
        "gemini-3.1-flash-lite",
    ).distinct()

    private var workingModel: String? = null

    private const val TAG = "ClozyGeminiOutfit"

    suspend fun generateOutfit(
        garments: List<Garment>,
        occasion: String,
        style: String,
        weather: String,
    ): AiOutfitResult? {
        if (BuildConfig.GEMINI_API_KEY.isBlank()) {
            android.util.Log.w(TAG, "GEMINI_API_KEY está vacía: revisa local.properties y recompila (Rebuild Project)")
            return null
        }
        if (garments.isEmpty()) return null

        val wardrobeDescription = garments.joinToString("\n") { g ->
            "ID ${g.id}: ${g.name} — categoría ${g.category.label} — color ${g.primaryColor.label} — estilo ${g.style?.label ?: "sin definir"}"
        }

        val prompt = """
Eres Clozy AI, un asistente de moda dentro de una app de armario digital.

Este es el armario real de la persona, cada prenda con su ID exacto:
$wardrobeDescription

Elige una combinación de ropa (outfit) usando SOLAMENTE IDs de la lista anterior, nunca inventes prendas que no estén ahí. Elige entre 2 y 4 prendas que combinen bien entre sí (normalmente una parte superior o un vestido, una parte inferior si aplica, y opcionalmente zapatos o un accesorio).

Ocasión deseada: $occasion
Estilo deseado: $style
Clima: $weather

Responde ÚNICAMENTE con un JSON válido, sin texto antes ni después, con este formato exacto:
{"garmentIds": [1,2,3], "name": "nombre corto y natural del outfit, en español", "explanation": "una o dos frases en español explicando por qué esta combinación funciona para la ocasión y el clima indicados"}
        """.trimIndent()

        for (name in (listOfNotNull(workingModel) + modelNames).distinct()) {
            val result = tryModel(name, prompt, garments, style)
            if (result != null) {
                workingModel = name
                android.util.Log.i(TAG, "Outfit generado con el modelo $name")
                return result
            }
        }
        android.util.Log.e(TAG, "Ningún modelo respondió; se usará el armado local")
        return null
    }

    // Reintenta el mismo modelo si Google responde 503 (alta demanda, es temporal).
    private suspend fun tryModel(
        name: String,
        prompt: String,
        garments: List<Garment>,
        style: String,
    ): AiOutfitResult? {
        repeat(3) { attempt ->
            try {
                val model = GenerativeModel(modelName = name, apiKey = BuildConfig.GEMINI_API_KEY)
                val rawText = model.generateContent(prompt).text
                val json = rawText?.let { extractJson(it) } ?: return null
                val obj = JSONObject(json)
                val idsArray = obj.getJSONArray("garmentIds")
                val ids = (0 until idsArray.length()).map { idsArray.getInt(it) }
                val validIds = ids.filter { id -> garments.any { it.id == id } }
                if (validIds.isEmpty()) return null
                return AiOutfitResult(
                    garmentIds = validIds,
                    name = obj.optString("name", "Look $style"),
                    explanation = obj.optString("explanation", ""),
                )
            } catch (e: Exception) {
                val msg = e.message.orEmpty()
                val temporary = "503" in msg || "UNAVAILABLE" in msg || "high demand" in msg
                android.util.Log.e(TAG, "Error con el modelo $name (intento ${attempt + 1})", e)
                if (!temporary) return null
                if (attempt < 2) delay(1500L * (attempt + 1))
            }
        }
        return null
    }

    private fun extractJson(text: String): String? {
        val start = text.indexOf('{')
        val end = text.lastIndexOf('}')
        if (start == -1 || end == -1 || end < start) return null
        return text.substring(start, end + 1)
    }
}
