package com.application.imgur.roomdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.application.imgur.Image
import com.application.imgur.roomdb.dao.ImageDao

/**
 * RoomDB class to create and manage the single DB object for entire application lifecycle.
 */
@Database(entities = [Image::class], version = 1, exportSchema = false)
abstract class GalleryDatabase : RoomDatabase() {

    /**
     * Method to fetch ArticleDao object to access Images(s) from DB Gallery.
     */
    abstract fun imageDao(): ImageDao

    companion object {

        /**
         * Volatile companion object to save single instance of DB state for re-usability.
         */
        @Volatile
        private var INSTANCE: GalleryDatabase? = null

        /**
         * Companion Method to create DB object for first time.
         * Then return the same instance when called upon within that application lifespan.
         *
         * @param context Context object to create DB object.
         * @return ArticleDatabase DB object to access DAO service(s).
         */
        fun getDatabase(context: Context): GalleryDatabase {
            val tempInstance = INSTANCE

            // Return DB object is already was created.
            if (tempInstance != null) {
                return tempInstance
            }

            // Synchronize block for thread safe DB object creation.
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, GalleryDatabase::class.java, "article_db").build()
                INSTANCE = instance
                return instance
            }
        }
    }
}