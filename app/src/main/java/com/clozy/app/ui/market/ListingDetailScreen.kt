package com.clozy.app.ui.market

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.components.GarmentThumbnail
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType
import com.clozy.app.ui.wardrobe.iconRes

@Composable
fun ListingDetailScreen(
    listing: Listing,
    onBackClick: () -> Unit,
    onBuyClick: () -> Unit,
    modifier: Modifier = Modifier,
    isBuying: Boolean = false,
    isOwnListing: Boolean = false,
) {
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
            Text("Detalle de publicación", style = ClozyType.CaptionBold, color = ClozyColors.TextMuted)
        }
        Column(Modifier.weight(1f).padding(horizontal = 24.dp)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(ClozyColors.Blush, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center,
            ) {
                GarmentThumbnail(
                    photoUrl = listing.photoUrl,
                    thumbnailRes = listing.thumbnailRes,
                    iconRes = listing.category.iconRes(),
                    tint = listing.primaryColor.swatch,
                    iconSize = 96.dp,
                    modifier = Modifier,
                )
            }

            Row(
                Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(Modifier.weight(1f)) {
                    Text(listing.name, style = ClozyType.Title.copy(fontSize = androidx.compose.ui.unit.TextUnit(20f, androidx.compose.ui.unit.TextUnitType.Sp)), color = ClozyColors.TextPrimary)
                    Text(
                        "Vendido por @${listing.sellerUsername.removePrefix("@")}",
                        style = ClozyType.Caption, color = ClozyColors.TextMuted,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                Text("${listing.price.asPrice()}", style = ClozyType.Headline18, color = ClozyColors.Primary)
            }

            Row(Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoPill(listing.category.label)
                InfoPill(listing.condition.label)
                InfoPill(listing.primaryColor.label)
            }

            Spacer(Modifier.weight(1f))

            if (isOwnListing) {
                Text(
                    "Esta es tu publicación, no puedes comprarla.",
                    style = ClozyType.Caption, color = ClozyColors.TextMuted,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
            } else {
                Text(
                    "Al confirmar, la prenda pasará automáticamente a tu armario digital.",
                    style = ClozyType.Caption, color = ClozyColors.TextMuted,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                ClozyPrimaryButton(
                    if (isBuying) "Procesando..." else "Comprar por ${listing.price.asPrice()}",
                    onClick = onBuyClick,
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoPill(text: String) {
    Text(
        text, style = ClozyType.CaptionBold.copy(fontSize = androidx.compose.ui.unit.TextUnit(11f, androidx.compose.ui.unit.TextUnitType.Sp)),
        color = ClozyColors.TextMuted,
        modifier = Modifier
            .background(ClozyColors.Blush, CircleShape)
            .border(1.dp, ClozyColors.TextFaint, CircleShape)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun ListingDetailPreview() {
    ClozyTheme { ListingDetailScreen(Listing(name = "Camiseta básica", price = 25000, sellerUsername = "sofia_m"), {}, {}) }
}
