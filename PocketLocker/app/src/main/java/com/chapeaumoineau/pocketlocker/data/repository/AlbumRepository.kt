package com.chapeaumoineau.pocketlocker.data.repository

import android.app.Application
import android.util.Log
import com.chapeaumoineau.pocketlocker.data.database.dao.AlbumDao
import com.chapeaumoineau.pocketlocker.data.model.Album
import com.chapeaumoineau.pocketlocker.util.CryptUtil
import com.chapeaumoineau.pocketlocker.util.CryptUtil.decryptString
import com.chapeaumoineau.pocketlocker.util.CryptUtil.encryptString
import com.chapeaumoineau.pocketlocker.util.CryptUtil.generateKey
import com.chapeaumoineau.pocketlocker.util.CryptUtil.generateSalt
import kotlinx.coroutines.flow.Flow
import javax.crypto.BadPaddingException
import javax.inject.Inject


class AlbumRepository @Inject constructor(
    app: Application,
    private val dao: AlbumDao
) {

    fun createAlbum(name: String, destination: StorageDestination, key: String) {
        val salt: ByteArray = generateSalt()
        val secret = generateKey(key, salt)
        val album = Album(
            tag = encryptString(ALBUM_TAG, secret),
            salt = salt,
            title = name,
            destination = destination.id,
            internalUrl = encryptString(name, secret)
        )
        dao.insertAlbum(album)
    }

    fun getAllAlbums(): Flow<List<Album>> = dao.getAllAlbums()

    fun getAlbumBydId(id: Int, key: String): Album? {
        val album = dao.getAlbumById(id)
        return try {
            val toTest = decryptString(album.tag, generateKey(key, album.salt))
            if (toTest == ALBUM_TAG) album
            else null
        } catch (e: Exception) {
            Log.w("DECRYPT ERROR", e.stackTraceToString())
            null
        }
    }

    companion object {
        const val ALBUM_TAG = "ALBUM"
    }

}

sealed class StorageDestination(val id: Int) {
    object Intern: StorageDestination(id = ID_INTERN)
    object Extern: StorageDestination(id = ID_EXTERN)

    companion object {
        const val ID_INTERN = 1
        const val ID_EXTERN = 2
    }
}