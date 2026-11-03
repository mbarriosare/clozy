package com.clozy.app.data

import com.clozy.app.ui.market.Listing
import com.clozy.app.ui.market.ListingCondition
import com.clozy.app.ui.market.ListingStatus
import com.clozy.app.ui.wardrobe.Garment
import com.clozy.app.ui.wardrobe.GarmentCategory
import com.clozy.app.ui.wardrobe.GarmentColor
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class PurchaseResult(val listing: Listing, val garment: Garment)

object MarketRepository {

    private val db get() = FirebaseFirestore.getInstance()
    private val collection get() = db.collection("listings")

    private fun docToListing(id: String, data: Map<String, Any?>): Listing = Listing(
        id = id,
        sellerId = data["sellerId"] as? String ?: "",
        sellerUsername = data["sellerUsername"] as? String ?: "",
        name = data["name"] as? String ?: "Prenda",
        price = (data["price"] as? Long)?.toInt() ?: 0,
        condition = (data["condition"] as? String)?.let { runCatching { ListingCondition.valueOf(it) }.getOrNull() } ?: ListingCondition.Good,
        category = (data["category"] as? String)?.let { runCatching { GarmentCategory.valueOf(it) }.getOrNull() } ?: GarmentCategory.Top,
        primaryColor = (data["primaryColor"] as? String)?.let { runCatching { GarmentColor.valueOf(it) }.getOrNull() } ?: GarmentColor.White,
        thumbnailRes = null,
        photoUrl = data["photoUrl"] as? String,
        status = (data["status"] as? String)?.let { runCatching { ListingStatus.valueOf(it) }.getOrNull() } ?: ListingStatus.Active,
        buyerId = data["buyerId"] as? String,
        garmentDocId = data["garmentDocId"] as? String,
        createdAt = (data["createdAt"] as? Long) ?: 0L,
    )

    private fun Listing.toMap(): Map<String, Any?> = mapOf(
        "sellerId" to sellerId,
        "sellerUsername" to sellerUsername,
        "name" to name,
        "price" to price,
        "condition" to condition.name,
        "category" to category.name,
        "primaryColor" to primaryColor.name,
        "photoUrl" to photoUrl,
        "status" to status.name,
        "buyerId" to buyerId,
        "garmentDocId" to garmentDocId,
        "createdAt" to createdAt,
    )

    suspend fun fetchActiveListings(): List<Listing> {
        val snapshot = collection.whereEqualTo("status", ListingStatus.Active.name).get().await()
        return snapshot.documents.mapNotNull { doc -> doc.data?.let { docToListing(doc.id, it) } }
    }

    suspend fun fetchMyListings(uid: String): List<Listing> {
        val snapshot = collection.whereEqualTo("sellerId", uid).get().await()
        return snapshot.documents.mapNotNull { doc -> doc.data?.let { docToListing(doc.id, it) } }
    }

    suspend fun fetchMyPurchases(uid: String): List<Listing> {
        val snapshot = collection.whereEqualTo("buyerId", uid).get().await()
        return snapshot.documents.mapNotNull { doc -> doc.data?.let { docToListing(doc.id, it) } }
    }

    suspend fun publish(listing: Listing): Listing {
        val ref = collection.add(listing.toMap()).await()
        return listing.copy(id = ref.id)
    }

    suspend fun updateStatus(listingId: String, status: ListingStatus) {
        collection.document(listingId).update("status", status.name).await()
    }

    suspend fun delete(listingId: String) {
        collection.document(listingId).delete().await()
    }

    // Compra atómica: marca la publicación como vendida Y crea la prenda en el armario del
    // comprador en la misma transacción. O pasan las dos cosas o ninguna.
    suspend fun purchase(listingId: String, buyerId: String): PurchaseResult {
        val ref = collection.document(listingId)
        val garmentRef = db.collection("users").document(buyerId).collection("garments").document()
        val result = db.runTransaction { tx ->
            val snap = tx.get(ref)
            val data = snap.data ?: throw IllegalStateException("La publicación ya no existe.")
            val current = docToListing(snap.id, data)
            if (current.sellerId == buyerId) throw IllegalStateException("No puedes comprar tu propia publicación.")
            if (current.status != ListingStatus.Active) throw IllegalStateException("Esta prenda ya no está disponible.")
            val garment = Garment(
                id = garmentRef.id.hashCode(),
                name = current.name,
                category = current.category,
                primaryColor = current.primaryColor,
                photoUrl = current.photoUrl,
            )
            tx.update(ref, mapOf("status" to ListingStatus.Sold.name, "buyerId" to buyerId))
            tx.set(garmentRef, WardrobeRepository.toFirestoreMap(garment))
            PurchaseResult(current.copy(status = ListingStatus.Sold, buyerId = buyerId), garment)
        }.await()
        WardrobeRepository.register(result.garment, garmentRef.id)
        return result
    }

    // Al eliminar la cuenta: borra las publicaciones abiertas. Las vendidas se conservan
    // porque forman parte del historial de compras del comprador.
    suspend fun deleteOpenListings(uid: String) {
        val snapshot = collection.whereEqualTo("sellerId", uid).get().await()
        for (doc in snapshot.documents) {
            if (doc.getString("status") != ListingStatus.Sold.name) doc.reference.delete().await()
        }
    }
}
