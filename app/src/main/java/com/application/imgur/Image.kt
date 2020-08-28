package com.application.imgur

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Image")
class Image(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "link")
    var link: String? = null,

    @ColumnInfo(name = "comment")
    var comment: String? = null

)