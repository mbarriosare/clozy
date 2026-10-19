package com.clozy.app.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

object PhotoRepository {

    private val storage get() = FirebaseStorage.getInstance()

    suspend fun uploadGarmentPhoto(uid: String, localUri: Uri): String {
        val fileName = "garment_${System.currentTimeMillis()}.jpg"
        val ref = storage.reference.child("users/$uid/garments/$fileName")
        ref.putFile(localUri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun deletePhoto(downloadUrl: String) {
        try {
            storage.getReferenceFromUrl(downloadUrl).delete().await()
        } catch (e: Exception) {
        }
    }
}
