package com.clozy.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyType

private val FieldShape = RoundedCornerShape(16.dp)
private val ButtonShape = RoundedCornerShape(16.dp)


@Composable
fun ClozyPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradient: List<Color> = listOf(ClozyColors.Primary, ClozyColors.PrimaryDark),
    shadowColor: Color = ClozyColors.Primary.copy(alpha = 0.35f),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                8.dp, ButtonShape,
                ambientColor = shadowColor,
                spotColor = shadowColor,
            )
            .background(Brush.verticalGradient(gradient), ButtonShape)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = ClozyType.Button, color = ClozyColors.White)
    }
}


@Composable
fun ClozyOutlinedButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(BorderStroke(1.111.dp, ClozyColors.Primary), ButtonShape)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = ClozyType.Button, color = ClozyColors.Primary)
    }
}


@Composable
fun ClozySocialButton(
    text: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(1.dp, ButtonShape, ambientColor = Color(0x0F000000), spotColor = Color(0x0F000000))
            .background(ClozyColors.White, ButtonShape)
            .border(BorderStroke(1.111.dp, ClozyColors.TextFaint), ButtonShape)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Text(text, style = ClozyType.ButtonSocial, color = ClozyColors.TextPrimary)
    }
}


@Composable
fun ClozyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isError: Boolean = false,
    errorText: String? = null,
    keyboardType: KeyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email,
) {
    var visible by remember { mutableStateOf(false) }
    val borderColor = if (isError) ClozyColors.Primary else ClozyColors.TextFaint

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, FieldShape, ambientColor = Color(0x0F211A1D), spotColor = Color(0x0F211A1D))
                .background(ClozyColors.White, FieldShape)
                .border(BorderStroke(1.111.dp, borderColor), FieldShape)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(leadingIcon, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(16.dp))
            Box(modifier = Modifier.weight(1f)) {
                if (value.isEmpty()) {
                    Text(placeholder, style = ClozyType.Field, color = ClozyColors.TextPrimary.copy(alpha = 0.5f))
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = ClozyType.Field.copy(color = ClozyColors.TextPrimary),
                    cursorBrush = SolidColor(ClozyColors.Primary),
                    visualTransformation = if (isPassword && !visible) PasswordVisualTransformation() else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            if (isPassword) {
                Icon(
                    imageVector = if (visible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = null,
                    tint = ClozyColors.TextMuted,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { visible = !visible },
                )
            }
        }
        if (isError && errorText != null) {
            Text(
                errorText,
                style = ClozyType.Caption,
                color = ClozyColors.Primary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp),
            )
        }
    }
}


@Composable
fun ClozyCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(6.dp)
    Box(
        modifier = modifier
            .size(16.dp)
            .background(if (checked) ClozyColors.Primary else Color.Transparent, shape)
            .border(BorderStroke(1.111.dp, if (checked) ClozyColors.Primary else ClozyColors.TextFaint), shape)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center,
    ) {
        if (checked) Icon(Icons.Filled.Check, null, tint = ClozyColors.White, modifier = Modifier.size(11.dp))
    }
}

@Composable
fun ClozyConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Box(
        modifier
            .fillMaxSize()
            .background(Color(0x73211A1D))
            .clickable(enabled = !isLoading, onClick = onCancel),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            Modifier
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxWidth()
                .background(ClozyColors.White, RoundedCornerShape(24.dp))
                .clickable(enabled = false) {}
                .padding(24.dp),
        ) {
            Box(Modifier.align(Alignment.CenterHorizontally).size(width = 40.dp, height = 4.dp).background(ClozyColors.TextFaint, CircleShape))
            Column(Modifier.padding(top = 20.dp)) {
                Text(title, style = ClozyType.Headline18, color = ClozyColors.TextPrimary)
                Text(message, style = ClozyType.BodyRegular.copy(lineHeight = 22.75.sp), color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 6.dp))
            }
            Column(Modifier.padding(top = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(ClozyColors.Error, RoundedCornerShape(16.dp))
                        .clickable(enabled = !isLoading, onClick = onConfirm)
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) { Text(if (isLoading) "Eliminando..." else confirmText, style = ClozyType.Button, color = ClozyColors.White) }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(ClozyColors.Blush, RoundedCornerShape(16.dp))
                        .clickable(enabled = !isLoading, onClick = onCancel)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) { Text("Cancelar", style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary) }
            }
        }
    }
}

@Composable
fun GarmentThumbnail(
    photoUrl: String?,
    thumbnailRes: Int?,
    iconRes: Int,
    tint: Color,
    modifier: Modifier = Modifier,
    iconSize: androidx.compose.ui.unit.Dp = 48.dp,
) {
    when {
        photoUrl != null -> coil.compose.AsyncImage(
            model = photoUrl,
            contentDescription = null,
            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            modifier = modifier.fillMaxSize(),
        )
        thumbnailRes != null -> androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(thumbnailRes),
            contentDescription = null,
            modifier = modifier.size(iconSize + 8.dp),
        )
        else -> androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(iconRes),
            contentDescription = null,
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(tint),
            modifier = modifier.size(iconSize),
        )
    }
}

object ClozyIcons {
    val Error = Icons.Outlined.ErrorOutline
    val WifiOff = Icons.Outlined.WifiOff
    val Person = Icons.Outlined.Person
    val PersonAdd = Icons.Outlined.PersonAdd
    val Mail = Icons.Outlined.Mail
    val Lock = Icons.Outlined.Lock
}
