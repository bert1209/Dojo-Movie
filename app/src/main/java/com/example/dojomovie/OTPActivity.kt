package com.example.dojomovie

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.dojomovie.DB.userDB
import com.example.dojomovie.DBhelper.Database_Helper

class OTPActivity : AppCompatActivity() {

    private lateinit var countdownText: TextView
    private var currentOtp: String = ""
    private lateinit var smsManager : SmsManager
    private lateinit var Database_Helper : Database_Helper
    private var phoneNumbers = ""
    private lateinit var otp: EditText



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_activity)
        smsManager = SmsManager.getDefault()
        var phoneNumber = intent.getStringExtra("PhoneNumber")
        var passwords = intent.getStringExtra("Password")
        phoneNumbers = phoneNumber.toString()
        currentOtp = generateOtp(6)

        val otp = findViewById<EditText>(R.id.pinView)
        val OKButton = findViewById<Button>(R.id.OkButton)

        Database_Helper = Database_Helper(this)
        countdownText = findViewById(R.id.countdownText)

        startCountdown()
        sendSMS(phoneNumber.toString(), currentOtp)

        OKButton.setOnClickListener(){
            if (otp.text.toString() == currentOtp) {
                val user = userDB().apply {
                    id = 0
                    phoneNum = phoneNumber.toString()
                    password = passwords.toString()
                }
                Database_Helper.insertUser(user)

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                Log.d("Register", "Register Gagal")
                otp.setText("")
            }
        }



    }

    private fun startCountdown() {
        val timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                countdownText.text = "Resend Code In : $secondsLeft s"
            }

            override fun onFinish() {
                countdownText.text = "Kirim ulang OTP"

                countdownText.setOnClickListener {
                    startCountdown()
                    val otp = findViewById<EditText>(R.id.pinView)
                    currentOtp = generateOtp(4) // Simpan OTP baru
                    sendSMS(phoneNumbers.toString(), currentOtp)
                    otp.setText("") // Reset PINView juga
                    otp.isEnabled = true
                    otp.requestFocus()
                }

            }
        }
        timer.start()
    }
    fun generateOtp(length: Int = 6): String {
        val chars = "0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
//    fun checkSMSPermission(phoneNumber : String, message: String){
//        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 100)
//        } else {
//
//        }
//    }

    fun sendSMS(phoneNumber : String, otpCode : String){
        smsManager.sendTextMessage(phoneNumber, null, "Kode verifikasi Anda adalah: ${otpCode}. Jangan bagikan kode ini kepada siapa pun.", null, null)
    }
}

