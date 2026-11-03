package com.clozy.app.ui.market

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.GarmentThumbnail
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType
import com.clozy.app.ui.wardrobe.GarmentCategory
import com.clozy.app.ui.wardrobe.iconRes

@Composable
fun MarketScreen(
    listings: List<Listing>,
    selectedCategory: GarmentCategory?,
    onCategoryClick: (GarmentCategory?) -> Unit,
    onPublishClick: () -> Unit,
    onListingClick: (Listing) -> Unit,
    onMyListingsClick: () -> Unit,
    onMyPurchasesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val filtered = listings.filter { selectedCategory == null || it.category == selectedCategory }

    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Mercado", style = ClozyType.Headline, color = ClozyColors.TextPrimary)
                Row(
                    Modifier
                        .background(ClozyColors.Primary, CircleShape)
                        .clickable(onClick = onPublishClick)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        "+ Publicar", style = ClozyType.BodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                        color = ClozyColors.White,
                    )
                }
            }
            Row(
                Modifier.padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    "Mis publicaciones", style = ClozyType.CaptionBold, color = ClozyColors.Primary,
                    modifier = Modifier.clickable(onClick = onMyListingsClick),
                )
                Text(
                    "Mis compras", style = ClozyType.CaptionBold, color = ClozyColors.Primary,
                    modifier = Modifier.clickable(onClick = onMyPurchasesClick),
                )
            }
        }

        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            com.clozy.app.ui.wardrobe.CategoryChip("Todo", selectedCategory == null) { onCategoryClick(null) }
            GarmentCategory.entries.forEach { cat ->
                com.clozy.app.ui.wardrobe.CategoryChip(cat.label, selectedCategory == cat) { onCategoryClick(cat) }
            }
        }

        if (filtered.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Todavía no hay publicaciones con estos filtros.",
                    style = ClozyType.BodyMedium, color = ClozyColors.TextMuted,
                    textAlign = TextAlign.Center, modifier = Modifier.padding(32.dp),
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 4.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(filtered, key = { it.id }) { listing ->
                    ListingCard(listing, onClick = { onListingClick(listing) })
                }
            }
        }
    }
}

@Composable
private fun ListingCard(listing: Listing, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        Modifier
            .fillMaxWidth()
            .background(ClozyColors.Surface, shape)
            .border(1.dp, ClozyColors.TextFaint, shape)
            .clickable(onClick = onClick),
    ) {
        Box(
            Modifier.fillMaxWidth().aspectRatio(1.1f).background(ClozyColors.Blush),
            contentAlignment = Alignment.Center,
        ) {
            GarmentThumbnail(
                photoUrl = listing.photoUrl,
                thumbnailRes = listing.thumbnailRes,
                iconRes = listing.category.iconRes(),
                tint = listing.primaryColor.swatch,
                modifier = Modifier,
            )
            if (listing.condition == ListingCondition.New) {
                Text(
                    "NUEVO",
                    style = ClozyType.CaptionBold.copy(fontSize = androidx.compose.ui.unit.TextUnit(9f, androidx.compose.ui.unit.TextUnitType.Sp)),
                    color = ClozyColors.White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(ClozyColors.Primary, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                )
            }
        }
        Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(listing.name, style = ClozyType.CaptionBold.copy(fontSize = androidx.compose.ui.unit.TextUnit(12f, androidx.compose.ui.unit.TextUnitType.Sp)), color = ClozyColors.TextPrimary)
            Text(
                "@${listing.sellerUsername.removePrefix("@")} · ${listing.condition.label}",
                style = ClozyType.Footnote.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified, fontSize = androidx.compose.ui.unit.TextUnit(10f, androidx.compose.ui.unit.TextUnitType.Sp)),
                color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 2.dp),
            )
            Text(
                "${listing.price.asPrice()}",
                style = ClozyType.CaptionBold.copy(fontSize = androidx.compose.ui.unit.TextUnit(14f, androidx.compose.ui.unit.TextUnitType.Sp)),
                color = ClozyColors.Primary,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun MarketPreview() {
    ClozyTheme { MarketScreen(emptyList(), null, {}, {}, {}, {}, {}) }
}
