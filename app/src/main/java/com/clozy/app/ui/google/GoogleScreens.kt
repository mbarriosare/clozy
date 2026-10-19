package com.clozy.app.ui.google

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import com.clozy.app.ui.components.ClozyIcons
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.R
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.components.ClozyOutlinedButton
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.status.FlowErrorScreen
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

data class GoogleAccount(val name: String, val email: String, val gradient: List<Color>) {
    val initials: String get() = name.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.take(2).joinToString("")
}

val MockGoogleAccounts = listOf(
    GoogleAccount("Valeria Montes", "valeria.montes@gmail.com", listOf(ClozyColors.Primary, ClozyColors.Lavender)),
    GoogleAccount("Valeria M.", "valeria.m@gmail.com", listOf(ClozyColors.Lavender, ClozyColors.Peach)),
)


@Composable
private fun GoogleHeader() {
    Column(Modifier.fillMaxWidth()) {
        Box(
            Modifier.fillMaxWidth().padding(start = 28.dp, end = 28.dp, top = 40.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center,
        ) {
            val shape = RoundedCornerShape(16.dp)
            Box(
                Modifier
                    .size(48.dp)
                    .shadow(2.dp, shape, ambientColor = Color(0x12211A1D), spotColor = Color(0x12211A1D))
                    .background(ClozyColors.White, shape)
                    .border(BorderStroke(1.111.dp, ClozyColors.TextFaint), shape),
                contentAlignment = Alignment.Center,
            ) {
                Image(painterResource(R.drawable.google_g), null, Modifier.size(18.dp))
            }
        }
        Box(Modifier.fillMaxWidth().height(1.111.dp).background(ClozyColors.TextFaint))
    }
}

@Composable
private fun AccountCard(content: @Composable () -> Unit, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, shape, ambientColor = Color(0x0D211A1D), spotColor = Color(0x0D211A1D))
            .background(ClozyColors.White, shape)
            .border(BorderStroke(1.111.dp, ClozyColors.TextFaint), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) { content() }
}


@Composable
fun GoogleAccountScreen(
    accounts: List<GoogleAccount>,
    onAccountClick: (GoogleAccount) -> Unit,
    onOtherAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        GoogleHeader()

        Column(
            Modifier.fillMaxWidth().padding(top = 24.dp, start = 28.dp, end = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text("Elige una cuenta", style = ClozyType.Headline18, color = ClozyColors.TextPrimary)
            Text("para continuar en Clozy", style = ClozyType.BodyMedium, color = ClozyColors.TextMuted)
        }

        Column(
            modifier = Modifier
                .padding(top = 31.dp)
                .align(Alignment.CenterHorizontally)
                .widthIn(max = 320.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            accounts.forEach { account ->
                AccountCard(onClick = { onAccountClick(account) }, content = {
                    Box(
                        Modifier.size(40.dp).background(Brush.linearGradient(account.gradient), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) { Text(account.initials, style = ClozyType.Button.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified), color = ClozyColors.White) }
                    Column {
                        Text(account.name, style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary)
                        Text(account.email, style = ClozyType.Caption, color = ClozyColors.TextMuted)
                    }
                })
            }
            AccountCard(onClick = onOtherAccountClick, content = {
                Box(
                    Modifier
                        .size(40.dp)
                        .background(ClozyColors.Blush, CircleShape)
                        .border(BorderStroke(1.111.dp, ClozyColors.TextFaint), CircleShape),
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Filled.Add, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(16.dp)) }
                Text("Usar otra cuenta", style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary)
            })
        }

        Spacer(Modifier.weight(1f))
        Text(
            "Al continuar, Google compartirá tu información con Clozy según los permisos solicitados.",
            style = ClozyType.Footnote.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified, lineHeight = androidx.compose.ui.unit.TextUnit(16.25f, androidx.compose.ui.unit.TextUnitType.Sp)),
            color = ClozyColors.TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
        )
    }
}

