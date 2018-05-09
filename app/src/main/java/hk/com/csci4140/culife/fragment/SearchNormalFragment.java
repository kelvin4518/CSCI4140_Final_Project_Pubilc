package hk.com.csci4140.culife.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cazaea.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.SearchNormalOptionModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by liujie(Jerry Liu) on 10/04/2018.
 */

public class SearchNormalFragment extends BaseFragment {

    private static final String TAG = "SearchNormalFrag";

    @BindView(R.id.search_normal_radio_group)
    RadioRealButtonGroup radioRealButtonGroup;

    @BindView(R.id.search_normal_mission_layout)
    LinearLayout missionLayout;

    @BindView(R.id.search_normal_mission_key_word)
    EditText mMissionKeyWord;

    @BindView(R.id.search_normal_mission_type_spinner)
    Spinner mMissionTypeSpinner;

    @BindView(R.id.search_normal_mission_date)
    TextView mMissionDate;

    @BindView(R.id.search_normal_mission_salary_spinner)
    Spinner mMissionSalarySpinner;

    @BindView(R.id.search_normal_mission_location_spinner)
    Spinner mMissionLocationSpinner;

    @BindView(R.id.search_normal_taker_layout)
    LinearLayout takerLayout;

    @BindView(R.id.search_normal_taker_industry_spinner)
    Spinner mTakerIndustrySpinner;

    @BindView(R.id.search_normal_taker_others)
    EditText mTakerOthers;

    @BindView(R.id.search_normal_taker_introduction)
    EditText mTakerIntroduction;

    @BindView(R.id.search_normal_taker_ability)
    EditText mTakerAbility;


    private SearchNormalOptionModel optionModel;
    private HashMap<String, String> parameter;

    private int selectedType = -1;
    private int selectedSalaryRange = -1;
    private int selectedLocation = -1;
    private int selectedIndustry = -1;

    private String pickEndDate = null;
    private String pickStartDate = null;
    private String pickStartDateTem = null;


    public enum SelectedTab{

        MISSION(), TAKER()

    }


