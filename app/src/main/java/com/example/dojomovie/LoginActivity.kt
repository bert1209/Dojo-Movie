package com.example.dojomovie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dojomovie.DBhelper.Database_Helper

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val dbHelper = Database_Helper(this)
        val userList = dbHelper.getUser()

        val signInBtn = findViewById<Button>(R.id.signInButton)
        val phoneNumET = findViewById<EditText>(R.id.phoneNumLoginET)
        val passwordET = findViewById<EditText>(R.id.PasswordLoginET)


        for (user in userList) {

//            Log.d("UserData", "ID: ${user.id.toString()}, Phone: ${user.phoneNum.toString()}, Password: ${user.password.toString()}")
            signInBtn.setOnClickListener(){
                Log.d("data", phoneNumET.text.toString())
                if(phoneNumET.text.toString() == user.phoneNum.toString() && passwordET.text.toString() == user.password.toString()){
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                    Log.d("login", "Sukses")
                }
            }
        }

    }
}