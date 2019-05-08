package com.cohen.searchfordoctor.data

import com.google.gson.annotations.SerializedName

data class OauthResult(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    val jti: String,
    val phoneVerified: Boolean,
    @SerializedName("refresh_token")
    val refreshToken: String,
    val scope: String,
    @SerializedName("token_type")
    val tokenType: String
)