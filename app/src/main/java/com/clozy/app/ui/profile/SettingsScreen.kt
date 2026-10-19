package com.clozy.app.ui.profile

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyBackRow
import com.clozy.app.ui.components.ClozyConfirmDialog
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

@Composable
private fun SectionLabel(text: String) {
    Text(text.uppercase(), style = ClozyType.LabelBold9, color = ClozyColors.TextMuted)
}

@Composable
private fun StaticInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(14.dp))
            Text(label, style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary)
        }
        Text(value, style = ClozyType.Caption, color = ClozyColors.TextMuted)
    }
}

@Composable
private fun NavRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(14.dp))
            Text(label, style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary)
        }
        Icon(Icons.Filled.ChevronRight, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(14.dp))
    }
}

@Composable
private fun Divider() {
    Box(Modifier.fillMaxWidth().height(1.dp).background(ClozyColors.TextFaint))
}

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDeleting: Boolean = false,
    deleteError: String? = null,
    showDeleteDialog: Boolean = false,
    onConfirmDelete: () -> Unit = {},
    onDismissDeleteDialog: () -> Unit = {},
) {
    Box(modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().background(ClozyColors.Background)) {
            ClozyGradientBar(listOf(ClozyColors.Lavender, ClozyColors.Primary))
            Column(Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
                ClozyBackRow(onBackClick)
                Text("Ajustes", style = ClozyType.Title, color = ClozyColors.TextPrimary, modifier = Modifier.padding(top = 16.dp))

                Column(Modifier.padding(top = 20.dp)) {
                    SectionLabel("Preferencias")
                    Column(Modifier.padding(top = 4.dp)) {
                        StaticInfoRow(Icons.Outlined.Language, "Idioma", "Español")
                        Divider()
                        StaticInfoRow(Icons.Outlined.Palette, "Apariencia", "Claro")
                        Divider()
                        StaticInfoRow(Icons.Outlined.Straighten, "Unidades", "Métrico")
                    }
                }

                Column(Modifier.padding(top = 28.dp)) {
                    SectionLabel("Sobre Clozy")
                    Column(Modifier.padding(top = 4.dp)) {
                        NavRow(Icons.Outlined.Help, "Ayuda y soporte", onHelpClick)
                        Divider()
                        NavRow(Icons.Outlined.Info, "Acerca de Clozy", onAboutClick)
                    }
                }

                Column(Modifier.padding(top = 28.dp)) {
                    SectionLabel("Zona de peligro")
                    if (deleteError != null) {
                        Text(
                            deleteError, style = ClozyType.Caption, color = ClozyColors.Error,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(ClozyColors.ErrorSoft, RoundedCornerShape(16.dp))
                            .clickable(onClick = onDeleteAccountClick)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Outlined.DeleteOutline, null, tint = ClozyColors.Error, modifier = Modifier.size(16.dp))
                        Text("Eliminar cuenta", style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.Bold), color = ClozyColors.Error)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
        if (showDeleteDialog) {
            ClozyConfirmDialog(
                title = "¿Eliminar tu cuenta?",
                message = "Esta acción borrará tu cuenta, tu armario y tu perfil de forma permanente. No se puede deshacer.",
                confirmText = "Eliminar cuenta",
                isLoading = isDeleting,
                onConfirm = onConfirmDelete,
                onCancel = onDismissDeleteDialog,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun SettingsPreview() { ClozyTheme { SettingsScreen({}, {}, {}, {}) } }
