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


        signInBtn.setOnClickListener {
            val phoneInput = phoneNumET.text.toString()
            val passInput = passwordET.text.toString()

            val user = userList.find { it.phoneNum == phoneInput && it.password == passInput }

            if (user != null) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                Log.d("login", "Sukses")
            } else {
                Log.d("login", "Gagal - user tidak ditemukan")
            }
        }


    }
}