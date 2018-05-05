package hk.com.csci4140.culife.progress;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;


import com.cazaea.sweetalert.SweetAlertDialog;

import hk.com.csci4140.culife.R;

/**
 * Created by liujie(Jerry Liu) on 14/04/2018.
 */

public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;
    private SweetAlertDialog pDialog;
    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;


    public ProgressDialogHandler(Context context, ProgressCancelListener
            mProgressCancelListener, boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog() {
        if (pDialog == null) {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setCancelable(cancelable);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(context.getString(R.string.http_loading_text));
            if (cancelable) {
                pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        mProgressCancelListener.onCancelProgress();
                    }
                });
            }
            if (!pDialog.isShowing()) {
                pDialog.show();
            }
        }
    }
    private void dismissProgressDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }
}
