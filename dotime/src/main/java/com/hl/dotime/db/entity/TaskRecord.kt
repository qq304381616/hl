package com.hl.dotime.db.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by HL on 2018/5/21.
 */
class TaskRecord() : Parcelable {
    var id: String? = null
    var taskId: String? = null
    var name: String? = null
    var status: Int? = 1 // 1开始 2暂停 3结束
    var markId: Int? = null
    var isDel: Int? = null

    var timerList: List<RecordTimer>? = null

    var mark: Mark? = null
    var task: Task? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        taskId = parcel.readString()
        name = parcel.readString()
        status = parcel.readValue(Int::class.java.classLoader) as? Int
        markId = parcel.readValue(Int::class.java.classLoader) as? Int
        isDel = parcel.readValue(Int::class.java.classLoader) as? Int
        timerList = parcel.createTypedArrayList(RecordTimer)
        mark = parcel.readParcelable(Mark::class.java.classLoader)
        task = parcel.readParcelable(Task::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(taskId)
        parcel.writeString(name)
        parcel.writeValue(status)
        parcel.writeValue(markId)
        parcel.writeValue(isDel)
        parcel.writeTypedList(timerList)
        parcel.writeParcelable(mark, flags)
        parcel.writeParcelable(task, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskRecord> {
        override fun createFromParcel(parcel: Parcel): TaskRecord {
            return TaskRecord(parcel)
        }

        override fun newArray(size: Int): Array<TaskRecord?> {
            return arrayOfNulls(size)
        }
    }
}