package com.lionel.zc.telephonydemo1

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.CellInfo
import android.telephony.PhoneStateListener
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 *
 * author: Lionel
 * date: 2021-10-15 20:46
 */
object MobileManager {
    fun start(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) return
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val sm =
            context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager


        val subscriptionInfo1 = sm.getActiveSubscriptionInfoForSimSlotIndex(0)
        val subscriptionInfo2 = sm.getActiveSubscriptionInfoForSimSlotIndex(1)
        val subId1 = subscriptionInfo1?.subscriptionId ?: -1
        val subId2 = subscriptionInfo2?.subscriptionId ?: -1

        val tm1 =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) tm.createForSubscriptionId(subId1) else tm
        val tm2 =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) tm.createForSubscriptionId(subId2) else tm


        val listener1 = MyPhoneStateListener2(subId1)
        val listener2 = MyPhoneStateListener2(subId2)



        Log.i("log_zc", "start: subId1: $subId1")
        Log.i("log_zc", "start: subId2: $subId2")
        val excutor = Executors.newSingleThreadExecutor()
        thread {
            /* tm.createForSubscriptionId(subId1).listen(
                 listener1,
                 PhoneStateListener.LISTEN_SIGNAL_STRENGTHS or PhoneStateListener.LISTEN_CELL_INFO
             )*/
            tm2.listen(
                listener2,
                PhoneStateListener.LISTEN_SIGNAL_STRENGTHS or PhoneStateListener.LISTEN_CELL_INFO
            )


            while (true) {
                Thread.sleep(3000)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    tm.requestCellInfoUpdate(
                        excutor,
                        object : TelephonyManager.CellInfoCallback() {
                            override fun onCellInfo(cellInfo: MutableList<CellInfo>) {
                                Log.i("log_zc", "onCellInfo: cellinfo: $cellInfo")
                            }

                        })
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tm.createForSubscriptionId(subId2).allCellInfo.apply {
                        Log.i("log_zc", "start: subid=1, cellInfo: $this")
                    }
                }
            }
        }
    }

    fun end(context: Context) {


    }
}