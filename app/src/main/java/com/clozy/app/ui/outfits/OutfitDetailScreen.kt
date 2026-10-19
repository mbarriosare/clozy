package com.clozy.app.ui.outfits

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.ui.components.ClozyOutlinedButton
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType
import com.clozy.app.ui.wardrobe.Garment
import com.clozy.app.ui.wardrobe.iconRes

/** Figma: OutfitDetalle (114:2974). */
@Composable
fun OutfitDetailScreen(
    outfit: Outfit,
    garments: List<Garment>,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onRegenerateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pieces = outfit.garmentIds.mapNotNull { id -> garments.find { it.id == id } }

    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        Row(
            Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.size(36.dp).background(ClozyColors.Blush, CircleShape).clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(18.dp)) }
            Text(outfit.name, style = ClozyType.Title.copy(fontSize = 20.sp), color = ClozyColors.TextPrimary)
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)) {
            Row(
                Modifier.fillMaxWidth().background(ClozyColors.Blush, RoundedCornerShape(24.dp)).padding(vertical = 32.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                pieces.forEach { g ->
                    Box(Modifier.padding(horizontal = 8.dp).size(64.dp), contentAlignment = Alignment.Center) {
                        com.clozy.app.ui.components.GarmentThumbnail(
                            photoUrl = g.photoUrl,
                            thumbnailRes = g.thumbnailRes,
                            iconRes = g.category.iconRes(),
                            tint = g.primaryColor.swatch,
                            iconSize = 56.dp,
                            modifier = Modifier.clip(RoundedCornerShape(14.dp)),
                        )
                    }
                }
            }

            Column(Modifier.padding(top = 20.dp)) {
                Text("PRENDAS DEL OUTFIT", style = ClozyType.LabelBold9, color = ClozyColors.TextMuted)
                Column(Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pieces.forEach { g ->
                        val shape = RoundedCornerShape(16.dp)
                        Row(
                            Modifier.fillMaxWidth().background(ClozyColors.White, shape).border(1.dp, ClozyColors.TextFaint, shape).padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(Modifier.size(34.dp), contentAlignment = Alignment.Center) {
                                com.clozy.app.ui.components.GarmentThumbnail(
                                    photoUrl = g.photoUrl,
                                    thumbnailRes = g.thumbnailRes,
                                    iconRes = g.category.iconRes(),
                                    tint = g.primaryColor.swatch,
                                    iconSize = 28.dp,
                                    modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                                )
                            }
                            Column(Modifier.weight(1f)) {
                                Text(g.category.label.uppercase(), style = ClozyType.CaptionBold.copy(fontSize = 9.sp, letterSpacing = 1.08.sp), color = ClozyColors.TextMuted)
                                Text(g.name, style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.SemiBold), color = ClozyColors.TextPrimary)
                            }
                            Icon(Icons.Filled.ChevronRight, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoChip("Ocasión", outfit.occasion, Modifier.weight(1f))
                InfoChip("Estilo", outfit.style, Modifier.weight(1f))
                InfoChip("Creado por", outfit.createdBy, Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ClozyOutlinedButton(
                    if (outfit.isFavorite) "♥ Guardado en favoritos" else "♡ Guardar outfit",
                    onClick = onToggleFavorite,
                )
                ClozyOutlinedButton("Regenerar outfit", onClick = onRegenerateClick)
                ClozyOutlinedButton("Eliminar outfit", onClick = onDeleteClick)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier.background(ClozyColors.White, shape).border(1.dp, ClozyColors.TextFaint, shape).padding(12.dp),
    ) {
        Text(label.uppercase(), style = ClozyType.CaptionBold.copy(fontSize = 8.sp, letterSpacing = 0.96.sp), color = ClozyColors.TextMuted)
        Text(value, style = ClozyType.BodyMedium.copy(fontSize = 12.sp, fontWeight = FontWeight.SemiBold), color = ClozyColors.TextPrimary, modifier = Modifier.padding(top = 2.dp))
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun OutfitDetailPreview() {
    ClozyTheme {
        OutfitDetailScreen(
            Outfit(1, "Look casual", "Casual", "Casual", emptyList()),
            emptyList(), {}, {}, {}, {},
        )
    }
}
