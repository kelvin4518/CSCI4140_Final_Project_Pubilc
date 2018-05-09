package hk.com.csci4140.culife.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.fragment.LoginFragment;
import hk.com.csci4140.culife.model.UserModel;

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
