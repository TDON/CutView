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
    private Button ovalCutBtn,rectCutBtn,roundRectBtn;
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
        ovalCutBtn = (Button) findViewById(R.id.ovalCutbtn);
        ovalCutBtn.setOnClickListener(this);

        rectCutBtn=(Button)findViewById(R.id.rectCutbtn);
        rectCutBtn.setOnClickListener(this);

        roundRectBtn=(Button)findViewById(R.id.roundRectCutbtn);
        roundRectBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        cutFragment=new CutFragment();
        cutFragment.setHandler(handler);

        switch (v.getId()) {
            case R.id.ovalCutbtn:

                cutFragment.setCutPathType(CutView.PathType.OVAL);

                break;

            case  R.id.rectCutbtn:

                cutFragment.setCutPathType(CutView.PathType.RECT);
                break;
            case R.id.roundRectCutbtn:

                cutFragment.setCutPathType(CutView.PathType.ROUNDRECT);
                break;

        }
        getFragmentManager().beginTransaction().
                replace(R.id.fragment_container, cutFragment).addToBackStack("Cut").commit();
    }
}
