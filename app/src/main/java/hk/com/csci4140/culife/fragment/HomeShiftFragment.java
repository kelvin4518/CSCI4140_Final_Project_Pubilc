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
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.SearchNormalOptionModel;
import hk.com.csci4140.culife.model.ShiftOptionModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by liujie(Jerry Liu) on 09/04/2018.
 */

public class HomeShiftFragment extends BaseFragment {

    private static final String TAG = "HomeShiftFrag";

    @BindView(R.id.home_shift_type_spinner)
    Spinner typeSpinner;

    @BindView(R.id.home_shift_salary_spinner)
    Spinner salarySpinner;

    @BindView(R.id.home_shift_select_date)
    TextView selectDate;

    private String mTitle;
    private String mPrevTitle;

    private HashMap<String, String> shiftOption;
    private ShiftOptionModel.Result result;

    private int selectedType = -1;
    private int selectedSalary = -1;

    private String pickStartDate = null;
    private String pickStartDateTem = null;
    private String pickEndDate = null;

    private void initialSetting(){
        //Set the Go Back
        setGoBackIcon();
        //Set Login Icon Invisible
        setLoginIconVisible(false);

        //Set the bottom navigation visibility
        setBottomNavFragment(false);
        setPrevBottomNavFragment(true);
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Set the title of this fragment, and set the prev title
        if(mTitle == null) {
            mTitle = getString(R.string.home_shift_title);
        }
        if(mPrevTitle == null){
            mPrevTitle = getPrevTitle();
        }
        setToolbarTitle(mTitle);

        initialSetting();

        //Get the shift option map
        Bundle bundle = getArguments();
        try {
            shiftOption = (HashMap<String, String>)bundle.getSerializable(Constant.HOME_SHIFT_OPTION_VALUE);
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        //Set previous Fragment's title
        setToolbarTitle(mPrevTitle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.fragment_home_shift, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onStart(){
        initialSetting();
        super.onStart();

        //Call Api
        getShiftOptionApi();
    }


    //Call get Shift Option api
    private void getShiftOptionApi(){
        ObserverOnNextListener<ShiftOptionModel> observer = new ObserverOnNextListener<ShiftOptionModel>() {
            @Override
            public void onNext(ShiftOptionModel model) {
                if(model.getStatus().equals(Constant.CONNECT_SUCCESS)){
                    result = model.getResult();

                    initialTypeSpinner();

                    initialSalarySpinner();

                    //Initialize the date
                    try{
                        pickStartDate = shiftOption.get(Constant.HOME_SHIFT_DATE_START);
                        pickEndDate = shiftOption.get(Constant.HOME_SHIFT_DATE_END);
                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                    if(pickStartDate != null && pickEndDate != null){
                        String dateRange = String.format(getString(R.string.home_shift_date_format), pickStartDate, pickEndDate);
                        selectDate.setText(dateRange);
                    }
                }
                else if(model.getStatus().equals(Constant.CONNECT_FAILED)){
                    showBottomSnackBar(getString(R.string.network_connect_errors));
                    Log.e(TAG, "Fail: " + model.getResult().getErrors().get(0));
                }
            }
        };
        HttpMethod.getInstance().getShiftOption(new ProgressObserver<ShiftOptionModel>(getContext(), observer), Utility.getLanguageId(getContext()));
    }


    //Initialize the type spinner
    private void initialTypeSpinner(){
        int previousTypeId;
        try {
            previousTypeId = Integer.parseInt(shiftOption.get(Constant.HOME_SHIFT_TYPE));
        }catch (Exception e){
            previousTypeId = -1;
        }

        ArrayList<String> typeList = new ArrayList<>();
        //Initial the type list
        typeList.add(getString(R.string.home_shift_type_default));
        for(ShiftOptionModel.Result.MissionType type : result.getMissionTypes()){
            typeList.add(type.getType());
            //Check if the id equals to the previous selected id
            if(type.getId() == previousTypeId){
                selectedType = result.getMissionTypes().indexOf(type) + 1;
            }
        }

        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<>(getContext(), R.layout.item_customize_spinner, typeList);
        typeSpinner.setAdapter(spinnerAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            typeSpinner.setSelection(selectedType);
        }
    }


    //Initialize the salary spinner
    private void initialSalarySpinner(){
        int previousSalaryId;
        try {
            previousSalaryId = Integer.parseInt(shiftOption.get(Constant.HOME_SHIFT_SALARY));
        }catch (Exception e){
            previousSalaryId = -1;
        }

        ArrayList<String> salaryList = new ArrayList<>();
        //Initial the salary list
        salaryList.add(getString(R.string.home_shift_salary_default));
        for(ShiftOptionModel.Result.SalaryRange salary : result.getSalaryRanges()){
            salaryList.add(salary.getRange());
            //Check if the id equals to the previous selected id
            if(salary.getId() == previousSalaryId){
                selectedSalary = result.getSalaryRanges().indexOf(salary) + 1;
            }
        }

        SpinnerAdapter spinnerAdapter =
                new ArrayAdapter<>(getContext(), R.layout.item_customize_spinner, salaryList);
        salarySpinner.setAdapter(spinnerAdapter);
        salarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSalary = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //If user has already select a item, set it
        if(selectedSalary != -1){
            salarySpinner.setSelection(selectedSalary);
        }
    }


    //Initialize the date picker
    @OnClick(R.id.home_shift_select_date)
    void onClickSelectDate(final TextView textView){

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
                            .setContentText(getString(R.string.home_shift_date_before_today_warning))
                            .setConfirmText(getString(R.string.warning_confirm))
                            .show();
                    return;
                }

                //If the end date is early thant start date
                if(Utility.compareDate(pickedDate, pickStartDateTem)){
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getString(R.string.warning_title))
                            .setContentText(getString(R.string.home_shift_date_start_after_end))
                            .setConfirmText(getString(R.string.warning_confirm))
                            .show();
                    return;
                }


                //Save the value to pickEndDate and pickStartDate
                pickEndDate = pickedDate;
                pickStartDate = pickStartDateTem;
                pickStartDateTem = null;

                //Set date text to text view
                String dateRange = String.format(getString(R.string.home_shift_date_format), pickStartDate, pickEndDate);
                textView.setText(dateRange);

            }
        }, mYear, mMonth, mDay);
        datePickerEnd.setTitle(getString(R.string.home_shift_pick_end_date_title));
        //弹出选择日期对话框

