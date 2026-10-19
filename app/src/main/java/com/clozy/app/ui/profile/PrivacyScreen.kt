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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyBackRow
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

enum class ProfileVisibility(val title: String, val description: String) {
    Public("Público", "Tu perfil puede ser visible para otros usuarios de Clozy."),
    Private("Privado", "Solo tú puedes ver tu perfil y la información asociada."),
}


@Composable
fun PrivacyScreen(
    selected: ProfileVisibility,
    onSelect: (ProfileVisibility) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Lavender, ClozyColors.Primary))
        Column(Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
            ClozyBackRow(onBackClick)
            Text("Visibilidad de mi perfil", style = ClozyType.Title, color = ClozyColors.TextPrimary, modifier = Modifier.padding(top = 16.dp))
            Text(
                "Controla quién puede ver tu perfil y la información que compartes en Clozy.",
                style = ClozyType.Caption, color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 4.dp),
            )
            Column(Modifier.padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileVisibility.entries.forEach { option ->
                    VisibilityOptionCard(option, option == selected) { onSelect(option) }
                }
            }
            Text(
                "Algunos datos, como tu correo electrónico, nunca serán visibles públicamente.",
                style = ClozyType.Caption, color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}

@Composable
private fun VisibilityOptionCard(option: ProfileVisibility, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val bg = if (selected) ClozyColors.Blush else ClozyColors.White
    val border = if (selected) ClozyColors.Primary else ClozyColors.TextFaint
    Row(
        Modifier
            .fillMaxWidth()
            .background(bg, shape)
            .border(1.dp, border, shape)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            Modifier
                .padding(top = 2.dp)
                .size(20.dp)
                .background(if (selected) ClozyColors.Primary else androidx.compose.ui.graphics.Color.Transparent, CircleShape)
                .border(2.dp, if (selected) ClozyColors.Primary else ClozyColors.TextFaint, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) Box(Modifier.size(8.dp).background(ClozyColors.White, CircleShape))
        }
        Column {
            Text(option.title, style = ClozyType.BodyMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold), color = if (selected) ClozyColors.Primary else ClozyColors.TextPrimary)
            Text(option.description, style = ClozyType.Caption, color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 2.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun PrivacyPreview() { ClozyTheme { PrivacyScreen(ProfileVisibility.Public, {}, {}) } }
