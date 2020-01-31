package com.example.catsdogs

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CatsDogsListJson(
    @SerializedName("data") val data: List<CatDogDataJson>,
    @SerializedName("message") val message: String
)

data class CatDogDataJson(
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
)

class CatDogItem(val imageUrl: String, val title: String) : Parcelable {

    constructor(data: CatDogDataJson) : this(
        imageUrl = data.url,
        title = data.title
    )

    constructor(parcel: Parcel) : this(
        imageUrl = parcel.readString() ?: "",
        title = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUrl)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CatDogItem> {
        override fun createFromParcel(parcel: Parcel): CatDogItem {
            return CatDogItem(parcel)
        }

        override fun newArray(size: Int): Array<CatDogItem?> {
            return arrayOfNulls(size)
        }
    }
}