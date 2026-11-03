package com.clozy.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clozy.app.ui.components.ClozyBackRow
import com.clozy.app.ui.components.ClozyGradientBar
import com.clozy.app.ui.theme.ClozyColors
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.theme.ClozyType

private data class InfoSection(val title: String, val body: String)

private val HelpSections = listOf(
    InfoSection(
        "¿Cómo agrego una prenda?",
        "Ve a Armario y toca \"+ Agregar\". Puedes tomar una foto, elegirla de la galería o llenar los datos a mano: nombre, categoría, colores y estampado.",
    ),
    InfoSection(
        "¿Cómo creo un outfit con Clozy AI?",
        "En Outfits toca \"Crea un outfit con Clozy AI\", elige la ocasión y el estilo, y Clozy AI armará una combinación con las prendas de tu armario. Necesitas al menos dos prendas del estilo elegido.",
    ),
    InfoSection(
        "¿Cómo marco una prenda como favorita?",
        "Abre la prenda desde tu armario y toca el corazón. Todas tus favoritas aparecen en Perfil, en \"Prendas favoritas\", con el enlace \"Ver todo\".",
    ),
    InfoSection(
        "¿Cómo vendo una prenda?",
        "En Mercado toca \"+ Publicar\", elige una prenda de tu armario, ponle precio y condición. Desde \"Mis publicaciones\" puedes pausarla, activarla o eliminarla.",
    ),
    InfoSection(
        "¿Qué pasa cuando compro una prenda?",
        "La prenda se agrega automáticamente a tu armario y queda registrada en \"Mis compras\". El pago dentro de la app es una simulación.",
    ),
    InfoSection(
        "¿Cómo cambio al modo oscuro?",
        "Ve a Perfil, luego Ajustes, y toca \"Apariencia\" para alternar entre Sistema, Claro y Oscuro.",
    ),
)

private val AboutSections = listOf(
    InfoSection(
        "¿Qué es Clozy?",
        "Clozy es una app para organizar tu armario digital, crear outfits con ayuda de inteligencia artificial y comprar o vender prendas entre usuarios.",
    ),
    InfoSection(
        "Clozy AI",
        "Los outfits se generan con Gemini, el modelo de Google. La app le envía los datos de tus prendas (nombre, categoría, color y estilo), la ocasión y el estilo elegidos, y Gemini devuelve la combinación con una explicación.",
    ),
    InfoSection(
        "Tecnología",
        "Desarrollada en Kotlin con Jetpack Compose. Usa Firebase para autenticación, base de datos y almacenamiento de fotos.",
    ),
    InfoSection(
        "Proyecto académico",
        "Clozy es un proyecto universitario y no está disponible como producto comercial.",
    ),
)

@Composable
private fun InfoScreen(
    title: String,
    subtitle: String,
    sections: List<InfoSection>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Lavender, ClozyColors.Primary))
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 32.dp),
        ) {
            ClozyBackRow(onBackClick)
            Text(title, style = ClozyType.Title, color = ClozyColors.TextPrimary, modifier = Modifier.padding(top = 16.dp))
            Text(subtitle, style = ClozyType.Caption, color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 4.dp))
            Column(Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                sections.forEach { section ->
                    Column {
                        Text(section.title, style = ClozyType.Headline18, color = ClozyColors.TextPrimary)
                        Text(
                            section.body, style = ClozyType.BodyRegular.copy(lineHeight = 22.75.sp),
                            color = ClozyColors.TextMuted, modifier = Modifier.padding(top = 6.dp),
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun HelpScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) =
    InfoScreen("Ayuda y soporte", "Preguntas frecuentes", HelpSections, onBackClick, modifier)

@Composable
fun AboutScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) =
    InfoScreen("Acerca de Clozy", "Versión 1.0", AboutSections, onBackClick, modifier)

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun HelpPreview() { ClozyTheme { HelpScreen({}) } }
