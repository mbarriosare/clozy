package com.clozy.app.ui.outfits

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType
import com.clozy.app.ui.wardrobe.GarmentStyle

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipGroup(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            val active = option == selected
            Text(
                option, style = ClozyType.CaptionBold,
                color = if (active) ClozyColors.White else ClozyColors.TextMuted,
                modifier = Modifier
                    .background(if (active) ClozyColors.Primary else ClozyColors.Blush, CircleShape)
                    .border(1.dp, if (active) ClozyColors.Primary else ClozyColors.TextFaint, CircleShape)
                    .clickable { onSelect(option) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            )
        }
    }
}

/** Figma: CrearOutfit (76:11774). El clima y "evitar" son solo preferencias visuales por
 *  ahora: Clozy AI elige prendas reales de tu armario según Ocasión y Estilo. */
@Composable
fun CreateOutfitScreen(
    onBackClick: () -> Unit,
    onCreateClick: (occasion: String, style: String) -> Unit,
    modifier: Modifier = Modifier,
    isGenerating: Boolean = false,
    errorMessage: String? = null,
) {
    var occasion by remember { mutableStateOf(OccasionOptions.first()) }
    var style by remember { mutableStateOf(GarmentStyle.Casual.label) }
    var weather by remember { mutableStateOf("Templado") }

    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        Row(
            Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                Modifier.size(36.dp).background(ClozyColors.Blush, CircleShape).clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(18.dp)) }
            Column {
                Text("Crear un outfit", style = ClozyType.Title.copy(fontSize = 20.sp), color = ClozyColors.TextPrimary)
                Text(
                    "Cuéntame qué necesitas y elegiré prendas de tu armario.",
                    style = ClozyType.Caption, color = ClozyColors.TextMuted,
                )
            }
        }
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("OCASIÓN", style = ClozyType.CaptionBold, color = ClozyColors.TextMuted)
                ChipGroup(OccasionOptions, occasion) { occasion = it }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("ESTILO", style = ClozyType.CaptionBold, color = ClozyColors.TextMuted)
                ChipGroup(GarmentStyle.entries.map { it.label }, style) { style = it }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("CLIMA", style = ClozyType.CaptionBold, color = ClozyColors.TextMuted)
                ChipGroup(WeatherOptions, weather) { weather = it }
            }
            if (errorMessage != null) {
                Text(errorMessage, style = ClozyType.Caption, color = ClozyColors.Error)
            }
            Spacer(Modifier.height(4.dp))
            ClozyPrimaryButton(
                if (isGenerating) "Creando outfit..." else "Crear mi outfit ✦",
                onClick = { onCreateClick(occasion, style) },
            )
            Text(
                "Cancelar", style = ClozyType.BodyMedium, color = ClozyColors.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ClozyColors.Blush, RoundedCornerShape(16.dp))
                    .clickable(onClick = onBackClick)
                    .padding(vertical = 14.dp),
            )
            androidx.compose.foundation.layout.Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun CreateOutfitPreview() { ClozyTheme { CreateOutfitScreen({}, { _, _ -> }) } }
