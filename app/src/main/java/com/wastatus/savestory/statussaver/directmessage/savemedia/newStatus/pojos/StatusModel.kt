package com.wastatus.savestory.statussaver.directmessage.savemedia.newStatus.fragments.fragments.pojos

import android.os.Parcel
import android.os.Parcelable

data class StatusModel(val path: String?, val name: String?, var isSaved:Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(name)
        parcel.writeByte(if (isSaved) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StatusModel> {
        override fun createFromParcel(parcel: Parcel): StatusModel {
            return StatusModel(parcel)
        }

        override fun newArray(size: Int): Array<StatusModel?> {
            return arrayOfNulls(size)
        }
    }

}