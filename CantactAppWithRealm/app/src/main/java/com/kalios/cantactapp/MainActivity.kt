package com.kalios.cantactapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var edt_name : EditText
    private lateinit var edt_phone_number : EditText
    private lateinit var edt_idnumber : EditText
    private lateinit var Btn_add_contact : Button
    private lateinit var Btn_delete : Button
    private lateinit var Image_chooser: Button
    private lateinit var contactRecyclerview:RecyclerView
    private lateinit var image_preview : ImageView
    private var profileImageByteArray: ByteArray? = null
    private val PICK_IMAGE_REQUEST = 1

    // its realm db dude
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edt_name=findViewById(R.id.edt_name)
        edt_phone_number = findViewById(R.id.edt_phonenumber)
        Btn_add_contact = findViewById(R.id.add_contact)
        Btn_delete = findViewById(R.id.delete_all)
        contactRecyclerview = findViewById(R.id.rv_contact)
        Image_chooser = findViewById(R.id.chooser_img)
        image_preview = findViewById(R.id.imageView)

        // its realm dude
        val config = RealmConfiguration.Builder(setOf(Contact::class))
            .schemaVersion(3) // Set the current schema version
            .build()
        realm = Realm.open(config)
        set_data_recyclerview()

        Image_chooser.setOnClickListener {
            chooseImage(this) // Assuming 'this' refers to your activity context
        }
        // delete data
        Btn_delete.setOnClickListener {
            runBlocking {
                launch(Dispatchers.Default){
                    deleteAllContacts()
                }
            }
            set_data_recyclerview()
        }
        // Add a new contact to Realm
        Btn_add_contact.setOnClickListener {
            runBlocking {
                launch(Dispatchers.Default) {
                    // Execute the Realm transaction in the background
                    addContactToRealm(edt_name.text.toString(),edt_phone_number.text.toString(), profileImageByteArray )
                }
            }
            set_data_recyclerview()
        }
    }
    // Retrieve all contacts from Realm and log their details

    private fun set_data_recyclerview(){
        // Get contacts from Realm (replace with your Realm initialization)
        val contacts = realm.query<Contact>().find()
        val adapter = ContactAdapter.ContactAdapter(contacts)
        contactRecyclerview.layoutManager = LinearLayoutManager(this)
        contactRecyclerview.adapter = adapter
    }
    // Realm Experiment
    private suspend fun addContactToRealm(name: String, phoneNumber: String,profileImageByteArray: ByteArray?) {
        realm.writeBlocking {
            copyToRealm(Contact().apply {
                this.profile_img = profileImageByteArray
                this.name = name
                this.phoneNumber = phoneNumber
            })
        }
    }
    private suspend fun deleteAllContacts() {
        realm.writeBlocking {
            val writeTransactionItems = query<Contact>().find()
            delete(writeTransactionItems.first())
        }
    }


    // ImagePicker
    private fun chooseImage(activity: Activity) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, activity: Activity): Bitmap? {
        var bitmap: Bitmap? = null
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return bitmap
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val bitmap = onActivityResult(requestCode, resultCode, data, this)
        if (bitmap != null) {
            profileImageByteArray = BitmapUtils.bitmapToByteArray(bitmap)
            val restoredBitmap = BitmapUtils.byteArrayToBitmap(profileImageByteArray)
            image_preview.setImageBitmap(restoredBitmap)
        }
    }
}