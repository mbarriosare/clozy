package com.clozy.app.ui.status

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.R
import com.clozy.app.ui.components.ClozyIcons
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType





@Composable
fun FlowLoadingScreen(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    iconContent: (@Composable () -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Brush.horizontalGradient(listOf(ClozyColors.Primary, ClozyColors.Lavender))),
        )
        Row(
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 40.dp),
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
            verticalArrangement = Arrangement.Center,
        ) {
            val shape = RoundedCornerShape(16.dp)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(3.dp, shape, ambientColor = Color(0x14211A1D), spotColor = Color(0x14211A1D))
                    .background(ClozyColors.White, shape)
                    .border(BorderStroke(1.dp, ClozyColors.TextFaint), shape),
                contentAlignment = Alignment.Center,
            ) {
                if (iconContent != null) iconContent()
                else Icon(icon, null, tint = ClozyColors.Primary, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(24.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(44.dp),
                color = ClozyColors.Primary,
                trackColor = ClozyColors.Primary.copy(alpha = 0.13f),
                strokeWidth = 3.dp,
            )
            Spacer(Modifier.height(24.dp))
            Text(title, style = ClozyType.Headline18, color = ClozyColors.TextPrimary, textAlign = TextAlign.Center)
            Text(
                subtitle,
                style = ClozyType.BodyMedium,
                color = ClozyColors.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        Spacer(Modifier.height(80.dp))
    }
}


@Composable
fun SuccessScreen(
    title: String,
    subtitle: String,
    buttonText: String,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ClozyColors.Background)
            .padding(horizontal = 32.dp, vertical = 56.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    Brush.linearGradient(listOf(ClozyColors.Primary, ClozyColors.Lavender)),
                    CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Filled.Check, null, tint = ClozyColors.White, modifier = Modifier.size(32.dp))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = ClozyType.Headline, color = ClozyColors.TextPrimary, textAlign = TextAlign.Center)
            Text(
                subtitle,
                style = ClozyType.BodyRegular,
                color = ClozyColors.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        ClozyPrimaryButton(
            text = buttonText,
            onClick = onContinue,
            modifier = Modifier.padding(top = 16.dp).widthIn(max = 296.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun LoadingPreview() {
    ClozyTheme { FlowLoadingScreen(ClozyIcons.PersonAdd, "Creando tu cuenta", "Por favor espera un momento...") }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun SuccessPreview() {
    ClozyTheme { SuccessScreen("¡Cuenta creada!", "Tu cuenta se creó correctamente.", "Continuar", {}) }
}
