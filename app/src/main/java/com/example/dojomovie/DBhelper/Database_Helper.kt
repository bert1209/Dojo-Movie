package com.example.dojomovie.DBhelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.dojomovie.DB.userDB

class Database_Helper(context: Context): SQLiteOpenHelper(context, "user.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val queryCreateUser =
                "CREATE TABLE IF NOT EXISTS Users( "+
                "id INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                "phoneNUM TEXT, " +
                "password TEXT" +
                ")"

        db?.execSQL(queryCreateUser)
    }

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

    fun getUserIDbyPhoneNumber(phoneNumber: String): String? {
        var userId: String? = null

        val db = readableDatabase
        val query = "SELECT id FROM Users WHERE phoneNUM = ?"
        val cursor = db.rawQuery(query, arrayOf(phoneNumber))

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id")).toString()
        }

        cursor.close()
        db.close()

        return userId
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
}