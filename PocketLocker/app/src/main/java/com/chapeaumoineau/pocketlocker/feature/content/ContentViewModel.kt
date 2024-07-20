package com.chapeaumoineau.pocketlocker.feature.content

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chapeaumoineau.pocketlocker.data.repository.AlbumRepository
import com.chapeaumoineau.pocketlocker.util.CryptUtil.decryptByteArray
import com.chapeaumoineau.pocketlocker.util.CryptUtil.decryptString
import com.chapeaumoineau.pocketlocker.util.CryptUtil.encryptByteArray
import com.chapeaumoineau.pocketlocker.util.CryptUtil.encryptString
import com.chapeaumoineau.pocketlocker.util.CryptUtil.generateKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import javax.crypto.SecretKey
import javax.inject.Inject


@HiltViewModel
class ContentViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val app: Application,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(ContentState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private lateinit var _fileDirectory: String
    private lateinit var _secret: SecretKey
    private val _tempDirectory = "${app.filesDir}/temp/"

    init {
        savedStateHandle.get<Int>("albumId")?.let { albumId ->
            savedStateHandle.get<String>("key")?.let { key ->
                viewModelScope.launch(Dispatchers.IO) {
                    val album = albumRepository.getAlbumBydId(albumId, key)
                    _state.update { it.copy(album = album) }
                    album?.let {
                        _secret = generateKey(key, album.salt)
                        _fileDirectory = "${app.filesDir}/${encryptString(album.title, _secret)}/"
                        loadFiles()
                    }
                }
            }
        }
    }

    fun onEvent(event: ContentEvent) {
        when (event) {
            is ContentEvent.AddElements -> viewModelScope.launch(Dispatchers.IO) {
                _state.value.album?.let { album ->
                    _state.update { it.copy(loadingData = LoadingData(0f, 0, event.uriList.size)) }
                    event.uriList.forEachIndexed { index, uri ->
                        _state.update { it.copy(loadingData = _state.value.loadingData!!.copy(current = index)) }
                        importFile(uri, System.currentTimeMillis().toString())
                    }
                    loadFiles()
                    _state.update { it.copy(loadingData = null) }
                }
            }
            is ContentEvent.DeleteElement -> viewModelScope.launch(Dispatchers.IO) {
                deleteFile(event.path)
                _state.value.album?.let { album ->
                    loadFiles()
                }
            }
        }
    }

    private val Uri.byteArray: ByteArray? get() {
        val resolver = app.contentResolver

        // this dynamically extends to take the bytes you read
        val inputStream: InputStream? = resolver.openInputStream(this)
        val byteBuffer = ByteArrayOutputStream()

        inputStream?.let { stream ->
            // this is storage overwritten on each iteration with bytes
            val bufferSize = 1024
            val buffer = ByteArray(bufferSize)

            // we need to know how may bytes were read to write them to the
            // byteBuffer
            var len = 0
            while (stream.read(buffer).also { len = it } != -1) {
                byteBuffer.write(buffer, 0, len)
            }

            // and then we can return your byte array.
            stream.close()
            return byteBuffer.toByteArray()
        }
        return null
    }

    private val ByteArray.encrypted: ByteArray get() = encryptByteArray(this, _secret)

    private val ByteArray.decrypted: ByteArray get() = decryptByteArray(this, _secret)

    private val Uri.realPath: String? get() {
        val cursor: Cursor? = app.contentResolver.query(this, null, null, null)
        val path = if (cursor == null) { this.path }
        else {
            cursor.moveToFirst()
            val index: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(index)
        }
        cursor?.close()
        return path
    }

    private val Uri.extension: String? get() {
        this.realPath?.let { path ->
            return path.substring(path.lastIndexOf(".").plus(1))
        }
        return null
    }

    private fun loadFiles() = viewModelScope.launch(Dispatchers.IO) {
        val files = File(_fileDirectory).listFiles()
        _state.update { it.copy(loadingData = LoadingData(0f, 0, files?.size ?: 1)) }
        files?.forEachIndexed { index, file ->
            _state.update { it.copy(loadingData = _state.value.loadingData!!.copy(current = index)) }
            Log.i("FILENAME", file.name)
            val name = file.name
            val data = file.readBytes().decrypted
            writeFile(data, _tempDirectory, name)
        }
        val pathList = File(_tempDirectory).listFiles()?.map { it.path } ?: emptyList()
        _state.update { it.copy(elements = pathList) }
        _state.update { it.copy(loadingData = null) }
        Log.i("FILES FOUND", pathList.toString())
    }

    private fun importFile(uri: Uri, name: String) {
        try {
            Log.i("ENCRYPTION", "Input file: $name")

            val encryptedData = uri.byteArray?.encrypted
            if (encryptedData != null) {
                val fileName = encryptString("$name.${uri.extension}", _secret)
                writeFile(encryptedData, _fileDirectory, "$fileName.plc")
            }
            else Log.w("ENCRYPTION", "Null is null, nothing to save.")


        } catch (e: Exception) {
            Log.w("ENCRYPTION", "Couldn't encrypt file")
            e.printStackTrace()
            // Handle exceptions such as IOException or other relevant exceptions
        }
    }

    private fun writeFile(data: ByteArray, directory: String, fileName: String) {
        val outputDirectory = File(directory)
        outputDirectory.mkdirs()

        val outputFile = File(directory, fileName.replace("/", ""))
        Log.i("WRITING", outputFile.toUri().toString())

        BufferedOutputStream(FileOutputStream(outputFile)).use { outputStream ->
            outputStream.write(data)
        }
    }

    private fun deleteFile(name: String) =
        File("${app.filesDir}/$_fileDirectory", name).canonicalFile.delete()

    sealed class UiEvent {

    }

    override fun onCleared() {
        val pathList = File(_tempDirectory).listFiles()
        pathList?.forEach { file ->
            file.delete()
        }
        super.onCleared()
    }

}