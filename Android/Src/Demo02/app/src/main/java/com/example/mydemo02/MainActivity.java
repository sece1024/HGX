package com.example.mydemo02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    // 控件声明
    private Button mBtnImagePicker;
    private Button mBtnStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 控件绑定
        mBtnImagePicker = findViewById(R.id.btn_imagepicker);
        mBtnStepView = findViewById(R.id.btn_stepview);


        setListeners();
    }


    // 设置监听器
    private void setListeners(){
        OnClick onClick = new OnClick();

        mBtnImagePicker.setOnClickListener(onClick);
        mBtnImagePicker.setOnClickListener(onClick);


    }


    // 点击类
    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){

                case  R.id.btn_imagepicker:
                    intent = new Intent(MainActivity.this, ImagePickerActivity.class);
                    break;
                case  R.id.btn_stepview:
                    intent = new Intent(MainActivity.this, StepViewActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}