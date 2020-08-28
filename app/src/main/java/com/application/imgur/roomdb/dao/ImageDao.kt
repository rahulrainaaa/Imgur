package com.application.imgur.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.application.imgur.Image

/**
 * DAO Module to fetch and other required DB operation(s) on Article(s).
 */
@Dao
interface ImageDao {

    @Query("SELECT * from Image")
    fun getAll(): LiveData<List<Image>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(articles: List<Image>)

    @Query("DELETE FROM Image")
    suspend fun deleteAll()
}