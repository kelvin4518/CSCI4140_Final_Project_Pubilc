package hk.com.csci4140.culife.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cazaea.sweetalert.SweetAlertDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.activity.MainActivity;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.EditProfileOptionModel;
import hk.com.csci4140.culife.model.StandardModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhenghao(Kelvin Zheng) on 08/04/2018.
 */

public class EditProfileFragment extends BaseFragment {

    private static final String TAG = "EditProfileFrag";

    @BindView(R.id.edit_profile_image)
    CircleImageView mIcon;

    @BindView(R.id.edit_profile_name)
    EditText mName;

    @BindView(R.id.edit_profile_phone)
    EditText mPhone;

    @BindView(R.id.edit_profile_age_range_spinner)
    Spinner mAgeRange;

    @BindView(R.id.edit_profile_email)
    EditText mEmail;

    @BindView(R.id.edit_profile_introduction)
    EditText mSelfIntro;

    @BindView(R.id.edit_profile_industry_list)
    TextView mIndustry;

    @BindView(R.id.edit_profile_others_industry)
    EditText mOtherIndustry;

    @BindView(R.id.edit_profile_ability)
    EditText mAbility;

    private String mTitle;
    private String mPrevTitle;
    private String errorText = "";
    private PrevFragment prevFragment;

    private EditProfileOptionModel mOptionsModel;
    private MultipartBody.Part iconPart;
    private int selectedAgeRange = -1;
    private ArrayList<String> industryList;
    private ArrayList<Integer> selectedIndustry;

    private enum PrevFragment{
        USER_PROFILE, FINISHED_REGISTER
    }


    //Initial Setting of every fragment
    private void initialSetting(){
        //Set the go back icon
        setGoBackIcon();

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //This fragment has toolbar
        setToolbarVisibility(true);

        //Set the title of this fragment, and set the prev title
        if(mTitle == null){
            if(getPrevTitle().equals(getString(R.string.register_finished_title))){
                //If previous fragment is Register Finished fragment, then the title should be "create profile"
                mTitle = "CULife";
                prevFragment = PrevFragment.FINISHED_REGISTER;
            }
            else {
                prevFragment = PrevFragment.USER_PROFILE;
                //TODO: edit profile title
            }
        }

        if(mPrevTitle == null){
            mPrevTitle = getPrevTitle();
        }

        //Set the tool bar title
        setToolbarTitle(mTitle);

        //Initial Setting of every fragment
        initialSetting();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        //Set the previous fragment's title
        setToolbarTitle(mPrevTitle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

        //TODO: Get the bundle

        //Check if user has already call the get options api
        if(mOptionsModel == null) {
            callGetOptionsHttp();
        }
        else {
            //Initial the age range
            initialAgeRange();

            //Check if the user has selected a industry
            if(selectedIndustry.size() == 0){
                mIndustry.setText(getString(R.string.default_value));
            }
            else {
                //Initial the selected industry
                //Sort the selected industry array by ascending
                Collections.sort(selectedIndustry);
                //Add the industry name to text and set it to TextView
                StringBuilder industryContent = new StringBuilder();
                for(int i = 0; i < selectedIndustry.size(); i ++){
                    if(i != (selectedIndustry.size() - 1)){
                        industryContent.append(industryList.get(selectedIndustry.get(i)));
                        industryContent.append("、");
                    }
                    else {
                        industryContent.append(industryList.get(selectedIndustry.get(i)));
                    }
                }
                mIndustry.setText(industryContent.toString());
            }
        }
    }


    @OnClick({ R.id.edit_profile_image, R.id.edit_profile_image_button})
    void onClickEditImage(){
        //Goto Image Picker and Crop Activity. Set the config
        Intent intent = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropMenuCropButtonTitle(getString(R.string.edit_profile_crop))
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1)
                .setFixAspectRatio(true)
                .getIntent(getContext());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    //Get the file path from Uri
    private String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Override the onActivityResult for getting image uri
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(getContext()).load(resultUri).into(mIcon);

                //Change the Uri to file, and to MultipartBody
                File file = new File(getRealPathFromURI(getContext(), resultUri));
                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"),
                                file
                        );
                iconPart = MultipartBody.Part.createFormData(Constant.PROFILE_ICON, file.getName(), requestFile);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    //Check if the user correctly input the information
    private boolean isInfoValid(){
        if(mName.getText() == null || mName.getText().toString().equals("")){
            errorText = getString(R.string.edit_profile_input_name);
            return false;
        }

        if(mPhone.getText() == null || mPhone.getText().toString().equals("")){
            errorText = getString(R.string.edit_profile_input_phone);
            return false;
        }

        if( (mPhone.getText().toString().length() != Constant.HONGKONG_PHONE_LENGTH)
                &&
                (mPhone.getText().toString().length() != Constant.CHINA_PHONE_LENGTH)){

            errorText = getString(R.string.edit_profile_input_correct_phone);
            return  false;
        }

        return true;
    }


