package com.example.dojomovie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.SurfaceControl
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dojomovie.DB.UserSession
import com.example.dojomovie.DB.transaction
import com.example.dojomovie.DB.userDB
import com.example.dojomovie.DBhelper.Database_Helper

class productDetailActivity : AppCompatActivity() {

    private var currentQuantity = 0
    val dbHelper = Database_Helper(this)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.product_detail_activity)

        val backButton = findViewById<ImageView>(R.id.arrowBack)
        val poster = findViewById<ImageView>(R.id.posterMovie)
        val posterBackground = findViewById<ImageView>(R.id.posterBackground) // <-- NEW
        val judul = findViewById<TextView>(R.id.MovieTitle)
        val harga = findViewById<TextView>(R.id.MoviePrice)
        val quantity = findViewById<EditText>(R.id.quantity)
        val totalHarga = findViewById<TextView>(R.id.totalPriceText)
        val plusButton = findViewById<Button>(R.id.plus)
        val minButton = findViewById<Button>(R.id.minus)
        val buyNowButton = findViewById<Button>(R.id.buyNowButton)

        val MoviePoster = intent.getStringExtra("MoviePoster")
        val MovieName = intent.getStringExtra("MovieName")
        val MoviePrice = intent.getIntExtra("MoviePrice", 0)
        val Movie_ID = intent.getStringExtra("Movie_ID")

        backButton?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            Log.d("debug", "MASUK")
        }

        val imageResource = resources.getIdentifier(MoviePoster, "drawable", packageName)
        poster.setImageResource(imageResource)
        posterBackground.setImageResource(imageResource) // <-- NEW

        judul.text = MovieName
        harga.text = "Price : Rp $MoviePrice"

        quantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val qty = s.toString().toIntOrNull() ?: 0
                val totalPrice = qty * MoviePrice
                totalHarga.text = "Total Price: Rp $totalPrice"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        minButton.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity -= 1
                quantity.setText(currentQuantity.toString())
                updateTotalPrice(MoviePrice, totalHarga)
            }
        }

        plusButton.setOnClickListener {
            currentQuantity += 1
            quantity.setText(currentQuantity.toString())
            updateTotalPrice(MoviePrice, totalHarga)
        }

        buyNowButton.setOnClickListener {
            val qty = quantity.text.toString().toIntOrNull() ?: 0
            val loggedInUser = UserSession.currentLoggedInUser

            if (loggedInUser != null) {
                Log.d("Current User", "Logged in user: ${loggedInUser.phoneNum}")
            } else {
                Log.d("Current User", "No user is logged in.")
            }

            val transaction = transaction(
                transaction_id = 0,
                film_id = Movie_ID.toString(),
                user_id = loggedInUser?.id.toString(),
                quantity = qty
            )

            Log.d("Quantity", qty.toString())
            Log.d("MovID", Movie_ID.toString())
            Log.d("UserID", loggedInUser?.id.toString())

            dbHelper.insertTransaction(transaction)

            if (qty > 0) {
                Toast.makeText(this, "Transaction successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
            }
        }

        Log.d("data", MoviePrice.toString())
        Log.d("data", MoviePoster.toString())
        Log.d("data", MovieName.toString())
    }

    private fun updateTotalPrice(moviePrice: Int, totalPriceText: TextView) {
        val totalPrice = currentQuantity * moviePrice
        totalPriceText.text = "Total Price: Rp $totalPrice"
    }
}