package com.clozy.app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

object AuthRepository {

    private val auth get() = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser? get() = auth.currentUser

    fun friendlyError(e: Exception): String = when (e) {
        is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
        is FirebaseAuthInvalidUserException -> "No existe una cuenta con ese correo."
        is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con ese correo."
        is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil."
        is FirebaseAuthRecentLoginRequiredException -> "Por seguridad, cierra sesión y vuelve a iniciarla antes de eliminar tu cuenta."
        else -> e.localizedMessage ?: "Ocurrió un error. Inténtalo de nuevo."
    }

    suspend fun register(name: String, email: String, password: String): FirebaseUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: error("No se pudo crear la cuenta.")
        user.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build()).await()
        return user
    }

    suspend fun login(email: String, password: String): FirebaseUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user ?: error("No se pudo iniciar sesión.")
    }

    suspend fun sendVerificationEmail() {
        auth.currentUser?.sendEmailVerification()?.await()
    }

    suspend fun isEmailVerified(): Boolean {
        auth.currentUser?.reload()?.await()
        return auth.currentUser?.isEmailVerified ?: false
    }

    fun friendlyPhoneError(e: Exception): String = when (e) {
        is FirebaseAuthInvalidCredentialsException -> "El código no es correcto."
        is FirebaseAuthUserCollisionException -> "Ese número ya está verificado en otra cuenta."
        else -> e.localizedMessage ?: "No pudimos verificar el número. Inténtalo de nuevo."
    }

    suspend fun linkPhoneCredential(credential: com.google.firebase.auth.PhoneAuthCredential): FirebaseUser {
        val user = auth.currentUser ?: error("No hay una sesión activa.")
        val result = user.linkWithCredential(credential).await()
        return result.user ?: error("No se pudo vincular el teléfono.")
    }

    suspend fun unlinkPhone() {
        auth.currentUser?.unlink(com.google.firebase.auth.PhoneAuthProvider.PROVIDER_ID)?.await()
    }

    suspend fun signInWithGoogleIdToken(idToken: String): FirebaseUser {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        return result.user ?: error("No se pudo iniciar sesión con Google.")
    }

    suspend fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun deleteAccount() {
        val user = auth.currentUser ?: error("No hay una sesión activa.")
        user.delete().await()
    }
}
