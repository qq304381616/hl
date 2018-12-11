package com.hl.dotime.db.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by HL on 2018/5/19.
 */
class Task() : Parcelable {
    var id: String? = null
    var name: String? = null
    var groupId: String? = null
    var isDel: Int? = null
    var iconName: String? = null
    var iconColor: String? = null

    var groupName: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        groupId = parcel.readString()
        isDel = parcel.readValue(Int::class.java.classLoader) as? Int
        iconName = parcel.readString()
        iconColor = parcel.readString()
        groupName = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(groupId)
        parcel.writeValue(isDel)
        parcel.writeString(iconName)
        parcel.writeString(iconColor)
        parcel.writeString(groupName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Task> {
        override fun createFromParcel(parcel: Parcel): Task {
            return Task(parcel)
        }

        override fun newArray(size: Int): Array<Task?> {
            return arrayOfNulls(size)
        }
    }
}