package com.hl.dotime.db.entity

import android.os.Parcel
import android.os.Parcelable

class Mark : Parcelable {
    var id: Int? = null
    var name: String? = null

    constructor() {

    }

    constructor(name: String?) {
        this.name = name
    }

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mark> {
        override fun createFromParcel(parcel: Parcel): Mark {
            return Mark(parcel)
        }

        override fun newArray(size: Int): Array<Mark?> {
            return arrayOfNulls(size)
        }
    }
}