package com.xrest.hamrobazar.ResponseClass

import android.os.Parcel
import android.os.Parcelable

data class User(
        val _id:String?=null,
        val Name:String?=null,
        val Username:String?=null,
        val Password:String?=null,
        val PhoneNumber:String?=null,
        val Profile:String?=null,
        val isOnline:Boolean?=null,
        var Friends:MutableList<friend>?=null



) {
}
data class friend(
        val _id:String?=null,
        val user:Users
)
data class Users(
        val _id:String?=null,
        val Name:String?=null,
        val Username:String?=null,
        val Password:String?=null,
        val PhoneNumber:String?=null,
        val Profile:String?=null,
        val isOnline:Boolean?=null,
):Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(_id)
                parcel.writeString(Name)
                parcel.writeString(Username)
                parcel.writeString(Password)
                parcel.writeString(PhoneNumber)
                parcel.writeString(Profile)
                parcel.writeValue(isOnline)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Users> {
                override fun createFromParcel(parcel: Parcel): Users {
                        return Users(parcel)
                }

                override fun newArray(size: Int): Array<Users?> {
                        return arrayOfNulls(size)
                }
        }
}