package com.pin.xposed.demo

import android.app.Application
import android.content.Context
import android.widget.Toast
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Author: taochen
 * Date: 2019-11-27 12:10
 * Description: xposed入口
 */
class Main : IXposedHookLoadPackage {

    private var ISHOOK_PACKAGE_TEST = false
    private val HOOK_PACKAGE_NAME_TEST = "com.android.test"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        val packageName = lpparam.packageName
        val processName = lpparam.processName

        XposedBridge.log("handleLoadPackage, packageName: $packageName, processName: $processName")

        if (HOOK_PACKAGE_NAME_TEST == packageName) {
            try {
                XposedHelpers.findAndHookMethod(
                    Application::class.java,
                    "attach",
                    Context::class.java,
                    object : XC_MethodHook() {
                        override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                            super.afterHookedMethod(param)
                            val context = param.args[0] as Context
                            val appClassLoader = context.classLoader
                            if (HOOK_PACKAGE_NAME_TEST == processName && !ISHOOK_PACKAGE_TEST) {
                                ISHOOK_PACKAGE_TEST = true
                                Toast.makeText(
                                    context,
                                    "获取到Android-TestAll=>>classloader: $appClassLoader",
                                    Toast.LENGTH_LONG
                                ).show()

                                AndroidTestHook().hook(appClassLoader, context)
                            }
                        }
                    })
            } catch (e: Throwable) {
                XposedBridge.log(e)
            }
        }
    }
}