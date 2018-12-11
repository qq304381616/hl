package com.hl.dotime.db.entity

import android.os.Parcel
import android.os.Parcelable

class RecordTimer() : Parcelable {
    var id: Int? = null
    var taskRecordId: String? = null
    var startTime: Long? = 0
    var endTime: Long? = 0
    var isDel: Int? = 0

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        taskRecordId = parcel.readString()
        startTime = parcel.readValue(Long::class.java.classLoader) as? Long
        endTime = parcel.readValue(Long::class.java.classLoader) as? Long
        isDel = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(taskRecordId)
        parcel.writeValue(startTime)
        parcel.writeValue(endTime)
        parcel.writeValue(isDel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecordTimer> {
        override fun createFromParcel(parcel: Parcel): RecordTimer {
            return RecordTimer(parcel)
        }

        override fun newArray(size: Int): Array<RecordTimer?> {
            return arrayOfNulls(size)
        }
    }

}