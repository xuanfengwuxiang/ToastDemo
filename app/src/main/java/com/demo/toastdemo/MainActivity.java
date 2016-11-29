package com.demo.toastdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //R.mipmap.icon6
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button test = (Button) findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunBeyToast runBeyToast = new RunBeyToast(getBaseContext(),9000,"恭喜"
                        ,Color.RED,99,R.mipmap.icon6
                        );
                runBeyToast.show();
            }
        });
    }
}