        new MaterialDialog.Builder(getContext())
                .title(getString(R.string.selection_title))
                .titleColor(getResources().getColor(R.color.colorAccent))
                .itemsColor(getResources().getColor(R.color.textColor))
                .items(R.array.home_shift_date_array)
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
                                                .setContentText(getString(R.string.home_shift_date_before_today_warning))
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
                            datePickerDialog.setTitle(getString(R.string.home_shift_pick_start_date_title));
                            //弹出选择日期对话框
                            datePickerDialog.show();
                        }
                    }
                })
                .show();
    }


    @OnClick(R.id.home_shift_confirm_button)
    void onClickConfirm(){

        //Put selected type id
        if(selectedType == 0){
            try{
                shiftOption.remove(Constant.HOME_SHIFT_TYPE);
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }
        else {
            shiftOption.put(Constant.HOME_SHIFT_TYPE, Integer.toString(result.getMissionTypes().get(selectedType - 1).getId()));
        }

        //Put selected salary id
        if(selectedSalary == 0){
            try{
                shiftOption.remove(Constant.HOME_SHIFT_SALARY);
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }
        else {
            shiftOption.put(Constant.HOME_SHIFT_SALARY, Integer.toString(result.getSalaryRanges().get(selectedSalary - 1).getId()));
        }

        //Put selected date
        if(selectDate.getText().toString().equals(getString(R.string.home_shift_date_default))){
            try{
                shiftOption.remove(Constant.HOME_SHIFT_DATE_START);
                shiftOption.remove(Constant.HOME_SHIFT_DATE_END);
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }
        else {
            shiftOption.put(Constant.HOME_SHIFT_DATE_START, pickStartDate);
            shiftOption.put(Constant.HOME_SHIFT_DATE_END, pickEndDate);
        }

        pressBack();
    }


}
