package com.haijunwei.photo_picker.engine

import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.luck.picture.lib.engine.CropFileEngine
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine
import com.yalantis.ucrop.model.AspectRatio
import com.bumptech.glide.request.transition.Transition


class UCropEngine : CropFileEngine {

    override fun onStartCrop(fragment: Fragment,
        srcUri: Uri,
        destinationUri: Uri,
        dataSource: ArrayList<String>?,
        requestCode: Int) {
        var uCrop = UCrop.of(srcUri, destinationUri, dataSource);
        uCrop.setImageEngine(object : UCropImageEngine {
            override fun loadImage(context: Context, url: String?, imageView: ImageView) {
                Glide.with(context).load(url).into(imageView);
            }

            override fun loadImage(context: Context,
                url: Uri?,
                maxWidth: Int,
                maxHeight: Int,
                call: UCropImageEngine.OnCallbackListener<Bitmap>?) {
                Glide.with(context).asBitmap().load(url).override(R.attr.maxWidth, R.attr.maxHeight)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(resource: Bitmap, @Nullable transition: Transition<in Bitmap?>?) {
                            call?.onCall(resource)
                        }

                        override fun onLoadCleared(@Nullable placeholder: Drawable?) {
                            call?.onCall(null)
                        }
                    })
            }
        })
        var options = UCrop.Options()
        options.setMultipleCropAspectRatio(AspectRatio("",1f,1f)) //多图裁剪时每张对应的裁剪比例
        options.isForbidSkipMultipleCrop(true) //多图裁剪时是否支持跳过
        options.isCropDragSmoothToCenter(true) //图片是否跟随裁剪框居中
        options. isForbidCropGifWebp(true) //是否禁止裁剪gif和webp
        options.isDarkStatusBarBlack(true)//状态栏字体颜色是否黑色模式
        uCrop.withOptions(options)
        uCrop.start(fragment.requireContext(), fragment, requestCode);
    }
}