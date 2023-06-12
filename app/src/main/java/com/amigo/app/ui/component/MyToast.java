package com.amigo.app.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.amigo.app.R;


public class MyToast extends RelativeLayout {

    private final ImageView iv_icon_00;
    private final TextView tv_note_00;
    private final Toast toast;
    private final CardView cv_root;


    public MyToast(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.component_mytoast,this,true);

        cv_root = findViewById(R.id.cv_root);


        iv_icon_00 = findViewById(R.id.imvw_icon_00);
        tv_note_00 = findViewById(R.id.txvw_note_00);

        toast = new Toast(context);
        toast.setView(this);
    }

    public void show(int duration){
        toast.setDuration(duration);
        toast.show();
    }

    public void show(int duration, int gravity, int x, int y){
        toast.setDuration(duration);
        toast.setGravity(gravity,x,y);
        toast.show();
    }
    public void show(int duration, int gravity){
        toast.setDuration(duration);
        toast.setGravity(gravity,0,0);
        toast.show();
    }


    public void setIcon(int resource, int color){
        iv_icon_00.setImageResource(resource);
        iv_icon_00.setColorFilter(color);
    }

    public void setBackground(int color){
        cv_root.setCardBackgroundColor(color);
    }
    public void setTextColor(int color){
        tv_note_00.setTextColor(color);
    }

    public void setMessage(String message){
        tv_note_00.setText(message);
    }
}
