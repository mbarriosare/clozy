package com.clozy.app.ui.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.clozy.app.R
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType


@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ClozyColors.Background),
    ) {

        GlowBlob(size = 224.dp, color = ClozyColors.Lavender.copy(alpha = 0.333f), x = 176.dp, y = (-64).dp)
        GlowBlob(size = 192.dp, color = ClozyColors.Primary.copy(alpha = 0.2f), x = (-104).dp, y = 128.dp)
        GlowBlob(size = 160.dp, color = ClozyColors.Peach.copy(alpha = 0.267f), x = 176.dp, y = 512.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp, top = 56.dp, bottom = 40.dp),
        ) {
            BrandHeader()

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
            ) {
                Hero()
                StyleChips(modifier = Modifier.padding(top = 16.dp))
            }

            Actions(onLoginClick = onLoginClick, onSignUpClick = onSignUpClick)
        }
    }
}

@Composable
internal fun GlowBlob(size: Dp, color: Color, x: Dp, y: Dp) {
    Box(
        modifier = Modifier
            .offset(x = x, y = y)
            .size(size)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(color, color.copy(alpha = 0f)),
                    radius = with(LocalDensity.current) { (size / 2).toPx() },
                ),
                shape = CircleShape,
            ),
    )
}

@Composable
private fun BrandHeader() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = painterResource(R.drawable.clozy_logo),
            contentDescription = null,
            modifier = Modifier.size(width = 40.dp, height = 48.229.dp),
        )
        Text(
            text = "APP DE MODA",
            style = ClozyType.Brand,
            color = ClozyColors.TextMuted,
        )
    }
}

@Composable
private fun Hero() {
    Column {
        Text(
            text = "TU ARMARIO DIGITAL ✦",
            style = ClozyType.Eyebrow,
            color = ClozyColors.Primary,
        )
        Text(
            text = "Clozy",
            style = ClozyType.Display,
            color = ClozyColors.TextPrimary,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = "Tu estilo, en perfecta armonía.",
            style = ClozyType.Subtitle,
            color = ClozyColors.TextMuted,
            modifier = Modifier
                .padding(top = 12.dp)
                .widthIn(max = 180.dp),
        )
    }
}

private data class ChipSpec(val label: String, val background: Color, val content: Color)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StyleChips(modifier: Modifier = Modifier) {
    val chips = listOf(
        ChipSpec("Casual", ClozyColors.Primary, ClozyColors.White),
        ChipSpec("Editorial", ClozyColors.Blush, ClozyColors.TextMuted),
        ChipSpec("Streetwear", ClozyColors.Lavender, ClozyColors.White),
        ChipSpec("Elegante", ClozyColors.Blush, ClozyColors.TextMuted),
    )
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        chips.forEach { chip ->
            Text(
                text = chip.label,
                style = ClozyType.Chip,
                color = chip.content,
                modifier = Modifier
                    .background(chip.background, CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun Actions(onLoginClick: () -> Unit, onSignUpClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = shape,
                    ambientColor = ClozyColors.Primary.copy(alpha = 0.35f),
                    spotColor = ClozyColors.Primary.copy(alpha = 0.35f),
                )
                .background(
                    Brush.verticalGradient(listOf(ClozyColors.Primary, ClozyColors.PrimaryDark)),
                    shape,
                )
                .clickable(onClick = onLoginClick)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("Iniciar sesión", style = ClozyType.Button, color = ClozyColors.White)
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.333.dp, ClozyColors.Primary), shape)
                .clickable(onClick = onSignUpClick)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("Crear cuenta", style = ClozyType.Button, color = ClozyColors.Primary)
        }

        Text(
            text = "ORGANIZA · COMBINA · VISTE",
            style = ClozyType.Footnote,
            color = ClozyColors.TextFaint,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun WelcomeScreenPreview() {
    ClozyTheme { WelcomeScreen(onLoginClick = {}, onSignUpClick = {}) }
}
