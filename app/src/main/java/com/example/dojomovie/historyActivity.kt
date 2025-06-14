// Dalam HistoryActivity.kt
package com.example.dojomovie

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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


class HistoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: Database_Helper
    private lateinit var transactionAdapter: transaction_adapter
    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history_activity)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)


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


        // Initialize the database helper
        dbHelper = Database_Helper(this)


        val transactionHistory: ArrayList<transaction> = ArrayList(dbHelper.getTransactionHistory(UserSession.currentLoggedInUser?.id.toString()))

        transactionAdapter = transaction_adapter(transactionHistory)
        val recyclerView: RecyclerView = findViewById(R.id.transactionRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transactionAdapter
    }
}
