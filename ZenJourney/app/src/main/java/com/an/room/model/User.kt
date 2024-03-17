package com.an.room.model

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName="users")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "useCloud") val useCloud: Boolean,
    @ColumnInfo(name = "useJournalForAffirmations") val useJournalForAffirmations: Boolean,
    @ColumnInfo(name = "pin") val pin: String = "",
): java.io.Serializable;