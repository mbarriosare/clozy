# Clozy

Clozy es una app Android para organizar tu armario digital, crear outfits con inteligencia artificial y comprar o vender prendas entre usuarios.
> Proyecto universitario. No está pensado para uso comercial.

## Funcionalidades

- **Autenticación:** registro e inicio de sesión con correo y contraseña o con Google, verificación de correo y recuperación de contraseña.
- **Armario digital:** agrega prendas con foto, categoría, colores y estampado; busca y filtra por categoría; edita o elimina.
- **Favoritos:** marca prendas con el corazón y consúltalas desde el perfil con "Ver todo".
- **Outfits con Clozy AI:** elige ocasión y estilo, y la IA arma una combinación con las prendas de tu armario y explica por qué funciona.
- **Mercado:** publica prendas con precio y condición, compra las de otros usuarios y consulta "Mis publicaciones" y "Mis compras". El pago es simulado.
- **Perfil y ajustes:** estilo personal, notificaciones, privacidad, seguridad y modo claro, oscuro o según el sistema.

## Cómo funciona Clozy AI

1. El usuario elige la ocasión, el estilo y el clima.
2. La app envía a Gemini la lista de prendas del armario (nombre, categoría, color y estilo) junto con esos datos.
3. Gemini responde en formato JSON con las prendas elegidas, un nombre para el outfit y una explicación.
4. La app valida que las prendas existan en el armario y guarda el outfit.

Si un modelo está saturado o no disponible, la app reintenta y prueba otros modelos. Si ninguno responde, arma el outfit con una lógica local para que el usuario nunca se quede sin resultado.

## Tecnologías

- Kotlin y Jetpack Compose (Material 3)
- Firebase: Authentication, Cloud Firestore y Storage
- Google Gemini (SDK `generativeai`)
- Coil para cargar imágenes
- Android: minSdk 24, compileSdk 34

## Instalación

### Requisitos

- Android Studio (versión reciente)
- Un proyecto de Firebase propio
- Una API key de Gemini (se obtiene en Google AI Studio)

### Pasos

1. Clona el repositorio:
   ```
   git clone https://github.com/mbarriosare/clozy.git
   ```
2. Ábrelo en Android Studio, eligiendo la carpeta que contiene `settings.gradle.kts`.
3. **Firebase:** crea un proyecto en la consola de Firebase, registra la app con el paquete `com.clozy.app`, descarga `google-services.json` y colócalo en `app/`. Activa Authentication (correo y Google), Firestore y Storage.
4. **Gemini:** en el archivo `local.properties` de la raíz del proyecto (no se sube a Git) agrega:
   ```
   GEMINI_API_KEY=tu_clave
   GEMINI_MODEL=gemini-3.6-flash
   ```
   `GEMINI_MODEL` es opcional. Si no se define, la app prueba una lista de modelos en orden.
5. Haz **Build → Rebuild Project** y ejecuta la app en un emulador o dispositivo.

### Reglas de seguridad de Firebase

Firestore:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    match /users/{uid}/{document=**} {
      allow read, write: if request.auth != null && request.auth.uid == uid;
    }

    match /listings/{listingId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null
        && request.resource.data.sellerId == request.auth.uid
        && request.resource.data.status == 'Active';
      allow update: if request.auth != null && (
        (resource.data.sellerId == request.auth.uid
          && request.resource.data.sellerId == request.auth.uid)
        || (resource.data.status == 'Active'
          && resource.data.sellerId != request.auth.uid
          && request.resource.data.status == 'Sold'
          && request.resource.data.buyerId == request.auth.uid
          && request.resource.data.diff(resource.data).affectedKeys().hasOnly(['status', 'buyerId']))
      );
      allow delete: if request.auth != null && resource.data.sellerId == request.auth.uid;
    }
  }
}
```

Storage:

```
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /users/{uid}/{allPaths=**} {
      allow read, write: if request.auth != null && request.auth.uid == uid;
    }
  }
}
```

## Estructura del proyecto

```
app/src/main/java/com/clozy/app/
├── MainActivity.kt        # Navegación y estado principal
├── data/                  # Repositorios: Auth, prendas, outfits, mercado y Gemini
└── ui/
    ├── components/        # Componentes reutilizables
    ├── theme/             # Colores, tipografía y modo oscuro
    ├── wardrobe/          # Armario y favoritos
    ├── outfits/           # Outfits y Clozy AI
    ├── market/            # Mercado
    ├── profile/           # Perfil y ajustes
    └── ...                # Inicio de sesión, registro y demás pantallas
```

## Limitaciones conocidas
- El pago del mercado es una simulación, no hay cobro real.
- La app está pensada para teléfonos y se bloquea en orientación vertical.
- La clave de Gemini se compila dentro de la app, por lo que este proyecto no debe publicarse como producto sin moverla a un backend.

