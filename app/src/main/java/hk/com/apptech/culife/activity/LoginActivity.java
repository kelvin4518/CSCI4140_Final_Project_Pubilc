package hk.com.apptech.culife.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import hk.com.apptech.culife.R;
import hk.com.apptech.culife.fragment.LoginFragment;
import hk.com.apptech.culife.model.UserModel;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Set the Login Fragment
        setFragment(new LoginFragment(), null);
    }

}
