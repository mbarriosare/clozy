package com.clozy.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.clozy.app.ui.bienvenida.BienvenidaScreen
import com.clozy.app.ui.components.ClozyIcons
import com.clozy.app.ui.components.ClozyMainScaffold
import com.clozy.app.ui.google.GoogleCancelledScreen
import com.clozy.app.ui.google.GoogleErrorScreen
import com.clozy.app.ui.home.HomeScreen
import com.clozy.app.ui.profile.ChangePasswordScreen
import com.clozy.app.ui.profile.EditProfileScreen
import com.clozy.app.ui.profile.MockProfile
import com.clozy.app.ui.profile.ProfileScreen
import com.clozy.app.ui.profile.NotificationSettings
import com.clozy.app.ui.profile.NotificationsScreen
import com.clozy.app.ui.profile.EditStyleScreen
import com.clozy.app.ui.profile.FavoriteColorOptions
import com.clozy.app.ui.profile.PersonalStyleOptions
import com.clozy.app.ui.profile.PhoneVerifyScreen
import com.clozy.app.ui.profile.PrivacyScreen
import com.clozy.app.ui.profile.SettingsScreen
import com.clozy.app.ui.profile.StylePreferences
import com.clozy.app.ui.profile.TermsScreen
import com.clozy.app.ui.profile.ProfileVisibility
import com.clozy.app.ui.profile.UserProfile
import com.clozy.app.ui.wardrobe.AddGarmentChoiceScreen
import com.clozy.app.ui.wardrobe.DeleteGarmentDialog
import com.clozy.app.ui.wardrobe.FavoritesScreen
import com.clozy.app.ui.wardrobe.GarmentDetailScreen
import com.clozy.app.ui.wardrobe.Garment
import com.clozy.app.ui.wardrobe.GarmentCategory
import com.clozy.app.ui.wardrobe.ManualGarmentFormScreen
import com.clozy.app.ui.wardrobe.WardrobeScreen
import com.clozy.app.ui.outfits.CreateOutfitScreen
import com.clozy.app.ui.outfits.Outfit
import com.clozy.app.ui.outfits.OutfitDetailScreen
import com.clozy.app.ui.outfits.OutfitsScreen
import com.clozy.app.data.OutfitRepository
import com.clozy.app.ui.login.EmailVerificationScreen
import com.clozy.app.ui.login.LoginScreen
import com.clozy.app.ui.recover.CheckEmailScreen
import com.clozy.app.ui.recover.ForgotPasswordScreen
import com.clozy.app.ui.register.RegisterErrors
import com.clozy.app.ui.register.RegisterScreen
import com.clozy.app.ui.status.FlowLoadingScreen
import com.clozy.app.ui.status.NoConnectionScreen
import com.clozy.app.ui.status.ServerErrorScreen
import com.clozy.app.ui.status.SuccessScreen
import com.clozy.app.ui.theme.ClozyTheme
import com.clozy.app.ui.welcome.WelcomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.clozy.app.data.AuthRepository
import com.clozy.app.data.ProfileRepository
import com.clozy.app.data.PhotoRepository
import com.clozy.app.data.WardrobeRepository
import com.google.firebase.FirebaseNetworkException


enum class Route {
    Bienvenida, Welcome, Home, Login, Register, CreatingAccount, AccountCreated,
    ForgotPassword, SendingLink, CheckEmail,
    LoggingIn, ServerError, NoConnection,
    GoogleConnecting, GoogleCancelled, GoogleError, EmailVerification,
    PhoneVerify, PhoneVerified,
    Wardrobe, AddGarmentChoice, AddGarmentManual, GarmentAdded,
    GarmentDetail, EditGarment, GarmentUpdated, GarmentDeleted,
    Profile, EditProfile, ChangePassword, ProfileUpdated, PasswordUpdatedProfile,
    Privacy, Notifications, Settings, Terms, EditStyle, StyleUpdated,
    Outfits, CreateOutfit, OutfitGenerating, OutfitDetail,
    Favorites,
}



private fun routeForNav(nav: String, current: Route): Route = when (nav) {
    "home" -> Route.Home
    "wardrobe" -> Route.Wardrobe
    "profile" -> Route.Profile
    "outfits" -> Route.Outfits
    else -> current
}

private fun pickOutfitGarments(garments: List<com.clozy.app.ui.wardrobe.Garment>, style: String): List<Int> {
    fun pick(category: com.clozy.app.ui.wardrobe.GarmentCategory) =
        garments.filter { it.category == category }.let { pool ->
            pool.firstOrNull { it.style?.label == style } ?: pool.randomOrNull()
        }
    val top = pick(com.clozy.app.ui.wardrobe.GarmentCategory.Top)
    val bottom = pick(com.clozy.app.ui.wardrobe.GarmentCategory.Bottom)
    val dress = pick(com.clozy.app.ui.wardrobe.GarmentCategory.Dress)
    val shoes = pick(com.clozy.app.ui.wardrobe.GarmentCategory.Shoes)
    val accessory = pick(com.clozy.app.ui.wardrobe.GarmentCategory.Accessory)
    val base = if (dress != null && (top == null || bottom == null)) listOf(dress) else listOfNotNull(top, bottom)
    if (base.isEmpty()) return emptyList()
    return (base + listOfNotNull(shoes, accessory)).map { it.id }
}

private fun outfitName(garments: List<com.clozy.app.ui.wardrobe.Garment>, style: String): String {
    val accessory = garments.firstOrNull { it.category == com.clozy.app.ui.wardrobe.GarmentCategory.Accessory }
    return if (accessory != null) "Look con ${accessory.name}" else "Look $style"
}

sealed class GoogleSignInOutcome {
    object Cancelled : GoogleSignInOutcome()
    data class Error(val message: String) : GoogleSignInOutcome()
    data class TokenReceived(val idToken: String) : GoogleSignInOutcome()
}

