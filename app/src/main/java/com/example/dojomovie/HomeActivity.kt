package com.example.dojomovie

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
//import com.example.dojomovie.DB.MovieDB
import com.example.dojomovie.DB.movieDB
import com.example.dojomovie.DBhelper.Database_Helper
import com.example.dojomovie.adapter.movie_adapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
//import com.example.dojomovie.DB.movieDB
import com.google.android.material.navigation.NavigationView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback {


    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var requestQueue: RequestQueue
    private lateinit var mMap: GoogleMap
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

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
                R.id.nav_home -> Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
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


        val recyclerView: RecyclerView = findViewById(R.id.RVHomePage)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = GridLayoutManager(this, 2)


        requestQueue = Volley.newRequestQueue(this)

        val url = "https://api.npoint.io/66cce8acb8f366d2a508"

        val stringRequest = StringRequest(Request.Method.GET, url,
            { response ->
                Log.d("Volley", "Response: $response")
                // Proses respons di sini
            },
            { error ->
                Log.e("Volley Error", "Error: ${error.toString()}")
            })

        requestQueue.add(stringRequest)


        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    Log.d("JSON", "a")
                    Log.d("API Response", "Response: ${response.toString()}")
                    val movieList = parseJSON(response) // JSONArray masuk ke parseJSON
                    val adapter = movie_adapter(movieList)
                    val dbHelper = Database_Helper(this)
                    dbHelper.insertAllMovies(movieList)
                    dbHelper.printAllMovies()
                    dbHelper.updateMoviePoster1()
                    dbHelper.updateMoviePoster2()
                    dbHelper.updateMoviePoster3()
                    recyclerView.adapter = adapter
                    Log.d("JSON", response.toString())
                    Log.d("Movie List", "Jumlah film yang dimuat: ${movieList.size}")



                } catch (e: JSONException) {
                    Log.d("JSON", "a")
                    e.printStackTrace()
                }
            },
            { error ->
                Log.e("Volley Error", error.toString())
                Log.d("JSON", "b")
            }
        )

        requestQueue.add(request)
        Log.d("JSON", "a")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun parseJSON(jsonArray: JSONArray): ArrayList<movieDB> {
        val movieList = ArrayList<movieDB>()
        Log.d("JSON", "HALOOO")

        for (i in 0 until jsonArray.length()) {
            val movieObject = jsonArray.getJSONObject(i)
            val id = movieObject.getString("id")
            val poster = movieObject.getString("image")
            val title = movieObject.getString("title")
            val price = movieObject.getInt("price")

            movieList.add(movieDB(id, poster, title, price))
        }

        return movieList
    }
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        val DoJoMovie = LatLng(-6.2088, 106.8456)
        mMap.addMarker(MarkerOptions().position(DoJoMovie).title("DoJo Movie"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DoJoMovie, 15.0f))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }


}