package com.example.dojomovie

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class AboutUsActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.aboutus)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val textView: TextView = findViewById(R.id.text_view)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = Color.parseColor("#FFFFFF")
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
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
                    Toast.makeText(applicationContext, "Already in About Us", Toast.LENGTH_SHORT).show()
                }
            }
            drawerLayout.closeDrawers() // Optional: closes drawer after clicking
            true
        }

        val mText = "DoJo Movie V1.0.0"
        val mSpannableString = SpannableString(mText)
        val mYellow = ForegroundColorSpan(Color.parseColor("#EEBA2C"))

        mSpannableString.setSpan(mYellow, 10,17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = mSpannableString
    }
}
