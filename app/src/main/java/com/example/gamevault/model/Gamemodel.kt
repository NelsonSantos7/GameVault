package com.example.gamevault.model

import android.os.Parcel
import android.os.Parcelable

data class Gamemodel(
    var id: String? = null,
    val title: String = "",
    val distributor: String = "",
    val releaseYear: Int = 0,
    val estimatedTime: Int = 0,
    val metacriticScore: Int = 0,
    val userId: String? = null  // Optional: to link the game to a specific user
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(distributor)
        parcel.writeInt(releaseYear)
        parcel.writeInt(estimatedTime)
        parcel.writeInt(metacriticScore)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Gamemodel> {
        override fun createFromParcel(parcel: Parcel): Gamemodel = Gamemodel(parcel)
        override fun newArray(size: Int): Array<Gamemodel?> = arrayOfNulls(size)
    }
}
