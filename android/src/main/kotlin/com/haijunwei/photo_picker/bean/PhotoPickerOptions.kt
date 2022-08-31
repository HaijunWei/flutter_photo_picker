package com.haijunwei.photo_picker.bean

import java.util.HashMap

/**
 *
 * @CreateDate:     2021/4/27 9:56
 * @Description:    Generated class from Pigeon that represents data sent in messages.
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
data class PhotoPickerOptions(
        var type: Int? = null,
        var maxAssetsCount: Int? = null,
        var allowEdit: Boolean? = null,
        var singleJumpEdit: Boolean? = null,
        var isRoundCliping: Boolean? = null,
        var photoEditCustomRatioW: Int? = null,
        var photoEditCustomRatioH: Int? = null,
        var imageSpanCount: Int? = null,
        var allowOpenCamera: Boolean? = null,
        var allowGif: Boolean? = null
) {
    fun toMap(): Map<String, Any?> {
        val toMapResult: MutableMap<String, Any?> = HashMap()
        toMapResult["type"] = type
        toMapResult["maxAssetsCount"] = maxAssetsCount
        toMapResult["allowEdit"] = allowEdit
        toMapResult["singleJumpEdit"] = singleJumpEdit
        toMapResult["isRoundCliping"] = isRoundCliping
        toMapResult["photoEditCustomRatioW"] = photoEditCustomRatioW
        toMapResult["photoEditCustomRatioH"] = photoEditCustomRatioH
        toMapResult["imageSpanCount"] = imageSpanCount
        toMapResult["allowOpenCamera"] = allowOpenCamera
        toMapResult["allowGif"] = allowGif
        return toMapResult
    }

    companion object {
        fun fromMap(map: Map<String?, Any?>?): PhotoPickerOptions {
            val fromMapResult = PhotoPickerOptions()
            if (map.isNullOrEmpty()) return fromMapResult

            fromMapResult.type = map["type"] as? Int
            fromMapResult.maxAssetsCount = map["maxAssetsCount"] as? Int
            fromMapResult.allowEdit = map["allowEdit"] as?  Boolean
            fromMapResult.singleJumpEdit = map["singleJumpEdit"] as? Boolean
            fromMapResult.isRoundCliping = map["isRoundCliping"] as? Boolean
            fromMapResult.photoEditCustomRatioW = map["photoEditCustomRatioW"] as? Int
            fromMapResult.photoEditCustomRatioH = map["photoEditCustomRatioH"] as? Int
            fromMapResult.imageSpanCount = map["imageSpanCount"] as? Int
            fromMapResult.allowOpenCamera = map["allowOpenCamera"] as? Boolean
            fromMapResult.allowGif = map["allowGif"] as? Boolean

            return fromMapResult
        }
    }
}