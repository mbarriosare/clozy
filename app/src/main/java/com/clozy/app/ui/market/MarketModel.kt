package com.clozy.app.ui.market

import com.clozy.app.ui.wardrobe.GarmentCategory
import com.clozy.app.ui.wardrobe.GarmentColor

enum class ListingStatus { Active, Sold, Paused }

enum class ListingCondition(val label: String) {
    New("Nuevo"), LikeNew("Como nuevo"), Good("Bueno"),
}

data class Listing(
    val id: String = "",
    val sellerId: String = "",
    val sellerUsername: String = "",
    val name: String = "",
    val price: Int = 0,
    val condition: ListingCondition = ListingCondition.Good,
    val category: GarmentCategory = GarmentCategory.Top,
    val primaryColor: GarmentColor = GarmentColor.White,
    val thumbnailRes: Int? = null,
    val photoUrl: String? = null,
    val status: ListingStatus = ListingStatus.Active,
    val buyerId: String? = null,
    val garmentDocId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)

// 25000 -> "$25.000"
fun Int.asPrice(): String = "$" + String.format(java.util.Locale.US, "%,d", this).replace(',', '.')
