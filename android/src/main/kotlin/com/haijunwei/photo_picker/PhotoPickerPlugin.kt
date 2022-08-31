package com.haijunwei.photo_picker

import android.util.Log
import androidx.annotation.NonNull
import com.haijunwei.photo_picker.bean.PhotoAsset
import com.haijunwei.photo_picker.bean.PhotoPickerOptions
import com.haijunwei.photo_picker.bean.PhotoPickerResult
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.haijunwei.photo_picker.engine.GlideEngine
import com.haijunwei.photo_picker.engine.UCropEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** PhotoPickerPlugin */
class PhotoPickerPlugin : FlutterPlugin, MethodCallHandler, ActivityAware, PhotoPicker {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private var activityBinding: ActivityPluginBinding? = null

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


    override fun pickPhoto(
        options: PhotoPickerOptions?,
        result: com.haijunwei.photo_picker.Result<PhotoPickerResult?>?
    ) {
        activityBinding?.activity?.apply {

            Log.d("loper7", "options->${options?.toMap().toString()}")

            //媒体类型
            val mediaType = options?.type?.let {
                //选择照片类型，0 = 图片，1 = 视频，2 = 混合
                when (it) {
                    0 -> SelectMimeType.ofImage()
                    1 -> SelectMimeType.ofVideo()
                    else -> SelectMimeType.ofAll()
                }
            } ?: SelectMimeType.ofAll()

            //选择模式
            val selectionMode = options?.maxAssetsCount?.let {
                when {
                    it > 1 -> SelectModeConfig.MULTIPLE
                    else -> SelectModeConfig.SINGLE
                }
            } ?: SelectModeConfig.SINGLE

            //单选模式下是否直接返回
            val isSingleDirectReturn = options?.singleJumpEdit ?: true

            //最大选择数量
            val maxSelectNum = options?.maxAssetsCount ?: 1

            //是否开启裁剪
            val isEnableCrop = options?.allowEdit ?: true

            //是否开启圆形裁剪
            val isCircleDimmedLayer = options?.isRoundCliping ?: false

            //裁剪宽高比例
            val photoEditCustomRatioW = options?.photoEditCustomRatioW ?: 0
            val photoEditCustomRatioH = options?.photoEditCustomRatioH ?: 0

            //列表每行显示个数
            val imageSpanCount = options?.imageSpanCount ?: 4

            //是否允许打开相机
            val allowOpenCamera = options?.allowOpenCamera ?: true

            //是否加载gif
            val allowGif = options?.allowGif ?: true


            val cropEngine =
                UCropEngine(isCircleDimmedLayer, photoEditCustomRatioW, photoEditCustomRatioH)

            var selector = PictureSelector.create(this)
                .openGallery(mediaType)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setSelectionMode(selectionMode)
                .isDirectReturnSingle(isSingleDirectReturn)
                .setMaxSelectNum(maxSelectNum)
                .setImageSpanCount(imageSpanCount)
                .isPreviewImage(true)
                .isGif(allowGif)
                .isMaxSelectEnabledMask(true)
                .isAutomaticTitleRecyclerTop(true)
                .isDisplayCamera(allowOpenCamera)

            if (isEnableCrop) {
                selector = selector.setCropEngine(cropEngine)
            }

            selector.forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(data: ArrayList<LocalMedia?>) {
                    val resultData = PhotoPickerResult()
                    resultData.assets = mutableListOf()
                    for (i in data) {
                        val photoAsset = PhotoAsset(i?.compressPath ?: i?.realPath)
                        photoAsset.width = i?.width?.toDouble()
                        photoAsset.height = i?.height?.toDouble()
                        resultData.assets?.add(photoAsset)
                    }

                    result?.success(resultData)
                }

                override fun onCancel() {
                    val resultData = PhotoPickerResult()
                    resultData.assets = mutableListOf()
                    result?.success(resultData)
                }
            })
        }
    }

    override fun openCamera(
        options: PhotoPickerOptions?,
        result: com.haijunwei.photo_picker.Result<PhotoPickerResult?>?
    ) {
        activityBinding?.activity?.apply {

            //媒体类型
            val mediaType = options?.type?.let {
                //选择照片类型，0 = 图片，1 = 视频，2 = 混合
                when (it) {
                    0 -> SelectMimeType.ofImage()
                    else -> SelectMimeType.ofVideo()
                }
            } ?: SelectMimeType.ofImage()

            //是否开启裁剪
            val isEnableCrop = options?.allowEdit ?: true

            //是否开启圆形裁剪
            val isCircleDimmedLayer = options?.isRoundCliping ?: false

            //裁剪宽高比例
            val photoEditCustomRatioW = options?.photoEditCustomRatioW ?: 0
            val photoEditCustomRatioH = options?.photoEditCustomRatioH ?: 0


            val cropEngine =
                UCropEngine(isCircleDimmedLayer, photoEditCustomRatioW, photoEditCustomRatioH)

            var selector = PictureSelector.create(this)
                .openCamera(mediaType)

            if (isEnableCrop) {
                selector = selector.setCropEngine(cropEngine)
            }

            selector.forResult(object : OnResultCallbackListener<LocalMedia?> {
                override fun onResult(data: ArrayList<LocalMedia?>) {
                    val resultData = PhotoPickerResult()
                    resultData.assets = mutableListOf()
                    for (i in data) {
                        val photoAsset = PhotoAsset(i?.compressPath ?: i?.realPath)
                        photoAsset.width = i?.width?.toDouble()
                        photoAsset.height = i?.height?.toDouble()
                        resultData.assets?.add(photoAsset)
                    }
                    result?.success(resultData)
                }

                override fun onCancel() {
                    result?.success(PhotoPickerResult())
                }
            })
        }
    }
}
