package com.clozy.app.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.R
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyType

data class NavItem(val label: String, val icon: Int, val route: String)

val ClozyNavItems = listOf(
    NavItem("Inicio", R.drawable.nav_home, "home"),
    NavItem("Armario", R.drawable.nav_wardrobe, "wardrobe"),
    NavItem("Outfits", R.drawable.nav_outfits, "outfits"),
    NavItem("Mercado", R.drawable.nav_market, "market"),
    NavItem("Perfil", R.drawable.nav_profile, "profile"),
)





@Composable
fun ClozyBottomNav(selected: String, onNavClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(ClozyColors.White)
            .border(1.333.dp, ClozyColors.TextFaint, RoundedCornerShape(0.dp))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ClozyNavItems.forEach { item ->
            val active = item.route == selected
            val color = if (active) ClozyColors.Primary else ClozyColors.TextMuted
            Column(
                modifier = Modifier
                    .clickable { onNavClick(item.route) }
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Image(
                    painter = painterResource(item.icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(color),
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    item.label.uppercase(),
                    style = ClozyType.CaptionBold.copy(fontSize = 9.sp, letterSpacing = 0.45.sp),
                    color = color,
                )
                Box(
                    Modifier
                        .height(2.dp)
                        .width(16.dp)
                        .background(if (active) ClozyColors.Primary else Color.Transparent, CircleShape),
                )
            }
        }
    }
}


@Composable
fun ClozyMainScaffold(
    selectedNav: String,
    onNavClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) { content() }
        ClozyBottomNav(selectedNav, onNavClick)
    }
}