    //Create RequestBody from String
    private RequestBody createPartFromString(String s){
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, s);
    }


    @OnClick(R.id.edit_profile_continue_button)
    void onClickContinue(){
        if(isInfoValid()){
            //Save the user input to Hash Map
            HashMap<String, RequestBody> mParameter = new HashMap<>();
            //Token
            mParameter.put(Constant.TOKEN, createPartFromString(UserModel.token));
            //Name
            mParameter.put(Constant.PROFILE_NAME, createPartFromString(mName.getText().toString()));
            //Phone
            mParameter.put(Constant.PROFILE_PHONE,createPartFromString(mPhone.getText().toString()));
            //Age Range
            if(selectedAgeRange > 0){
                int ageId = mOptionsModel.getResult().getAgeRangeList().get(selectedAgeRange).getId();
                mParameter.put(Constant.PROFILE_AGE_RANGE, createPartFromString(Integer.toString(ageId)));
            }
            //Email
            if(mEmail.getText() != null){
                String email = mEmail.getText().toString();
                mParameter.put(Constant.PROFILE_EMAIL, createPartFromString(email));
            }
            //Self Introduction
            if(mSelfIntro.getText() != null ){
                String selfIntro = mSelfIntro.getText().toString();
                mParameter.put(Constant.PROFILE_SELF_INTRO, createPartFromString(selfIntro));
            }
            //Industry
            Collections.sort(selectedIndustry);
            for(int i = 0; i < selectedIndustry.size(); i ++){
                int position = selectedIndustry.get(i);
                String id = Integer.toString(mOptionsModel.getResult().getIndustryList().get(position).getId());
                mParameter.put("industry[" + i + "]", createPartFromString(id));
            }
            //Other Industry
            if(mOtherIndustry.getText() != null){
                String otherIndustry = mOtherIndustry.getText().toString();
                mParameter.put(Constant.PROFILE_OTHER_INDUSTRY, createPartFromString(otherIndustry));
            }
            //Ability
            if(mAbility.getText() != null){
                String ability = mAbility.getText().toString();
                mParameter.put(Constant.PROFILE_ABILITY, createPartFromString(ability));
            }


            //Call API the upload data
            callCreateProfileHttp(mParameter, iconPart);
        }
        else {
            //Error dialog
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.warning_title))
                    .setContentText(errorText)
                    .setConfirmText(getString(R.string.warning_confirm))
                    .show();
        }
    }


    @OnClick(R.id.edit_profile_industry_list)
    void onClickIndustry(){
        //Set the industry list fragment
        setPrevTitle(mTitle);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constant.EDIT_INDUSTRY_LIST, industryList);
        bundle.putIntegerArrayList(Constant.EDIT_SELECTED_INDUSTRY_LIST, selectedIndustry);
        replaceFragment(new IndustryListFragment(), bundle);
    }


    //Initial the Age Range Spinner
    private void initialAgeRange(){
        ArrayList<String> ageRangeList = new ArrayList<>();
        //Initial the Age Range
        for(EditProfileOptionModel.Result.AgeRange ageRange : mOptionsModel.getResult().getAgeRangeList()){
            ageRangeList.add(ageRange.getAgeRange());
        }

        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<>(getContext(), R.layout.item_customize_spinner, ageRangeList);
        mAgeRange.setAdapter(spinnerAdapter);
        mAgeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAgeRange = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //If user has already select a item, set it
        if(selectedAgeRange != -1){
            mAgeRange.setSelection(selectedAgeRange);
        }
    }


    //Call the Http to get the options value
    private void callGetOptionsHttp(){
        //Initialize all the list
        selectedIndustry = new ArrayList<>();
        industryList = new ArrayList<>();

        ObserverOnNextListener<EditProfileOptionModel> observer = new ObserverOnNextListener<EditProfileOptionModel>() {
            @Override
            public void onNext(EditProfileOptionModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){

                    mOptionsModel = model;

                    //Initial the spinner
                    initialAgeRange();

                    //Initial the Industry list
                    for(EditProfileOptionModel.Result.Industry industry: mOptionsModel.getResult().getIndustryList()){
                        industryList.add(industry.getIndustryName());
                    }

                    //TODO: initial the user profile (from bundle)

                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().getEditProfileOptions(new ProgressObserver<EditProfileOptionModel>(getContext(), observer), UserModel.token, Utility.getLanguageId(getContext()));
    }


    //Call the Http to upload user profile
    private void callCreateProfileHttp(HashMap<String, RequestBody> map, @Nullable MultipartBody.Part iconFile){
        ObserverOnNextListener<StandardModel> observer = new ObserverOnNextListener<StandardModel>() {
            @Override
            public void onNext(StandardModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    //TODO: update icon url in UserModel
                    switch (prevFragment){
                        case FINISHED_REGISTER:
                            //Goto Main Activity
                            replaceActivity(MainActivity.class, null);
                            break;
                        case USER_PROFILE:
                            //TODO: Go to User Profile Fragment
                            break;
                        default:
                            break;
                    }
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().createProfile(new ProgressObserver<StandardModel>(getContext(), observer), map, iconFile);
    }



}
