package com.haijunwei.photo_picker.bean

import java.util.*

/**
 *
 * @CreateDate:     2021/4/26 15:50
 * @Description:
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
data class PhotoAsset(var filePath: String?="",var width:Double?=0.0,var height:Double?=0.0){
    fun toMap(): Map<String, Any?>? {
        val toMapResult: MutableMap<String, Any?> = HashMap()
        toMapResult["filePath"] = filePath
        toMapResult["width"] = width
        toMapResult["height"] = height
        return toMapResult
    }

    fun fromMap(map: Map<String?, Any?>): PhotoAsset? {
        val fromMapResult = PhotoAsset()
        val filePath: String? = map["assets"].toString()
        val width: Double? = map["width"] as Double?
        val height: Double? = map["height"] as Double?
        fromMapResult.filePath = filePath
        fromMapResult.width = width
        fromMapResult.height = height
        return fromMapResult
    }
}