package com.clozy.app.ui.status

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.R
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.components.ClozyIcons
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType


@Composable
fun IconBadge(icon: ImageVector, tint: Color, background: Color) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(background, shape)
            .border(BorderStroke(1.dp, ClozyColors.TextFaint), shape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(28.dp))
    }
}


@Composable
fun FlowErrorScreen(
    badge: @Composable () -> Unit,
    title: String,
    message: String,
    primaryText: String,
    onPrimaryClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryGradient: List<Color> = listOf(ClozyColors.Primary, ClozyColors.PrimaryDark),
    primaryShadow: Color = ClozyColors.Primary.copy(alpha = 0.27f),
    secondaryText: String = "Volver",
    secondaryStyle: androidx.compose.ui.text.TextStyle = ClozyType.Button,
) {
    Column(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(start = 28.dp, end = 28.dp, top = 40.dp, bottom = 32.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.clozy_logo),
                    contentDescription = null,
                    modifier = Modifier.size(width = 32.dp, height = 38.578.dp),
                )
                Text("CLOZY", style = ClozyType.Overline, color = ClozyColors.TextMuted)
            }

            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            ) {
                badge()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 8.dp),
                ) {
                    Text(
                        title,
                        style = ClozyType.Title.copy(letterSpacing = (-0.5).sp),
                        color = ClozyColors.TextPrimary,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        message,
                        style = ClozyType.BodyMedium.copy(lineHeight = 22.75.sp),
                        color = ClozyColors.TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.widthIn(max = 288.dp),
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ClozyPrimaryButton(
                    primaryText, onPrimaryClick,
                    gradient = primaryGradient, shadowColor = primaryShadow,
                )
                Text(
                    secondaryText,
                    style = secondaryStyle,
                    color = ClozyColors.TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onBackClick)
                        .padding(vertical = 16.dp),
                )
            }
        }
    }
}


@Composable
fun ServerErrorScreen(onRetryClick: () -> Unit, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    FlowErrorScreen(
        badge = { IconBadge(ClozyIcons.Error, ClozyColors.Primary, ClozyColors.PrimarySoft) },
        title = "Algo salió mal",
        message = "No pudimos completar la solicitud. Inténtalo nuevamente.",
        primaryText = "Intentar de nuevo",
        onPrimaryClick = onRetryClick,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}


@Composable
fun NoConnectionScreen(onRetryClick: () -> Unit, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    FlowErrorScreen(
        badge = { IconBadge(ClozyIcons.WifiOff, ClozyColors.Peach, ClozyColors.PeachSoft) },
        title = "Sin conexión",
        message = "No pudimos conectarnos. Revisa tu conexión a internet e inténtalo nuevamente.",
        primaryText = "Reintentar",
        onPrimaryClick = onRetryClick,
        onBackClick = onBackClick,
        primaryGradient = listOf(ClozyColors.Peach, ClozyColors.PeachDark),
        primaryShadow = ClozyColors.Peach.copy(alpha = 0.27f),
        modifier = modifier,
    )
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun ServerErrorPreview() { ClozyTheme { ServerErrorScreen({}, {}) } }

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun NoConnectionPreview() { ClozyTheme { NoConnectionScreen({}, {}) } }
