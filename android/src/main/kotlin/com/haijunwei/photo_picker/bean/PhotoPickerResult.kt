package com.haijunwei.photo_picker.bean

import java.util.ArrayList
import java.util.HashMap

/**
 *
 * @CreateDate:     2021/4/27 9:55
 * @Description:    Generated class from Pigeon that represents data sent in messages.
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
data class PhotoPickerResult(var assets: MutableList<PhotoAsset>? = null) {
    fun toMap(): Map<String, List<Any?>?> {
        val toMapResult: MutableMap<String, List<Any?>?> = HashMap()
        val objects: MutableList<Any?> = ArrayList()
        if (assets == null) {
            toMapResult["assets"] = null
            return toMapResult
        }
        for (i in assets!!.indices) {
            objects.add(assets!![i].toMap())
        }
        toMapResult["assets"] = objects
        return toMapResult
    }
}