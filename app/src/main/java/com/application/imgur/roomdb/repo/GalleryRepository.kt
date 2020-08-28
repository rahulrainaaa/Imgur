package com.application.imgur.roomdb.repo

import androidx.lifecycle.LiveData
import com.application.imgur.Image
import com.application.imgur.roomdb.dao.ImageDao

/**
 * Repository class to fetch, insert and delete Article(s) in DB.
 */
class GalleryRepository(private val imageDao: ImageDao) {

    /**
     * LiveData fetched from the articleDao from RoomDB.
     */
    val allImages: LiveData<List<Image>> = imageDao.getAll()

    /**
     * Suspended method to insert list of Article(s) in to RoomDB.
     *
     * @param articles List<Article> to be inserted into DB.
     */
    suspend fun insert(articles: List<Image>) = imageDao.insert(articles)

    /**
     * Suspended method to delete all article records from DB.
     */
    suspend fun deleteAll() = imageDao.deleteAll()

}