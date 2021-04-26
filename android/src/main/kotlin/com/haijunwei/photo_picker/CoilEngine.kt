package android.src.main.kotlin.com.loper7.image_picker

import android.content.Context
import android.widget.ImageView
import coil.load
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView

/**
 *
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
class CoilEngine : ImageEngine {

    private fun GlideEngine() {}

    companion object{
        private var instance: CoilEngine? = null

        fun create(): CoilEngine? {
            if (null == instance) {
                synchronized(CoilEngine::class.java) {
                    if (null == instance) {
                        instance = CoilEngine()
                    }
                }
            }
            return instance
        }
    }
    
    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        imageView.load(url)
    }

    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?,
        callback: OnImageCompleteCallback?
    ) {
        imageView.load(url)
    }

    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?
    ) {
        imageView.load(url)
    }

    override fun loadAsGifImage(context: Context, url: String, imageView: ImageView) {
        imageView.load(url)
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        imageView.load(url)
    }

    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        imageView.load(url)
    }

}