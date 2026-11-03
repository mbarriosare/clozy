package com.clozy.app.ui.market

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.GarmentThumbnail
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType
import com.clozy.app.ui.wardrobe.iconRes

private fun statusLabel(status: ListingStatus) = when (status) {
    ListingStatus.Active -> "Activa"
    ListingStatus.Sold -> "Vendida"
    ListingStatus.Paused -> "Pausada"
}

private fun statusColor(status: ListingStatus) = when (status) {
    ListingStatus.Active -> Color(0xFF2E9E5B)
    ListingStatus.Sold -> ClozyColors.TextMuted
    ListingStatus.Paused -> ClozyColors.Peach
}

@Composable
private fun MarketHeader(title: String, onBackClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(36.dp).background(ClozyColors.Blush, CircleShape).clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(18.dp)) }
        Text(title, style = ClozyType.Title.copy(fontSize = androidx.compose.ui.unit.TextUnit(20f, androidx.compose.ui.unit.TextUnitType.Sp)), color = ClozyColors.TextPrimary)
    }
}

@Composable
fun MyListingsScreen(
    listings: List<Listing>,
    onBackClick: () -> Unit,
    onPauseClick: (Listing) -> Unit,
    onActivateClick: (Listing) -> Unit,
    onDeleteClick: (Listing) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        MarketHeader("Mis publicaciones", onBackClick)
        if (listings.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "Todavía no has publicado ninguna prenda.",
                    style = ClozyType.BodyMedium, color = ClozyColors.TextMuted,
                    textAlign = TextAlign.Center, modifier = Modifier.padding(32.dp),
                )
            }
        } else {
            LazyColumn(
                Modifier.weight(1f).fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(listings, key = { it.id }) { listing ->
                    ListingRow(listing, onPauseClick, onActivateClick, onDeleteClick)
                }
                item { androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp)) }
            }
        }
    }
}

@Composable
private fun ListingRow(
    listing: Listing,
    onPauseClick: (Listing) -> Unit,
    onActivateClick: (Listing) -> Unit,
    onDeleteClick: (Listing) -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        Modifier.fillMaxWidth().background(ClozyColors.Surface, shape).border(1.dp, ClozyColors.TextFaint, shape).padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier.size(56.dp).background(ClozyColors.Blush, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                GarmentThumbnail(listing.photoUrl, listing.thumbnailRes, listing.category.iconRes(), listing.primaryColor.swatch, iconSize = 28.dp)
            }
            Column(Modifier.weight(1f)) {
                Text(listing.name, style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.Bold), color = ClozyColors.TextPrimary)
                Text("${listing.price.asPrice()}", style = ClozyType.CaptionBold, color = ClozyColors.Primary, modifier = Modifier.padding(top = 2.dp))
            }
            Text(
                statusLabel(listing.status),
                style = ClozyType.CaptionBold.copy(fontSize = androidx.compose.ui.unit.TextUnit(10f, androidx.compose.ui.unit.TextUnitType.Sp)),
                color = Color.White,
                modifier = Modifier.background(statusColor(listing.status), CircleShape).padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
        if (listing.status == ListingStatus.Sold) {
            Text(
                "Transferida al armario del comprador.",
                style = ClozyType.Caption, color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 10.dp),
            )
        } else {
            Row(Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (listing.status == ListingStatus.Active) {
                    ActionLabel("Pausar") { onPauseClick(listing) }
                } else {
                    ActionLabel("Activar") { onActivateClick(listing) }
                }
                ActionLabel("Eliminar", danger = true) { onDeleteClick(listing) }
            }
        }
    }
}

@Composable
private fun ActionLabel(text: String, danger: Boolean = false, onClick: () -> Unit) {
    Text(
        text, style = ClozyType.CaptionBold.copy(fontSize = androidx.compose.ui.unit.TextUnit(11f, androidx.compose.ui.unit.TextUnitType.Sp)),
        color = if (danger) ClozyColors.Error else ClozyColors.TextPrimary,
        modifier = Modifier
            .background(if (danger) ClozyColors.ErrorSoft else ClozyColors.Blush, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}

@Composable
fun MyPurchasesScreen(purchases: List<Listing>, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        MarketHeader("Mis compras", onBackClick)
        if (purchases.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "Todavía no has comprado ninguna prenda.",
                    style = ClozyType.BodyMedium, color = ClozyColors.TextMuted,
                    textAlign = TextAlign.Center, modifier = Modifier.padding(32.dp),
                )
            }
        } else {
            LazyColumn(
                Modifier.weight(1f).fillMaxWidth().padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(purchases, key = { it.id }) { listing ->
                    val shape = RoundedCornerShape(16.dp)
                    Row(
                        Modifier.fillMaxWidth().background(ClozyColors.Surface, shape).border(1.dp, ClozyColors.TextFaint, shape).padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            Modifier.size(56.dp).background(ClozyColors.Blush, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            GarmentThumbnail(listing.photoUrl, listing.thumbnailRes, listing.category.iconRes(), listing.primaryColor.swatch, iconSize = 28.dp)
                        }
                        Column(Modifier.weight(1f)) {
                            Text(listing.name, style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.Bold), color = ClozyColors.TextPrimary)
                            Text(
                                "Vendido por @${listing.sellerUsername.removePrefix("@")} · ${listing.price.asPrice()}",
                                style = ClozyType.Caption, color = ClozyColors.TextMuted,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                    }
                }
                item { androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp)) }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun MyListingsPreview() { ClozyTheme { MyListingsScreen(emptyList(), {}, {}, {}, {}) } }
