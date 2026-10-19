package com.clozy.app.ui.outfits

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType
import com.clozy.app.ui.wardrobe.Garment
import com.clozy.app.ui.wardrobe.iconRes

/** Figma: MisOutfits (76:11044). */
@Composable
fun OutfitsScreen(
    outfits: List<Outfit>,
    garments: List<Garment>,
    onCreateClick: () -> Unit,
    onOutfitClick: (Outfit) -> Unit,
    modifier: Modifier = Modifier,
) {
    val favorites = outfits.filter { it.isFavorite }

    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 16.dp)) {
            Text("Mis outfits", style = ClozyType.Headline, color = ClozyColors.TextPrimary)
            Text("Looks para cada momento.", style = ClozyType.BodyRegular, color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 2.dp))
        }

        if (outfits.isEmpty()) {
            Column(Modifier.weight(1f).fillMaxWidth().padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)) {
                ClozyAiCard(onCreateClick)
                Text(
                    "Todavía no tienes outfits. Crea el primero con Clozy AI.",
                    style = ClozyType.BodyMedium, color = ClozyColors.TextMuted, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                )
            }
        } else {
            Column(Modifier.weight(1f).fillMaxWidth()) {
                androidx.compose.foundation.lazy.LazyColumn(Modifier.fillMaxSize()) {
                    item { ClozyAiCard(onCreateClick, Modifier.padding(horizontal = 24.dp)) }
                    item { OutfitSection("Para ti", outfits, garments, onOutfitClick, Modifier.padding(top = 20.dp)) }
                    if (favorites.isNotEmpty()) {
                        item { OutfitSection("Mis favoritos", favorites, garments, onOutfitClick, Modifier.padding(top = 20.dp, bottom = 20.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClozyAiCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(ClozyColors.Primary, ClozyColors.Lavender)), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .padding(20.dp),
    ) {
        Text("CLOZY AI", style = ClozyType.CaptionBold.copy(fontSize = 10.sp, letterSpacing = 2.sp), color = Color.White.copy(alpha = 0.75f))
        Text(
            "Crea un outfit con Clozy AI ✦", style = ClozyType.Headline18, color = Color.White,
            modifier = Modifier.padding(top = 4.dp),
        )
        Text(
            "Cuéntame qué necesitas y crearé un look con tus prendas.",
            style = ClozyType.BodyRegular.copy(fontSize = 12.sp, lineHeight = 16.sp), color = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 4.dp),
        )
        Row(Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Empezar", style = ClozyType.CaptionBold, color = Color.White)
            Icon(Icons.Filled.ChevronRight, null, tint = Color.White, modifier = Modifier.size(12.dp))
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun OutfitSection(
    title: String, items: List<Outfit>, garments: List<Garment>,
    onOutfitClick: (Outfit) -> Unit, modifier: Modifier = Modifier,
) {
    Column(modifier.padding(horizontal = 24.dp)) {
        Text(title.uppercase(), style = ClozyType.LabelBold9, color = ClozyColors.TextMuted)
        androidx.compose.foundation.layout.FlowRow(
            modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items.forEach { outfit ->
                OutfitCard(outfit, garments, onClick = { onOutfitClick(outfit) }, modifier = Modifier.weight(1f, fill = false))
            }
        }
    }
}

@Composable
private fun OutfitCard(outfit: Outfit, garments: List<Garment>, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    val pieces = outfit.garmentIds.mapNotNull { id -> garments.find { it.id == id } }.take(3)
    Column(
        modifier
            .background(ClozyColors.White, shape)
            .border(1.dp, ClozyColors.TextFaint, shape)
            .clickable(onClick = onClick),
    ) {
        Row(
            Modifier.background(ClozyColors.Blush).aspectRatio(1.6f).padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            pieces.forEach { g ->
                Box(Modifier.weight(1f).aspectRatio(1f).padding(4.dp), contentAlignment = Alignment.Center) {
                    com.clozy.app.ui.components.GarmentThumbnail(
                        photoUrl = g.photoUrl,
                        thumbnailRes = g.thumbnailRes,
                        iconRes = g.category.iconRes(),
                        tint = g.primaryColor.swatch,
                        iconSize = 40.dp,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    )
                }
            }
        }
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(outfit.name, style = ClozyType.CaptionBold.copy(fontSize = 12.sp), color = ClozyColors.TextPrimary)
            Text(
                "${outfit.occasion} · ${outfit.style}", style = ClozyType.Footnote.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified, fontSize = 10.sp),
                color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun OutfitsEmptyPreview() {
    ClozyTheme { OutfitsScreen(emptyList(), emptyList(), {}, {}) }
}
