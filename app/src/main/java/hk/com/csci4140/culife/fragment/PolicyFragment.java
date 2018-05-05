package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.R;

/**
 * Created by liujie(Jerry Liu) on 31/04/2018.
 */

public class PolicyFragment extends BaseFragment {

    private static final String TAG = "PolicyFrag";

    private String mTitle;

    //TODO: replace with correct policy html file
    private String traditional = "file:///android_asset/policy-traditional.html";

    @BindView(R.id.webview)
    WebView policyView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Set the title of this fragment
        mTitle = getString(R.string.policy_fragment_title);
        setToolbarTitle(mTitle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_policy, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onStart(){
        super.onStart();

        initPolicyView();
    }

    private void initPolicyView(){
        policyView.loadUrl(traditional);
        policyView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @OnClick(R.id.policy_agree_button)
    void onClickAgree(){
        //Go to Home Fragment
        setFragment(new HomeFragment(), null);
    }
}
