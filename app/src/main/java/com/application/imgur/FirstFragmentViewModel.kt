package com.application.imgur

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.imgur.roomdb.database.GalleryDatabase
import com.application.imgur.roomdb.repo.GalleryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class FirstFragmentViewModel : ViewModel() {

    // Repositories.
    private lateinit var imageRepository: GalleryRepository

    // Live Data to observe on.
    val flagHttpProcessingLiveData = MutableLiveData(false)
    var flagWebImagesEof = false
    lateinit var allImages: LiveData<List<Image>>

    // Objects for saving state against screen orientation change.
    var strQuery: String = ""
    var pageNo: Int = 1
    var initialize: Boolean = false

    fun initialize(context: Context): FirstFragmentViewModel {
        if (initialize) return this // Avoid multiple initialization.
        val imageDao = GalleryDatabase.getDatabase(context).imageDao()
        imageRepository = GalleryRepository(imageDao)
        allImages = imageRepository.allImages
        return this
    }

    fun fetchContent(page: Int = pageNo, keyword: String = strQuery) = viewModelScope.launch {

        if (flagHttpProcessingLiveData.value == true) return@launch
        strQuery = keyword
        pageNo = page
        if (page <= 1) {
            imageRepository.deleteAll()
            flagWebImagesEof = false
        }
        flagHttpProcessingLiveData.value = true
        val httpResponse = HttpHandler().get("https://api.imgur.com/3/gallery/search/$page?q=$keyword")
        parseAndCacheResponseBody(httpResponse)
        flagHttpProcessingLiveData.value = false
    }

    fun updateDBImage(image: Image) = viewModelScope.launch(Dispatchers.IO) {
        imageRepository.insert(image)
    }

    private fun parseAndCacheResponseBody(httpResponse: HttpHandler.WebCallModel) = viewModelScope.launch(Dispatchers.Default) {

        if (httpResponse.code == 200) {        // Successful Response.
            val jsonImageData = JSONObject(httpResponse.body)
            if (jsonImageData.getInt("status") == 200 && jsonImageData.getBoolean("success")) {

                val fetchedImageList = ArrayList<Image>()
                val jsonArrayPostData = jsonImageData.getJSONArray("data")
                for (dataIndex in 0 until jsonArrayPostData.length()) {

                    val jsonArrayImagesTemp = jsonArrayPostData.getJSONObject(dataIndex)
                    if (jsonArrayImagesTemp.has("images")) {

                        val jsonArrayImages = jsonArrayImagesTemp.getJSONArray("images")
                        for (imageIndex in 0 until jsonArrayImages.length()) {

                            val jsonImage = jsonArrayImages.getJSONObject(imageIndex)
                            if (jsonImage.optString("type") == "image/jpeg") {

                                val link = jsonImage.optString("link")
                                fetchedImageList.add(Image(link = link))
                            }
                        }
                    }
                }

                if (fetchedImageList.isEmpty()) flagWebImagesEof = true
                imageRepository.insert(fetchedImageList)
            }
        } else if (httpResponse.code == -1) {      // Exception occurred.

        } else {

        }
    }
}
