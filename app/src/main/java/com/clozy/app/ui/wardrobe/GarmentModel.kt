package com.clozy.app.ui.wardrobe

import androidx.compose.ui.graphics.Color
import com.clozy.app.R
import com.clozy.app.ui.theme.ClozyColors

enum class GarmentCategory(val label: String) {
    Top("Parte superior"), Bottom("Parte inferior"), Dress("Vestidos"),
    Shoes("Zapatos"), Accessory("Accesorios"),
}

enum class GarmentColor(val label: String, val swatch: Color) {
    White("Blanco", ClozyColors.ColorWhite),
    Black("Negro", ClozyColors.ColorBlack),
    Blue("Azul", ClozyColors.ColorBlue),
    Pink("Rosa", ClozyColors.ColorPink),
    Fuchsia("Fucsia", ClozyColors.ColorFuchsia),
    Lilac("Lila", ClozyColors.ColorLilac),
    Coral("Coral", ClozyColors.ColorCoral),
    Gray("Gris", ClozyColors.ColorGray),
    Green("Verde", ClozyColors.ColorGreen),
}

enum class GarmentPattern(val label: String) {
    Solid("Liso"), Floral("Floral"), Stripes("Rayas"), Plaid("Cuadros"),
    Animal("Animal print"), Geometric("Geométrico"), Other("Otro"),
}

enum class GarmentStyle(val label: String) {
    Casual("Casual"), Formal("Formal"), Sport("Deportivo"),
    Romantic("Romántico"), Fashion("Moda"), Minimal("Minimal"),
}

enum class GarmentSeason(val label: String) {
    AllYear("Todo el año"), SpringSummer("Primavera/Verano"), FallWinter("Otoño/Invierno"),
}

data class Garment(
    val id: Int,
    val name: String,
    val category: GarmentCategory,
    val primaryColor: GarmentColor,
    val secondaryColors: List<GarmentColor> = emptyList(),
    val pattern: GarmentPattern = GarmentPattern.Solid,
    val isFavorite: Boolean = false,
    val style: GarmentStyle? = null,
    val season: GarmentSeason? = null,
    val usageCount: Int = 0,

    val thumbnailRes: Int? = null,
    val photoUrl: String? = null,
)


fun GarmentCategory.iconRes(): Int = when (this) {
    GarmentCategory.Top, GarmentCategory.Accessory -> R.drawable.ic_garment_generic
    GarmentCategory.Bottom -> R.drawable.illus_pants
    GarmentCategory.Dress -> R.drawable.ic_garment_dress
    GarmentCategory.Shoes -> R.drawable.ic_garment_shoes
}

val MockGarments = listOf(
    Garment(1, "Camiseta básica", GarmentCategory.Top, GarmentColor.White, isFavorite = true, thumbnailRes = R.drawable.thumb_camiseta),
    Garment(2, "Jeans rectos", GarmentCategory.Bottom, GarmentColor.Blue, thumbnailRes = R.drawable.thumb_jeans),
    Garment(3, "Blazer negro", GarmentCategory.Top, GarmentColor.Black, isFavorite = true, thumbnailRes = R.drawable.thumb_blazer),
    Garment(4, "Vestido floral", GarmentCategory.Dress, GarmentColor.Pink, secondaryColors = listOf(GarmentColor.White, GarmentColor.Green), pattern = GarmentPattern.Floral, style = GarmentStyle.Romantic, season = GarmentSeason.SpringSummer, usageCount = 3, thumbnailRes = R.drawable.thumb_vestido),
    Garment(5, "Zapatillas blancas", GarmentCategory.Shoes, GarmentColor.White, isFavorite = true, thumbnailRes = R.drawable.thumb_zapatillas),
    Garment(6, "Bolso mini", GarmentCategory.Accessory, GarmentColor.Fuchsia, thumbnailRes = R.drawable.thumb_bolso),
)
