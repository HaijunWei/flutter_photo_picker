package com.haijunwei.photo_picker

import com.haijunwei.photo_picker.bean.PhotoPickerOptions
import com.haijunwei.photo_picker.bean.PhotoPickerResult
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import java.util.*

/** Generated class from Pigeon.  */
object Messages {
    /** Sets up an instance of `PhotoPicker` to handle messages through the `binaryMessenger`.  */
    fun setup(binaryMessenger: BinaryMessenger?, api: PhotoPicker?) {
        run {
            pickPhoto(binaryMessenger,api)
            pickPhotoFromCamera(binaryMessenger,api)
        }
    }


    private fun pickPhoto(binaryMessenger: BinaryMessenger?, api: PhotoPicker?){
        val channel = BasicMessageChannel(binaryMessenger!!, "dev.flutter.pigeon.PhotoPicker.pickPhoto", StandardMessageCodec())
        if (api == null) {
            channel.setMessageHandler(null)
            return
        }

        channel.setMessageHandler { message: Any?, reply: BasicMessageChannel.Reply<Any> ->
            val wrapped: MutableMap<String, Any> = HashMap()
            try {
                val input = PhotoPickerOptions.fromMap(message as Map<String?, Any?>?)
                api.pickPhoto(input, object : Result<PhotoPickerResult?> {
                    override fun success(result: PhotoPickerResult?) {
                        wrapped["result"] = result?.toMap()?:""
                        reply.reply(wrapped)
                    }
                })
            } catch (exception: Error) {
                wrapped["error"] = wrapError(exception)
                reply.reply(wrapped)
            } catch (exception: RuntimeException) {
                wrapped["error"] = wrapError(exception)
                reply.reply(wrapped)
            }
        }
    }

    private fun pickPhotoFromCamera(binaryMessenger: BinaryMessenger?, api: PhotoPicker?){
        val channel = BasicMessageChannel(binaryMessenger!!, "dev.flutter.pigeon.PhotoPicker.pickPhotoFromCamera", StandardMessageCodec())
        if (api == null) {
            channel.setMessageHandler(null)
            return
        }
        channel.setMessageHandler { message: Any?, reply: BasicMessageChannel.Reply<Any> ->
            val wrapped: MutableMap<String, Any> = HashMap()
            try {
                val input = PhotoPickerOptions.fromMap(message as Map<String?, Any?>?)
                api.openCamera(input, object : Result<PhotoPickerResult?> {
                    override fun success(result: PhotoPickerResult?) {
                        wrapped["result"] = result?.toMap()?:""
                        reply.reply(wrapped)
                    }
                })
            } catch (exception: Error) {
                wrapped["error"] = wrapError(exception)
                reply.reply(wrapped)
            } catch (exception: RuntimeException) {
                wrapped["error"] = wrapError(exception)
                reply.reply(wrapped)
            }
        }
    }
}

private fun wrapError(exception: Throwable): Map<String, Any?> {
    val errorMap: MutableMap<String, Any?> = HashMap()
    errorMap["message"] = exception.toString()
    errorMap["code"] = exception.javaClass.simpleName
    errorMap["details"] = null
    return errorMap
}


interface Result<T> {
    fun success(result: T)
}

/** Generated interface from Pigeon that represents a handler of messages from Flutter. */
interface PhotoPicker {
    fun pickPhoto(options: PhotoPickerOptions?, result: Result<PhotoPickerResult?>?)

    fun openCamera(options: PhotoPickerOptions?,result: Result<PhotoPickerResult?>?)
}