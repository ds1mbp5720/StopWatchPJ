package com.lee.fragment.stopwatchpj

import android.os.Parcel
import android.os.Parcelable

data class myTime(
    var hour: String?, var minute: String?,
    var second: String?, var milsecond: String?
                  ):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hour)
        parcel.writeString(minute)
        parcel.writeString(second)
        parcel.writeString(milsecond)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<myTime> {
        override fun createFromParcel(parcel: Parcel): myTime {
            return myTime(parcel)
        }

        override fun newArray(size: Int): Array<myTime?> {
            return arrayOfNulls(size)
        }
    }

}