@Composable
private fun PermissionCard(title: String, description: String) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .widthIn(max = 321.dp)
            .fillMaxWidth()
            .shadow(1.dp, shape, ambientColor = Color(0x0D211A1D), spotColor = Color(0x0D211A1D))
            .background(ClozyColors.White, shape)
            .border(BorderStroke(1.111.dp, ClozyColors.TextFaint), shape)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            Modifier.padding(top = 2.dp).size(20.dp).background(ClozyColors.Primary, CircleShape),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Filled.Check, null, tint = ClozyColors.White, modifier = Modifier.size(10.dp)) }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary)
            Text(description, style = ClozyType.Caption, color = ClozyColors.TextMuted)
        }
    }
}


@Composable
fun GooglePermissionsScreen(
    onContinueClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        GoogleHeader()
        Column(
            modifier = Modifier.weight(1f).padding(start = 28.dp, end = 28.dp, top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                "Google permitiría que Clozy acceda a esta información sobre ti",
                style = ClozyType.Title.copy(fontSize = androidx.compose.ui.unit.TextUnit(16f, androidx.compose.ui.unit.TextUnitType.Sp), lineHeight = androidx.compose.ui.unit.TextUnit(22f, androidx.compose.ui.unit.TextUnitType.Sp), letterSpacing = androidx.compose.ui.unit.TextUnit(-0.24f, androidx.compose.ui.unit.TextUnitType.Sp)),
                color = ClozyColors.TextPrimary,
            )
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                PermissionCard("Información básica de tu cuenta", "Nombre, dirección de correo electrónico y foto de perfil.")
                PermissionCard("Información necesaria para crear tu cuenta en Clozy", "Datos básicos necesarios para identificar tu cuenta.")
            }
            Text(
                "Puedes revisar o cambiar estos permisos en cualquier momento desde la configuración de tu cuenta de Google.",
                style = ClozyType.Caption.copy(lineHeight = androidx.compose.ui.unit.TextUnit(19.5f, androidx.compose.ui.unit.TextUnitType.Sp)),
                color = ClozyColors.TextMuted,
            )
            Spacer(Modifier.weight(1f))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ClozyPrimaryButton("Continuar", onContinueClick)
                ClozyOutlinedButton("Cancelar", onCancelClick)
            }
        }
    }
}


fun maskEmail(email: String): String {
    val at = email.indexOf('@')
    if (at < 0) return email
    return email.take(2.coerceAtMost(at)) + "••••••" + email.substring(at)
}


@Composable
private fun CodeInput(value: String, onValueChange: (String) -> Unit, isError: Boolean, length: Int = 6) {
    BasicTextField(
        value = value,
        onValueChange = { new -> onValueChange(new.filter { it.isDigit() }.take(length)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        cursorBrush = SolidColor(Color.Transparent),
        textStyle = ClozyType.Title.copy(color = ClozyColors.TextPrimary),
        decorationBox = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                repeat(length) { i ->
                    val shape = RoundedCornerShape(16.dp)
                    Box(
                        Modifier
                            .weight(1f)
                            .height(52.dp)
                            .background(if (isError) ClozyColors.ErrorSoft else ClozyColors.White, shape)
                            .border(
                                BorderStroke(1.111.dp, if (isError) ClozyColors.Error else ClozyColors.TextFaint),
                                shape,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(value.getOrNull(i)?.toString() ?: "", style = ClozyType.Title, color = ClozyColors.TextPrimary)
                    }
                }
            }
        },
    )
}


@Composable
fun GoogleVerifyScreen(
    email: String,
    onVerifyClick: (code: String) -> Unit,
    onResendClick: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
) {
    var code by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        GoogleHeader()
        Column(
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Verifica tu identidad", style = ClozyType.Headline18, color = ClozyColors.TextPrimary)
                Text(
                    "Enviamos un código de verificación a tu correo electrónico.",
                    style = ClozyType.BodyMedium, color = ClozyColors.TextMuted,
                    modifier = Modifier.widthIn(max = 305.dp),
                )
                val shape = RoundedCornerShape(12.dp)
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth()
                        .background(ClozyColors.White, shape)
                        .border(BorderStroke(1.111.dp, ClozyColors.TextFaint), shape)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(ClozyIcons.Mail, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(16.dp))
                    Text(maskEmail(email), style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "CÓDIGO DE VERIFICACIÓN",
                    style = ClozyType.CaptionBold.copy(letterSpacing = androidx.compose.ui.unit.TextUnit(1.68f, androidx.compose.ui.unit.TextUnitType.Sp)),
                    color = ClozyColors.TextMuted,
                )
                CodeInput(code, { code = it }, isError = errorMessage != null)
                if (errorMessage != null) {
                    Text(errorMessage, style = ClozyType.Caption, color = ClozyColors.Error)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("¿No recibiste el código?", style = ClozyType.Caption, color = ClozyColors.TextMuted)
                    Text(
                        "Reenviar código",
                        style = ClozyType.CaptionBold, color = ClozyColors.Primary,
                        modifier = Modifier.clickable(onClick = onResendClick),
                    )
                }
                Text(
                    "El código puede tardar unos segundos en llegar.",
                    style = ClozyType.Footnote.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified),
                    color = ClozyColors.TextFaint,
                )
            }
            Spacer(Modifier.height(14.dp))
            ClozyPrimaryButton("Verificar", onClick = { onVerifyClick(code) })
        }
    }
}


