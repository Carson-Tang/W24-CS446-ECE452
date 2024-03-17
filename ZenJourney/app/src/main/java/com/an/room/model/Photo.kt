package com.an.room.model

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName="photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "photoBase64") val photoBase64: String,
    @ColumnInfo(name = "uploadDate") val uploadDate: String,
): java.io.Serializable;