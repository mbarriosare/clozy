package com.clozy.app.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.draw.clip
import com.clozy.app.ui.wardrobe.Garment
import com.clozy.app.ui.wardrobe.iconRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.R
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

data class UserProfile(
    val name: String,
    val username: String,
    val email: String,
    val styleTags: List<Pair<String, Boolean>>,
    val favoriteColors: List<Color>,
    val itemsCount: Int,
    val looksCount: Int,
    val favoritesCount: Int,
    val city: String = "",
    val phone: String = "",
    val topSize: String = "",
    val bottomSize: String = "",
    val shoeSize: String = "",
)

data class ColorOption(val label: String, val color: Color)

val PersonalStyleOptions = listOf("Casual", "Elegante", "Deportivo", "Minimal", "Streetwear", "Formal")

val FavoriteColorOptions = listOf(
    ColorOption("Fucsia", ClozyColors.Primary),
    ColorOption("Lila", ClozyColors.Lavender),
    ColorOption("Coral", ClozyColors.Peach),
    ColorOption("Rosa", ClozyColors.StyleRosa),
    ColorOption("Oscuro", ClozyColors.ColorBlack),
    ColorOption("Marfil", ClozyColors.StyleIvory),
)

val TopBottomSizes = listOf("XS", "S", "M", "L", "XL")
val ShoeSizes = listOf("35", "36", "37", "38")

val MockProfile = UserProfile(
    name = "Valeria Montes",
    username = "@valeriamontes",
    email = "valeria.montes@atelierstudio.co",
    styleTags = listOf("Casual" to true, "Minimal" to true, "Elegante" to false, "Streetwear" to false),
    favoriteColors = listOf(
        ClozyColors.Primary, ClozyColors.Lavender, ClozyColors.Peach,
        ClozyColors.ColorPink, ClozyColors.ColorBlack, ClozyColors.ColorWhite,
    ),
    itemsCount = 24, looksCount = 8, favoritesCount = 6,
)



@Composable
fun ProfileScreen(
    profile: UserProfile,
    onSettingsClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onEditStyleClick: () -> Unit,
    onSeeAllFavoritesClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onTermsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    favorites: List<Garment> = emptyList(),
    onFavoriteClick: (Garment) -> Unit = {},
) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Header(onSettingsClick)
            IdentityCard(profile, onEditProfileClick)
            SectionDivider()
            MyStyleSection(profile, onEditStyleClick)
            SectionDivider()
            MyClosetSection(profile)
            SectionDivider()
            FavoritePiecesSection(favorites, onSeeAllFavoritesClick, onFavoriteClick)
            SectionDivider()
            ConfigSection(onNotificationsClick, onPrivacyClick, onSecurityClick, onTermsClick)
            Column(Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
                LogoutButton(onLogoutClick)
            }
        }
    }
}

@Composable
private fun SectionDivider() {
    Box(Modifier.fillMaxWidth().height(1.dp).background(ClozyColors.TextFaint))
}

@Composable
private fun Header(onSettingsClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(width = 6.dp, height = 20.dp)
                    .background(Brush.verticalGradient(listOf(ClozyColors.Primary, ClozyColors.Lavender)), CircleShape),
            )
            Text("Mi perfil", style = ClozyType.Title.copy(fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = (-0.32).sp), color = ClozyColors.TextPrimary)
        }
        Box(
            Modifier
                .size(36.dp)
                .background(ClozyColors.White, CircleShape)
                .border(1.dp, ClozyColors.TextFaint, CircleShape)
                .clickable(onClick = onSettingsClick),
            contentAlignment = Alignment.Center,
        ) { Icon(Icons.Outlined.Settings, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(18.dp)) }
    }
}

@Composable
private fun IdentityCard(profile: UserProfile, onEditClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.size(68.dp).background(Brush.linearGradient(listOf(ClozyColors.Primary, ClozyColors.Lavender)), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            val initials = profile.name.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.take(2).joinToString("")
            Text(initials, style = ClozyType.Title.copy(fontSize = 20.sp), color = ClozyColors.White)
        }
        Column(Modifier.weight(1f)) {
            Text(profile.name, style = ClozyType.Title.copy(fontSize = 16.sp, lineHeight = 20.sp, letterSpacing = (-0.16).sp), color = ClozyColors.TextPrimary)
            Text(profile.username, style = ClozyType.CaptionBold.copy(fontSize = 12.sp), color = ClozyColors.Primary, modifier = Modifier.padding(top = 2.dp))
            Text(profile.email, style = ClozyType.Footnote.copy(letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified, fontSize = 10.sp), color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 2.dp))
        }
        Box(
            Modifier
                .background(ClozyColors.Primary, RoundedCornerShape(12.dp))
                .clickable(onClick = onEditClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) { Text("Editar", style = ClozyType.CaptionBold.copy(fontSize = 11.sp), color = ClozyColors.White) }
    }
}

