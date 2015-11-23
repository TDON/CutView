package com.example.don.cutviewdemo;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



/**
 * Created by don on 15/11/23.
 */
public class CutFragment extends Fragment implements View.OnClickListener,CutListener{
    private CutView cutView;
    private Button cutBtn,cancelBtn;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fagment_cut,null);

        init(view);

        return view;
    }

    private void init(View view) {

        cutView=(CutView)view.findViewById(R.id.cutview);
        cutView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.hand));
        cutView.setCutListener(this);//设置剪裁监听，用于剪裁完成后获取圆形头像

        cutBtn=(Button)view.findViewById(R.id.cutBtn);
        cutBtn.setOnClickListener(this);
        cancelBtn=(Button)view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void cutResultWithBitmap(Bitmap bitmap) {

        //将剪裁的得来的Bitmap通过handler发送到MainActivity
        handler.obtainMessage(0,bitmap).sendToTarget();
        getFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.cutBtn:
                cutView.clipImage();
                break;
            case R.id.cancelBtn:
                getFragmentManager().popBackStack();
                break;


        }

    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
