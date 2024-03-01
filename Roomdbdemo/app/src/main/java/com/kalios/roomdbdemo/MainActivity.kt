package com.kalios.roomdbdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(),UserClickDeleteInterface,UserClickInterface {
    lateinit var rv_User : RecyclerView
    lateinit var btn_addUser:FloatingActionButton
    private lateinit var userDao: UserDao
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_User = findViewById(R.id.rv_user)
        btn_addUser = findViewById(R.id.Fab_add_User)
        val db = MyDatabase.getDatabase(this)
        userDao = db.userDao()

        rv_User.layoutManager = LinearLayoutManager(this)

        val UserAdapter = userAdapter(this,this,this)
        rv_User.adapter =UserAdapter

        GlobalScope.launch (Dispatchers.IO){
            Log.i("kalios",userDao.getAll().toString())
        }

        GlobalScope.launch(Dispatchers.IO) {
            val users = userDao.getAll()
            runOnUiThread {
                UserAdapter.updateList(users)
            }
        }


        btn_addUser.setOnClickListener {
            startActivity(Intent(this, add_edit_user::class.java))
            this.finish()
        }

    }

    override fun onDeleteIconClick(user: User) {
        GlobalScope.launch(Dispatchers.IO) {
            userDao.delete(user)
        }
    }

    override fun onUserClick(user: User) {
        val intent = Intent(this@MainActivity, add_edit_user::class.java)
        intent.putExtra("userType", "Edit")
        intent.putExtra("userProfile",user.profile)
        intent.putExtra("userFirstName", user.firstName)
        intent.putExtra("userLastName", user.lastName)
        intent.putExtra("userId", user.id)
        startActivity(intent)
        this.finish()
    }
}