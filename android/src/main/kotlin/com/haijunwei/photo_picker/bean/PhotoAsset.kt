package com.haijunwei.photo_picker.bean

import com.haijunwei.photo_picker.Messages.PhotoPickerResult
import java.util.*

/**
 *
 * @CreateDate:     2021/4/26 15:50
 * @Description:
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
data class PhotoAsset(var filePath: String?=""){
    fun toMap(): Map<String, Any?>? {
        val toMapResult: MutableMap<String, Any?> = HashMap()
        toMapResult["filePath"] = filePath
        return toMapResult
    }

    fun fromMap(map: Map<String?, Any?>): PhotoAsset? {
        val fromMapResult = PhotoAsset()
        val filePath: String? = map["assets"].toString()
        fromMapResult.filePath = filePath
        return fromMapResult
    }
}