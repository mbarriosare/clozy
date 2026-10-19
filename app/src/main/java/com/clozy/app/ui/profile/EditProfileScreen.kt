package com.clozy.app.ui.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
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
import androidx.compose.ui.text.input.KeyboardType
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
fun EditProfileScreen(
    profile: UserProfile,
    onBackClick: () -> Unit,
    onSaveClick: (name: String, username: String, city: String, phone: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf(profile.name) }
    var username by remember { mutableStateOf(profile.username) }
    var city by remember { mutableStateOf(profile.city) }
    var phone by remember { mutableStateOf(profile.phone) }

    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 32.dp),
        ) {
            ClozyBackRow(onBackClick)
            Text("Editar perfil", style = ClozyType.Title, color = ClozyColors.TextPrimary, modifier = Modifier.padding(top = 16.dp))

            Box(Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                Box {
                    Box(
                        Modifier.size(72.dp).background(Brush.linearGradient(listOf(ClozyColors.Primary, ClozyColors.Lavender)), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) { Text("VM", style = ClozyType.Title.copy(fontSize = androidx.compose.ui.unit.TextUnit(20f, androidx.compose.ui.unit.TextUnitType.Sp)), color = ClozyColors.White) }
                    Box(
                        Modifier.align(Alignment.BottomEnd).size(28.dp).background(ClozyColors.Primary, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) { Icon(Icons.Filled.Edit, null, tint = ClozyColors.White, modifier = Modifier.size(12.dp)) }
                }
            }

            Column(Modifier.padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("DATOS MODIFICABLES", style = ClozyType.LabelBold9, color = ClozyColors.TextMuted)
                Column(Modifier.padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ClozyTextField(name, { name = it }, "Nombre", Icons.Outlined.Person, keyboardType = KeyboardType.Text)
                    ClozyTextField(username, { username = it }, "Usuario", Icons.Outlined.Person, keyboardType = KeyboardType.Text)
                    ClozyTextField(city, { city = it }, "Ciudad", Icons.Outlined.LocationOn, keyboardType = KeyboardType.Text)
                }
            }

            Column(Modifier.padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("DATOS DE LA CUENTA", style = ClozyType.LabelBold9, color = ClozyColors.TextMuted)
                Column(Modifier.padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LockedField(profile.email, Icons.Outlined.Mail, "🔒 El correo electrónico está asociado a tu cuenta y no puede modificarse.")
                    ClozyTextField(phone, { phone = it }, "Teléfono", Icons.Outlined.Phone, keyboardType = KeyboardType.Phone)
                }
            }

            Spacer(Modifier.height(40.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ClozyPrimaryButton("Guardar cambios", onClick = { onSaveClick(name, username, city, phone) })
                Text(
                    "Cancelar", style = ClozyType.Button, color = ClozyColors.TextMuted,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().clickable(onClick = onBackClick).padding(vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun LockedField(value: String, leadingIcon: androidx.compose.ui.graphics.vector.ImageVector?, caption: String) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    Column {
        Row(
            Modifier
                .fillMaxWidth()
                .background(ClozyColors.Blush, shape)
                .border(1.dp, ClozyColors.TextFaint, shape)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (leadingIcon != null) Icon(leadingIcon, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(16.dp))
            Text(value, style = ClozyType.Field, color = ClozyColors.TextMuted, modifier = Modifier.weight(1f))
            Icon(Icons.Outlined.Lock, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(16.dp))
        }
        Text(caption, style = ClozyType.Caption, color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 4.dp, start = 4.dp))
    }
}

@Composable
private fun PhoneField(verifiedPhone: String?, onVerifyClick: (String) -> Unit) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    if (verifiedPhone != null) {
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(ClozyColors.Blush, shape)
                    .border(1.dp, ClozyColors.TextFaint, shape)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Outlined.Phone, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(16.dp))
                Text(verifiedPhone, style = ClozyType.Field, color = ClozyColors.TextMuted, modifier = Modifier.weight(1f))
                Icon(Icons.Filled.CheckCircle, null, tint = androidx.compose.ui.graphics.Color(0xFF2E9E5B), modifier = Modifier.size(16.dp))
            }
            Text(
                "Número verificado.", style = ClozyType.Caption, color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            )
        }
    } else {
        var digits by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(ClozyColors.White, shape)
                    .border(1.dp, ClozyColors.TextFaint, shape)
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Outlined.Phone, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(16.dp))
                Text("+57", style = ClozyType.Field, color = ClozyColors.TextPrimary)
                androidx.compose.foundation.text.BasicTextField(
                    value = digits,
                    onValueChange = { digits = it.filter(Char::isDigit).take(10) },
                    singleLine = true,
                    textStyle = ClozyType.Field.copy(color = ClozyColors.TextPrimary),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone,
                    ),
                    decorationBox = { inner ->
                        if (digits.isEmpty()) {
                            Text("300 123 4567", style = ClozyType.Field, color = ClozyColors.TextPrimary.copy(alpha = 0.5f))
                        }
                        inner()
                    },
                    modifier = Modifier.weight(1f),
                )
            }
            Text(
                "Tu número no está verificado.",
                style = ClozyType.Caption, color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            )
            Text(
                "Verificar número",
                style = ClozyType.CaptionBold, color = ClozyColors.Primary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable(enabled = digits.length >= 10) { onVerifyClick(digits) },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun EditProfilePreview() { ClozyTheme { EditProfileScreen(MockProfile, {}, { _, _, _, _ -> }) } }
