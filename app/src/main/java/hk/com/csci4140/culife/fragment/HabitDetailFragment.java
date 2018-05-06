package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cazaea.sweetalert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;



public class HabitDetailFragment extends BaseFragment {

    private static final String TAG = "HabitDetaiFrag";

    private String mHabitName;
    private String mHabitContent;


    private String mTitle;
    private String mPrevTitle;


    //Initial Setting of every fragment
    private void initialSetting() {
        //set Go Back Icon();
        // setGoBackIcon();
        getToolbar().setNavigationIcon(R.drawable.ic_action_go_back);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the previous navigation bar selected item
                getBottomNav().setCurrentItem(((MainActivity) getActivity()).getPreviousItem());
            }
        });


        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(true);
        setPrevBottomNavFragment(true);

        //Use to set the menu icon
        setHasOptionsMenu(true);
    }





    // before the page is shown to the suer
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Set the title of this fragment, and set the prev title
        if (mTitle == null) {
            mTitle = getString(R.string.user_profile_fragment_title);
        }
        if (mPrevTitle == null) {
            mPrevTitle = getPrevTitle();
        }
//        setToolbarTitle(mTitle);
//
//        setPrevTitle("习惯名称");

        //Set the toolbar title of this fragment
        mTitle = "习惯标题";
        setToolbarTitle(mTitle);

        initialSetting();

//        initialSetting();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        //In case the duplicate menu item
//        try {
//            for(int i = 0; i < menu.size(); i ++){
//                menu.getItem(i).setVisible(false);
//            }
//        }catch (Exception e){
//            Log.e(TAG, "onCreateOptionsMenu: " + e.toString());
//        }
//
//        inflater.inflate(R.menu.user_profile_menu, menu);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_habit_detail, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onStart(){
        initialSetting();
        super.onStart();
    }






    // User interacting with the page

    @BindView(R.id.confirm_complete)
    Button confirmBtn;

    @OnClick(R.id.confirm_complete)
    void onClickConfirmComplete(){
        //Show the warning text
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("habit is complete")
                .setContentText("congradulation on finishing a new task")
                .setConfirmText(getString(R.string.warning_confirm))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        if(confirmBtn.getText().toString().equalsIgnoreCase("确认完成")){
                            confirmBtn.setText("取消");
                            confirmBtn.setBackgroundColor(getResources().getColor(R.color.greyDim));
                        } else if(confirmBtn.getText().toString().equalsIgnoreCase("取消")){
                            confirmBtn.setText("确认完成");
                            confirmBtn.setBackgroundColor(getResources().getColor(R.color.blue_btn_bg_color));
                        }
//                        confirmBtn.setText("确认完成");
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }



    // the page is dismissed
    @Override
    public void onDestroy(){
        super.onDestroy();

        //Set the title of previous fragment
        setToolbarTitle(mPrevTitle);
    }



}
