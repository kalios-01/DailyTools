package com.kalios.roomdbdemo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

class add_edit_user : AppCompatActivity() {

    private lateinit var profile_preview: ImageView
    private lateinit var img_chooser: Button
    private lateinit var btn_add_update: Button
    private lateinit var edt_firstName: EditText
    private lateinit var edt_lastName: EditText
    var userID = -1;
    private var image_byteArray: ByteArray? = null
    private lateinit var userDao: UserDao

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_user)

        profile_preview = findViewById(R.id.image_preview)
        img_chooser = findViewById(R.id.img_chooser)
        btn_add_update = findViewById(R.id.btn_add_update)
        edt_firstName = findViewById(R.id.edt_firstName)
        edt_lastName = findViewById(R.id.edt_lastName)

        // Db Init
        val db = MyDatabase.getDatabase(this)
        userDao = db.userDao()


        val userType = intent.getStringExtra("userType")
        if (userType.equals("Edit")) {
            // on below line we are setting data to edit text.
            val firstName = intent.getStringExtra("userFirstName")
            val lastName = intent.getStringExtra("userLastName")
            val profile = intent.getStringExtra("userProfile")
            userID = intent.getIntExtra("userId", -1)
            btn_add_update.text = "Update User"
            edt_firstName.setText(firstName)
            edt_lastName.setText(lastName)
            val bitmapProfile = BitmapUtils.byteArrayToBitmap(profile?.encodeToByteArray())
            profile_preview.setImageBitmap(bitmapProfile)
        } else {
            btn_add_update.text = "Save Note"
        }

        img_chooser.setOnClickListener {
            openGallery()
        }

        btn_add_update.setOnClickListener {
            val firstName = edt_firstName.text.toString()
            val lastName = edt_lastName.text.toString()
            // on below line we are checking the type
            // and then saving or updating the data.
            if (userType.equals("Edit")) {
                if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                    val updatedUser =
                        User(profile = image_byteArray, firstName = firstName, lastName = lastName)
                    updatedUser.id = userID
                    GlobalScope.launch {
                        userDao.update(updatedUser)
                    }

                    Toast.makeText(this, "User Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                    // if the string is not empty we are calling a
                    // add note method to add data to our room database.
                    GlobalScope.launch {
                        userDao.insertAll(
                            User(
                                profile = image_byteArray,
                                firstName = firstName,
                                lastName = lastName
                            )
                        )
                    }
                    Toast.makeText(this, " User Added", Toast.LENGTH_LONG).show()
                }
            }
            // opening the new activity on below line
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }

    }

    // function for opening Dialog
    private val getAction = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val image_bitmap = uriToBitmap(it)
            profile_preview.setImageBitmap(image_bitmap)
            image_byteArray = BitmapUtils.bitmapToByteArray(image_bitmap)

        }
    }

    // Funtion for geting images from gallery
    private fun openGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getAction.launch("image/*")
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}