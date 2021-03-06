package hk.com.csci4140.culife.observer;

import android.content.Context;
import android.util.Log;

import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.BaseActivity;
import hk.com.csci4140.culife.progress.ProgressCancelListener;
import hk.com.csci4140.culife.progress.ProgressDialogHandler;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by liujie(Jerry Liu) on 14/04/2018.
 */

public class ProgressObserver<T> implements Observer<T>, ProgressCancelListener {

    private static final String TAG = "ProgressObserver";

    private ObserverOnNextListener listener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context context;
    private Disposable d;

    public ProgressObserver(Context context, ObserverOnNextListener listener) {
        this.listener = listener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }


    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        Log.d(TAG, "onSubscribe: ");
        showProgressDialog();
    }

    @Override
    public void onNext(T t) {
        listener.onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        ((BaseActivity)context).showBottomSnackBar(context.getString(R.string.server_connect_errors));
        Log.e(TAG, "onError: ", e);
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        Log.d(TAG, "onComplete: ");
    }

    @Override
    public void onCancelProgress() {
        //如果处于订阅状态，则取消订阅
        if (!d.isDisposed()) {
            d.dispose();
        }
    }
}
