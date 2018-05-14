package hk.com.csci4140.culife.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cazaea.sweetalert.SweetAlertDialog;

import org.json.JSONObject;

import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.model.UserChatModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.utility.SessionManager;
import hk.com.csci4140.culife.utility.Utility;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by zhenghao(Kelvin Zheng) on 30/04/2018.
 */

@RuntimePermissions
public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //TODO: replace the Imageview in layout to desire image
        setContentView(R.layout.activity_welcome);

        //Set the Language of app
        Utility.setLanguage(WelcomeActivity.this);



        try {
//            JSONObject object = new JSONObject();
//            object.put(Constant.USER_CHAT_LIST_OTHER_USER_ID,"9");
//            object.put(Constant.USER_CHAT_LIST_ICON_LINK,"https://avatars0.githubusercontent.com/u/9919?s=280&v=4");
//            object.put(Constant.USER_CHAT_LIST_NAME,"username!");
//            object.put(Constant.USER_CHAT_LIST_LAST_MESSAGE,"some message");
//            object.put(Constant.USER_CHAT_LIST_LAST_DATE,"04/05/2018");
//            Log.d("TEST ADD", "here 1 ");
//            UserModel.addNewChatToChatList(object);
//            Log.d("TEST ADD", "here 2 ");
        }catch (Exception e){

        }


        //Initial User login status
        UserModel.initModel(WelcomeActivity.this);
        Log.d(TAG, "onCreate: welcome init user model");
        if(!UserModel.isRemember){
            //If user has not checked the remember me, clear login status
            UserModel.logout(WelcomeActivity.this);
        }


        SessionManager.putString(WelcomeActivity.this,Constant.USER_CHATTING_DATABASE,"");


        // initialize the UserChatModel
        // UserChatModel.initUserChatModelFromLocalStorage(WelcomeActivity.this);




        //Permission Check
        WelcomeActivityPermissionsDispatcher.showNextWithPermissionCheck(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        WelcomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @NeedsPermission({Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //After 2 second, go to MainActivity
                if(UserModel.isLogin){
                    replaceActivity(MainActivity.class, null);
                }
                else{
                    replaceActivity(LoginActivity.class, null);
                }

            }
        }, 2000);
    }


    @OnShowRationale({Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    //用户拒绝了一次之后弹出来，解释为了要弹这一次的对话框，用于解释为什么需要这个权限
    void showWhy(final PermissionRequest request) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.warning_title))
                .setContentText(getString(R.string.permission_require))
                .setConfirmText(getString(R.string.permission_check))
                .showCancelButton(false)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        request.proceed();//再次执行请求
                    }
                })
                .show();
    }


    //If user deny to open permission
    @OnPermissionDenied({Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForPermission() {
        // make snackbar
        Snackbar mSnackbar = Snackbar.make(findViewById(R.id.welcome_activity_container), getString(R.string.permission_reject), Snackbar.LENGTH_SHORT);
        // get snackbar view
        View mView = mSnackbar.getView();
        // get textview inside snackbar view
        TextView mTextView = mView.findViewById(android.support.design.R.id.snackbar_text);
        // set text to center
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        // show the snackbar
        mSnackbar.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Kill app
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }, 2000);

    }


}
