package com.clozy.app.ui.wardrobe

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyBackRow
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.components.ClozyTextField
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

@Composable
private fun WardrobeHeader(title: String, onBackClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(36.dp)
                .background(ClozyColors.Blush, CircleShape)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(18.dp)) }
        Text(title, style = ClozyType.Title, color = ClozyColors.TextPrimary)
    }
}

@Composable
private fun MethodOption(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .background(ClozyColors.White, shape)
            .border(1.dp, ClozyColors.TextFaint, shape)
            .clickable(onClick = onClick)
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(48.dp).background(ClozyColors.Blush, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) { Icon(icon, null, tint = ClozyColors.Primary, modifier = Modifier.size(24.dp)) }
        Column {
            Text(title, style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.Bold), color = ClozyColors.TextPrimary)
            Text(subtitle, style = ClozyType.Caption, color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 2.dp))
        }
    }
}


@Composable
fun AddGarmentChoiceScreen(
    onBackClick: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onChooseGalleryClick: () -> Unit,
    onManualClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        WardrobeHeader("Agregar prenda", onBackClick)
        Column(
            Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("¿Cómo quieres agregarla?", style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.Bold), color = ClozyColors.TextPrimary)
            MethodOption(Icons.Outlined.CameraAlt, "Tomar una foto", "Usa tu cámara para fotografiar tu prenda.", onTakePhotoClick)
            MethodOption(Icons.Outlined.Image, "Elegir de mi galería", "Selecciona una foto que ya tengas en tu dispositivo.", onChooseGalleryClick)
            Box(Modifier.fillMaxWidth().height(1.dp).background(ClozyColors.TextFaint).padding(vertical = 8.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(ClozyColors.Blush, RoundedCornerShape(16.dp))
                    .border(1.dp, ClozyColors.TextFaint, RoundedCornerShape(16.dp))
                    .clickable(onClick = onManualClick)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Agregar manualmente", style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.Bold), color = ClozyColors.TextPrimary)
                    Text(
                        "Registra la información de la prenda sin utilizar una fotografía.",
                        style = ClozyType.Caption, color = ClozyColors.TextMuted,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                Icon(Icons.Filled.ChevronRight, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.height(80.dp))
            Text(
                "Cancelar", style = ClozyType.BodyMedium, color = ClozyColors.TextMuted,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ClozyColors.Blush, RoundedCornerShape(16.dp))
                    .clickable(onClick = onCancelClick)
                    .padding(vertical = 14.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun <T> ChipGroup(
    options: List<T>,
    labelOf: (T) -> String,
    isSelected: (T) -> Boolean,
    onToggle: (T) -> Unit,
) {
    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { opt ->
            val selected = isSelected(opt)
            Text(
                labelOf(opt),
                style = ClozyType.CaptionBold.copy(fontSize = androidx.compose.ui.unit.TextUnit(12f, androidx.compose.ui.unit.TextUnitType.Sp)),
                color = if (selected) ClozyColors.White else ClozyColors.TextMuted,
                modifier = Modifier
                    .background(if (selected) ClozyColors.Primary else ClozyColors.Blush, CircleShape)
                    .border(1.dp, if (selected) ClozyColors.Primary else ClozyColors.TextFaint, CircleShape)
                    .clickable { onToggle(opt) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            )
        }
    }
}

private fun sectionLabelStyle() = ClozyType.CaptionBold.copy(
    fontSize = androidx.compose.ui.unit.TextUnit(12f, androidx.compose.ui.unit.TextUnitType.Sp),
    letterSpacing = androidx.compose.ui.unit.TextUnit(0.3f, androidx.compose.ui.unit.TextUnitType.Sp),
)





@Composable
fun ManualGarmentFormScreen(
    onBackClick: () -> Unit,
    onSaveClick: (Garment) -> Unit,
    modifier: Modifier = Modifier,
    initial: Garment? = null,
    photoUri: android.net.Uri? = null,
    onImageClick: () -> Unit = {},
) {
    var name by remember { mutableStateOf(initial?.name ?: "") }
    var category by remember { mutableStateOf(initial?.category) }
    var primaryColor by remember { mutableStateOf(initial?.primaryColor) }
    var secondaryColors by remember { mutableStateOf(initial?.secondaryColors?.toSet() ?: setOf()) }
    var pattern by remember { mutableStateOf(initial?.pattern ?: GarmentPattern.Solid) }
    var favorite by remember { mutableStateOf(initial?.isFavorite ?: false) }
    var style by remember { mutableStateOf(initial?.style) }
    var season by remember { mutableStateOf(initial?.season) }
    var expanded by remember { mutableStateOf(initial?.style != null || initial?.season != null) }

    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        WardrobeHeader(if (initial != null) "Editar prenda" else "Agregar prenda", onBackClick)
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            val displayPhoto = photoUri ?: initial?.photoUrl
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(if (displayPhoto != null) 180.dp else 66.dp)
                    .background(ClozyColors.Blush, RoundedCornerShape(16.dp))
                    .border(1.dp, ClozyColors.TextFaint, RoundedCornerShape(16.dp))
                    .clickable(onClick = onImageClick),
                contentAlignment = Alignment.Center,
            ) {
                if (displayPhoto != null) {
                    coil.compose.AsyncImage(
                        model = displayPhoto,
                        contentDescription = null,
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Image, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(28.dp))
                        Text(
                            "Toca para agregar una foto",
                            style = ClozyType.Caption, color = ClozyColors.TextMuted,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("NOMBRE", style = sectionLabelStyle(), color = ClozyColors.TextMuted)
                ClozyTextField(
                    value = name, onValueChange = { name = it },
                    placeholder = "Ej. Vestido floral",
                    leadingIcon = androidx.compose.material.icons.Icons.Outlined.Image,
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("CATEGORÍA", style = sectionLabelStyle(), color = ClozyColors.TextMuted)
                ChipGroup(GarmentCategory.entries, { it.label }, { it == category }, { category = it })
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("COLOR PRINCIPAL", style = sectionLabelStyle(), color = ClozyColors.TextMuted)
                ChipGroup(GarmentColor.entries, { it.label }, { it == primaryColor }, { primaryColor = it })
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("COLORES SECUNDARIOS", style = sectionLabelStyle(), color = ClozyColors.TextMuted)
                    Text("Selección múltiple", style = ClozyType.Footnote.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified), color = ClozyColors.TextMuted)
                }
                ChipGroup(
                    GarmentColor.entries, { it.label }, { it in secondaryColors },
                    { c -> secondaryColors = if (c in secondaryColors) secondaryColors - c else secondaryColors + c },
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("ESTAMPADO", style = sectionLabelStyle(), color = ClozyColors.TextMuted)
                ChipGroup(GarmentPattern.entries, { it.label }, { it == pattern }, { pattern = it })
            }
            if (!expanded) {
                Text(
                    "VER MÁS", style = ClozyType.CaptionBold.copy(letterSpacing = androidx.compose.ui.unit.TextUnit(1f, androidx.compose.ui.unit.TextUnitType.Sp)),
                    color = ClozyColors.Primary,
                    modifier = Modifier.clickable { expanded = true },
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("ESTILO", style = sectionLabelStyle(), color = ClozyColors.TextMuted)
                    ChipGroup(GarmentStyle.entries, { it.label }, { it == style }, { style = it })
                }
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("TEMPORADA", style = sectionLabelStyle(), color = ClozyColors.TextMuted)
                    ChipGroup(GarmentSeason.entries, { it.label }, { it == season }, { season = it })
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(ClozyColors.White, RoundedCornerShape(16.dp))
                    .border(1.dp, ClozyColors.TextFaint, RoundedCornerShape(16.dp))
                    .clickable { favorite = !favorite }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Favorita", style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.Bold), color = ClozyColors.TextPrimary)
                Icon(
                    if (favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    null, tint = if (favorite) ClozyColors.Primary else ClozyColors.TextFaint, modifier = Modifier.size(18.dp),
                )
            }
            Spacer(Modifier.height(4.dp))
        }
        Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
            ClozyPrimaryButton(
                if (initial != null) "Guardar cambios" else "Guardar prenda",
                onClick = {
                    onSaveClick(
                        Garment(
                            id = initial?.id ?: (0..99999).random(),
                            name = name.ifBlank { "Prenda sin nombre" },
                            category = category ?: GarmentCategory.Top,
                            primaryColor = primaryColor ?: GarmentColor.White,
                            secondaryColors = secondaryColors.toList(),
                            pattern = pattern,
                            isFavorite = favorite,
                            style = style,
                            season = season,
                            usageCount = initial?.usageCount ?: 0,
                            thumbnailRes = initial?.thumbnailRes,
                            photoUrl = initial?.photoUrl,
                        ),
                    )
                },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun AddChoicePreview() { ClozyTheme { AddGarmentChoiceScreen({}, {}, {}, {}, {}) } }

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun ManualFormPreview() { ClozyTheme { ManualGarmentFormScreen({}, {}) } }
