package com.zzh.apt;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zzh.annotation.BindView;
import com.zzh.api.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    public TextView textView;

    @BindView(R.id.tv2)
    public TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        textView.setText("呵呵");

        textView2.setText("啦啦啦啦啦啦考虑手机打开了房间克里斯多夫");
    }
}