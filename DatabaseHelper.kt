package com.godrone.app

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "GoDrone.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_SECOND_NAME = "second_name"
        const val COLUMN_FIRST_LAST_NAME = "first_last_name"
        const val COLUMN_SECOND_LAST_NAME = "second_last_name"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_USER_TYPE = "user_type"
        const val COLUMN_PILOT_CODE = "pilot_code"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FIRST_NAME TEXT,
                $COLUMN_SECOND_NAME TEXT,
                $COLUMN_FIRST_LAST_NAME TEXT,
                $COLUMN_SECOND_LAST_NAME TEXT,
                $COLUMN_USERNAME TEXT UNIQUE,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_USER_TYPE TEXT,
                $COLUMN_PILOT_CODE TEXT
            )
        """
        db.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    /**
     * Insert a new user into the database.
     * @param user ContentValues containing user data.
     * @return Long - ID of the inserted row, or -1 if an error occurred.
     */
    fun insertUser(user: ContentValues): Long {
        val db = writableDatabase
        return db.insert(TABLE_USERS, null, user)
    }

    /**
     * Authenticate a user by username and password.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return Boolean - true if the user exists and credentials match, false otherwise.
     */
    fun authenticateUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password),
            null,
            null,
            null
        )
        val isAuthenticated = cursor.count > 0
        cursor.close()
        return isAuthenticated
    }

    /**
     * Get additional user details (e.g., user type and pilot code) after authentication.
     * @param username The username of the user.
     * @return Cursor containing the user details or null if not found.
     */
    fun getUserDetails(username: String): Cursor? {
        val db = readableDatabase
        return db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_TYPE, COLUMN_PILOT_CODE),
            "$COLUMN_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )
    }
}
