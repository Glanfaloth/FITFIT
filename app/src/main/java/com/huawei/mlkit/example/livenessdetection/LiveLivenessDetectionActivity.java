package com.huawei.mlkit.example.livenessdetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.hackzurich.R;
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCapture;
import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureResult;

public class LiveLivenessDetectionActivity extends AppCompatActivity {
    private static final String TAG = LiveLivenessDetectionActivity.class.getSimpleName();

    private static final String[] PERMISSIONS = {
        Manifest.permission.CAMERA
    };

    private static final int RC_CAMERA_AND_EXTERNAL_STORAGE = 0x01 << 8;

    private Button mBtn;
    private TextView mTextResult;
    private ImageView mImageResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_liveness_detection);

        mBtn = findViewById(R.id.capture_btn);
        mTextResult = findViewById(R.id.text_detect_result);
        mImageResult = findViewById(R.id.img_detect_result);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(LiveLivenessDetectionActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startCaptureActivity();
                    return;
                }
                ActivityCompat.requestPermissions(LiveLivenessDetectionActivity.this, PERMISSIONS, RC_CAMERA_AND_EXTERNAL_STORAGE);
            }
        });
    }

    private void startCaptureActivity() {
        MLLivenessCapture capture = MLLivenessCapture.getInstance();
        capture.startDetect(this, this.callback);
    }

    private MLLivenessCapture.Callback callback = new MLLivenessCapture.Callback() {
        @Override
        public void onSuccess(MLLivenessCaptureResult result) {
            mTextResult.setText(result.toString());
            mTextResult.setBackgroundResource(result.isLive() ? R.drawable.bg_blue : R.drawable.bg_red);
            mImageResult.setImageBitmap(result.getBitmap());
        }

        @Override
        public void onFailure(int errorCode) {
            mTextResult.setText("errorCode:" + errorCode);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult ");
        if (requestCode == RC_CAMERA_AND_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCaptureActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i(TAG, "onActivityResult requestCode " + requestCode + ", resultCode " + resultCode);
    }
}
