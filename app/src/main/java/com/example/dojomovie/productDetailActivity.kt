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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dojomovie.DB.UserSession
import com.example.dojomovie.DB.transaction
import com.example.dojomovie.DB.userDB
import com.example.dojomovie.DBhelper.Database_Helper
import jp.wasabeef.glide.transformations.BlurTransformation

class productDetailActivity : AppCompatActivity() {
    private var blurImage : ImageView ?=null
    private var currentQuantity = 0
    val dbHelper = Database_Helper(this)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.product_detail_activity)

        val backButton = findViewById<ImageView>(R.id.arrowBack)
        val poster = findViewById<ImageView>(R.id.posterMovie)
        val posterBackground = findViewById<ImageView>(R.id.posterBackground)
        val judul = findViewById<TextView>(R.id.MovieTitle)
        val harga = findViewById<TextView>(R.id.MoviePrice)
        val quantity = findViewById<EditText>(R.id.quantity)
        val totalHarga = findViewById<TextView>(R.id.totalPriceText)
        val plusButton = findViewById<Button>(R.id.plus)
        val minButton = findViewById<Button>(R.id.minus)
        val buyNowButton = findViewById<Button>(R.id.buyNowButton)

        // Movie data from intent
        val MoviePoster = intent.getStringExtra("MoviePoster")
        val MovieName = intent.getStringExtra("MovieName")
        val MoviePrice = intent.getIntExtra("MoviePrice", 0)
        val Movie_ID = intent.getStringExtra("Movie_ID")

        // Load drawable resource
        val imageResource = resources.getIdentifier(MoviePoster, "drawable", packageName)

        if (imageResource != 0) {
            // Normal poster (no blur)
            poster.setImageResource(imageResource)

            // Blurred background
            Glide.with(this)
                .load(imageResource)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(10, 3)))
                .into(posterBackground)
        } else {
            Toast.makeText(this, "Image not found: $MoviePoster", Toast.LENGTH_SHORT).show()
            Log.e("Image Load", "Drawable not found for name: $MoviePoster")
        }

        // Set title and price
        judul.text = MovieName
        harga.text = "Price : Rp $MoviePrice"

        // Quantity listener
        quantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val qty = s.toString().toIntOrNull() ?: 0
                val totalPrice = qty * MoviePrice
                totalHarga.text = "Total Price: Rp $totalPrice"
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Quantity buttons
        minButton.setOnClickListener {
            if (currentQuantity > 1) {
                currentQuantity--
                quantity.setText(currentQuantity.toString())
                updateTotalPrice(MoviePrice, totalHarga)
            }
        }

        plusButton.setOnClickListener {
            currentQuantity++
            quantity.setText(currentQuantity.toString())
            updateTotalPrice(MoviePrice, totalHarga)
        }

        // Buy button
        buyNowButton.setOnClickListener {
            val qty = quantity.text.toString().toIntOrNull() ?: 0
            val loggedInUser = UserSession.currentLoggedInUser

            if (loggedInUser == null) {
                Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val transaction = transaction(
                transaction_id = 0,
                film_id = Movie_ID ?: "",
                user_id = loggedInUser.id.toString(),
                quantity = qty
            )

            if (qty > 0) {
                dbHelper.insertTransaction(transaction)
                Toast.makeText(this, "Transaction successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
            } else {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
            }
        }

        // Back button
        backButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun updateTotalPrice(moviePrice: Int, totalPriceText: TextView) {
        val totalPrice = currentQuantity * moviePrice
        totalPriceText.text = "Total Price: Rp $totalPrice"
    }
}