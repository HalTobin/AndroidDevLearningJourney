package com.chapeaumoineau.pocketlocker.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chapeaumoineau.pocketlocker.data.repository.StorageDestination

@Entity
data class Album(
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "salt", typeAffinity = ColumnInfo.BLOB) val salt: ByteArray,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "destination") val destination: Int,
    @ColumnInfo(name = "internal_url") val internalUrl: String,
) {

    val storageDestination: StorageDestination? get() {
        return when (this.destination) {
            StorageDestination.ID_INTERN -> StorageDestination.Intern
            StorageDestination.ID_EXTERN -> StorageDestination.Extern
            else -> null
        }
    }

}