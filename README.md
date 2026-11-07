## Instalación de la aplicación

### Opción 1: instalar el APK (recomendada)

No necesitas configurar nada: la app ya incluye Firebase y Gemini.

1. Ve a la sección [**Releases**](https://github.com/mbarriosare/clozy/releases) de este repositorio y descarga el archivo `app-debug.apk` de la última versión.
2. Pásalo a tu teléfono Android o arrástralo a un emulador.
3. Si el teléfono lo pide, activa **Instalar apps de orígenes desconocidos** para el navegador o el explorador de archivos que uses.
4. Abre el APK, instala la app y crea tu cuenta.

Requiere Android 7.0 (API 24) o superior.

### Opción 2: ejecutar desde el código

Requisitos: Android Studio (versión reciente).

1. Clona el repositorio:
   ```
   git clone https://github.com/mbarriosare/clozy.git
   ```
2. Ábrelo en Android Studio, eligiendo la carpeta que contiene `settings.gradle.kts`.
3. Espera a que termine el **Gradle Sync**. El archivo `google-services.json` de Firebase ya está en `app/`, así que no hay que configurarlo.
4. **Clave de Gemini (opcional).** Si quieres que Clozy AI use Gemini, crea una clave gratis en [Google AI Studio](https://aistudio.google.com) y agrégala en el archivo `local.properties` de la raíz del proyecto, que no se sube a Git:
   ```
   GEMINI_API_KEY=tu_clave
   GEMINI_MODEL=gemini-3.6-flash
   ```
   `GEMINI_MODEL` es opcional. Sin la clave, la app funciona igual y arma los outfits con una lógica local, solo que sin la explicación de la IA.
5. Haz **Build → Rebuild Project** y ejecuta la app en un emulador o dispositivo.

> **Nota sobre Google Sign-In:** el inicio de sesión con Google depende de la huella SHA-1 de quien compila la app, registrada en Firebase. Si compilas el proyecto en tu propio computador, ese botón puede fallar. El inicio de sesión con correo y contraseña funciona siempre. Con el APK de la Opción 1 no hay este problema.
