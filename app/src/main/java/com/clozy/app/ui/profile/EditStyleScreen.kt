package com.clozy.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyBackRow
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

data class StylePreferences(
    val styles: Set<String>,
    val colors: Set<String>,
    val topSize: String,
    val bottomSize: String,
    val shoeSize: String,
)

@Composable
private fun SectionLabel(text: String) {
    Text(text.uppercase(), style = ClozyType.LabelBold9, color = ClozyColors.TextMuted, modifier = Modifier.padding(bottom = 12.dp))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StyleChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        label,
        style = ClozyType.Button,
        color = if (selected) ClozyColors.White else ClozyColors.TextMuted,
        modifier = Modifier
            .background(if (selected) ClozyColors.Primary else ClozyColors.White, CircleShape)
            .border(1.dp, if (selected) ClozyColors.Primary else ClozyColors.TextFaint, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun ColorChip(option: ColorOption, selected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            Modifier
                .size(36.dp)
                .background(option.color, CircleShape)
                .border(if (selected) 3.dp else 1.dp, if (selected) ClozyColors.Primary else ClozyColors.TextFaint, CircleShape)
                .clickable(onClick = onClick),
        )
        Text(option.label, style = ClozyType.CaptionRegular.copy(fontSize = androidx.compose.ui.unit.TextUnit(9f, androidx.compose.ui.unit.TextUnitType.Sp)), color = ClozyColors.TextMuted)
    }
}

@Composable
private fun SizeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        label,
        style = ClozyType.CaptionBold,
        color = if (selected) ClozyColors.White else ClozyColors.TextMuted,
        modifier = Modifier
            .background(if (selected) ClozyColors.Primary else ClozyColors.White, RoundedCornerShape(12.dp))
            .border(1.dp, if (selected) ClozyColors.Primary else ClozyColors.TextFaint, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}

@Composable
private fun SizeRow(label: String, options: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            options.forEach { SizeChip(it, it == selected, onClick = { onSelect(it) }) }
        }
    }
}

/** Figma: MiEstilo (70:5652). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditStyleScreen(
    initial: StylePreferences,
    onBackClick: () -> Unit,
    onSaveClick: (StylePreferences) -> Unit,
    modifier: Modifier = Modifier,
) {
    var styles by remember { mutableStateOf(initial.styles) }
    var colors by remember { mutableStateOf(initial.colors) }
    var topSize by remember { mutableStateOf(initial.topSize) }
    var bottomSize by remember { mutableStateOf(initial.bottomSize) }
    var shoeSize by remember { mutableStateOf(initial.shoeSize) }

    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender, ClozyColors.Peach))
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 32.dp),
        ) {
            ClozyBackRow(onBackClick)
            Text("Mi estilo", style = ClozyType.Title, color = ClozyColors.TextPrimary, modifier = Modifier.padding(top = 16.dp))

            Column(Modifier.padding(top = 24.dp)) {
                SectionLabel("Estilo personal")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PersonalStyleOptions.forEach { option ->
                        StyleChip(option, option in styles) {
                            styles = if (option in styles) styles - option else styles + option
                        }
                    }
                }
            }

            Column(Modifier.padding(top = 28.dp)) {
                SectionLabel("Colores favoritos")
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FavoriteColorOptions.forEach { option ->
                        ColorChip(option, option.label in colors) {
                            colors = if (option.label in colors) colors - option.label else colors + option.label
                        }
                    }
                }
            }

            Column(Modifier.padding(top = 28.dp)) {
                SectionLabel("Tallas")
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SizeRow("Superior", TopBottomSizes, topSize) { topSize = it }
                    SizeRow("Inferior", TopBottomSizes, bottomSize) { bottomSize = it }
                    SizeRow("Calzado", ShoeSizes, shoeSize) { shoeSize = it }
                }
            }

            Spacer(Modifier.height(32.dp))
            ClozyPrimaryButton(
                "Guardar preferencias",
                onClick = { onSaveClick(StylePreferences(styles, colors, topSize, bottomSize, shoeSize)) },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun EditStylePreview() {
    ClozyTheme {
        EditStyleScreen(
            initial = StylePreferences(setOf("Casual", "Minimal"), setOf("Fucsia", "Lila"), "S", "S", "36"),
            onBackClick = {}, onSaveClick = {},
        )
    }
}
