package com.clozy.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.clozy.app.R


object ClozyColors {
    val Background = Color(0xFFFFF9F7)
    val Primary = Color(0xFFF72585)
    val PrimaryDark = Color(0xFFC4006A)
    val Lavender = Color(0xFFB8A4FF)
    val Peach = Color(0xFFFFB38A)
    val PrimarySoft = Color(0xFFFFF0F6)
    val PeachSoft = Color(0xFFFFF4EE)
    val PeachDark = Color(0xFFE08060)
    val ColorWhite = Color(0xFFFFFFFF)
    val ColorBlack = Color(0xFF211A1D)
    val ColorBlue = Color(0xFF6C8EBF)
    val ColorPink = Color(0xFFF9A8C0)
    val ColorFuchsia = Color(0xFFE0559A)
    val ColorLilac = Color(0xFFB8A4FF)
    val ColorCoral = Color(0xFFFF9D80)
    val ColorGray = Color(0xFFB0A8AC)
    val ColorGreen = Color(0xFF8FBF9F)
    val StyleRosa = Color(0xFFFF8FAB)
    val StyleIvory = Color(0xFFEDE4E1)
    val CardDark = Color(0xFF211A1D)
    val CardBorderDark = Color(0xFF3A2F33)
    val AiGradientStart = Color(0xFF4A2830)
    val AiGradientEnd = Color(0xFF6B2060)
    val AiAccent = Color(0xFFFF8FAB)
    val AiSubtext = Color(0xFFD4A0B8)
    val Error = Color(0xFFE53935)
    val ErrorSoft = Color(0xFFFFF0F0)
    val Blush = Color(0xFFFDF0ED)
    val TextPrimary = Color(0xFF211A1D)
    val TextMuted = Color(0xFF9E8E92)
    val TextFaint = Color(0xFFEEE4E1)
    val White = Color(0xFFFFFFFF)
}


val Sora = FontFamily(
    Font(R.font.sora_regular, FontWeight.Normal),
    Font(R.font.sora_semibold, FontWeight.Medium),
    Font(R.font.sora_bold, FontWeight.Bold),
    Font(R.font.sora_extrabold, FontWeight.ExtraBold),
)

object ClozyType {
    val Eyebrow = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Bold,
        fontSize = 10.sp, lineHeight = 15.sp, letterSpacing = 3.sp,
    )
    val Brand = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Bold,
        fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 2.4.sp,
    )
    val Display = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.ExtraBold,
        fontSize = 48.sp, lineHeight = 48.sp, letterSpacing = (-1.92).sp,
    )
    val Subtitle = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Medium,
        fontSize = 16.sp, lineHeight = 22.sp,
    )
    val Chip = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Bold,
        fontSize = 10.sp, lineHeight = 15.sp,
    )
    val Button = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Bold,
        fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.35.sp,
    )
    val ButtonSocial = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Medium,
        fontSize = 14.sp, lineHeight = 20.sp,
    )
    val Field = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Medium,
        fontSize = 14.sp, lineHeight = 20.sp,
    )
    val Title = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp, lineHeight = 28.sp, letterSpacing = (-0.4).sp,
    )
    val Caption = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Medium,
        fontSize = 12.sp, lineHeight = 16.sp,
    )
    val CaptionRegular = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Normal,
        fontSize = 12.sp, lineHeight = 16.sp,
    )
    val BodyRegular = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Normal,
        fontSize = 14.sp, lineHeight = 20.sp,
    )
    val BodyMedium = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Medium,
        fontSize = 14.sp, lineHeight = 20.sp,
    )
    val Headline = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp, lineHeight = 32.sp, letterSpacing = (-0.48).sp,
    )
    val Headline18 = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp, lineHeight = 28.sp, letterSpacing = (-0.36).sp,
    )
    val LabelBold9 = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Bold,
        fontSize = 9.sp, lineHeight = 13.5.sp, letterSpacing = 1.8.sp,
    )
    val Overline = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Bold,
        fontSize = 10.sp, lineHeight = 15.sp, letterSpacing = 2.sp,
    )
    val CaptionBold = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Bold,
        fontSize = 12.sp, lineHeight = 16.sp,
    )
    val Footnote = TextStyle(
        fontFamily = Sora, fontWeight = FontWeight.Medium,
        fontSize = 10.sp, lineHeight = 15.sp, letterSpacing = 1.sp,
    )
}

@Composable
fun ClozyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = Typography(bodyLarge = ClozyType.Subtitle),
        content = content,
    )
}
