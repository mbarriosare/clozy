package com.clozy.app.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

/**
 * Se muestra cuando la cuenta existe en Firebase pero el correo todavía no fue
 * confirmado. No corresponde a un frame de Figma; se construyó para que el
 * registro con correo/contraseña sea real (ver flujo de "olvidé mi contraseña").
 */
@Composable
fun EmailVerificationScreen(
    email: String,
    onCheckAgainClick: () -> Unit,
    onResendClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    isChecking: Boolean = false,
    errorMessage: String? = null,
    resendMessage: String? = null,
) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        ) {
            Box(
                Modifier.size(80.dp).background(ClozyColors.Blush, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Outlined.Email, null, tint = ClozyColors.Primary, modifier = Modifier.size(36.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.widthIn(max = 304.dp)) {
                Text("Confirma tu correo", style = ClozyType.Title, color = ClozyColors.TextPrimary, textAlign = TextAlign.Center)
                Text(
                    "Te enviamos un enlace de confirmación a $email. Ábrelo y luego vuelve aquí.",
                    style = ClozyType.BodyRegular,
                    color = ClozyColors.TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp),
                )
                if (errorMessage != null) {
                    Text(
                        errorMessage, style = ClozyType.Caption, color = ClozyColors.Error,
                        textAlign = TextAlign.Center, modifier = Modifier.padding(top = 12.dp),
                    )
                }
                if (resendMessage != null) {
                    Text(
                        resendMessage, style = ClozyType.Caption, color = ClozyColors.Primary,
                        textAlign = TextAlign.Center, modifier = Modifier.padding(top = 12.dp),
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ClozyPrimaryButton(
                    if (isChecking) "Comprobando..." else "Ya confirmé mi correo",
                    onClick = onCheckAgainClick,
                )
                Text(
                    "Reenviar correo",
                    style = ClozyType.BodyMedium,
                    color = ClozyColors.TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().clickable(onClick = onResendClick),
                )
                Text(
                    "Cerrar sesión",
                    style = ClozyType.Caption,
                    color = ClozyColors.TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().clickable(onClick = onLogoutClick).padding(top = 8.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun EmailVerificationPreview() {
    ClozyTheme { EmailVerificationScreen("prueba@gmail.com", {}, {}, {}) }
}
