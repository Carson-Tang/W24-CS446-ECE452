package com.an.room.model

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName="journals")
data class Journal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "moods") val moods: List<String>,
    @ColumnInfo(name = "content") val content: String,
): java.io.Serializable;