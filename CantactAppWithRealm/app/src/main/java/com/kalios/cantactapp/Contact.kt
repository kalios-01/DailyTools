package com.kalios.cantactapp

import android.graphics.Bitmap
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Contact : RealmObject {
    @PrimaryKey
    var name: String = ""
    var phoneNumber: String = ""
    var profile_img: ByteArray? = null
}
