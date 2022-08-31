package com.haijunwei.photo_picker_example

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 *
 * @CreateDate:     2021/4/27 16:46
 * @Description:
 * @Author:         LOPER7
 * @Email:          loper7@163.com
 */
class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}