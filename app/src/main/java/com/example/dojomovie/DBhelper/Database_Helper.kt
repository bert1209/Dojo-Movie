package com.example.dojomovie.DBhelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.dojomovie.DB.movieDB
import com.example.dojomovie.DB.transaction
import com.example.dojomovie.DB.userDB

class Database_Helper(context: Context): SQLiteOpenHelper(context, "user.db", null, 1) {

    var currentLoggedInUser: userDB? = null

    override fun onCreate(db: SQLiteDatabase?) {
        val queryCreateUser = """
        CREATE TABLE IF NOT EXISTS Users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            phoneNUM TEXT UNIQUE,
            password TEXT
        )
    """.trimIndent()
        db?.execSQL(queryCreateUser)

        val queryCreateMovie = """
        CREATE TABLE IF NOT EXISTS Movies (
            Movie_ID TEXT PRIMARY KEY,
            MoviePoster TEXT,
            MovieName TEXT,
            MoviePrice INTEGER
        )
    """.trimIndent()
        db?.execSQL(queryCreateMovie)

        val queryCreateTransaction = """
        CREATE TABLE IF NOT EXISTS Transactions (
            Transaction_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            User_ID INTEGER,
            Movie_ID TEXT,
            Quantity INTEGER
        )
    """.trimIndent()
        db?.execSQL(queryCreateTransaction)
    }


    var logged_user: userDB? = null


    fun insertUser(users: userDB){
        val db = writableDatabase
        val values = ContentValues().apply {
            put("phoneNUM", users.phoneNum)
            put("password", users.password)
        }
        Log.d("data","data ${users.phoneNum} ${users.password}" )
        db.insert("Users", null, values)
        db.close()

    }

    fun getUser() : ArrayList<userDB> {
        val users = ArrayList<userDB>()

        val db = readableDatabase
        val query = "SELECT * FROM Users"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        if(cursor.count > 0) {
            do {
                val user = userDB()
                user.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                user.phoneNum = cursor.getString(cursor.getColumnIndexOrThrow("phoneNUM"))
                user.password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return users
    }

    fun insertAllMovies(movieList: ArrayList<movieDB>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (movie in movieList) {
                val values = ContentValues().apply {
                    put("Movie_ID", movie.movieID)
                    put("MoviePoster", movie.moviePoster)
                    put("MovieName", movie.movieName)
                    put("MoviePrice", movie.moviePrice)
                }
                db.insertWithOnConflict("Movies", null, values, SQLiteDatabase.CONFLICT_IGNORE)
            }
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DBHelper", "Insert error: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun insertTransaction(transaction: transaction) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues().apply {
                put("Movie_ID", transaction.film_id)
                put("Quantity", transaction.quantity)
                put("User_ID", transaction.user_id)
            }
            db.insertWithOnConflict("Transactions", null, values, SQLiteDatabase.CONFLICT_IGNORE)

            db.setTransactionSuccessful()
        } catch (e: Exception) {
            Log.e("DBHelper", "Insert error: ${e.message}")
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    @SuppressLint("Range")
    fun getTransactionHistory(userId: String): List<transaction> {
        val db = readableDatabase
        val transactionHistory = mutableListOf<transaction>()

        val query = "SELECT * FROM Transactions WHERE User_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId))

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val transaction = transaction(
                    transaction_id = cursor.getInt(cursor.getColumnIndexOrThrow("Transaction_ID")),
                    film_id = cursor.getString(cursor.getColumnIndexOrThrow("Movie_ID")),
                    user_id = cursor.getString(cursor.getColumnIndexOrThrow("User_ID")),
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"))
                )
                transactionHistory.add(transaction)
            } while (cursor.moveToNext())
        } else {
            Log.e("DBHelper", "No data found for user ID: $userId")
        }

        cursor.close()
        db.close()
        return transactionHistory
    }



    fun printAllMovies() {
        val db = readableDatabase
        val query = "SELECT * FROM Movies"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow("Movie_ID"))
                val poster = cursor.getString(cursor.getColumnIndexOrThrow("MoviePoster"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("MovieName"))
                val price = cursor.getInt(cursor.getColumnIndexOrThrow("MoviePrice"))

                Log.d("DB_MOVIE", "ID: $id, Poster: $poster, Name: $name, Price: $price")
            } while (cursor.moveToNext())
        } else {
            Log.d("DB_MOVIE", "No data found in Movies table.")
        }

