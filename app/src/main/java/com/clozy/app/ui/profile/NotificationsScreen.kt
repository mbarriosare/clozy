package com.clozy.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyBackRow
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

data class NotificationSettings(
    val outfitReminders: Boolean = true,
    val styleRecommendations: Boolean = true,
    val newItems: Boolean = false,
    val marketplaceActivity: Boolean = false,
)


@Composable
fun NotificationsScreen(
    settings: NotificationSettings,
    onSettingsChange: (NotificationSettings) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        Column(Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
            ClozyBackRow(onBackClick)
            Text("Notificaciones", style = ClozyType.Title, color = ClozyColors.TextPrimary, modifier = Modifier.padding(top = 16.dp))
            Column(Modifier.padding(top = 12.dp)) {
                ToggleRow("Recordatorios de outfits", settings.outfitReminders) {
                    onSettingsChange(settings.copy(outfitReminders = it))
                }
                ToggleRow("Recomendaciones de estilo", settings.styleRecommendations) {
                    onSettingsChange(settings.copy(styleRecommendations = it))
                }
                ToggleRow("Nuevas prendas", settings.newItems) {
                    onSettingsChange(settings.copy(newItems = it))
                }
                ToggleRow("Actividad del marketplace", settings.marketplaceActivity, showDivider = false) {
                    onSettingsChange(settings.copy(marketplaceActivity = it))
                }
            }
        }
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, showDivider: Boolean = true, onCheckedChange: (Boolean) -> Unit) {
    Column {
        Row(
            Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(label, style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary)
            ClozySwitch(checked, onCheckedChange)
        }
        if (showDivider) Box(Modifier.fillMaxWidth().height(1.dp).background(ClozyColors.TextFaint))
    }
}

@Composable
private fun ClozySwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Box(
        Modifier
            .size(width = 44.dp, height = 24.dp)
            .background(if (checked) ClozyColors.Primary else ClozyColors.TextFaint, CircleShape)
            .clickable { onCheckedChange(!checked) }
            .padding(2.dp),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Box(Modifier.size(20.dp).background(Color.White, CircleShape))
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun NotificationsPreview() { ClozyTheme { NotificationsScreen(NotificationSettings(), {}, {}) } }
