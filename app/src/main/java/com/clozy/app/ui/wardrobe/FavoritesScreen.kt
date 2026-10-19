package com.clozy.app.ui.wardrobe

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyType

@Composable
fun FavoritesScreen(
    garments: List<Garment>,
    onBackClick: () -> Unit,
    onGarmentClick: (Garment) -> Unit,
    modifier: Modifier = Modifier,
) {
    val favorites = garments.filter { it.isFavorite }

    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        Row(
            Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier.size(36.dp).background(ClozyColors.Blush, CircleShape).clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(18.dp))
            }
            Column {
                Text("Mis favoritos", style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary)
                Text(
                    if (favorites.size == 1) "1 prenda" else "${favorites.size} prendas",
                    style = ClozyType.Footnote,
                    color = ClozyColors.TextMuted,
                )
            }
        }

        if (favorites.isEmpty()) {
            Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    "Aún no tienes favoritos. Marca prendas con el corazón desde tu armario.",
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
                contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(favorites, key = { it.id }) { garment ->
                    GarmentCard(garment, onClick = { onGarmentClick(garment) })
                }
            }
        }
    }
}
