package com.tristate.firebasechat.progressbares;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;

import com.tristate.firebasechat.R;


public class CustomLoaderDialog {
    Context c;
    Dialog dialog;
    Typeface fontLight = null;

    public CustomLoaderDialog(Context context) {
        this.c = context;
        dialog = new Dialog(c, R.style.DialogTheme);
    }

    /**
     * This method use for show progress dialog
     *
     * @param isCancelable set true if you set cancel progressDialog by user event
     */
    public void show(Boolean isCancelable) {

        dialog.setCancelable(isCancelable);
        dialog.setContentView(R.layout.progress_layout);
        dialog.show();

    }

    public Boolean isShowing() {
        return dialog.isShowing();
    }

    public void hide() {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
        }
    }
}
