package com.example.clutterrevision;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsHelper {
    public void checkPermissions(Activity activity) {
        List<String> permsissions = new ArrayList<>();
        int permissionRecordAudio = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
        int permissionInternet = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        if (permissionRecordAudio != PackageManager.PERMISSION_GRANTED) {
            permsissions.add(Manifest.permission.RECORD_AUDIO);
        }
        if (permissionInternet != PackageManager.PERMISSION_GRANTED) {
            permsissions.add(Manifest.permission.INTERNET);
        }
        if (!permsissions.isEmpty()) {
            ActivityCompat.requestPermissions(activity,permsissions.toArray(new String[permsissions.size()]), Constants.MY_PERMISSIONS_REQUESTS);
        }
    }

    public void onPermissionRequestResults(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUESTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }
        }
    }
}
