package com.example.newpolycom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    DemoView demoView;
    LineView lineView;
    LineView lineView1;
    IMainView view1;
    IMainView view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        demoView = findViewById(R.id.view);
        lineView = findViewById(R.id.view_line);
        lineView.setmOrientation(0);

        view1 = findViewById(R.id.view_line2);
        view2 = findViewById(R.id.view_line3);
        lineView1 = findViewById(R.id.view_line1);
        lineView1.setmOrientation(2);
        demoView.setOnViewLongEventListener(new OnViewLongEventListener() {
            @Override
            public void onDefaultClick(LongClickView view) {
                Log.e("TAG", "This is onDefaultClick:  " + view.getLongClickNumber());
            }

            @Override
            public void onLongClickDown(LongClickView view) {
                Log.e("TAG", "This is onLongClickDown:  " + view.getLongClickNumber());
            }

            @Override
            public void onLongClickUp(LongClickView view) {
                Log.e("TAG", "This is onLongClickUp:  " + view.getLongClickNumber());
            }

            @Override
            public void onDisabled(LongClickView view) {
                Log.e("TAG", "This is onDisabled:  " + view.getLongClickNumber());
            }
        });

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn11).setOnClickListener(this);
        findViewById(R.id.btn12).setOnClickListener(this);
        findViewById(R.id.btn13).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn11:
                view1.setStatus(0);
                view2.setStatus(0);
                break;
            case R.id.btn12:
                view1.setStatus(1);
                view2.setStatus(1);
                break;
            case R.id.btn13:
                view1.setStatus(2);
                view2.setStatus(2);
                break;
            default:
                view1.setmPosition(Integer.parseInt(((TextView) view).getText().toString()));
                view2.setmPosition(Integer.parseInt(((TextView) view).getText().toString()));
                break;
        }
    }
}
