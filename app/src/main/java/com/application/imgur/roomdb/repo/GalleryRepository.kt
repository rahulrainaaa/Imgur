package com.application.imgur.roomdb.repo

import androidx.lifecycle.LiveData
import com.application.imgur.Image
import com.application.imgur.roomdb.dao.ImageDao

/**
 * Repository class to fetch, insert and delete Article(s) in DB.
 */
class GalleryRepository(private val imageDao: ImageDao) {

    /**
     * LiveData fetched from the imageDao from RoomDB.
     */
    val allImages: LiveData<List<Image>> = imageDao.getAll()

    /**
     * LiveData fetched from the imageDao from RoomDB.
     */
    val selectedImages: LiveData<List<Image>> = imageDao.getSelected()

    /**
     * Suspended method to insert list of Image(s) in to RoomDB.
     *
     * @param images List<Image> to be inserted into DB.
     */
    suspend fun insert(images: List<Image>) = imageDao.insert(images)

    /**
     * Suspended method to insert or update Image data in to RoomDB.
     *
     * @param image image to be inserted or updated into DB.
     */
    suspend fun insert(image: Image) = imageDao.insert(image)

    /**
     * Suspended method to delete all image records from DB.
     */
    suspend fun deleteAll() = imageDao.deleteAll()

}