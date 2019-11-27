package com.pin.xposed.demo

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

/**
 * Author: taochen
 * Date: 2019-11-27 14:34
 * Description: Hook Android Test
 */
class AndroidTestHook {

    fun hook(classLoader: ClassLoader, context: Context) {
        try {
            XposedHelpers.findAndHookMethod(
                "com.android.test.KotlinActivity",
                classLoader,
                "onCreate",
                Bundle::class.java,
                object : XC_MethodHook() {

                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)
                        val intent = (param.thisObject as Activity).intent
                        intent.putExtra("message", "你已经被劫持了!")
                        Toast.makeText(context, "KotlinActivity intent被劫持!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                        XposedBridge.log("AndroidTestHook, KotlinActivity onCreate")
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}