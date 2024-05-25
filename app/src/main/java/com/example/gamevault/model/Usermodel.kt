package com.example.gamevault.model

import android.os.Parcel
import android.os.Parcelable

data class Usermodel(
    var id: String? = null,
    val username: String = "",
    val email: String = "",
    val password: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Usermodel> {
        override fun createFromParcel(parcel: Parcel): Usermodel = Usermodel(parcel)
        override fun newArray(size: Int): Array<Usermodel?> = arrayOfNulls(size)
    }
}
