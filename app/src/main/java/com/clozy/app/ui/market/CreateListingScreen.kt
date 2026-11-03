package com.clozy.app.ui.market

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.components.GarmentThumbnail
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType
import com.clozy.app.ui.wardrobe.Garment
import com.clozy.app.ui.wardrobe.iconRes

@Composable
fun CreateListingScreen(
    garments: List<Garment>,
    onBackClick: () -> Unit,
    onPublishClick: (garment: Garment, price: Int, condition: ListingCondition) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    var selected by remember { mutableStateOf<Garment?>(null) }
    var priceText by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf(ListingCondition.Good) }

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
            Text("Publicar prenda", style = ClozyType.Title.copy(fontSize = androidx.compose.ui.unit.TextUnit(20f, androidx.compose.ui.unit.TextUnitType.Sp)), color = ClozyColors.TextPrimary)
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)) {
            Text("ELIGE UNA PRENDA DE TU ARMARIO", style = ClozyType.LabelBold9, color = ClozyColors.TextMuted)
            if (garments.isEmpty()) {
                Text(
                    "Agrega prendas a tu armario para poder publicarlas.",
                    style = ClozyType.BodyMedium, color = ClozyColors.TextMuted,
                    modifier = Modifier.padding(top = 12.dp),
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.padding(top = 12.dp).height(((garments.size / 3 + 1) * 110).dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false,
                ) {
                    items(garments, key = { it.id }) { g ->
                        val isSelected = selected?.id == g.id
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(ClozyColors.Blush, RoundedCornerShape(12.dp))
                                .border(if (isSelected) 2.dp else 1.dp, if (isSelected) ClozyColors.Primary else ClozyColors.TextFaint, RoundedCornerShape(12.dp))
                                .clickable { selected = g },
                            contentAlignment = Alignment.Center,
                        ) {
                            GarmentThumbnail(g.photoUrl, g.thumbnailRes, g.category.iconRes(), g.primaryColor.swatch, iconSize = 32.dp)
                            if (isSelected) {
                                Box(
                                    Modifier.align(Alignment.TopEnd).padding(4.dp).size(18.dp).background(ClozyColors.Primary, CircleShape),
                                    contentAlignment = Alignment.Center,
                                ) { Icon(Icons.Filled.Check, null, tint = ClozyColors.White, modifier = Modifier.size(10.dp)) }
                            }
                        }
                    }
                }
            }

            Column(Modifier.padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("PRECIO", style = ClozyType.LabelBold9, color = ClozyColors.TextMuted)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(ClozyColors.Surface, RoundedCornerShape(16.dp))
                        .border(1.dp, ClozyColors.TextFaint, RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("$", style = ClozyType.Field, color = ClozyColors.TextPrimary)
                    BasicTextField(
                        value = priceText,
                        onValueChange = { priceText = it.filter(Char::isDigit) },
                        singleLine = true,
                        textStyle = ClozyType.Field.copy(color = ClozyColors.TextPrimary),
                        cursorBrush = SolidColor(ClozyColors.Primary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).padding(start = 8.dp),
                    )
                }
            }

            Column(Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("CONDICIÓN", style = ClozyType.LabelBold9, color = ClozyColors.TextMuted)
                Row(Modifier.padding(top = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ListingCondition.entries.forEach { opt ->
                        val active = opt == condition
                        Text(
                            opt.label, style = ClozyType.CaptionBold,
                            color = if (active) ClozyColors.White else ClozyColors.TextMuted,
                            modifier = Modifier
                                .background(if (active) ClozyColors.Primary else ClozyColors.Blush, CircleShape)
                                .border(1.dp, if (active) ClozyColors.Primary else ClozyColors.TextFaint, CircleShape)
                                .clickable { condition = opt }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                        )
                    }
                }
            }

            if (errorMessage != null) {
                Text(errorMessage, style = ClozyType.Caption, color = ClozyColors.Error, modifier = Modifier.padding(top = 16.dp))
            }

            Spacer(Modifier.height(24.dp))
            ClozyPrimaryButton(
                "Publicar",
                onClick = {
                    val garment = selected
                    val price = priceText.toIntOrNull()
                    if (garment != null && price != null && price > 0) {
                        onPublishClick(garment, price, condition)
                    }
                },
            )
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun CreateListingPreview() { ClozyTheme { CreateListingScreen(emptyList(), {}, { _, _, _ -> }) } }
