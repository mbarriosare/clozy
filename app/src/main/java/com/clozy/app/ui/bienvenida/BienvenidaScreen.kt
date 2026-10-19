package com.clozy.app.ui.bienvenida

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.R
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType
import com.clozy.app.ui.welcome.GlowBlob






@Composable
fun BienvenidaScreen(
    onStartClick: () -> Unit,
    onHaveAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {
        GlowBlob(size = 256.dp, color = ClozyColors.Lavender.copy(alpha = 0.25f), x = 184.dp, y = (-80).dp)
        GlowBlob(size = 224.dp, color = ClozyColors.Primary.copy(alpha = 0.133f), x = (-96).dp, y = 240.dp)
        GlowBlob(size = 176.dp, color = ClozyColors.Peach.copy(alpha = 0.2f), x = 184.dp, y = 464.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 58.dp, end = 58.dp, top = 96.dp, bottom = 88.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.clozy_logo),
                    contentDescription = null,
                    modifier = Modifier.size(width = 64.dp, height = 77.dp),
                )
                Text(
                    "TU ARMARIO DIGITAL ✦",
                    style = ClozyType.Eyebrow.copy(fontSize = 9.sp, lineHeight = 13.5.sp, letterSpacing = 2.52.sp),
                    color = ClozyColors.Primary,
                )
            }

            IllustrationArea(Modifier.weight(1f).fillMaxWidth().heightIn(min = 280.dp))

            Text(
                text = buildAnnotatedString {
                    append("Tu estilo.\n")
                    withStyle(SpanStyle(color = ClozyColors.Primary)) { append("Tu armario.\n") }
                    append("Tu mundo.")
                },
                style = ClozyType.Display.copy(fontSize = 36.sp, lineHeight = 45.sp, letterSpacing = (-1.44).sp),
                color = ClozyColors.TextPrimary,
            )
            Text(
                "Organiza tus prendas, descubre nuevos outfits y deja que Clozy encuentre tu próximo look.",
                style = ClozyType.BodyMedium.copy(lineHeight = 22.75.sp),
                color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 12.dp).widthIn(max = 296.dp),
            )
            Column(
                modifier = Modifier.padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ClozyPrimaryButton("Empezar", onStartClick)
                Text(
                    "Ya tengo una cuenta",
                    style = ClozyType.BodyMedium,
                    color = ClozyColors.TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onHaveAccountClick)
                        .padding(vertical = 8.dp),
                )
            }
        }
    }
}


@Composable
private fun IllustrationArea(modifier: Modifier = Modifier) {
    Box(modifier) {
        Image(
            painterResource(R.drawable.illus_shirt), null,
            Modifier.offset(x = (-14.29).dp, y = (-0.63).dp).size(width = 142.579.dp, height = 160.759.dp),
        )
        Image(
            painterResource(R.drawable.illus_pants), null,
            Modifier.offset(x = 180.57.dp, y = 37.14.dp).size(width = 130.869.dp, height = 167.981.dp),
        )
        Image(
            painterResource(R.drawable.illus_bag), null,
            Modifier.offset(x = 216.dp, y = (-1.93).dp).size(width = 62.dp, height = 70.dp),
        )
        Image(
            painterResource(R.drawable.illus_squiggle), null,
            Modifier.offset(x = 4.dp, y = 206.24.dp).size(width = 72.dp, height = 50.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun BienvenidaPreview() { ClozyTheme { BienvenidaScreen({}, {}) } }
