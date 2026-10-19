package com.clozy.app.ui.wardrobe

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType


@Composable
fun WardrobeScreen(
    garments: List<Garment>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: GarmentCategory?,
    onCategoryClick: (GarmentCategory?) -> Unit,
    onAddClick: () -> Unit,
    onGarmentClick: (Garment) -> Unit,
    modifier: Modifier = Modifier,
) {
    val filtered = garments.filter { g ->
        (selectedCategory == null || g.category == selectedCategory) &&
            (searchQuery.isBlank() || g.name.contains(searchQuery, ignoreCase = true))
    }

    Column(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {
        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Mi armario", style = ClozyType.Headline, color = ClozyColors.TextPrimary)
                Row(
                    Modifier
                        .background(ClozyColors.Primary, CircleShape)
                        .clickable(onClick = onAddClick)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) { Text("+ Agregar", style = ClozyType.BodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold), color = ClozyColors.White) }
            }
            Text(
                "${garments.size} prendas",
                style = ClozyType.BodyMedium,
                color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        SearchBar(searchQuery, onSearchQueryChange, Modifier.padding(horizontal = 24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CategoryChip("Todo", selectedCategory == null) { onCategoryClick(null) }
            GarmentCategory.entries.forEach { cat ->
                CategoryChip(cat.label, selectedCategory == cat) { onCategoryClick(cat) }
            }
        }

        if (filtered.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "No encontramos prendas con estos filtros.",
                    style = ClozyType.BodyMedium,
                    color = ClozyColors.TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp),
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(filtered, key = { it.id }) { garment ->
                    GarmentCard(garment, onClick = { onGarmentClick(garment) })
                }
            }
        }
    }
}

@Composable
private fun SearchBar(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ClozyColors.White, shape)
            .border(1.dp, ClozyColors.TextFaint, shape)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Outlined.Search, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(16.dp))
        Box(Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text("Buscar en mi armario...", style = ClozyType.Field, color = ClozyColors.TextPrimary.copy(alpha = 0.5f))
            }
            BasicTextField(
                value = value, onValueChange = onValueChange, singleLine = true,
                textStyle = ClozyType.Field.copy(color = ClozyColors.TextPrimary),
                cursorBrush = SolidColor(ClozyColors.Primary),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
internal fun CategoryChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val shape = CircleShape
    Text(
        label,
        style = ClozyType.CaptionBold.copy(fontSize = androidx.compose.ui.unit.TextUnit(12f, androidx.compose.ui.unit.TextUnitType.Sp)),
        color = if (selected) ClozyColors.White else ClozyColors.TextMuted,
        modifier = Modifier
            .background(if (selected) ClozyColors.Primary else ClozyColors.Blush, shape)
            .border(1.dp, if (selected) ClozyColors.Primary else ClozyColors.TextFaint, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}

@Composable
internal fun GarmentCard(garment: Garment, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ClozyColors.White, shape)
            .border(1.dp, ClozyColors.TextFaint, shape)
            .clickable(onClick = onClick),
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1.35f)
                .background(ClozyColors.Blush),
            contentAlignment = Alignment.Center,
        ) {
            com.clozy.app.ui.components.GarmentThumbnail(
                photoUrl = garment.photoUrl,
                thumbnailRes = garment.thumbnailRes,
                iconRes = garment.category.iconRes(),
                tint = garment.primaryColor.swatch,
                modifier = Modifier,
            )
            if (garment.isFavorite) {
                Icon(
                    Icons.Filled.Favorite, null, tint = ClozyColors.Primary,
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(14.dp),
                )
            }
        }
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(garment.name, style = ClozyType.CaptionBold.copy(fontSize = androidx.compose.ui.unit.TextUnit(12f, androidx.compose.ui.unit.TextUnitType.Sp)), color = ClozyColors.TextPrimary)
            Text(
                garment.category.label,
                style = ClozyType.Footnote.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified, fontSize = androidx.compose.ui.unit.TextUnit(10f, androidx.compose.ui.unit.TextUnitType.Sp)),
                color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun WardrobePreview() {
    ClozyTheme { WardrobeScreen(MockGarments, "", {}, null, {}, {}, {}) }
}
