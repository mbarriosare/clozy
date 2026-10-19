package com.clozy.app.ui.recover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyBackRow
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.components.ClozyIcons
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.components.ClozyTextField
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType


@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onSendLinkClick: (email: String) -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    var email by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 28.dp, vertical = 32.dp),
        ) {
            ClozyBackRow(onBackClick)
            Spacer(Modifier.height(32.dp))
            Text("¿Olvidaste tu contraseña?", style = ClozyType.Title, color = ClozyColors.TextPrimary)
            Text(
                "Ingresa tu correo y te enviaremos un enlace.",
                style = ClozyType.BodyRegular,
                color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 8.dp).widthIn(max = 304.dp),
            )
            Spacer(Modifier.height(28.dp))
            ClozyTextField(
                value = email, onValueChange = { email = it },
                placeholder = "Correo electrónico", leadingIcon = ClozyIcons.Mail,
                isError = errorMessage != null, errorText = errorMessage,
            )
            Spacer(Modifier.weight(1f))
            ClozyPrimaryButton("Enviar enlace", onClick = { onSendLinkClick(email) })
            Text(
                "Volver a iniciar sesión",
                style = ClozyType.BodyMedium,
                color = ClozyColors.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .clickable(onClick = onBackToLoginClick),
            )
        }
    }
}


@Composable
fun CheckEmailScreen(
    onBackToLoginClick: () -> Unit,
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
            Modifier.size(80.dp).background(ClozyColors.Blush, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(ClozyIcons.Mail, null, tint = ClozyColors.Primary, modifier = Modifier.size(56.dp))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.widthIn(max = 296.dp)) {
            Text("Revisa tu correo", style = ClozyType.Title, color = ClozyColors.TextPrimary, textAlign = TextAlign.Center)
            Text(
                "Te enviamos un enlace para restablecer tu contraseña. Ábrelo desde tu correo para crear una nueva.",
                style = ClozyType.BodyRegular,
                color = ClozyColors.TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        ClozyPrimaryButton(
            "Entendido", onBackToLoginClick,
            modifier = Modifier.padding(top = 16.dp).widthIn(max = 296.dp),
        )
    }
}


@Composable
fun ResetPasswordScreen(
    onBackClick: () -> Unit,
    onResetClick: (password: String, confirm: String) -> Unit,
    modifier: Modifier = Modifier,
    passwordError: String? = null,
    confirmError: String? = null,
) {
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Lavender, ClozyColors.Primary))
        Column(modifier = Modifier.weight(1f).padding(horizontal = 28.dp, vertical = 32.dp)) {
            ClozyBackRow(onBackClick)
            Spacer(Modifier.height(32.dp))
            Text("Nueva contraseña", style = ClozyType.Title, color = ClozyColors.TextPrimary)
            Text(
                "Ingresa tu nueva contraseña.",
                style = ClozyType.BodyRegular,
                color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 4.dp),
            )
            Spacer(Modifier.height(28.dp))
            ClozyTextField(
                value = password, onValueChange = { password = it },
                placeholder = "Nueva contraseña", leadingIcon = ClozyIcons.Lock, isPassword = true,
                isError = passwordError != null, errorText = passwordError,
            )
            Spacer(Modifier.height(28.dp))
            ClozyTextField(
                value = confirm, onValueChange = { confirm = it },
                placeholder = "Confirmar contraseña", leadingIcon = ClozyIcons.Lock, isPassword = true,
                isError = confirmError != null, errorText = confirmError,
            )
            Spacer(Modifier.weight(1f))
            ClozyPrimaryButton("Restablecer contraseña", onClick = { onResetClick(password, confirm) })
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun ForgotPreview() { ClozyTheme { ForgotPasswordScreen({}, {}, {}) } }

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun CheckEmailPreview() { ClozyTheme { CheckEmailScreen({}) } }

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun ResetPreview() { ClozyTheme { ResetPasswordScreen({}, { _, _ -> }) } }