    private SelectedTab currentTabSelected = SelectedTab.MISSION;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_search_normal, container, false);
        ButterKnife.bind(this, mView);

        radioRealButtonGroup.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
                if(position == 0){
                    clickShowMission();
                }
                else if(position == 1){
                    clickShowTaker();
                }
            }
        });


        switch (currentTabSelected){
            case MISSION:
                radioRealButtonGroup.setPosition(0);
                clickShowMission();
                break;
            case TAKER:
                radioRealButtonGroup.setPosition(1);
                clickShowTaker();
                break;
            default:
                break;
        }


        return mView;
    }


    @Override
    public void onStart(){
        super.onStart();
        getBottomNav().setVisibility(View.GONE);
        setFragmentContentLayoutMarginBottom(0);

        //If has not called the api
        if(optionModel == null){
            getSearchOptions();
        }
        else {
            //Initialize the spinner
            initTypeSpinner();
            initSalarySpinner();
            initLocationSpinner();
            initIndustrySpinner();
        }
    }


    //Click Search Mission
    private void clickShowMission(){
        currentTabSelected = SelectedTab.MISSION;
        missionLayout.setVisibility(View.VISIBLE);
        takerLayout.setVisibility(View.GONE);
    }


    //Click Search Taker
    private void clickShowTaker(){
        currentTabSelected = SelectedTab.TAKER;
        missionLayout.setVisibility(View.GONE);
        takerLayout.setVisibility(View.VISIBLE);
    }



    //Call Http to get Search Options
    private void getSearchOptions(){
        //Get parameter
        String token = UserModel.token;
        int languageId = Utility.getLanguageId(getContext());
        int region = UserModel.region;

        //Call api to add/remove fav
        ObserverOnNextListener<SearchNormalOptionModel> observer = new ObserverOnNextListener<SearchNormalOptionModel>() {
            @Override
            public void onNext(SearchNormalOptionModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    optionModel = model;

                    //Initialize the spinner
                    initTypeSpinner();
                    initSalarySpinner();
                    initLocationSpinner();
                    initIndustrySpinner();
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().getSearchNormalOptions(new ProgressObserver<SearchNormalOptionModel>(getContext(), observer), token, languageId, region);
    }


    //Initialize the type spinner
    private void initTypeSpinner(){
        ArrayList<String> typeList = new ArrayList<>();
        //Initial the type list
        for(SearchNormalOptionModel.Result.MissionType type : optionModel.getResult().getMissionTypeList()){
            typeList.add(type.getType());
        }

        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<>(getContext(), R.layout.item_customize_spinner, typeList);
        mMissionTypeSpinner.setAdapter(spinnerAdapter);
        mMissionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //If user has already select a item, set it
        if(selectedType != -1){
            mMissionTypeSpinner.setSelection(selectedType);
        }
    }


    //Initialize the salary spinner
    private void initSalarySpinner(){
        ArrayList<String> salaryList = new ArrayList<>();
        //Initial the salary range list
        for(SearchNormalOptionModel.Result.SalaryRange range : optionModel.getResult().getSalaryRangeList()){
            salaryList.add(range.getRange());
        }

        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<>(getContext(), R.layout.item_customize_spinner, salaryList);
        mMissionSalarySpinner.setAdapter(spinnerAdapter);
        mMissionSalarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSalaryRange = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //If user has already select a item, set it
        if(selectedSalaryRange != -1){
            mMissionSalarySpinner.setSelection(selectedSalaryRange);
        }
    }


    //Initialize the location spinner
    private void initLocationSpinner(){
        ArrayList<String> locationList = new ArrayList<>();
        //Initial the location list
        for(SearchNormalOptionModel.Result.District district : optionModel.getResult().getDistrictList()){
            locationList.add(district.getDistrict());
        }

        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<>(getContext(), R.layout.item_customize_spinner, locationList);
        mMissionLocationSpinner.setAdapter(spinnerAdapter);
        mMissionLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedLocation = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //If user has already select a item, set it
        if(selectedLocation != -1){
            mMissionLocationSpinner.setSelection(selectedLocation);
        }
    }


    //Initialize the location spinner
    private void initIndustrySpinner(){
        ArrayList<String> industryList = new ArrayList<>();
        //Initial the industry list
        for(SearchNormalOptionModel.Result.Industry industry : optionModel.getResult().getIndustryList()){
            industryList.add(industry.getIndustry());
        }

        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<>(getContext(), R.layout.item_customize_spinner, industryList);
        mTakerIndustrySpinner.setAdapter(spinnerAdapter);
        mTakerIndustrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedIndustry = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //If user has already select a item, set it
        if(selectedIndustry != -1){
            mTakerIndustrySpinner.setSelection(selectedIndustry);
        }
    }


    @OnClick(R.id.search_normal_mission_date)
    void onClickPickDate(final TextView textView){
        //Get the current date
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerEnd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            //选择完日期后会调用该回调函数
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //If picked date is early than today
                String pickedDate = Utility.getDateString(year, (monthOfYear) + 1, dayOfMonth);
                String today = Utility.getToday();
                if(Utility.compareDate(pickedDate, today)){
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.warning_title))
                            .setContentText(getString(R.string.search_normal_date_before_today_warning))
                            .setConfirmText(getString(R.string.warning_confirm))
                            .show();
                    return;
                }

                //If the end date is early thant start date
                if(Utility.compareDate(pickedDate, pickStartDateTem)){
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.warning_title))
                            .setContentText(getString(R.string.search_normal_date_start_after_end))
                            .setConfirmText(getString(R.string.warning_confirm))
                            .show();
                    return;
                }


                //Save the value to pickEndDate and pickStartDate
                pickEndDate = pickedDate;
                pickStartDate = pickStartDateTem;
                pickStartDateTem = null;

                //Set date text to textview
                String dateRange = String.format(getString(R.string.search_normal_date_format), pickStartDate, pickEndDate);
                textView.setText(dateRange);

            }
        }, mYear, mMonth, mDay);
        datePickerEnd.setTitle(getString(R.string.search_normal_pick_end_date_title));
        //弹出选择日期对话框

        new MaterialDialog.Builder(getContext())
                .title(getString(R.string.selection_title))
                .titleColor(getResources().getColor(R.color.colorAccent))
                .itemsColor(getResources().getColor(R.color.textColor))
                .items(R.array.search_normal_date_array)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(which == 0){
                            textView.setText(text.toString());
                        }
                        else {
                            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                                //选择完日期后会调用该回调函数
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    //If picked date is early than today
                                    String pickedDate = Utility.getDateString(year, (monthOfYear) + 1, dayOfMonth);
                                    String today = Utility.getToday();
                                    if(Utility.compareDate(pickedDate, today)){
                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText(getString(R.string.warning_title))
                                                .setContentText(getString(R.string.search_normal_date_before_today_warning))
                                                .setConfirmText(getString(R.string.warning_confirm))
                                                .show();
                                        return;
                                    }

                                    //Save the value to temporary variable
                                    pickStartDateTem = pickedDate;

                                    datePickerEnd.show();

                                }
                            }, mYear, mMonth, mDay);

                            //Set the title of start dialog
                            datePickerDialog.setTitle(getString(R.string.search_normal_pick_start_date_title));
                            //弹出选择日期对话框
                            datePickerDialog.show();
                        }
                    }
                })
                .show();

    }






    @OnClick(R.id.search_normal_continue_button)
    void onClickContinue(){
        //TODO: save the data that user input
        //TODO: pass the data to next fragment
        //TODO: goto search result fragment
        Bundle bundle = new Bundle();
        setPrevTitle(getString(R.string.search_management_title));
        switch (currentTabSelected){
            case MISSION:
                ((BaseFragment)getParentFragment()).replaceFragment(new SearchMissionResultFragment(), bundle);
                break;
            case TAKER:
                ((BaseFragment)getParentFragment()).replaceFragment(new SearchTakerResultFragment(), bundle);
                break;
            default:
                break;
        }
    }
}
