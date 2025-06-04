// Dalam HistoryActivity.kt
package com.example.dojomovie

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dojomovie.DB.UserSession
import com.example.dojomovie.DBhelper.Database_Helper
import com.example.dojomovie.adapter.transaction_adapter
import com.example.dojomovie.DB.transaction
import com.google.android.material.navigation.NavigationView


class profileActivity : AppCompatActivity() {

    private lateinit var dbHelper: Database_Helper
    private lateinit var transactionAdapter: transaction_adapter
    lateinit var toggle : ActionBarDrawerToggle

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val phoneNum = findViewById<TextView>(R.id.phoneNumber)
        val logOutButton = findViewById<Button>(R.id.LogOutButton)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        phoneNum.setText("+62 ${UserSession.currentLoggedInUser?.phoneNum}")


        toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = Color.parseColor("#FFFFFF")
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_person -> {
                    startActivity(Intent(this, profileActivity::class.java))
                    Toast.makeText(applicationContext, "Clicked Profile", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    Toast.makeText(applicationContext, "Clicked History", Toast.LENGTH_SHORT).show()
                }

                R.id.nav_aboutUs -> {
                    startActivity(Intent(this, AboutUsActivity::class.java))
                    Toast.makeText(applicationContext, "Clicked About Us", Toast.LENGTH_SHORT).show()
                }
            }

            true
        }

        logOutButton.setOnClickListener(){
            UserSession.currentLoggedInUser = null
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val phoneText = "+62 ${UserSession.currentLoggedInUser?.phoneNum}"
        val mSpannableString = SpannableString(phoneText)
        val mYellow = ForegroundColorSpan(Color.parseColor("#EEBA2C"))

        mSpannableString.setSpan(mYellow, 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        phoneNum.text = mSpannableString
    }
}
