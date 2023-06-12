package com.amigo.app.libs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by pho0890910 on 8/3/2018.
 */
public class Loading {

    private static ProgressDialog g_progressDialog;

    public static void showLoading( Context p_cContext, String msg) //, ILoadingTimerListener p_loadingTimerListener, int p_intTimeOutTime )
    {
        if(((Activity) p_cContext).isFinishing())
        {
           return;
        }
        cancelLoading();

        g_progressDialog = new ProgressDialog(p_cContext);
        g_progressDialog.setTitle(null);
        g_progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        g_progressDialog.setIndeterminate(true);
        g_progressDialog.setMessage(msg);
        g_progressDialog.setCancelable(false);
        g_progressDialog.show();
    }

    public static void cancelLoading()
    {
        if (g_progressDialog != null)
        {
            g_progressDialog.cancel();
            g_progressDialog = null;
        }
    }


}