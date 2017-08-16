package me.peng.pengexpress0726.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import me.peng.pengexpress0726.utils.permission.PermissionReq;

/**
 * Created by Administrator on 2017/7/26.
 */

public class PermissionActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
