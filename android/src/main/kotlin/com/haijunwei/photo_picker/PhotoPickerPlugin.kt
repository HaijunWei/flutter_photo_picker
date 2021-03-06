package com.haijunwei.photo_picker

import android.util.Log
import androidx.annotation.NonNull
import com.haijunwei.photo_picker.bean.PhotoAsset
import com.haijunwei.photo_picker.bean.PhotoPickerOptions
import com.haijunwei.photo_picker.bean.PhotoPickerResult
import com.haijunwei.photo_picker.theme.PhotoPickerTheme
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.pictureselector.GlideEngine
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar


/** PhotoPickerPlugin */
class PhotoPickerPlugin : FlutterPlugin, MethodCallHandler, ActivityAware, PhotoPicker {
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


    override fun pickPhoto(options: PhotoPickerOptions?, result: com.haijunwei.photo_picker.Result<PhotoPickerResult?>?) {
        activityBinding?.activity?.apply {

            Log.d("loper7", "options->${options?.toMap().toString()}")

            //????????????
            var mediaType = options?.type?.let {
                //?????????????????????0 = ?????????1 = ?????????2 = ??????
                when (it) {
                    0 -> PictureMimeType.ofImage()
                    1 -> PictureMimeType.ofVideo()
                    else -> PictureMimeType.ofAll()
                }
            } ?: PictureMimeType.ofAll()

            //????????????
            var selectionMode = options?.maxAssetsCount?.let {
                when {
                    it > 1 -> PictureConfig.MULTIPLE
                    else -> PictureConfig.SINGLE
                }
            } ?: PictureConfig.SINGLE

            //?????????????????????????????????
            var isSingleDirectReturn = options?.singleJumpEdit ?: true

            //??????????????????
            var maxSelectNum = options?.maxAssetsCount ?: 1

            //??????????????????
            var isEnableCrop = options?.allowEdit ?: true

            //????????????????????????
            var isCircleDimmedLayer = options?.isRoundCliping ?: false

            //??????????????????
            var photoEditCustomRatioW = options?.photoEditCustomRatioW ?: 0
            var photoEditCustomRatioH = options?.photoEditCustomRatioH ?: 0

            //??????????????????
            var freeStyleCropEnabled = photoEditCustomRatioW * photoEditCustomRatioH <= 0

            //????????????????????????
            var imageSpanCount = options?.imageSpanCount ?: 4

            //????????????????????????
            var allowOpenCamera = options?.allowOpenCamera ?: true

            //????????????gif
            var allowGif = options?.allowGif ?: true


            PictureSelector.create(this)
                    .openGallery(mediaType)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .selectionMode(selectionMode)
                    .isSingleDirectReturn(isSingleDirectReturn)
                    .maxSelectNum(maxSelectNum)
                    .isPreviewEggs(true)
                    .imageSpanCount(imageSpanCount)
                    .isPreviewImage(true)
                    .isWeChatStyle(false)
                    .isCamera(allowOpenCamera)
                    .isGif(allowGif)
                    .isEnableCrop(isEnableCrop)
                    .withAspectRatio(photoEditCustomRatioW, photoEditCustomRatioH)
                    .freeStyleCropEnabled(freeStyleCropEnabled)
                    .circleDimmedLayer(isCircleDimmedLayer)
                    .isCompress(true)
                    .showCropFrame(true)
                    .showCropGrid(true)
                    .isMultipleSkipCrop(true)
                    .compressFocusAlpha(true)
                    .isMaxSelectEnabledMask(true)
                    .isAutomaticTitleRecyclerTop(true)
                    .isOriginalImageControl(true)
                    .setPictureStyle(PhotoPickerTheme.buildPictureParameterStyle(this))
                    .setPictureCropStyle(PhotoPickerTheme.buildPictureCropParameterStyle(this))
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(data: List<LocalMedia?>) {
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

    override fun openCamera(options: PhotoPickerOptions?, result: com.haijunwei.photo_picker.Result<PhotoPickerResult?>?) {
        activityBinding?.activity?.apply {

            //????????????
            var mediaType = options?.type?.let {
                //?????????????????????0 = ?????????1 = ?????????2 = ??????
                when (it) {
                    0 -> PictureMimeType.ofImage()
                    else -> PictureMimeType.ofVideo()
                }
            } ?: PictureMimeType.ofImage()

            //??????????????????
            var isEnableCrop = options?.allowEdit ?: true

            //????????????????????????
            var isCircleDimmedLayer = options?.isRoundCliping ?: false

            //??????????????????
            var photoEditCustomRatioW = options?.photoEditCustomRatioW ?: 0
            var photoEditCustomRatioH = options?.photoEditCustomRatioH ?: 0

            //??????????????????
            var freeStyleCropEnabled = photoEditCustomRatioW * photoEditCustomRatioH <= 0

            
            R.layout.picture_dialog_camera_selected
            PictureSelector.create(this)
                    .openCamera(mediaType)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isPreviewEggs(true)
                    .isPreviewImage(true)
                    .isWeChatStyle(true)
                    .isEnableCrop(isEnableCrop)
                    .withAspectRatio(photoEditCustomRatioW, photoEditCustomRatioH)
                    .freeStyleCropEnabled(freeStyleCropEnabled)
                    .circleDimmedLayer(isCircleDimmedLayer)
                    .isCompress(true)
                    .compressFocusAlpha(true)
                    .isMaxSelectEnabledMask(true)
                    .isOriginalImageControl(true)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .setPictureStyle(PhotoPickerTheme.buildPictureParameterStyle(this))
                    .setPictureCropStyle(PhotoPickerTheme.buildPictureCropParameterStyle(this))
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(data: List<LocalMedia?>) {
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
