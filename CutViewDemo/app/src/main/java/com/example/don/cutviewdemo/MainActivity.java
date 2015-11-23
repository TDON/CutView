package com.example.don.cutviewdemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView resultView;
    private Button toCutBtn;
    private CutFragment cutFragment;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            resultView.setImageBitmap(bitmap);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消Title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        resultView = (ImageView) findViewById(R.id.resultView);
        toCutBtn = (Button) findViewById(R.id.toCutbtn);
        toCutBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toCutbtn:

                cutFragment=new CutFragment();
                cutFragment.setHandler(handler);
                getFragmentManager().beginTransaction().
                        replace(R.id.fragment_container, cutFragment).addToBackStack("Cut").commit();

                break;

        }
    }
}
