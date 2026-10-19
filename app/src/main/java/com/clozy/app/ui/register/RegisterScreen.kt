package com.clozy.app.ui.register

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyCheckbox
import com.clozy.app.ui.components.ClozyIcons
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.components.ClozyTextField
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType


data class RegisterErrors(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirm: String? = null,
    val terms: String? = null,
)


@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterClick: (name: String, email: String, password: String, confirm: String, acceptedTerms: Boolean) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    errors: RegisterErrors = RegisterErrors(),
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var accepted by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize().background(ClozyColors.Background)) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(ClozyColors.Lavender, ClozyColors.Primary, ClozyColors.Peach),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 28.dp, end = 28.dp, top = 34.dp, bottom = 24.dp),
        ) {
            Row(
                modifier = Modifier.clickable(onClick = onBackClick),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(20.dp))
                Text("Volver", style = ClozyType.BodyMedium, color = ClozyColors.TextMuted)
            }

            Spacer(Modifier.height(24.dp))
            Text("Crea tu cuenta", style = ClozyType.Title, color = ClozyColors.TextPrimary)
            Text(
                "Regístrate para comenzar.",
                style = ClozyType.CaptionRegular,
                color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 2.dp),
            )

            Column(
                modifier = Modifier.padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ClozyTextField(
                    value = name, onValueChange = { name = it },
                    placeholder = "Nombre", leadingIcon = ClozyIcons.Person,
                    keyboardType = KeyboardType.Text,
                    isError = errors.name != null, errorText = errors.name,
                )
                ClozyTextField(
                    value = email, onValueChange = { email = it },
                    placeholder = "Correo electrónico", leadingIcon = ClozyIcons.Mail,
                    isError = errors.email != null, errorText = errors.email,
                )
                ClozyTextField(
                    value = password, onValueChange = { password = it },
                    placeholder = "Contraseña", leadingIcon = ClozyIcons.Lock, isPassword = true,
                    isError = errors.password != null, errorText = errors.password,
                )
                ClozyTextField(
                    value = confirm, onValueChange = { confirm = it },
                    placeholder = "Confirmar contraseña", leadingIcon = ClozyIcons.Lock, isPassword = true,
                    isError = errors.confirm != null, errorText = errors.confirm,
                )
            }

            Row(
                modifier = Modifier.padding(top = 36.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top,
            ) {
                ClozyCheckbox(accepted, { accepted = it }, Modifier.padding(top = 2.dp))
                Text(
                    "Acepto los términos y condiciones.",
                    style = ClozyType.Caption,
                    color = if (errors.terms != null) ClozyColors.Primary else ClozyColors.TextMuted,
                )
            }

            Spacer(Modifier.height(20.dp))
            ClozyPrimaryButton(
                text = "Crear cuenta",
                onClick = { onRegisterClick(name, email, password, confirm, accepted) },
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = ClozyColors.TextMuted)) { append("¿Ya tienes una cuenta? ") }
                    withStyle(SpanStyle(color = ClozyColors.Primary, fontWeight = FontWeight.Bold)) { append("Iniciar sesión") }
                },
                style = ClozyType.Caption,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .clickable(onClick = onLoginClick),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun RegisterPreview() {
    ClozyTheme { RegisterScreen({}, { _, _, _, _, _ -> }, {}) }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun RegisterErrorsPreview() {
    ClozyTheme {
        RegisterScreen(
            {}, { _, _, _, _, _ -> }, {},
            errors = RegisterErrors(
                name = "Escribe tu nombre.",
                email = "Correo no válido.",
                password = "Mínimo 8 caracteres.",
                confirm = "Las contraseñas no coinciden.",
                terms = "Debes aceptar los términos.",
            ),
        )
    }
}
