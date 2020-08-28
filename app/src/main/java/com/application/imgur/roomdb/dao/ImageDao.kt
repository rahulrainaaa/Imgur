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

    @Query("SELECT * from Image where comment Not Null")
    fun getSelected(): LiveData<List<Image>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(image: List<Image>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: Image)

    @Query("DELETE FROM Image where comment IS Null")
    suspend fun deleteAll()
}