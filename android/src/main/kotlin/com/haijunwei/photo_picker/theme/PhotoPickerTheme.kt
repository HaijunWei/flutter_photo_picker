//package com.haijunwei.photo_picker.theme
//
//import android.content.Context
//import android.graphics.Color
//import androidx.core.content.ContextCompat
//import com.haijunwei.photo_picker.R
//import com.luck.picture.lib.style.PictureCropParameterStyle
//import com.luck.picture.lib.style.PictureParameterStyle
//
///**
// *
// * @CreateDate:     2021/4/27 14:26
// * @Description:
// * @Author:         LOPER7
// * @Email:          loper7@163.com
// */
//object PhotoPickerTheme {
//
//    fun buildPictureCropParameterStyle(context: Context) :PictureCropParameterStyle{
//        // 裁剪主题
//        return  PictureCropParameterStyle(
//                buildPictureParameterStyle(context).pictureStatusBarColor,
//                buildPictureParameterStyle(context).pictureTitleBarBackgroundColor,
//                buildPictureParameterStyle(context).pictureTitleTextColor,
//                buildPictureParameterStyle(context).isChangeStatusBarFontColor)
//    }
//
//    fun buildPictureParameterStyle(context: Context) :PictureParameterStyle{
//        // 相册主题
//        val mPictureParameterStyle = PictureParameterStyle()
//        // 是否改变状态栏字体颜色(黑白切换)
//        mPictureParameterStyle.isChangeStatusBarFontColor = false
//        // 是否开启右下角已完成(0/9)风格
//        mPictureParameterStyle.isOpenCompletedNumStyle = true
//        // 是否开启类似QQ相册带数字选择风格
//        mPictureParameterStyle.isOpenCheckNumStyle = true
//        // 相册状态栏背景色
//        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#272726")
//        // 相册列表标题栏背景色
//        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#272726")
//        //相册父容器背景色
//        mPictureParameterStyle.pictureContainerBackgroundColor = Color.parseColor("#272726")
//        // 相册列表标题栏右侧上拉箭头
//        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up
//        // 相册列表标题栏右侧下拉箭头
//        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down
//        // 相册文件夹列表选中圆点
//        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval
//        // 相册返回箭头
//        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back
//        // 标题栏字体颜色
//        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(context, R.color.picture_color_white)
//        // 相册右侧取消按钮字体颜色
//        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(context, R.color.picture_color_white)
//        // 相册列表勾选图片样式
//        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_checkbox_num_selector
//        // 选择相册目录背景样式
//        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_item_select_bg
//        // 相册列表底部背景色
//        mPictureParameterStyle.pictureBottomBgColor =Color.parseColor("#272726")
//        // 已选数量圆点背景样式
//        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval
//        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
//        mPictureParameterStyle.picturePreviewTextColor = Color.parseColor("#03C160")
//        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
//        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(context, R.color.picture_color_9b)
//        // 相册列表已完成色值(已完成 可点击色值)
//        mPictureParameterStyle.pictureCompleteTextColor = Color.parseColor("#03C160")
//        // 相册列表未完成色值(请选择 不可点击色值)
//        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(context, R.color.picture_color_9b)
//        // 预览界面底部背景色
//        mPictureParameterStyle.picturePreviewBottomBgColor = Color.parseColor("#272726")
//        // 外部预览界面删除按钮样式
//        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete
//        // 外部预览界面是否显示删除按钮
//        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true
//        // 相册右侧按钮背景样式,只针对isWeChatStyle 为true时有效果
//        mPictureParameterStyle.pictureUnCompleteBackgroundStyle = R.drawable.picture_send_button_default_bg
//        // 相册右侧按钮可点击背景样式,只针对isWeChatStyle 为true时有效果
//        mPictureParameterStyle.pictureCompleteBackgroundStyle = R.drawable.picture_send_button_bg
//        //文件夹字体颜色
//        mPictureParameterStyle.folderTextColor =Color.parseColor("#DDDDDD")
//        //原图勾选样式
//        mPictureParameterStyle.pictureOriginalControlStyle =R.drawable.picture_original_wechat_checkbox
//        //原图文字颜色
//        mPictureParameterStyle.pictureOriginalFontColor =Color.parseColor("#FFFFFF")
//
//
//        return mPictureParameterStyle
//    }
//}