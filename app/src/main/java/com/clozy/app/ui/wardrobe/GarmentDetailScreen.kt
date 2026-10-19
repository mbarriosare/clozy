package com.clozy.app.ui.wardrobe

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.ui.components.ClozyOutlinedButton
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

@Composable
private fun DetailHeader(onBackClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(36.dp).background(ClozyColors.Blush, CircleShape).clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(18.dp)) }
        Text(
            "DETALLE DE PRENDA",
            style = ClozyType.CaptionBold.copy(fontSize = 10.sp, letterSpacing = 2.sp),
            color = ClozyColors.TextMuted,
        )
    }
}

@Composable
private fun AttributeCard(label: String, value: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier
            .background(ClozyColors.White, shape)
            .border(1.dp, ClozyColors.TextFaint, shape)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(label.uppercase(), style = ClozyType.CaptionBold.copy(fontSize = 9.sp, letterSpacing = 1.35.sp), color = ClozyColors.TextMuted)
        Text(value, style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.Bold), color = ClozyColors.TextPrimary, modifier = Modifier.padding(top = 2.dp))
    }
}


@Composable
fun GarmentDetailScreen(
    garment: Garment,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        DetailHeader(onBackClick)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(192.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(ClozyColors.Blush, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center,
            ) {
                com.clozy.app.ui.components.GarmentThumbnail(
                    photoUrl = garment.photoUrl,
                    thumbnailRes = garment.thumbnailRes,
                    iconRes = garment.category.iconRes(),
                    tint = garment.primaryColor.swatch,
                    iconSize = 96.dp,
                    modifier = Modifier,
                )
            }
            Column(Modifier.padding(top = 20.dp)) {
                Text(garment.name, style = ClozyType.Title.copy(fontSize = 20.sp), color = ClozyColors.TextPrimary)
                Text(garment.category.label, style = ClozyType.BodyRegular, color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 2.dp))
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(top = 20.dp).height(220.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                userScrollEnabled = false,
            ) {
                item { AttributeCard("Color principal", garment.primaryColor.label) }
                item { AttributeCard("Estilo", garment.style?.label ?: "Sin definir") }
                item { AttributeCard("Estampado", garment.pattern.label) }
                item { AttributeCard("Temporada", garment.season?.label ?: "Sin definir") }
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                    AttributeCard("Uso", if (garment.usageCount == 0) "Sin usar aún" else "Usada ${garment.usageCount} veces", Modifier.fillMaxWidth())
                }
                if (garment.secondaryColors.isNotEmpty()) {
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .background(ClozyColors.White, RoundedCornerShape(16.dp))
                                .border(1.dp, ClozyColors.TextFaint, RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                        ) {
                            Text("COLORES SECUNDARIOS", style = ClozyType.CaptionBold.copy(fontSize = 9.sp, letterSpacing = 1.35.sp), color = ClozyColors.TextMuted)
                            Row(Modifier.padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                garment.secondaryColors.forEach { c ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Box(Modifier.size(14.dp).background(c.swatch, CircleShape).border(1.dp, ClozyColors.TextFaint, CircleShape))
                                        Text(c.label, style = ClozyType.BodyMedium.copy(fontSize = 12.sp), color = ClozyColors.TextPrimary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ClozyOutlinedActionButton("Editar prenda", onEditClick)
                ClozyOutlinedActionButton(
                    if (garment.isFavorite) "Quitar de favoritas" else "Marcar como favorita",
                    onToggleFavorite,
                )
                ClozyOutlinedActionButton("Eliminar prenda", onDeleteClick, danger = true)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ClozyOutlinedActionButton(text: String, onClick: () -> Unit, danger: Boolean = false) {
    val bg = if (danger) ClozyColors.ErrorSoft else ClozyColors.Blush
    val fg = if (danger) ClozyColors.Error else ClozyColors.TextPrimary
    Box(
        Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) { Text(text, style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.Bold), color = fg) }
}


@Composable
fun DeleteGarmentDialog(onConfirm: () -> Unit, onCancel: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .background(Color(0x73211A1D))
            .clickable(onClick = onCancel),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            Modifier
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxWidth()
                .background(ClozyColors.White, RoundedCornerShape(24.dp))
                .clickable(enabled = false) {}
                .padding(24.dp),
        ) {
            Box(Modifier.align(Alignment.CenterHorizontally).size(width = 40.dp, height = 4.dp).background(ClozyColors.TextFaint, CircleShape))
            Column(Modifier.padding(top = 20.dp)) {
                Text("¿Eliminar esta prenda?", style = ClozyType.Headline18, color = ClozyColors.TextPrimary)
                Text(
                    "La prenda se eliminará de tu armario y dejará de estar disponible para tus recomendaciones de outfits.",
                    style = ClozyType.BodyRegular.copy(lineHeight = 22.75.sp),
                    color = ClozyColors.TextMuted,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
            Column(Modifier.padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    Modifier.fillMaxWidth().background(ClozyColors.Error, RoundedCornerShape(16.dp)).clickable(onClick = onConfirm).padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) { Text("Eliminar", style = ClozyType.Button, color = ClozyColors.White) }
                Box(
                    Modifier.fillMaxWidth().background(ClozyColors.Blush, RoundedCornerShape(16.dp)).clickable(onClick = onCancel).padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) { Text("Cancelar", style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary) }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun DetailPreview() {
    ClozyTheme { GarmentDetailScreen(MockGarments[3], {}, {}, {}, {}) }
}
