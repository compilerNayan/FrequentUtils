package com.simbalarry.frequentutils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by compilerNayan on 2/25/2018.
 */

public class PermissionUtil
{

    public static boolean hasPermissions(final Context context, final String[] permissionsToCheck)
    {
        boolean hasPermissions = true;
        for(String permission : permissionsToCheck)
        {
            int result = ActivityCompat.checkSelfPermission(context, permission);
            hasPermissions = hasPermissions && (result == PackageManager.PERMISSION_GRANTED);
        }
        return hasPermissions;
    }

    public static void askPermissions(final Activity activity,
                                      final String[] permissionsToAsk,
                                      final int permissionCode)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            ActivityCompat.requestPermissions(activity, permissionsToAsk, permissionCode);
        }
    }

    public static void askPermissionWithReason(final Activity activity,
                                               final String permissionName,
                                               final int permissionCode,
                                               final String reason)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            String[] permissions = {permissionName};
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionName))
            {
                showReasonPopUpAndAskPermissions(activity, permissions, permissionCode, reason);
            }
            else
            {
                askPermissions(activity, permissions, permissionCode);
            }
        }
    }

    public static void askMultiplePermissionsWithSameReason(final Activity activity,
                                                            final String[] permissionsToAsk,
                                                            final int permissionCode,
                                                            final String reason)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(needToShowReasonPopUp(activity, permissionsToAsk))
            {
                showReasonPopUpAndAskPermissions(activity, permissionsToAsk, permissionCode,
                        reason);
            }
            else
            {
                askPermissions(activity, permissionsToAsk, permissionCode);
            }
        }
    }

    private static boolean needToShowReasonPopUp(final Activity activity,
                                                 final String[] permissionsToAsk)
    {
        boolean needToShowReason = false;
        for(String permission : permissionsToAsk)
        {
            needToShowReason = ActivityCompat
                    .shouldShowRequestPermissionRationale(activity,permission);
            if(needToShowReason)
            {
                break;
            }
        }
        return needToShowReason;
    }

    private static void showReasonPopUpAndAskPermissions(final Activity activity,
                                                         final String[] permissions,
                                                         final int permissionCode,
                                                         final String reason)
    {
        new AlertDialog.Builder(activity)
                .setTitle(activity.getText(R.string.permission_popup_title))
                .setMessage(reason)
                .setPositiveButton(activity.getText(R.string.ok),
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                askPermissions(activity, permissions,permissionCode);
                            }
                        })
                .setNegativeButton(activity.getText(R.string.cancel),
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.dismiss();
                            }
                        })
                .create()
                .show();
    }
}