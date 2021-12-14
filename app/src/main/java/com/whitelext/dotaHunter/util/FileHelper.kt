package com.whitelext.dotaHunter.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Log
import com.whitelext.dotaHunter.util.Utils.getBitmapFromURL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream

object FileHelper {

    enum class Folder(val folderName: String) {
        FAVORITES("favorites"),
        RANKS("ranks"),
    }

    enum class Entity(val entityName: String, val extension: String) {
        AVATAR("avatar", "png"),
        RANK("rank", "png"),
    }

    fun saveEntity(
        id: String,
        url: String,
        entity: Entity,
        folder: Folder,
        context: Context
    ): String? {
        val resultDir = File(context.filesDir, folder.folderName)
        if (!resultDir.exists()) {
            resultDir.mkdir()
        }

        val result = File(resultDir, getFileNameByEntity(id, entity))
        if (result.exists()) {
            return result.path
        }
        return try {
            CoroutineScope(Dispatchers.IO).launch {
                val fos = FileOutputStream(result)
                if (entity == Entity.AVATAR || entity == Entity.RANK) {
                    val bitmap = getBitmapFromURL(url) ?: throw Exception("bitmap is null")
                    bitmap.compress(CompressFormat.PNG, 100, fos)
                }
                fos.flush()
                fos.close()
            }
            result.path
        } catch (e: Exception) {
            Log.e("saveEntity: ", e.toString())
            null
        }
    }

    fun deleteEntity(id: String, entity: Entity, folder: Folder, context: Context) {
        val dir = File(context.filesDir, folder.folderName)
        if (!dir.exists()) {
            return
        }
        val file = File(dir, getFileNameByEntity(id, entity))
        if (!file.exists()) {
            return
        }
        try {
            file.delete()
        } catch (e: Exception) {
            Log.e("deleteEntity: ", e.toString())
        }
    }

    fun getImageEntity(id: String, entity: Entity, folder: Folder, context: Context): Bitmap? {
        try {
            val imageDir = File(context.filesDir, folder.folderName)
            if (!imageDir.exists()) {
                return null
            }
            val imageFile = File(imageDir, getFileNameByEntity(id, entity))
            if (!imageFile.exists()) {
                return null
            }
            return BitmapFactory.decodeStream(FileInputStream(imageFile))
        } catch (e: FileNotFoundException) {
            Log.e("getImageEntity: ", e.toString())
            return null
        }
    }

    private fun getFileNameByEntity(id: String, entity: Entity): String {
        return "${entity.entityName}_$id.${entity.extension}"
    }
}
