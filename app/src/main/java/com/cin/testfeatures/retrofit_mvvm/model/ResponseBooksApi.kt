package com.cin.testfeatures.retrofit_mvvm.model

import com.google.gson.annotations.SerializedName

data class VolumeResponse(
    @SerializedName("kind") val kind: String,
    @SerializedName("totalItems") val totalItems: Int,
    @SerializedName("items") val listVolumes: List<ModelVolume>,
)

data class ModelVolume(
    @SerializedName("kind") val kind: String,
    @SerializedName("id") val id: String,
    @SerializedName("etag") val etag: String,
    @SerializedName("selfLink") val selfLink: String,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo,
)

data class VolumeInfo(
    @SerializedName("title") val title: String?,
    @SerializedName("authors") val authors: List<String>,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("publishedDate") val publishedDate: String,
    @SerializedName("description") val description: String?,
    @SerializedName("pageCount") val pageCount: Int,
    @SerializedName("printType") val printType: String,
    @SerializedName("imageLinks") val imageLinks: ImageLinks,
)

data class ImageLinks(
    @SerializedName("smallThumbnail") val smallThumbnail: String?,
    @SerializedName("thumbnail") val thumbnail: String?,
)


