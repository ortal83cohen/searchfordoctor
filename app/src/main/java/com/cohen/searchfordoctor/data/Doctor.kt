package com.cohen.searchfordoctor.data

data class Doctor(
    val address: String,
    val email: Any,
    val highlighted: Boolean,
    val id: String,
    val integration: Any,
    val lat: Double,
    val lng: Double,
    val name: String,
    val openingHours: List<Any>,
    val phoneNumber: String,
    val photoId: Any,
    val rating: Double,
    val reviewCount: Any,
    val source: String,
    val specialityIds: List<Int>,
    val website: Any
)