sealed class PhoneAuthOutcome {
    data class CodeSent(val verificationId: String) : PhoneAuthOutcome()
    data class AutoVerified(val credential: com.google.firebase.auth.PhoneAuthCredential) : PhoneAuthOutcome()
    data class Failed(val message: String) : PhoneAuthOutcome()
}

class MainActivity : ComponentActivity() {

    private var googleResult by mutableStateOf<GoogleSignInOutcome?>(null)
    private lateinit var googleSignInClient: com.google.android.gms.auth.api.signin.GoogleSignInClient
    private lateinit var googleLauncher: androidx.activity.result.ActivityResultLauncher<android.content.Intent>

    private var phoneAuthResult by mutableStateOf<PhoneAuthOutcome?>(null)

    private fun launchGoogleSignIn() {
        googleResult = null
        googleSignInClient.signOut().addOnCompleteListener {
            googleLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun startPhoneVerification(fullNumber: String) {
        phoneAuthResult = null
        val options = com.google.firebase.auth.PhoneAuthOptions.newBuilder(com.google.firebase.auth.FirebaseAuth.getInstance())
            .setPhoneNumber(fullNumber)
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                    phoneAuthResult = PhoneAuthOutcome.AutoVerified(credential)
                }

                override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                    android.util.Log.e("ClozyPhoneAuth", "onVerificationFailed", e)
                    phoneAuthResult = PhoneAuthOutcome.Failed(e.localizedMessage ?: "No pudimos enviar el código.")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken,
                ) {
                    phoneAuthResult = PhoneAuthOutcome.CodeSent(verificationId)
                }
            })
            .build()
        com.google.firebase.auth.PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private var pendingCameraUri: android.net.Uri? = null
    var pickedImageUri by mutableStateOf<android.net.Uri?>(null)
        private set

    private lateinit var cameraLauncher: androidx.activity.result.ActivityResultLauncher<android.net.Uri>
    private lateinit var galleryLauncher: androidx.activity.result.ActivityResultLauncher<androidx.activity.result.PickVisualMediaRequest>
    private lateinit var cameraPermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>

    fun clearPickedImage() {
        pickedImageUri = null
    }

    fun launchGallery() {
        galleryLauncher.launch(
            androidx.activity.result.PickVisualMediaRequest(androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly),
        )
    }

    fun launchCamera() {
        val hasPermission = androidx.core.content.ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CAMERA,
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            val photoFile = java.io.File.createTempFile(
                "clozy_photo_", ".jpg",
                java.io.File(cacheDir, "camera_photos").apply { mkdirs() },
            )
            val uri = androidx.core.content.FileProvider.getUriForFile(this, "com.clozy.app.fileprovider", photoFile)
            pendingCameraUri = uri
            cameraLauncher.launch(uri)
        } else {
            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = com.google.android.gms.auth.api.signin.GoogleSignInOptions
            .Builder(com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso)

        googleLauncher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
        ) { activityResult ->
            val task = com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            try {
                val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
                val idToken = account.idToken
                googleResult = if (idToken != null) {
                    GoogleSignInOutcome.TokenReceived(idToken)
                } else {
                    GoogleSignInOutcome.Error("No se pudo obtener el token de Google.")
                }
            } catch (e: com.google.android.gms.common.api.ApiException) {
                android.util.Log.e("ClozyGoogleSignIn", "statusCode=${e.statusCode} message=${e.message}", e)
                googleResult = if (e.statusCode == com.google.android.gms.common.api.CommonStatusCodes.CANCELED) {
                    GoogleSignInOutcome.Cancelled
                } else {
                    GoogleSignInOutcome.Error(e.localizedMessage ?: "Error al conectar con Google.")
                }
            }
        }

        cameraLauncher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.TakePicture(),
        ) { success ->
            if (success) pickedImageUri = pendingCameraUri
        }

        galleryLauncher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia(),
        ) { uri ->
            if (uri != null) pickedImageUri = uri
        }

        cameraPermissionLauncher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.RequestPermission(),
        ) { granted ->
            if (granted) launchCamera()
        }

        setContent {
            ClozyTheme {
                var route by rememberSaveable { mutableStateOf(Route.Bienvenida) }
                var forgotError by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                var loginError by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                var loginEmail by androidx.compose.runtime.remember { mutableStateOf("") }
                var verifyChecking by androidx.compose.runtime.remember { mutableStateOf(false) }
                var verifyError by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                var verifyResendMessage by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                var selectedNav by androidx.compose.runtime.remember { mutableStateOf("home") }
                var garments by androidx.compose.runtime.remember { mutableStateOf(emptyList<Garment>()) }
                var outfits by androidx.compose.runtime.remember { mutableStateOf(emptyList<Outfit>()) }
                var selectedOutfit by androidx.compose.runtime.remember { mutableStateOf<Outfit?>(null) }
                var pendingOutfitOccasion by androidx.compose.runtime.remember { mutableStateOf("") }
                var pendingOutfitStyle by androidx.compose.runtime.remember { mutableStateOf("") }
                var outfitError by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                var wardrobeQuery by androidx.compose.runtime.remember { mutableStateOf("") }
                var wardrobeCategory by androidx.compose.runtime.remember { mutableStateOf<GarmentCategory?>(null) }
                var selectedGarment by androidx.compose.runtime.remember { mutableStateOf<Garment?>(null) }
                var garmentOrigin by androidx.compose.runtime.remember { mutableStateOf(Route.Wardrobe) }
                var showDeleteDialog by androidx.compose.runtime.remember { mutableStateOf(false) }
                var userProfile by androidx.compose.runtime.remember {
                    mutableStateOf(
                        MockProfile.copy(
                            name = "", username = "", email = "", city = "", phone = "",
                            itemsCount = 0, looksCount = 0, favoritesCount = 0,
                            styleTags = PersonalStyleOptions.map { it to false }, favoriteColors = emptyList(),
                        ),
                    )
                }
                var pwCurrentError by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                var pwNewError by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                var pwConfirmError by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                var profileVisibility by androidx.compose.runtime.remember { mutableStateOf(ProfileVisibility.Public) }
                var notificationSettings by androidx.compose.runtime.remember { mutableStateOf(NotificationSettings()) }
                var showDeleteAccountDialog by androidx.compose.runtime.remember { mutableStateOf(false) }
                var pendingPhoneNumber by androidx.compose.runtime.remember { mutableStateOf("") }
                var phoneVerificationId by androidx.compose.runtime.remember { mutableStateOf("") }
                var phoneCode by androidx.compose.runtime.remember { mutableStateOf("") }
                var phoneVerifying by androidx.compose.runtime.remember { mutableStateOf(false) }
                var phoneError by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                var verifiedPhoneDisplay by androidx.compose.runtime.remember {
                    mutableStateOf(AuthRepository.currentUser?.phoneNumber)
                }
                var isDeletingAccount by androidx.compose.runtime.remember { mutableStateOf(false) }
                var deleteAccountError by androidx.compose.runtime.remember { mutableStateOf<String?>(null) }
                var registerErrors by androidx.compose.runtime.remember { mutableStateOf(RegisterErrors()) }
                var pendingName by androidx.compose.runtime.remember { mutableStateOf("") }
                var pendingEmail by androidx.compose.runtime.remember { mutableStateOf("") }
                var pendingPassword by androidx.compose.runtime.remember { mutableStateOf("") }
                var forgotEmail by androidx.compose.runtime.remember { mutableStateOf("") }
                val scope = rememberCoroutineScope()
                BackHandler(enabled = route != Route.Bienvenida && route != Route.CreatingAccount) {
                    route = if (route == Route.Welcome) Route.Bienvenida else Route.Welcome
                }

                when (route) {
                    Route.Bienvenida -> BienvenidaScreen(
                        onStartClick = { route = Route.Welcome },
                        onHaveAccountClick = { route = Route.Login },
                    )
                    Route.Welcome -> WelcomeScreen(
                        onLoginClick = { route = Route.Login },
                        onSignUpClick = { route = Route.Register },
                    )
                    Route.Login -> LoginScreen(
                        errorMessage = loginError,
                        onLoginClick = { email, pass ->
                            loginError = when {
                                email.isBlank() && pass.isBlank() -> "Ingresa tu correo y contraseña."
                                email.isBlank() -> "Ingresa tu correo."
                                pass.isBlank() -> "Ingresa tu contraseña."
                                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo no válido."
                                else -> null
                            }
                            if (loginError == null) {
                                loginEmail = email
                                pendingPassword = pass
                                route = Route.LoggingIn
                            }
                        },

                        onForgotPasswordClick = { route = Route.ForgotPassword },
                        onGoogleClick = {
                            route = Route.GoogleConnecting
                            launchGoogleSignIn()
                        },
                        onCreateAccountClick = { route = Route.Register },
                    )
                    Route.Register -> RegisterScreen(
                        onBackClick = { route = Route.Welcome },
                        onLoginClick = { route = Route.Login },
                        errors = registerErrors,
                        onRegisterClick = { name, email, pass, confirm, terms ->
                            val errs = RegisterErrors(
                                name = if (name.isBlank()) "Escribe tu nombre." else null,
                                email = when {
                                    email.isBlank() -> "Escribe tu correo."
                                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo no válido."
                                    else -> null
                                },
                                password = when {
                                    pass.isBlank() -> "Escribe una contraseña."
                                    pass.length < 8 -> "Mínimo 8 caracteres."
                                    else -> null
                                },
                                confirm = when {
                                    confirm.isBlank() -> "Confirma tu contraseña."
                                    confirm != pass -> "Las contraseñas no coinciden."
                                    else -> null
                                },
                                terms = if (!terms) "Debes aceptar los términos." else null,
                            )
                            registerErrors = errs
                            if (errs == RegisterErrors()) {
                                pendingName = name
                                pendingEmail = email
                                pendingPassword = pass
                                route = Route.CreatingAccount
                            }
                        },
                    )
                    Route.CreatingAccount -> {
                        LaunchedEffect(pendingEmail) {
                            try {
                                val user = AuthRepository.register(pendingName, pendingEmail, pendingPassword)
                                garments = emptyList()
                                outfits = emptyList()
                                val defaultUsername = "@" + pendingEmail.substringBefore("@")
                                ProfileRepository.save(user.uid, pendingName, defaultUsername, "")
                                userProfile = userProfile.copy(
                                    name = pendingName, username = defaultUsername,
                                    email = pendingEmail, city = "", phone = "",
                                    styleTags = PersonalStyleOptions.map { it to false },
                                    favoriteColors = emptyList(),
                                    topSize = "", bottomSize = "", shoeSize = "",
                                )
                                AuthRepository.sendVerificationEmail()
                                verifyError = null
                                verifyResendMessage = null
                                route = Route.EmailVerification
                            } catch (e: FirebaseNetworkException) {
                                route = Route.NoConnection
                            } catch (e: Exception) {
                                registerErrors = RegisterErrors(email = AuthRepository.friendlyError(e))
                                route = Route.Register
                            }
                        }
                        FlowLoadingScreen(
                            icon = ClozyIcons.PersonAdd,
                            title = "Creando tu cuenta",
                            subtitle = "Por favor espera un momento...",
                        )
                    }
                    Route.LoggingIn -> {
                        LaunchedEffect(loginEmail) {
                            try {
                                val user = AuthRepository.login(loginEmail, pendingPassword)
                                if (!user.isEmailVerified) {
                                    verifyError = null
                                    verifyResendMessage = null
                                    route = Route.EmailVerification
                                } else {
                                    garments = WardrobeRepository.fetchAll(user.uid)
                                    outfits = OutfitRepository.fetchAll(user.uid)
                                    val stored = ProfileRepository.fetch(user.uid)
                                    val defaultUsername = "@" + loginEmail.substringBefore("@")
                                    userProfile = userProfile.copy(
                                        name = stored?.name ?: (user.displayName ?: ""),
                                        username = stored?.username ?: defaultUsername,
                                        email = loginEmail,
                                        city = stored?.city ?: "", phone = stored?.phone ?: "",
                                        styleTags = PersonalStyleOptions.map { it to (stored?.styles?.contains(it) == true) },
                                        favoriteColors = FavoriteColorOptions.filter { stored?.colors?.contains(it.label) == true }.map { it.color },
                                        topSize = stored?.topSize ?: "",
                                        bottomSize = stored?.bottomSize ?: "",
                                        shoeSize = stored?.shoeSize ?: "",
                                    )
                                    verifiedPhoneDisplay = user.phoneNumber
                                    route = Route.Home
                                }
                            } catch (e: FirebaseNetworkException) {
                                route = Route.NoConnection
                            } catch (e: Exception) {
                                loginError = AuthRepository.friendlyError(e)
                                route = Route.Login
                            }
                        }
                        FlowLoadingScreen(
                            icon = ClozyIcons.Lock,
                            title = "Iniciando sesión",
                            subtitle = "Por favor espera un momento...",
                        )
                    }
                    Route.EmailVerification -> EmailVerificationScreen(
                        email = AuthRepository.currentUser?.email ?: loginEmail.ifBlank { pendingEmail },
                        isChecking = verifyChecking,
                        errorMessage = verifyError,
                        resendMessage = verifyResendMessage,
                        onResendClick = {
                            verifyError = null
                            verifyResendMessage = null
                            scope.launch {
                                try {
                                    AuthRepository.sendVerificationEmail()
                                    verifyResendMessage = "Te reenviamos el correo de confirmación."
                                } catch (e: Exception) {
                                    verifyError = AuthRepository.friendlyError(e)
                                }
                            }
                        },
                        onLogoutClick = {
                            AuthRepository.logout()
                            garments = emptyList()
                                outfits = emptyList()
                            verifiedPhoneDisplay = null
                            selectedNav = "home"
                            route = Route.Login
                        },
                        onCheckAgainClick = {
                            verifyError = null
                            verifyResendMessage = null
                            verifyChecking = true
                            scope.launch {
                                try {
                                    val verified = AuthRepository.isEmailVerified()
                                    verifyChecking = false
                                    if (!verified) {
                                        verifyError = "Todavía no confirmamos tu correo. Revisa tu bandeja de entrada."
                                    } else {
                                        val user = AuthRepository.currentUser
                                        if (user == null) {
                                            route = Route.Login
                                        } else {
                                            garments = WardrobeRepository.fetchAll(user.uid)
                                    outfits = OutfitRepository.fetchAll(user.uid)
                                            val stored = ProfileRepository.fetch(user.uid)
                                            val defaultUsername = "@" + (user.email ?: "").substringBefore("@")
                                            userProfile = userProfile.copy(
                                                name = stored?.name ?: (user.displayName ?: ""),
                                                username = stored?.username ?: defaultUsername,
                                                email = user.email ?: "",
                                                city = stored?.city ?: "", phone = stored?.phone ?: "",
                                                styleTags = PersonalStyleOptions.map { it to (stored?.styles?.contains(it) == true) },
                                                favoriteColors = FavoriteColorOptions.filter { stored?.colors?.contains(it.label) == true }.map { it.color },
                                                topSize = stored?.topSize ?: "",
                                                bottomSize = stored?.bottomSize ?: "",
                                                shoeSize = stored?.shoeSize ?: "",
                                            )
                                            verifiedPhoneDisplay = user.phoneNumber
                                            route = Route.Home
                                        }
                                    }
                                } catch (e: FirebaseNetworkException) {
                                    verifyChecking = false
                                    route = Route.NoConnection
                                } catch (e: Exception) {
                                    verifyChecking = false
                                    verifyError = AuthRepository.friendlyError(e)
                                }
                            }
                        },
                    )
                    Route.GoogleConnecting -> {
                        LaunchedEffect(googleResult) {
                            when (val result = googleResult) {
                                null -> {}
                                is GoogleSignInOutcome.Cancelled -> {
                                    route = Route.GoogleCancelled
                                }
                                is GoogleSignInOutcome.Error -> {
                                    route = Route.GoogleError
                                }
                                is GoogleSignInOutcome.TokenReceived -> {
                                    try {
                                        val user = AuthRepository.signInWithGoogleIdToken(result.idToken)
                                        garments = WardrobeRepository.fetchAll(user.uid)
                                    outfits = OutfitRepository.fetchAll(user.uid)
                                        val stored = ProfileRepository.fetch(user.uid)
                                        val defaultUsername = "@" + (user.email ?: "usuario").substringBefore("@")
                                        if (stored == null) {
                                            val name = user.displayName ?: "Usuario Clozy"
                                            ProfileRepository.save(user.uid, name, defaultUsername, "")
                                            userProfile = userProfile.copy(
                                                name = name, username = defaultUsername,
                                                email = user.email ?: "", city = "", phone = "",
                                                styleTags = PersonalStyleOptions.map { it to false },
                                                favoriteColors = emptyList(),
                                                topSize = "", bottomSize = "", shoeSize = "",
                                            )
                                        } else {
                                            userProfile = userProfile.copy(
                                                name = stored.name, username = stored.username,
                                                email = user.email ?: "", city = stored.city,
                                                styleTags = PersonalStyleOptions.map { it to (it in stored.styles) },
                                                favoriteColors = FavoriteColorOptions.filter { it.label in stored.colors }.map { it.color },
                                                topSize = stored.topSize, bottomSize = stored.bottomSize, shoeSize = stored.shoeSize,
                                            )
                                        }
                                        verifiedPhoneDisplay = user.phoneNumber
                                        route = Route.Home
                                    } catch (e: FirebaseNetworkException) {
                                        android.util.Log.e("ClozyGoogleSignIn", "Sin conexión al validar con Firebase", e)
                                        route = Route.NoConnection
                                    } catch (e: Exception) {
                                        android.util.Log.e("ClozyGoogleSignIn", "Firebase rechazó el token de Google: ${e.message}", e)
                                        route = Route.GoogleError
                                    }
                                }
                            }
                        }
                        FlowLoadingScreen(
                            icon = ClozyIcons.Lock,
                            title = "Conectando con Google",
                            subtitle = "Por favor espera un momento...",
                            iconContent = {
                                androidx.compose.foundation.Image(
                                    androidx.compose.ui.res.painterResource(R.drawable.google_g), null,
                                    androidx.compose.ui.Modifier.size(22.dp),
                                )
                            },
                        )
                    }
                    Route.GoogleCancelled -> GoogleCancelledScreen(
                        onRetryClick = {
                            route = Route.GoogleConnecting
                            launchGoogleSignIn()
                        },
                        onOtherWayClick = { route = Route.Login },
                    )
                    Route.GoogleError -> GoogleErrorScreen(
                        onRetryClick = {
                            route = Route.GoogleConnecting
                            launchGoogleSignIn()
                        },
                        onOtherWayClick = { route = Route.Login },
                    )
                    Route.ServerError -> ServerErrorScreen(
                        onRetryClick = { route = Route.LoggingIn },
                        onBackClick = { route = Route.Login },
                    )
                    Route.NoConnection -> NoConnectionScreen(
                        onRetryClick = { route = Route.LoggingIn },
                        onBackClick = { route = Route.Login },
                    )
                    Route.ForgotPassword -> ForgotPasswordScreen(
                        onBackClick = { route = Route.Login },
                        onBackToLoginClick = { route = Route.Login },
                        errorMessage = forgotError,
                        onSendLinkClick = { email ->
                            forgotError = when {
                                email.isBlank() -> "Escribe tu correo."
                                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Correo no válido."
                                else -> null
                            }
                            if (forgotError == null) {
                                forgotEmail = email
                                route = Route.SendingLink
                            }
                        },
                    )
                    Route.SendingLink -> {
                        LaunchedEffect(forgotEmail) {
                            try {
                                AuthRepository.sendPasswordReset(forgotEmail)
                                route = Route.CheckEmail
                            } catch (e: FirebaseNetworkException) {
                                route = Route.NoConnection
                            } catch (e: Exception) {
                                forgotError = AuthRepository.friendlyError(e)
                                route = Route.ForgotPassword
                            }
                        }
                        FlowLoadingScreen(
                            icon = ClozyIcons.Mail,
                            title = "Enviando enlace",
                            subtitle = "Por favor espera un momento...",
                        )
                    }
                    Route.CheckEmail -> CheckEmailScreen(
                        onBackToLoginClick = { route = Route.Login },
                    )
                    Route.AccountCreated -> SuccessScreen(
                        title = "¡Cuenta creada!",
                        subtitle = "Tu cuenta se creó correctamente.",
                        buttonText = "Continuar",
                        onContinue = { route = Route.Home },
                    )
                    Route.Home -> HomeScreen(
                        userName = userProfile.name.ifBlank { userProfile.username.removePrefix("@") },
                        itemsCount = garments.size,
                        looksCount = 0,
                        favoritesCount = garments.count { it.isFavorite },
                        selectedNav = selectedNav,
                        onNavClick = { nav ->
                            selectedNav = nav
                            route = routeForNav(nav, route)
                        },
                        onNotificationsClick = {},
                        onWardrobeClick = { selectedNav = "wardrobe"; route = Route.Wardrobe },
                        onOutfitsClick = { selectedNav = "outfits"; route = Route.Outfits },
                        onAiClick = {},
                        onCreateLookClick = {},
                    )
                    Route.Profile -> ClozyMainScaffold(
                        selectedNav = selectedNav,
                        onNavClick = { nav ->
                            selectedNav = nav
                            route = routeForNav(nav, route)
                        },
                    ) {
                        ProfileScreen(
                            profile = userProfile.copy(
                                itemsCount = garments.size,
                                favoritesCount = garments.count { it.isFavorite },
                            ),
                            onSettingsClick = { route = Route.Settings },
                            onEditProfileClick = { route = Route.EditProfile },
                            onEditStyleClick = { route = Route.EditStyle },
                            onSeeAllFavoritesClick = { route = Route.Favorites },
                            favorites = garments.filter { it.isFavorite },
                            onFavoriteClick = { g ->
                                selectedGarment = g
                                garmentOrigin = Route.Profile
                                route = Route.GarmentDetail
                            },
                            onNotificationsClick = { route = Route.Notifications },
                            onPrivacyClick = { route = Route.Privacy },
                            onSecurityClick = { route = Route.ChangePassword },
                            onTermsClick = { route = Route.Terms },
                            onLogoutClick = {
                                AuthRepository.logout()
                                garments = emptyList()
                                outfits = emptyList()
                                verifiedPhoneDisplay = null
                                selectedNav = "home"
                                route = Route.Bienvenida
                            },
                        )
                    }
                    Route.EditProfile -> EditProfileScreen(
                        profile = userProfile,
                        onBackClick = { route = Route.Profile },
                        onSaveClick = { name, username, city, phone ->
                            userProfile = userProfile.copy(name = name, username = username, city = city, phone = phone)
                            route = Route.ProfileUpdated
                            AuthRepository.currentUser?.let { u ->
                                scope.launch { ProfileRepository.save(u.uid, name, username, city, phone) }
                            }
                        },
                    )
                    Route.ProfileUpdated -> SuccessScreen(
                        title = "¡Perfil actualizado!",
                        subtitle = "Tus cambios se guardaron correctamente.",
                        buttonText = "Ver perfil",
                        onContinue = { route = Route.Profile },
                    )
                    Route.PhoneVerify -> {
                        LaunchedEffect(phoneAuthResult) {
                            when (val result = phoneAuthResult) {
                                null -> {}
                                is PhoneAuthOutcome.Failed -> {
                                    phoneError = result.message
                                }
                                is PhoneAuthOutcome.CodeSent -> {
                                    phoneVerificationId = result.verificationId
                                    phoneError = null
                                }
                                is PhoneAuthOutcome.AutoVerified -> {
                                    phoneVerifying = true
                                    try {
                                        val user = AuthRepository.linkPhoneCredential(result.credential)
                                        verifiedPhoneDisplay = user.phoneNumber
                                        phoneVerifying = false
                                        route = Route.PhoneVerified
                                    } catch (e: Exception) {
                                        phoneVerifying = false
                                        phoneError = AuthRepository.friendlyPhoneError(e)
                                    }
                                }
                            }
                        }
                        PhoneVerifyScreen(
                            phoneNumber = pendingPhoneNumber,
                            code = phoneCode,
                            onCodeChange = { phoneCode = it },
                            isVerifying = phoneVerifying,
                            errorMessage = phoneError,
                            onBackClick = { route = Route.EditProfile },
                            onResendClick = { startPhoneVerification(pendingPhoneNumber) },
                            onVerifyClick = {
                                if (phoneCode.length != 6) {
                                    phoneError = "Ingresa el código de 6 dígitos."
                                } else if (phoneVerificationId.isBlank()) {
                                    phoneError = "Todavía estamos enviando el código. Espera un momento."
                                } else {
                                    phoneVerifying = true
                                    phoneError = null
                                    scope.launch {
                                        try {
                                            val credential = com.google.firebase.auth.PhoneAuthProvider.getCredential(phoneVerificationId, phoneCode)
                                            val user = AuthRepository.linkPhoneCredential(credential)
                                            verifiedPhoneDisplay = user.phoneNumber
                                            phoneVerifying = false
                                            route = Route.PhoneVerified
                                        } catch (e: Exception) {
                                            phoneVerifying = false
                                            phoneError = AuthRepository.friendlyPhoneError(e)
                                        }
                                    }
                                }
                            },
                        )
                    }
                    Route.PhoneVerified -> SuccessScreen(
                        title = "¡Teléfono verificado!",
                        subtitle = "Tu número quedó asociado a tu cuenta.",
                        buttonText = "Ver perfil",
                        onContinue = { route = Route.EditProfile },
                    )
                    Route.ChangePassword -> ChangePasswordScreen(
                        onBackClick = { route = Route.Profile },
                        currentError = pwCurrentError,
                        newError = pwNewError,
                        confirmError = pwConfirmError,
                        onUpdateClick = { curr, new, confirm ->
                            pwCurrentError = if (curr.isBlank()) "Ingresa tu contraseña actual." else null
                            pwNewError = if (new.length < 8) "Mínimo 8 caracteres." else null
                            pwConfirmError = if (confirm != new) "Las contraseñas no coinciden." else null
                            if (pwCurrentError == null && pwNewError == null && pwConfirmError == null) {
                                route = Route.PasswordUpdatedProfile
                            }
                        },
                    )
                    Route.PasswordUpdatedProfile -> SuccessScreen(
                        title = "¡Contraseña actualizada!",
                        subtitle = "Tu contraseña se cambió correctamente.",
                        buttonText = "Ver perfil",
                        onContinue = { route = Route.Profile },
                    )
                    Route.Privacy -> PrivacyScreen(
                        selected = profileVisibility,
                        onSelect = { profileVisibility = it },
                        onBackClick = { route = Route.Profile },
                    )
                    Route.Notifications -> NotificationsScreen(
                        settings = notificationSettings,
                        onSettingsChange = { notificationSettings = it },
                        onBackClick = { route = Route.Profile },
                    )
                    Route.Terms -> TermsScreen(onBackClick = { route = Route.Profile })
                    Route.EditStyle -> EditStyleScreen(
                        initial = StylePreferences(
                            styles = userProfile.styleTags.filter { it.second }.map { it.first }.toSet(),
                            colors = FavoriteColorOptions.filter { it.color in userProfile.favoriteColors }.map { it.label }.toSet(),
                            topSize = userProfile.topSize,
                            bottomSize = userProfile.bottomSize,
                            shoeSize = userProfile.shoeSize,
                        ),
                        onBackClick = { route = Route.Profile },
                        onSaveClick = { prefs ->
                            userProfile = userProfile.copy(
                                styleTags = PersonalStyleOptions.map { it to (it in prefs.styles) },
                                favoriteColors = FavoriteColorOptions.filter { it.label in prefs.colors }.map { it.color },
                                topSize = prefs.topSize, bottomSize = prefs.bottomSize, shoeSize = prefs.shoeSize,
                            )
                            route = Route.StyleUpdated
                            AuthRepository.currentUser?.let { u ->
                                scope.launch {
                                    ProfileRepository.saveStyle(
                                        u.uid, prefs.styles.toList(), prefs.colors.toList(),
                                        prefs.topSize, prefs.bottomSize, prefs.shoeSize,
                                    )
                                }
                            }
                        },
                    )
                    Route.StyleUpdated -> SuccessScreen(
                        title = "¡Estilo actualizado!",
                        subtitle = "Tus preferencias se guardaron correctamente.",
                        buttonText = "Ver perfil",
                        onContinue = { route = Route.Profile },
                    )
                    Route.Settings -> SettingsScreen(
                        onBackClick = { route = Route.Profile },
                        onHelpClick = {},
                        onAboutClick = {},
                        deleteError = deleteAccountError,
                        isDeleting = isDeletingAccount,
                        showDeleteDialog = showDeleteAccountDialog,
                        onDeleteAccountClick = {
                            deleteAccountError = null
                            showDeleteAccountDialog = true
                        },
                        onDismissDeleteDialog = { showDeleteAccountDialog = false },
                        onConfirmDelete = {
                            val uid = AuthRepository.currentUser?.uid
                            if (uid == null) {
                                deleteAccountError = "No hay una sesión activa."
                            } else {
                                isDeletingAccount = true
                                scope.launch {
                                    try {
                                        WardrobeRepository.deleteAll(uid)
                                        ProfileRepository.deleteDoc(uid)
                                        AuthRepository.deleteAccount()
                                        isDeletingAccount = false
                                        showDeleteAccountDialog = false
                                        garments = emptyList()
                                outfits = emptyList()
                                        userProfile = userProfile.copy(name = "", username = "", email = "", city = "")
                                        selectedNav = "home"
                                        route = Route.Bienvenida
                                    } catch (e: Exception) {
                                        isDeletingAccount = false
                                        deleteAccountError = AuthRepository.friendlyError(e)
                                    }
                                }
                            }
                        },
                    )
                    Route.Wardrobe -> ClozyMainScaffold(
                        selectedNav = selectedNav,
                        onNavClick = { nav ->
                            selectedNav = nav
                            route = routeForNav(nav, route)
                        },
                    ) {
                        WardrobeScreen(
                            garments = garments,
                            searchQuery = wardrobeQuery,
                            onSearchQueryChange = { wardrobeQuery = it },
                            selectedCategory = wardrobeCategory,
                            onCategoryClick = { wardrobeCategory = it },
                            onAddClick = { route = Route.AddGarmentChoice },
                            onGarmentClick = { garment ->
                                selectedGarment = garment
                                garmentOrigin = Route.Wardrobe
                                route = Route.GarmentDetail
                            },
                        )
                    }
                    Route.Favorites -> ClozyMainScaffold(
                        selectedNav = "profile",
                        onNavClick = { nav ->
                            selectedNav = nav
                            route = routeForNav(nav, route)
                        },
                    ) {
                        FavoritesScreen(
                            garments = garments,
                            onBackClick = { route = Route.Profile },
                            onGarmentClick = { garment ->
                                selectedGarment = garment
                                garmentOrigin = Route.Favorites
                                route = Route.GarmentDetail
                            },
                        )
                    }
                    Route.Outfits -> ClozyMainScaffold(
                        selectedNav = selectedNav,
                        onNavClick = { nav ->
                            selectedNav = nav
                            route = routeForNav(nav, route)
                        },
                    ) {
                        OutfitsScreen(
                            outfits = outfits,
                            garments = garments,
                            onCreateClick = { outfitError = null; route = Route.CreateOutfit },
                            onOutfitClick = { outfit ->
                                selectedOutfit = outfit
                                route = Route.OutfitDetail
                            },
                        )
                    }
                    Route.CreateOutfit -> CreateOutfitScreen(
                        onBackClick = { route = Route.Outfits },
                        errorMessage = outfitError,
                        onCreateClick = { occasion, style ->
                            pendingOutfitOccasion = occasion
                            pendingOutfitStyle = style
                            outfitError = null
                            route = Route.OutfitGenerating
                        },
                    )
                    Route.OutfitGenerating -> {
                        LaunchedEffect(pendingOutfitOccasion, pendingOutfitStyle) {
                            delay(900)
                            val pieceIds = pickOutfitGarments(garments, pendingOutfitStyle)
                            if (pieceIds.size < 2) {
                                outfitError = "No tienes suficientes prendas de este estilo para armar un outfit. Agrega más prendas a tu armario."
                                route = Route.CreateOutfit
                            } else {
                                val pieces = pieceIds.mapNotNull { id -> garments.find { it.id == id } }
                                val newOutfit = Outfit(
                                    id = (0..999999).random(),
                                    name = outfitName(pieces, pendingOutfitStyle),
                                    occasion = pendingOutfitOccasion,
                                    style = pendingOutfitStyle,
                                    garmentIds = pieceIds,
                                    createdBy = "Clozy AI",
                                )
                                AuthRepository.currentUser?.let { u ->
                                    try {
                                        OutfitRepository.add(u.uid, newOutfit)
                                        outfits = outfits + newOutfit
                                        selectedOutfit = newOutfit
                                        route = Route.OutfitDetail
                                    } catch (e: Exception) {
                                        outfitError = "No pudimos guardar el outfit. Inténtalo de nuevo."
                                        route = Route.CreateOutfit
                                    }
                                }
                            }
                        }
                        FlowLoadingScreen(
                            icon = ClozyIcons.PersonAdd,
                            title = "Creando tu outfit",
                            subtitle = "Clozy AI está eligiendo tus prendas...",
                        )
                    }
                    Route.OutfitDetail -> {
                        val outfit = selectedOutfit
                        if (outfit == null) {
                            route = Route.Outfits
                        } else {
                            OutfitDetailScreen(
                                outfit = outfit,
                                garments = garments,
                                onBackClick = { route = Route.Outfits },
                                onToggleFavorite = {
                                    val updated = outfit.copy(isFavorite = !outfit.isFavorite)
                                    outfits = outfits.map { if (it.id == updated.id) updated else it }
                                    selectedOutfit = updated
                                    AuthRepository.currentUser?.let { u ->
                                        scope.launch { OutfitRepository.update(u.uid, updated) }
                                    }
                                },
                                onRegenerateClick = {
                                    val newPieceIds = pickOutfitGarments(garments, outfit.style)
                                    if (newPieceIds.size < 2) {
                                        return@OutfitDetailScreen
                                    }
                                    val pieces = newPieceIds.mapNotNull { id -> garments.find { it.id == id } }
                                    val updated = outfit.copy(
                                        garmentIds = newPieceIds,
                                        name = outfitName(pieces, outfit.style),
                                    )
                                    outfits = outfits.map { if (it.id == updated.id) updated else it }
                                    selectedOutfit = updated
                                    AuthRepository.currentUser?.let { u ->
                                        scope.launch { OutfitRepository.update(u.uid, updated) }
                                    }
                                },
                                onDeleteClick = {
                                    outfits = outfits.filterNot { it.id == outfit.id }
                                    selectedOutfit = null
                                    route = Route.Outfits
                                    AuthRepository.currentUser?.let { u ->
                                        scope.launch { OutfitRepository.delete(u.uid, outfit) }
                                    }
                                },
                            )
                        }
                    }
                    Route.GarmentDetail -> {
                        val g = selectedGarment
                        if (g == null) {
                            route = Route.Wardrobe
                        } else {
                            Box {
                                GarmentDetailScreen(
                                    garment = g,
                                    onBackClick = { route = garmentOrigin },
                                    onEditClick = { clearPickedImage(); route = Route.EditGarment },
                                    onToggleFavorite = {
                                        val updated = g.copy(isFavorite = !g.isFavorite)
                                        garments = garments.map { if (it.id == updated.id) updated else it }
                                        selectedGarment = updated
                                        AuthRepository.currentUser?.let { u ->
                                            scope.launch { WardrobeRepository.update(u.uid, updated) }
                                        }
                                    },
                                    onDeleteClick = { showDeleteDialog = true },
                                )
                                if (showDeleteDialog) {
                                    DeleteGarmentDialog(
                                        onConfirm = {
                                            garments = garments.filterNot { it.id == g.id }
                                            showDeleteDialog = false
                                            route = Route.GarmentDeleted
                                            AuthRepository.currentUser?.let { u ->
                                                scope.launch {
                                                    WardrobeRepository.delete(u.uid, g)
                                                    g.photoUrl?.let { PhotoRepository.deletePhoto(it) }
                                                }
                                            }
                                        },
                                        onCancel = { showDeleteDialog = false },
                                    )
                                }
                            }
                        }
                    }
                    Route.EditGarment -> {
                        val g = selectedGarment
                        if (g == null) {
                            route = Route.Wardrobe
                        } else {
                            ManualGarmentFormScreen(
                                initial = g,
                                photoUri = pickedImageUri,
                                onImageClick = { launchGallery() },
                                onBackClick = { route = Route.GarmentDetail },
                                onSaveClick = { updated ->
                                    val newPhotoUri = pickedImageUri
                                    val previousPhotoUrl = g.photoUrl
                                    route = Route.GarmentUpdated
                                    AuthRepository.currentUser?.let { u ->
                                        scope.launch {
                                            val garmentToSave = if (newPhotoUri != null) {
                                                try {
                                                    val newUrl = PhotoRepository.uploadGarmentPhoto(u.uid, newPhotoUri)
                                                    if (previousPhotoUrl != null) PhotoRepository.deletePhoto(previousPhotoUrl)
                                                    updated.copy(photoUrl = newUrl)
                                                } catch (e: Exception) {
                                                    updated
                                                }
                                            } else {
                                                updated
                                            }
                                            garments = garments.map { if (it.id == garmentToSave.id) garmentToSave else it }
                                            selectedGarment = garmentToSave
                                            WardrobeRepository.update(u.uid, garmentToSave)
                                            clearPickedImage()
                                        }
                                    }
                                },
                            )
                        }
                    }
                    Route.GarmentUpdated -> SuccessScreen(
                        title = "¡Cambios guardados!",
                        subtitle = "La prenda se actualizó correctamente.",
                        buttonText = "Ver prenda",
                        onContinue = { route = Route.GarmentDetail },
                    )
                    Route.GarmentDeleted -> SuccessScreen(
                        title = "Prenda eliminada",
                        subtitle = "Ya no está disponible en tu armario.",
                        buttonText = "Volver al armario",
                        onContinue = { selectedGarment = null; route = Route.Wardrobe },
                    )
                    Route.AddGarmentChoice -> AddGarmentChoiceScreen(
                        onBackClick = { route = Route.Wardrobe },
                        onCancelClick = { route = Route.Wardrobe },
                        onTakePhotoClick = {
                            clearPickedImage()
                            launchCamera()
                            route = Route.AddGarmentManual
                        },
                        onChooseGalleryClick = {
                            clearPickedImage()
                            launchGallery()
                            route = Route.AddGarmentManual
                        },
                        onManualClick = {
                            clearPickedImage()
                            route = Route.AddGarmentManual
                        },
                    )
                    Route.AddGarmentManual -> ManualGarmentFormScreen(
                        onBackClick = { route = Route.AddGarmentChoice },
                        photoUri = pickedImageUri,
                        onImageClick = { launchGallery() },
                        onSaveClick = { newGarment ->
                            val photoUri = pickedImageUri
                            route = Route.GarmentAdded
                            AuthRepository.currentUser?.let { u ->
                                scope.launch {
                                    val garmentToSave = if (photoUri != null) {
                                        try {
                                            newGarment.copy(photoUrl = PhotoRepository.uploadGarmentPhoto(u.uid, photoUri))
                                        } catch (e: Exception) {
                                            newGarment
                                        }
                                    } else {
                                        newGarment
                                    }
                                    garments = garments + garmentToSave
                                    WardrobeRepository.add(u.uid, garmentToSave)
                                    clearPickedImage()
                                }
                            }
                        },
                    )
                    Route.GarmentAdded -> SuccessScreen(
                        title = "¡Prenda agregada!",
                        subtitle = "Ya está disponible en tu armario.",
                        buttonText = "Ver armario",
                        onContinue = { route = Route.Wardrobe },
                    )
                }
            }
        }
    }
}
