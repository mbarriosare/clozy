package com.clozy.app.ui.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.R
import com.clozy.app.ui.components.ClozyMainScaffold
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType


@Composable
fun HomeScreen(
    userName: String,
    onNotificationsClick: () -> Unit,
    onWardrobeClick: () -> Unit,
    onOutfitsClick: () -> Unit,
    onAiClick: () -> Unit,
    onCreateLookClick: () -> Unit,
    selectedNav: String,
    onNavClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    itemsCount: Int = 24,
    looksCount: Int = 8,
    favoritesCount: Int = 6,
) {
    ClozyMainScaffold(selectedNav, onNavClick, modifier.fillMaxSize().background(ClozyColors.Background)) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        ) {
            HomeHeader(userName, onNotificationsClick)
            TodayLookCard(onCreateLookClick, Modifier.padding(horizontal = 24.dp))
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                ClosetStats(itemsCount, looksCount, favoritesCount, onWardrobeClick)
                Box(Modifier.fillMaxWidth().height(1.dp).background(ClozyColors.TextFaint))
                ForYouRow(onOutfitsClick)
                ClozyAiCard(onAiClick)
            }
        }
    }
}

@Composable
private fun HomeHeader(userName: String, onNotificationsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 32.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column {
            Text("Hola, $userName 👋", style = ClozyType.Caption, color = ClozyColors.TextMuted)
            Text(
                "¿Qué te vas\na poner hoy?",
                style = ClozyType.Title.copy(fontSize = 20.sp, lineHeight = 25.sp, letterSpacing = (-0.5).sp),
                color = ClozyColors.TextPrimary,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(36.dp)
                .shadow(2.dp, CircleShape, ambientColor = Color(0x14000000), spotColor = Color(0x14000000))
                .background(ClozyColors.White, CircleShape)
                .border(1.333.dp, ClozyColors.TextFaint, CircleShape)
                .clickable(onClick = onNotificationsClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.Notifications, null, tint = ClozyColors.TextPrimary, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun TodayLookCard(onCreateLookClick: () -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(24.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(270.dp)
            .background(ClozyColors.CardDark, shape),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 16.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "LOOK DE HOY",
                style = ClozyType.CaptionBold.copy(fontSize = 9.sp, letterSpacing = 2.25.sp),
                color = ClozyColors.TextMuted,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("01", "02", "03").forEachIndexed { i, label ->
                    val active = i == 0
                    Box(
                        Modifier
                            .background(
                                if (active) ClozyColors.Lavender else Color.Transparent,
                                CircleShape,
                            )
                            .border(1.333.dp, if (active) ClozyColors.Lavender else ClozyColors.CardBorderDark, CircleShape)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        Text(
                            label,
                            style = ClozyType.CaptionBold.copy(fontSize = 8.sp, letterSpacing = 0.4.sp),
                            color = if (active) ClozyColors.White else ClozyColors.TextMuted,
                        )
                    }
                }
            }
        }

        Image(
            painter = painterResource(R.drawable.illus_shirt),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp, bottom = 24.dp)
                .size(width = 140.dp, height = 158.dp),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 16.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "CASUAL",
                    style = ClozyType.CaptionBold.copy(fontSize = 9.sp, letterSpacing = 0.9.sp),
                    color = ClozyColors.White,
                    modifier = Modifier.background(ClozyColors.Lavender, CircleShape).padding(horizontal = 10.dp, vertical = 4.dp),
                )
                Text(
                    "24°C",
                    style = ClozyType.CaptionBold.copy(fontSize = 9.sp),
                    color = ClozyColors.TextMuted,
                    modifier = Modifier
                        .border(1.333.dp, ClozyColors.CardBorderDark, CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable(onClick = onCreateLookClick),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Crear look", style = ClozyType.CaptionBold.copy(fontSize = 12.sp), color = ClozyColors.Lavender)
                Icon(Icons.Filled.ChevronRight, null, tint = ClozyColors.Lavender, modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
private fun ClosetStats(items: Int, looks: Int, favorites: Int, onSeeAllClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "MI ARMARIO",
                style = ClozyType.CaptionBold.copy(fontSize = 9.sp, letterSpacing = 1.98.sp),
                color = ClozyColors.TextMuted,
            )
            Row(
                modifier = Modifier.clickable(onClick = onSeeAllClick),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Ver armario", style = ClozyType.CaptionBold.copy(fontSize = 10.sp), color = ClozyColors.Primary)
                Icon(Icons.Filled.ChevronRight, null, tint = ClozyColors.Primary, modifier = Modifier.size(12.dp))
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
            StatItem("$items", "PRENDAS", Modifier.weight(1f))
            Box(Modifier.width(1.dp).height(40.dp).background(ClozyColors.TextFaint))
            StatItem("0$looks".takeLast(2), "LOOKS", Modifier.weight(1f).padding(start = 16.dp))
            Box(Modifier.width(1.dp).height(40.dp).background(ClozyColors.TextFaint))
            StatItem("0$favorites".takeLast(2), "FAVORITOS", Modifier.weight(1f).padding(start = 16.dp))
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            value,
            style = ClozyType.Display.copy(fontSize = 30.sp, lineHeight = 30.sp, letterSpacing = (-1.2).sp),
            color = ClozyColors.TextPrimary,
        )
        Text(
            label,
            style = ClozyType.CaptionBold.copy(fontSize = 8.sp, letterSpacing = 1.44.sp),
            color = ClozyColors.TextMuted,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun ForYouRow(onSeeOutfitsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                "PARA TI",
                style = ClozyType.CaptionBold.copy(fontSize = 9.sp, letterSpacing = 1.98.sp),
                color = ClozyColors.TextMuted,
            )
            Text(
                "Prueba algo nuevo",
                style = ClozyType.Headline18.copy(fontSize = 14.sp, lineHeight = 17.5.sp, letterSpacing = (-0.14).sp),
                color = ClozyColors.TextPrimary,
                modifier = Modifier.padding(top = 4.dp),
            )
            Text(
                "Looks basados en tu estilo.",
                style = ClozyType.BodyRegular.copy(fontSize = 11.sp, lineHeight = 16.5.sp),
                color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 4.dp),
            )
            Row(
                modifier = Modifier.padding(top = 8.dp).clickable(onClick = onSeeOutfitsClick),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Ver outfits", style = ClozyType.CaptionBold.copy(fontSize = 11.sp), color = ClozyColors.Primary)
                Icon(Icons.Filled.ChevronRight, null, tint = ClozyColors.Primary, modifier = Modifier.size(12.dp))
            }
        }
        Image(
            painter = painterResource(R.drawable.illus_shirt),
            contentDescription = null,
            modifier = Modifier.size(width = 52.dp, height = 66.dp),
        )
    }
}

@Composable
private fun ClozyAiCard(onDiscoverClick: () -> Unit) {
    val shape = RoundedCornerShape(24.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(ClozyColors.AiGradientStart, ClozyColors.AiGradientEnd)), shape)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "CLOZY AI",
                style = ClozyType.CaptionBold.copy(fontSize = 9.sp, letterSpacing = 1.98.sp),
                color = ClozyColors.AiAccent,
            )
            Text(
                "Tu asistente de estilo personal.",
                style = ClozyType.BodyMedium.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                color = ClozyColors.Background,
                modifier = Modifier.padding(top = 2.dp),
            )
            Text(
                "Encuentra el look perfecto.",
                style = ClozyType.BodyRegular.copy(fontSize = 10.sp, lineHeight = 15.sp),
                color = ClozyColors.AiSubtext,
                modifier = Modifier.padding(top = 2.dp),
            )
            Row(
                modifier = Modifier.padding(top = 8.dp).clickable(onClick = onDiscoverClick),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Descubrir", style = ClozyType.CaptionBold.copy(fontSize = 10.sp), color = ClozyColors.AiAccent)
                Icon(Icons.Filled.ChevronRight, null, tint = ClozyColors.AiAccent, modifier = Modifier.size(12.dp))
            }
        }
        Box(
            modifier = Modifier.size(44.dp).background(ClozyColors.Primary.copy(alpha = 0.83f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier.size(28.dp).background(ClozyColors.Primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.AutoAwesome, null, tint = ClozyColors.White, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun HomePreview() {
    ClozyTheme {
        HomeScreen(
            userName = "Valeria",
            onNotificationsClick = {}, onWardrobeClick = {}, onOutfitsClick = {},
            onAiClick = {}, onCreateLookClick = {},
            selectedNav = "home", onNavClick = {},
        )
    }
}
