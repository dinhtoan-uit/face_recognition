package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.tensorflow.lite.examples.detection.R;

public class File extends AppCompatActivity {

    public  static  final String MY_REPLY = "12";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_or_shoot);
    }

    // Hàm trả về string camera nếu click vào button camera
    public void Camera(View view)
    {
        String rp = "camera";
        Intent reply = new Intent();
        reply.putExtra(MY_REPLY,rp);
        setResult(RESULT_OK,reply);
        finish();
    }
    // Hàm trả về string gallery nếu click vào button gallery
    public void Gallery(View view)
    {
        String rp = "gallery";
        Intent reply = new Intent();
        reply.putExtra(MY_REPLY,rp);
        setResult(RESULT_OK,reply);
        finish();
    }
}