        cursor.close()
        db.close()
    }
    fun getAllMovies(): ArrayList<movieDB> {
        val movies = ArrayList<movieDB>()
        val db = readableDatabase
        val query = "SELECT * FROM Movies"
        val cursor = db.rawQuery(query, null)

        try {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndexOrThrow("Movie_ID"))
                    val poster = cursor.getString(cursor.getColumnIndexOrThrow("MoviePoster"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("MovieName"))
                    val price = cursor.getInt(cursor.getColumnIndexOrThrow("MoviePrice"))


                    val movie = movieDB(id, poster, name, price)
                    movies.add(movie)
                } while (cursor.moveToNext())
            } else {
                Log.d("DB_MOVIE", "No data found in Movies table.")
            }
        } catch (e: Exception) {
            Log.e("DB_MOVIE", "Error while accessing database: ${e.message}")
        } finally {
            cursor.close()
            db.close()
        }

        return movies
    }




    fun getMovieById(movieID: String): movieDB? {
        var result: movieDB? = null
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT Movie_ID, MoviePoster, MovieName, MoviePrice FROM Movies WHERE Movie_ID = ?",
            arrayOf(movieID)
        )
        if (cursor.moveToFirst()) {
            result = movieDB(
                movieID = cursor.getString(cursor.getColumnIndexOrThrow("Movie_ID")),
                moviePoster = cursor.getString(cursor.getColumnIndexOrThrow("MoviePoster")),
                movieName = cursor.getString(cursor.getColumnIndexOrThrow("MovieName")),
                moviePrice = cursor.getInt(cursor.getColumnIndexOrThrow("MoviePrice"))
            )
        }
        cursor.close()
        db.close()
        return result
    }


    fun getPhoneNumberById(id: String): String? {
        var userPhoneNumber: String? = null

        val db = readableDatabase
        val query = "SELECT phoneNUM FROM Users WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(id))

        if (cursor.moveToFirst()) {
            userPhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phoneNUM"))
        }

        cursor.close()
        db.close()

        return userPhoneNumber
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Users")
        onCreate(db)
    }

    fun updateMoviePoster1() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("MoviePoster", "kong")
        }

        val whereClause = "Movie_ID = ?"
        val whereArgs = arrayOf("MV001")

        // Melakukan update pada tabel Movies
        val rowsUpdated = db.update("Movies", values, whereClause, whereArgs)

        if (rowsUpdated > 0) {
            Log.d("DBHelper", "Movie poster updated successfully.")
        } else {
            Log.d("DBHelper", "Movie poster update failed.")
        }

        db.close()
    }
    fun updateMoviePoster2() {
        val db = writableDatabase


        val values = ContentValues().apply {
            put("MoviePoster", "finalll")
        }

        val whereClause = "Movie_ID = ?"
        val whereArgs = arrayOf("MV002")

        // Melakukan update pada tabel Movies
        val rowsUpdated = db.update("Movies", values, whereClause, whereArgs)

        if (rowsUpdated > 0) {
            Log.d("DBHelper", "Movie poster updated successfully.")
        } else {
            Log.d("DBHelper", "Movie poster update failed.")
        }

        db.close()
    }fun updateMoviePoster3() {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("MoviePoster", "bond")
        }

        // Menentukan kondisi untuk update berdasarkan Movie_ID
        val whereClause = "Movie_ID = ?"
        val whereArgs = arrayOf("MV003")

        // Melakukan update pada tabel Movies
        val rowsUpdated = db.update("Movies", values, whereClause, whereArgs)

        if (rowsUpdated > 0) {
            Log.d("DBHelper", "Movie poster updated successfully.")
        } else {
            Log.d("DBHelper", "Movie poster update failed.")
        }

        db.close()
    }





}