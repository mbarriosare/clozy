package com.clozy.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.clozy.app.ui.components.ClozyBackRow
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.components.ClozyPrimaryButton
import com.clozy.app.ui.components.ClozyTextField
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType


@Composable
fun ChangePasswordScreen(
    onBackClick: () -> Unit,
    onUpdateClick: (current: String, new: String, confirm: String) -> Unit,
    modifier: Modifier = Modifier,
    currentError: String? = null,
    newError: String? = null,
    confirmError: String? = null,
) {
    var current by remember { mutableStateOf("") }
    var new by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        Column(Modifier.weight(1f).padding(horizontal = 24.dp, vertical = 32.dp)) {
            ClozyBackRow(onBackClick)
            Text("Cambiar contraseña", style = ClozyType.Title, color = ClozyColors.TextPrimary, modifier = Modifier.padding(top = 16.dp))
            Text(
                "Ingresa tu contraseña actual y una nueva.",
                style = ClozyType.BodyRegular, color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 4.dp),
            )
            Column(Modifier.padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ClozyTextField(
                    current, { current = it }, "Contraseña actual", ClozyIconsLock,
                    isPassword = true, isError = currentError != null, errorText = currentError,
                )
                ClozyTextField(
                    new, { new = it }, "Nueva contraseña", ClozyIconsLock,
                    isPassword = true, isError = newError != null, errorText = newError,
                )
                ClozyTextField(
                    confirm, { confirm = it }, "Confirmar nueva contraseña", ClozyIconsLock,
                    isPassword = true, isError = confirmError != null, errorText = confirmError,
                )
            }
            Text(
                "Mínimo 8 caracteres.", style = ClozyType.Caption, color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 8.dp),
            )
            Spacer(Modifier.weight(1f))
            ClozyPrimaryButton("Actualizar contraseña", onClick = { onUpdateClick(current, new, confirm) })
        }
    }
}

private val ClozyIconsLock = androidx.compose.material.icons.Icons.Outlined.Lock

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun ChangePasswordPreview() { ClozyTheme { ChangePasswordScreen({}, { _, _, _ -> }) } }
