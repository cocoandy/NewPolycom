package com.example.newpolycom;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnTakePhoto;
    private Button btnCameraShooting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setListeners();
    }

    private void initViews(){
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnCameraShooting = findViewById(R.id.btn_camera_shooting);
    }

    private void setListeners(){
        btnTakePhoto.setOnClickListener(this);
        btnCameraShooting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_take_photo:
                takePhotoWithPermission();
                break;
            case R.id.btn_camera_shooting:
                cameraShootingWithPermission();
                break;
            default:
                break;
        }
    }

    private void takePhotoWithPermission(){
        if(PermissionsUtil.hasPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})){
            takePhoto();
        }else{
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(String[] permission) {
                    takePhoto();
                }

                @Override
                public void permissionDenied(String[] permission) {
                    Toast.makeText(MainActivity.this, "您拒绝了外置存储的访问权限", Toast.LENGTH_LONG).show();
                }
            }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        }
    }

    //照相
    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //存放图片的路径
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/sty/",
                "text.jpg");
        //Uri photoOutputUri = Uri.fromFile(file);
        Uri photoOutputUri = FileProvider.getUriForFile(
                this,
                "com.sty.camera.provider",
                file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);

        //开启一个activity 并获取结果
        startActivityForResult(intent, 1);
    }

    private void cameraShootingWithPermission(){
        if(PermissionsUtil.hasPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})){
            cameraShooting();
        }else{
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(String[] permission) {
                    cameraShooting();
                }

                @Override
                public void permissionDenied(String[] permission) {
                    Toast.makeText(MainActivity.this, "您拒绝了外置存储的访问权限", Toast.LENGTH_LONG).show();
                }
            }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        }
    }

    //录像
    private void cameraShooting(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        //存放图片的路径
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/sty/",
                "text.3gp");
        //Uri photoOutputUri = Uri.fromFile(file);
        Uri photoOutputUri = FileProvider.getUriForFile(
                this,
                "com.sty.camera.provider",
                file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoOutputUri);

        //开启一个activity 并获取结果
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Tag", "方法被调用了");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
