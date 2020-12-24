package com.example.dtuparking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.Annotation;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Animation top,bottom;
    ImageView imglogo;
    TextView txtlogo;
    SeesionManager seesionManager;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anhxa();

        // set hiệu ứng cho logo và text
        imglogo.setAnimation(top);
        txtlogo.setAnimation(bottom);
        seesionManager = new SeesionManager(getApplication());


        if(CheckInternet.isConnect(getBaseContext())){
            // tạo hiệu ứng chuyển trang với thời gian khi animation thực hiện
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this,login.class);
                    intent.putExtra("ktra",true);

                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View,String>(imglogo,"image_name");
                    pairs[1] = new Pair<View,String>(txtlogo,"text_name");

                    // hệ diều hành android phải trên 5.0 mới có animation
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                        startActivity(intent,options.toBundle());

                    }
                    else {
                        startActivity(intent);
                        finish();
                    }

                }
            },2000);
        }
        else {
            Intent intent = new Intent(MainActivity.this,MaQRCode.class);
            startActivity(intent);
            finish();
        }


    }

    private void anhxa() {
        // ánh xạ các đối tượng
        top = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        imglogo = (ImageView) findViewById(R.id.imageView);
        txtlogo = (TextView) findViewById(R.id.textView);
    }


}