package com.haijunwei.photo_picker

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import com.haijunwei.photo_picker.bean.PhotoAsset
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

            Log.d("loper7","options->${arg?.toMap().toString()}")

            //媒体类型
            var mediaType = arg?.type?.toInt()?.let {
                //选择照片类型，0 = 图片，1 = 视频，2 = 混合
               when(it){
                   0->PictureMimeType.ofImage()
                   1->PictureMimeType.ofVideo()
                   else->PictureMimeType.ofAll()
               }
            }?:PictureMimeType.ofAll()

            //选择模式
            var selectionMode  = arg?.maxAssetsCount?.toInt()?.let {
                when{
                    it>1-> PictureConfig.MULTIPLE
                    else ->PictureConfig.SINGLE
                }
            }?:PictureConfig.SINGLE

            //单选模式下是否直接返回
            var isSingleDirectReturn = arg?.singleJumpEdit?:true

            //最大选择数量
            var maxSelectNum = arg?.maxAssetsCount?.toInt()?:1

            //是否开启裁剪
            var isEnableCrop = arg?.allowEdit?:true

            //是否开启圆形裁剪
            var isCircleDimmedLayer = arg?.isRoundCliping?:false

            PictureSelector.create(this)
                    .openGallery(mediaType)
                    .imageEngine(GlideEngine.createGlideEngine())
                    .selectionMode(selectionMode)
                    .isSingleDirectReturn(isSingleDirectReturn)
                    .isWeChatStyle(true)
                    .maxSelectNum(maxSelectNum)
                    .isPreviewEggs(true)
                    .imageSpanCount(4)//每行显示个数
                    .isPreviewImage(true)
                    .isGif(true)//是否显示gif
                    .isEnableCrop(isEnableCrop)
                    .withAspectRatio(1,1)
                    .freeStyleCropEnabled(true)
                    .circleDimmedLayer(isCircleDimmedLayer)
                    .isCompress(true)
                    .compressFocusAlpha(true)
                    .isMaxSelectEnabledMask(true)
                    .isAutomaticTitleRecyclerTop(true)
                    .isOriginalImageControl(true)
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(data: List<LocalMedia?>) {
                            val resultData = Messages.PhotoPickerResult()
                            resultData.assets = mutableListOf()
                            for (i in data) {
                                val photoAsset = PhotoAsset(i?.compressPath?:i?.realPath)
                                resultData.assets.add(photoAsset)
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
