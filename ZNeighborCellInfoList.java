package com.mobile.mobileinfo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * author: Lionel
 * date: 2021-10-08 21:50
 */
class ZNeighborCellInfoList {
    public void a(Context context, TelephonyManager tm) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            try {
                Method method = tm.getClass().getDeclaredMethod("getNeighboringCellInfo");
                List<NeighboringCellInfo> neighboringCellInfoList = (List<NeighboringCellInfo>) method.invoke(tm);
                NeighboringCellInfo neighboringCellInfo = neighboringCellInfoList.get(0);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            List<CellInfo> cellInfoList = tm.getAllCellInfo();
            CellInfo cellInfo = cellInfoList.get(0);
            if (cellInfo instanceof CellInfoGsm) {
                CellInfoGsm cInfo = (CellInfoGsm) cellInfo;
                CellSignalStrengthGsm cellSignalStrengthGsm = cInfo.getCellSignalStrength();
                cellSignalStrengthGsm.getDbm();
                cellSignalStrengthGsm.getLevel();
            } else if (cellInfo instanceof CellInfoCdma) {
                CellInfoCdma cInfo = (CellInfoCdma) cellInfo;
                CellSignalStrengthCdma cellSignalStrengthCdma = cInfo.getCellSignalStrength();
                cellSignalStrengthCdma.getDbm();
                cellSignalStrengthCdma.getCdmaDbm();
            } else if (cellInfo instanceof CellInfoWcdma) {
                CellInfoWcdma cInfo = (CellInfoWcdma) cellInfo;
                CellSignalStrengthWcdma cellSignalStrengthWcdma = cInfo.getCellSignalStrength();
                cellSignalStrengthWcdma.getDbm();
            } else if (cellInfo instanceof CellInfoLte) {
                CellInfoLte cInfo = (CellInfoLte) cellInfo;
                CellSignalStrengthLte csl = cInfo.getCellSignalStrength();
                csl.getRsrp();
                csl.getRsrq();
                csl.getRssi();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (cellInfo instanceof CellInfoTdscdma) {
                        CellInfoTdscdma cInfo = (CellInfoTdscdma) cellInfo;
                        CellSignalStrengthTdscdma cellSignalStrengthTdscdma = cInfo.getCellSignalStrength();
                        cellSignalStrengthTdscdma.getDbm();
                    } else if (cellInfo instanceof CellInfoNr) {
                        CellInfoNr cInfo = (CellInfoNr) cellInfo;
                        cInfo.getCellSignalStrength();
                        CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cInfo.getCellSignalStrength();
                        cellSignalStrengthNr.getDbm();
                    }
                }
            }
        }

    }
}
