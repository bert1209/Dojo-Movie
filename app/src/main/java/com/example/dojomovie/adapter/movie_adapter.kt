package com.example.dojomovie.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.lifecycle.viewmodel.savedstate.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.dojomovie.DB.movieDB
import com.example.dojomovie.DBhelper.Database_Helper
import com.example.dojomovie.OTPActivity
import com.example.dojomovie.productDetailActivity

class movie_adapter(private var movieList: ArrayList<movieDB>) : RecyclerView.Adapter<movie_adapter.MyViewHolder>(){

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePoster : ImageView = itemView.findViewById(com.example.dojomovie.R.id.posterFilmIV)
        val movieName : TextView = itemView.findViewById(com.example.dojomovie.R.id.movieNameTV)
        val moviePrice : TextView = itemView.findViewById(com.example.dojomovie.R.id.moviePriceTV)
    }
    private lateinit var Database_Helper : Database_Helper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.example.dojomovie.R.layout.movie_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dbHelper = Database_Helper(holder.itemView.context)
        val movieList = dbHelper.getAllMovies()

        val currentItem = movieList[position]

        val imageResId = holder.itemView.context.resources.getIdentifier(currentItem.moviePoster, "drawable", holder.itemView.context.packageName)

        if (imageResId != 0) {
            holder.moviePoster.setImageResource(imageResId)
        } else {
            Log.e("ImageLoading", "Resource ID not found for ${currentItem.moviePoster}")
        }

        holder.movieName.text = currentItem.movieName
        holder.moviePrice.text = "Rp ${currentItem.moviePrice}"

        holder.itemView.setOnClickListener {

            val context = it.context
            val intent = Intent(context, productDetailActivity::class.java)
            intent.putExtra("MoviePoster", currentItem.moviePoster)
            intent.putExtra("MovieName", currentItem.movieName)
            intent.putExtra("MoviePrice", currentItem.moviePrice)
            intent.putExtra("Movie_ID", currentItem.movieID)
            context.startActivity(intent)
        }
    }

//    fun getMovieFromDatabase(position: Int): movieDB {
//        val movies = Database_Helper.getAllMovies()
//        return movies[position]
//    }

}