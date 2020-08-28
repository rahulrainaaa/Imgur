package com.application.imgur

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.application.imgur.roomdb.database.GalleryDatabase
import com.application.imgur.roomdb.repo.GalleryRepository

class SecondFragmentViewModel : ViewModel() {

    private lateinit var imageRepository: GalleryRepository
    lateinit var selectedImages: LiveData<List<Image>>
    private var initialize = false

    fun initialize(context: Context): SecondFragmentViewModel {
        if (initialize) return this // Avoid multiple initialization.
        val imageDao = GalleryDatabase.getDatabase(context).imageDao()
        imageRepository = GalleryRepository(imageDao)
        selectedImages = imageRepository.selectedImages
        return this
    }
}