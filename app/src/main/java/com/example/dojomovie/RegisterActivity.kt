package com.example.dojomovie

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dojomovie.DBhelper.Database_Helper
import com.example.dojomovie.databinding.ActivityMainBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var Database_Helper : Database_Helper
    private lateinit var userRV: RecyclerView
//    private lateinit var adapter: user_adapter
    private val dbHelper = Database_Helper(this)
//    private val userList = dbHelper.getUser()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val phoneNumET = findViewById<EditText>(R.id.phoneNumET)
        val passwordET = findViewById<EditText>(R.id.passwordET)
        val confrimPasswordET = findViewById<EditText>(R.id.confirmPasswordET)


        Database_Helper = Database_Helper(this)
        binding = ActivityMainBinding.inflate(layoutInflater)


        signUpButton.setOnClickListener(){
            if(passwordET.text.toString() == confrimPasswordET.text.toString()){
                if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 100)
                    Log.d("tes", "Ini MASUK" )
//            checkSMSPermission()
                } else{
                    // Di ActivityA.kt
                    val intent = Intent(this, OTPActivity::class.java)
                    intent.putExtra("PhoneNumber", phoneNumET.toString())
                    intent.putExtra("Password", passwordET.toString())
                    startActivity(intent)
                    phoneNumET.setText("")
                    passwordET.setText("")
                    confrimPasswordET.setText("")


                }
            } else {
                Toast.makeText(applicationContext, "Make Sure to Check your Password Once Again", Toast.LENGTH_SHORT).show()
            }


        }

        val dbHelper = Database_Helper(this)
        val userList = dbHelper.getUser()

        for (user in userList) {
            Log.d(
                "UserData",
                "ID: ${user.id.toString()}, Phone: ${user.phoneNum.toString()}, Password: ${user.password.toString()}"
            )
        }



    }

    fun registerClicked(view: View) {
        val intent = Intent(this, OTPActivity::class.java)
        startActivity(intent)
    }

    fun checkSMSPermission(phoneNumber : String, password : String){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 100)
            Log.d("tes", "Ini MASUK" )
//            checkSMSPermission()
        } else{
            // Di ActivityA.kt
            val intent = Intent(this, OTPActivity::class.java)
            intent.putExtra("PhoneNumber", phoneNumber)
            intent.putExtra("Password", password)
            startActivity(intent)

        }
    }


}
