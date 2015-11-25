# CutView
Android 头像剪切，圆形

使用示例：
 cutView=(CutView)view.findViewById(R.id.cutview);
        cutView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.hand));
        cutView.setCutListener(this);//设置剪裁监听，用于剪裁完成后获取圆形头像

        //自定义CutView的一些属性
        cutView.setShadeColor(0X6674d6b5);//色值要使用argb，带透明度的
        cutView.setPathColor(0Xfff9771e);//argb
        cutView.setCutRadius(150);//这里的单位是px
        cutView.setPathEffect(new CornerPathEffect(3.0f));//将默认的圆形虚线线条改为实线
        cutView.setPathWidth(2);//设置线条宽度，默认是2，单位是px
        cutView.setCutFillColor(0xffffffff);//图片剪切空白部分颜色设置为白色，argb

        cutView.setPathType(cutPathType);

        cutView.setRoundRectRadius(10.0f);//如果剪切类型为圆角矩形，需要设置圆角矩形的圆角半径，可不设置默认是3.0f

//        cutView.setPathType(CutView.PathType.RECT);//设置剪切类型为矩形，那么矩形的宽高就是上面的设置的CutRadius的2倍，也就是300px。

//        cutView.setPathType(CutView.PathType.ROUNDRECT);//设置剪切类型为矩形，那么矩形的宽高就是上面的设置的CutRadius的2倍，也就是300px。
//        cutView.setRoundRectRadius(10.0f);//如果剪切类型为圆角矩形，需要设置圆角矩形的圆角半径，可不设置默认是3.0f


布局文件示例：
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">


    <com.example.don.cutviewdemo.CutView

        android:id="@+id/cutview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"

        />

    <LinearLayout

        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:orientation="horizontal">

        <Button
            android:id="@+id/cutBtn"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="剪切"/>
        <Button
            android:id="@+id/cancelBtn"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="取消"/>


    </LinearLayout>



</RelativeLayout>