@Composable
private fun SectionHeader(title: String, actionLabel: String?, onActionClick: (() -> Unit)?) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title.uppercase(), style = ClozyType.LabelBold9, color = ClozyColors.TextMuted)
        if (actionLabel != null) {
            Text(
                actionLabel, style = ClozyType.CaptionBold.copy(fontSize = 10.sp), color = ClozyColors.Primary,
                modifier = Modifier.clickable { onActionClick?.invoke() },
            )
        }
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun MyStyleSection(profile: UserProfile, onEditClick: () -> Unit) {
    Column(Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
        SectionHeader("Mi estilo", "Editar →", onEditClick)
        androidx.compose.foundation.layout.FlowRow(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            profile.styleTags.forEach { (label, active) ->
                Text(
                    label.uppercase(),
                    style = ClozyType.CaptionBold.copy(fontSize = 9.sp, letterSpacing = 0.9.sp),
                    color = if (active) ClozyColors.White else ClozyColors.TextMuted,
                    modifier = Modifier
                        .background(if (active) ClozyColors.Primary else ClozyColors.Blush, CircleShape)
                        .border(1.dp, if (active) ClozyColors.Primary else ClozyColors.TextFaint, CircleShape)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
        }
        Row(Modifier.padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Colores:", style = ClozyType.Caption, color = ClozyColors.TextMuted)
            profile.favoriteColors.forEach { c ->
                Box(Modifier.size(20.dp).background(c, CircleShape).border(1.dp, ClozyColors.TextFaint, CircleShape))
            }
        }
    }
}

@Composable
private fun MyClosetSection(profile: UserProfile) {
    Column(Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
        SectionHeader("Mi armario", null, null)
        Row(Modifier.padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            ProfileStat("${profile.itemsCount}", "PRENDAS")
            Box(Modifier.padding(horizontal = 16.dp).width(1.dp).height(40.dp).background(ClozyColors.TextFaint))
            ProfileStat("0${profile.looksCount}".takeLast(2), "LOOKS")
            Box(Modifier.padding(horizontal = 16.dp).width(1.dp).height(40.dp).background(ClozyColors.TextFaint))
            ProfileStat("0${profile.favoritesCount}".takeLast(2), "FAVORITOS")
        }
    }
}

@Composable
private fun ProfileStat(value: String, label: String) {
    Column {
        Text(value, style = ClozyType.Display.copy(fontSize = 24.sp, lineHeight = 24.sp, letterSpacing = (-0.72).sp), color = ClozyColors.TextPrimary)
        Text(label, style = ClozyType.CaptionBold.copy(fontSize = 8.sp, letterSpacing = 1.2.sp), color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun FavoritePiecesSection(
    favorites: List<Garment>,
    onSeeAllClick: () -> Unit,
    onFavoriteClick: (Garment) -> Unit,
) {
    Column(Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
        SectionHeader("Prendas favoritas", "Ver todo →", onSeeAllClick)
        if (favorites.isEmpty()) {
            Text(
                "Aún no tienes favoritos.",
                style = ClozyType.BodyMedium,
                color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 12.dp),
            )
        } else {
            val shown = favorites.take(6)
            val rows = (shown.size + 2) / 3
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(top = 8.dp).height((rows * 56 + (rows - 1) * 8).dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false,
            ) {
                items(shown, key = { it.id }) { g ->
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ClozyColors.Blush)
                            .clickable { onFavoriteClick(g) },
                        contentAlignment = Alignment.Center,
                    ) {
                        com.clozy.app.ui.components.GarmentThumbnail(
                            photoUrl = g.photoUrl,
                            thumbnailRes = g.thumbnailRes,
                            iconRes = g.category.iconRes(),
                            tint = g.primaryColor.swatch,
                            iconSize = 32.dp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConfigRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(14.dp))
            Text(label, style = ClozyType.BodyMedium, color = ClozyColors.TextPrimary)
        }
        Icon(Icons.Filled.ChevronRight, null, tint = ClozyColors.TextMuted, modifier = Modifier.size(14.dp))
    }
}

@Composable
private fun ConfigSection(
    onNotificationsClick: () -> Unit, onPrivacyClick: () -> Unit,
    onSecurityClick: () -> Unit, onTermsClick: () -> Unit,
) {
    Column(Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
        Text("CONFIGURACIÓN", style = ClozyType.LabelBold9, color = ClozyColors.TextMuted)
        Column(Modifier.padding(top = 8.dp)) {
            ConfigRow(Icons.Outlined.Notifications, "Notificaciones", onNotificationsClick)
            SectionDivider()
            ConfigRow(Icons.Outlined.VisibilityOff, "Privacidad", onPrivacyClick)
            SectionDivider()
            ConfigRow(Icons.Outlined.Lock, "Seguridad", onSecurityClick)
            SectionDivider()
            ConfigRow(Icons.Outlined.Description, "Términos y condiciones", onTermsClick)
        }
    }
}

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(ClozyColors.Blush, RoundedCornerShape(16.dp))
            .border(1.dp, ClozyColors.TextFaint, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) { Text("Cerrar sesión", style = ClozyType.BodyMedium.copy(fontWeight = FontWeight.Bold), color = ClozyColors.Primary) }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun ProfilePreview() {
    ClozyTheme { ProfileScreen(MockProfile, {}, {}, {}, {}, {}, {}, {}, {}, {}) }
}
