package com.pin.xposed.demo

import android.content.Context
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

/**
 * Author: taochen
 * Date: 2019-11-27 16:34
 * Description: Hook伊对
 */
class YiduiHook {

    fun hook(classLoader: ClassLoader, context: Context) {
        try {
            XposedHelpers.findAndHookMethod(
                "com.yidui.ui.wallet.MyWalletActivity",
                classLoader,
                "j",
                object : XC_MethodHook() {

                    override fun beforeHookedMethod(param: MethodHookParam) {
                        super.beforeHookedMethod(param)
                        val cField = XposedHelpers.findField(param.thisObject.javaClass, "c")
                        var bill = cField.get(param.thisObject)
                        val avaliableField = bill.javaClass.getField("avaliable")
                        avaliableField.setInt(bill, 1000000000)
                        XposedBridge.log(
                            "YiduiHook, MyWalletActivity j, bill: bill.avaliable=>>${avaliableField.getInt(
                                bill
                            )}"
                        )
                    }

                    override fun afterHookedMethod(param: MethodHookParam) {
                        super.afterHookedMethod(param)
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}