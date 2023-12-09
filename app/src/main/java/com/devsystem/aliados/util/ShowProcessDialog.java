package com.devsystem.aliados.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devsystem.aliados.R;


public class ShowProcessDialog {
    private Context context;
    private Dialog dialog;

    public ShowProcessDialog(Context context) {
        this.context = context;
        this.dialog  = new Dialog(context);
    }

    public  void showDialog(){
        int llPadding = 50;
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setBackgroundColor(context.getColor(R.color.red_500));
        // ll.setAlpha((float) 0.5);
        ll.setGravity(Gravity.CENTER);

        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);

        TextView tvText = new TextView(context);
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.WHITE);
        tvText.setTextSize(15);

        ll.addView(progressBar);
        ll.addView(tvText);
        this.dialog.setCancelable(false);
        this.dialog.addContentView(ll,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.dialog.show();

    }

    public  void hideDialog() {

        this.dialog.dismiss();

    }


}