@Composable
private fun GoogleFailedBadge() {
    Box(Modifier.size(width = 64.dp, height = 64.dp)) {
        val shape = RoundedCornerShape(16.dp)
        Box(
            Modifier
                .size(56.dp)
                .shadow(3.dp, shape, ambientColor = Color(0x14211A1D), spotColor = Color(0x14211A1D))
                .background(ClozyColors.White, shape)
                .border(BorderStroke(1.dp, ClozyColors.TextFaint), shape),
            contentAlignment = Alignment.Center,
        ) { Image(painterResource(R.drawable.google_g), null, Modifier.size(18.dp)) }
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
                .background(ClozyColors.Blush, CircleShape)
                .border(BorderStroke(1.dp, ClozyColors.TextFaint), CircleShape),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Filled.Close, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(16.dp)) }
    }
}


@Composable
fun GoogleCancelledScreen(onRetryClick: () -> Unit, onOtherWayClick: () -> Unit, modifier: Modifier = Modifier) {
    FlowErrorScreen(
        badge = { GoogleFailedBadge() },
        title = "Inicio de sesión cancelado",
        message = "El inicio de sesión con Google fue cancelado. Puedes intentarlo nuevamente.",
        primaryText = "Intentar nuevamente",
        onPrimaryClick = onRetryClick,
        onBackClick = onOtherWayClick,
        secondaryText = "Iniciar sesión de otra forma",
        secondaryStyle = ClozyType.BodyMedium,
        primaryShadow = ClozyColors.Primary.copy(alpha = 0.35f),
        modifier = modifier,
    )
}


@Composable
fun GoogleErrorScreen(onRetryClick: () -> Unit, onOtherWayClick: () -> Unit, modifier: Modifier = Modifier) {
    FlowErrorScreen(
        badge = { GoogleFailedBadge() },
        title = "No pudimos conectar con Google",
        message = "Ocurrió un problema al iniciar sesión con Google. Inténtalo nuevamente.",
        primaryText = "Intentar nuevamente",
        onPrimaryClick = onRetryClick,
        onBackClick = onOtherWayClick,
        secondaryText = "Iniciar sesión de otra forma",
        secondaryStyle = ClozyType.BodyMedium,
        primaryShadow = ClozyColors.Primary.copy(alpha = 0.35f),
        modifier = modifier,
    )
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun AccountPreview() { ClozyTheme { GoogleAccountScreen(MockGoogleAccounts, {}, {}) } }

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun PermissionsPreview() { ClozyTheme { GooglePermissionsScreen({}, {}) } }

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun CancelledPreview() { ClozyTheme { GoogleCancelledScreen({}, {}) } }

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun VerifyPreview() { ClozyTheme { GoogleVerifyScreen("valeria.montes@gmail.com", {}, {}) } }

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun VerifyErrorPreview() {
    ClozyTheme { GoogleVerifyScreen("valeria.montes@gmail.com", {}, {}, errorMessage = "Ingresa el código de 6 dígitos.") }
}
