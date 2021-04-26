package com.haijunwei.photo_picker

import android.app.Application
import android.src.main.kotlin.com.loper7.image_picker.CoilEngine
import androidx.annotation.NonNull
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** PhotoPickerPlugin */
class PhotoPickerPlugin : FlutterPlugin, MethodCallHandler, ActivityAware, Messages.PhotoPicker {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var activityBinding: ActivityPluginBinding? = null

    companion object {
        fun registerWith(registrar: Registrar) {
            if (registrar.activity() == null) {
                // If a background flutter view tries to register the plugin, there will be no activity from the registrar,
                // we stop the registering process immediately because the ImagePicker requires an activity.
                return
            }
            val plugin = PhotoPickerPlugin()
            Messages.setup(registrar.messenger(), plugin)
        }
    }


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "photo_picker")
        channel.setMethodCallHandler(this)
        Messages.setup(flutterPluginBinding.binaryMessenger, this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun pickPhoto(arg: Messages.PhotoPickerOptions?, result: Messages.Result<Messages.PhotoPickerResult>?) {
        activityBinding?.activity.apply {
            PictureSelector.create(this)
                    .openGallery(PictureMimeType.ofAll())
                    .imageEngine(CoilEngine.create())
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(data: List<LocalMedia?>) {
                            val resultData = Messages.PhotoPickerResult()
                            resultData.assets = mutableListOf()
                            for (i in data) {
                                resultData.assets.add(i?.compressPath?:i?.realPath)
                            }

                            result?.success(resultData)
                        }

                        override fun onCancel() {
                            result?.success(Messages.PhotoPickerResult())
                        }
                    })
        }
    }

    override fun onDetachedFromActivity() {
        activityBinding = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }
}
