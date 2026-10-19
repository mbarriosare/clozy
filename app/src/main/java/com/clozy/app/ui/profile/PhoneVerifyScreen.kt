package com.clozy.app.ui.profile

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyBackRow
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

private fun maskPhone(fullNumber: String): String {
    if (fullNumber.length < 4) return fullNumber
    return fullNumber.dropLast(4).map { if (it.isDigit()) '•' else it }.joinToString("") + fullNumber.takeLast(4)
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
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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

/**
 * No corresponde a un frame de Figma como tal; reutiliza el patrón de casillas de 6
 * dígitos que sí estaba diseñado para "Verificar identidad" (1:1864), ahora aplicado
 * a verificar el número de teléfono con un SMS real de Firebase.
 */
@Composable
fun PhoneVerifyScreen(
    phoneNumber: String,
    code: String,
    onCodeChange: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onResendClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isVerifying: Boolean = false,
    errorMessage: String? = null,
) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        Column(Modifier.padding(horizontal = 24.dp, vertical = 32.dp)) {
            ClozyBackRow(onBackClick)
            Column(Modifier.padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Verifica tu número", style = ClozyType.Headline18, color = ClozyColors.TextPrimary)
                Text(
                    "Enviamos un código por SMS a ${maskPhone(phoneNumber)}.",
                    style = ClozyType.BodyMedium, color = ClozyColors.TextMuted,
                    modifier = Modifier.widthIn(max = 305.dp),
                )
            }
            Column(Modifier.padding(top = 28.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "CÓDIGO DE VERIFICACIÓN", style = ClozyType.CaptionBold,
                    color = ClozyColors.TextMuted,
                )
                CodeInput(code, onCodeChange, isError = errorMessage != null)
                if (errorMessage != null) {
                    Text(errorMessage, style = ClozyType.Caption, color = ClozyColors.Error)
                }
            }
            Text(
                "Reenviar código",
                style = ClozyType.CaptionBold, color = ClozyColors.Primary,
                modifier = Modifier.padding(top = 12.dp).clickable(onClick = onResendClick),
            )
            Spacer(Modifier.height(28.dp))
            ClozyPrimaryButton(
                if (isVerifying) "Verificando..." else "Verificar",
                onClick = onVerifyClick,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun PhoneVerifyPreview() {
    ClozyTheme { PhoneVerifyScreen("+573001234567", "123", {}, {}, {}, {}) }
}
