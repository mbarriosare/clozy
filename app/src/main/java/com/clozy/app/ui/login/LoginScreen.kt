package com.clozy.app.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.R
import com.clozy.app.ui.components.ClozyIcons
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.components.ClozySocialButton
import com.clozy.app.ui.components.ClozyTextField
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType





@Composable
fun LoginScreen(
    onLoginClick: (email: String, password: String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {

        Box(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(ClozyColors.Primary, ClozyColors.Lavender, ClozyColors.Peach),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 28.dp, end = 28.dp, top = 77.dp),
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.clozy_logo),
                    contentDescription = null,
                    modifier = Modifier.size(width = 36.dp, height = 43.385.dp),
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text("Inicia sesión", style = ClozyType.Title, color = ClozyColors.TextPrimary)
                    Text("Continúa con tu cuenta.", style = ClozyType.CaptionRegular, color = ClozyColors.TextMuted)
                }
                Box(
                    Modifier
                        .size(40.dp)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    ClozyColors.Lavender.copy(alpha = 0.6f),
                                    ClozyColors.Primary.copy(alpha = 0.6f),
                                ),
                            ),
                            CircleShape,
                        ),
                )
            }

            Column(
                modifier = Modifier.padding(top = 38.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                ClozyTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Correo electrónico",
                    leadingIcon = ClozyIcons.Mail,
                    isError = errorMessage != null,
                )
                ClozyTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Contraseña",
                    leadingIcon = ClozyIcons.Lock,
                    isPassword = true,
                    isError = errorMessage != null,
                    errorText = errorMessage,
                )
            }

            Text(
                text = "¿Olvidaste tu contraseña?",
                style = ClozyType.Caption.copy(fontWeight = FontWeight.Bold),
                color = ClozyColors.Primary,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 11.dp, end = 5.dp)
                    .clickable(onClick = onForgotPasswordClick),
            )

            Column(
                modifier = Modifier.padding(top = 43.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ClozyPrimaryButton("Iniciar sesión", onClick = { onLoginClick(email, password) })

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.weight(1f).height(1.dp).background(ClozyColors.TextFaint))
                    Text("o", style = ClozyType.Caption, color = ClozyColors.TextMuted)
                    Box(Modifier.weight(1f).height(1.dp).background(ClozyColors.TextFaint))
                }

                ClozySocialButton(
                    text = "Continuar con Google",
                    onClick = onGoogleClick,
                    icon = { Image(painterResource(R.drawable.google_g), null, Modifier.size(18.dp)) },
                )
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = ClozyColors.TextMuted)) { append("¿No tienes una cuenta? ") }
                    withStyle(SpanStyle(color = ClozyColors.Primary, fontWeight = FontWeight.Bold)) {
                        append("Crear cuenta")
                    }
                },
                style = ClozyType.Caption,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .clickable(onClick = onCreateAccountClick),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun LoginPreview() {
    ClozyTheme {
        LoginScreen({ _, _ -> }, {}, {}, {})
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun LoginErrorPreview() {
    ClozyTheme {
        LoginScreen({ _, _ -> }, {}, {}, {}, errorMessage = "Correo o contraseña incorrectos.")
    }
}
