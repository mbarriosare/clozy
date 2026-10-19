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

private data class TermsSection(val title: String, val body: String)

private val TermsSections = listOf(
    TermsSection(
        "1. Aceptación de los términos",
        "Al crear una cuenta en Clozy aceptas estos términos de uso. Si no estás de acuerdo con alguna parte, no deberías usar la aplicación.",
    ),
    TermsSection(
        "2. Tu cuenta",
        "Eres responsable de mantener la confidencialidad de tu contraseña y de toda la actividad que ocurra en tu cuenta. Debes notificarnos si detectas un uso no autorizado.",
    ),
    TermsSection(
        "3. Contenido que subes",
        "Las fotos de prendas y la información de tu armario son tuyas. Nos das permiso para almacenarlas y mostrarlas dentro de la app únicamente para brindarte el servicio.",
    ),
    TermsSection(
        "4. Privacidad",
        "Tratamos tus datos conforme a lo descrito en nuestra sección de Privacidad, dentro de Ajustes de perfil. No compartimos tu información con terceros sin tu consentimiento.",
    ),
    TermsSection(
        "5. Uso permitido",
        "No está permitido usar Clozy para fines ilegales, subir contenido ofensivo, o intentar vulnerar la seguridad de la plataforma.",
    ),
    TermsSection(
        "6. Cambios en el servicio",
        "Podemos actualizar o descontinuar funciones de la app en cualquier momento. Te avisaremos de cambios importantes dentro de la aplicación.",
    ),
    TermsSection(
        "7. Contacto",
        "Si tienes dudas sobre estos términos, puedes escribirnos desde la sección de Ayuda y soporte en Ajustes.",
    ),
)

@Composable
fun TermsScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().background(ClozyColors.Background)) {
        ClozyGradientBar(listOf(ClozyColors.Primary, ClozyColors.Lavender))
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 32.dp),
        ) {
            ClozyBackRow(onBackClick)
            Text("Términos y condiciones", style = ClozyType.Title, color = ClozyColors.TextPrimary, modifier = Modifier.padding(top = 16.dp))
            Text(
                "Última actualización: octubre de 2026",
                style = ClozyType.Caption, color = ClozyColors.TextMuted,
                modifier = Modifier.padding(top = 4.dp),
            )
            Column(Modifier.padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                TermsSections.forEach { section ->
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

@Preview(showBackground = true, widthDp = 412, heightDp = 917)
@Composable
private fun TermsPreview() { ClozyTheme { TermsScreen({}) } }
