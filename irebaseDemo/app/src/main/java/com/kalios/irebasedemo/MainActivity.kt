package com.kalios.irebasedemo

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var email : EditText
    private lateinit var pass: EditText
    lateinit var btn_login : Button
    lateinit var btn_signup: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        email = findViewById(R.id.email_editText)
        pass = findViewById(R.id.pass_editText)
        btn_login = findViewById(R.id.button)
        btn_signup = findViewById(R.id.button2)

        btn_signup.setOnClickListener {
            signUp(email = email.text.toString(), password = pass.text.toString(),this)
        }
        btn_login.setOnClickListener {
            login(email = email.text.toString(), password = pass.text.toString(),this)
        }
    }

    fun signUp(email: String, password: String, context: Context) {
        val auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    // User creation successful
                    Log.d("Signup", "User created successfully!")
                    // Handle successful signup (e.g., navigate to main activity)
                } else {
                    // User creation failed
                    Log.w("Signup", "User creation failed.", task.exception)
                    // Handle signup failure (e.g., display error message)
                }
            }
    }
    fun login(email: String, password: String, context: Context) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Log.d("Login", "User logged in successfully!")
                    // Handle successful login (e.g., navigate to main activity)
                } else {
                    // Login failed
                    Log.w("Login", "Login failed.", task.exception)
                    // Handle login failure (e.g., display error message)
                }
            }
    }

}