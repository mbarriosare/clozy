package com.clozy.app.ui.outfits

data class Outfit(
    val id: Int,
    val name: String,
    val occasion: String,
    val style: String,
    val garmentIds: List<Int>,
    val isFavorite: Boolean = false,
    val createdBy: String = "Tú",
)

val OccasionOptions = listOf("Casual", "Trabajo", "Cita", "Salida", "Evento", "Universidad", "Viaje")
val WeatherOptions = listOf("Caluroso", "Templado", "Frío", "Lluvioso")
