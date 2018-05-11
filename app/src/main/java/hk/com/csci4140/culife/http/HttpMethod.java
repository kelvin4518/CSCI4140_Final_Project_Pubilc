package hk.com.csci4140.culife.http;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import hk.com.csci4140.culife.model.CollectionsModel;
import hk.com.csci4140.culife.model.CreateMissionModel;
import hk.com.csci4140.culife.model.CreateMissionOptionModel;
import hk.com.csci4140.culife.model.EditProfileOptionModel;
import hk.com.csci4140.culife.model.GetMissionDetailModel;
import hk.com.csci4140.culife.model.LoginModel;
import hk.com.csci4140.culife.model.GetVerificationCodeModel;
import hk.com.csci4140.culife.model.MapDistrictModel;
import hk.com.csci4140.culife.model.MapModel;
import hk.com.csci4140.culife.model.NearbyListModel;
import hk.com.csci4140.culife.model.RegisterVerifyPhoneModel;
import hk.com.csci4140.culife.model.SearchNormalOptionModel;
import hk.com.csci4140.culife.model.ShiftOptionModel;
import hk.com.csci4140.culife.model.StandardModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by linjiajie(August Lin) on 14/11/2017.
 */

public class HttpMethod {

    public static final String BASE_URL = "http://119.81.130.181/runninggold/public/index.php/api/";
    //public static final String BASE_URL = "http://ec2-54-251-167-117.ap-southeast-1.compute.amazonaws.com:8000/api/";

    private static final int DEFAULT_TIMEOUT = 60;

    public static RunningGoldService runningGoldService;
    //单例
    public static RunningGoldService getRunningGoldService() {
        if (runningGoldService == null) {
            synchronized (HttpMethod.class) {
                if (runningGoldService == null) {
                    new HttpMethod();
                }
            }
        }
        return runningGoldService;
    }

    private HttpMethod() {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        runningGoldService = retrofit.create(RunningGoldService.class);
    }


    //Create instance
    private static class SingletonHolder{
        private static final HttpMethod INSTANCE = new HttpMethod();
    }


    //Get instance
    public static HttpMethod getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public static void ApiSubscribe(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    //Register by Phone
    public void registerByPhone(Observer<GetVerificationCodeModel> observer, String phone, int region) {
        ApiSubscribe(HttpMethod.getRunningGoldService().registerPhone(phone, region), observer);
    }


    //Verify by Register Phone
    public void verifyByRegisterPhone(Observer<RegisterVerifyPhoneModel> observer, HashMap<String, String> map){
        ApiSubscribe(HttpMethod.getRunningGoldService().verifyByRegisterPhone(map), observer);
    }


    //Login by Phone
    public void loginByPhone(Observer<LoginModel> observer, HashMap<String, String> map){
        ApiSubscribe(HttpMethod.getRunningGoldService().loginByPhone(map), observer);
    }

    //Login by Email
    public void loginByEmail(Observer<LoginModel> observer, HashMap<String, String> map){
        ApiSubscribe(HttpMethod.getRunningGoldService().loginByEmail(map), observer);
    }

    //Forget Password
    public void forgetPassword(Observer<GetVerificationCodeModel> observer, String phone){
        ApiSubscribe(HttpMethod.getRunningGoldService().forgetPassword(phone), observer);
    }


    //Get the Edit Profile Options
    public void getEditProfileOptions(Observer<EditProfileOptionModel> observer, String token, int lang){
        ApiSubscribe(HttpMethod.getRunningGoldService().getEditProfileOptions(token, lang), observer);
    }


    //Create(Edit) User Profile
    public void createProfile(Observer<StandardModel> observer, HashMap<String, RequestBody> map, @Nullable MultipartBody.Part iconFile){
        if(iconFile == null) {
            ApiSubscribe(HttpMethod.getRunningGoldService().createProfile(map), observer);
        }
        else {
            ApiSubscribe(HttpMethod.getRunningGoldService().createProfile(map, iconFile), observer);
        }
    }


    //Set Region
    public void setRegion(Observer<StandardModel> observer, String token, int region){
        ApiSubscribe(HttpMethod.getRunningGoldService().setRegion(token, region), observer);
    }


    //Set Is Show Location
    public void setIsShowLocation(Observer<StandardModel> observer, String token, int isShown){
        ApiSubscribe(HttpMethod.getRunningGoldService().setIsShowLocation(token, isShown), observer);
    }


    //Get the create mission options
    public void getCreateMissionOptions(Observer<CreateMissionOptionModel> observer, String token, int lang){
        ApiSubscribe(HttpMethod.getRunningGoldService().getCreateMissionOptions(token, lang), observer);
    }


    //Create Mission
    public void createMission(Observer<CreateMissionModel> observer, HashMap<String, String> map){
        ApiSubscribe(HttpMethod.getRunningGoldService().createMission(map), observer);
    }


    //Get Mission Detail
    public void getMissionDetail(Observer<GetMissionDetailModel> observer,String token, int missionId, int region, int lang){
        ApiSubscribe(HttpMethod.getRunningGoldService().getMissionDetail(token, missionId, region, lang), observer);
    }

    public void getMissionDetail(Observer<GetMissionDetailModel> observer, int missionId, int region, int lang){
        ApiSubscribe(HttpMethod.getRunningGoldService().getMissionDetail(missionId, region, lang), observer);
    }


    //Get Search Normal Options
    public void getSearchNormalOptions(Observer<SearchNormalOptionModel> observer, String token, int lang, int region){
        ApiSubscribe(HttpMethod.getRunningGoldService().getSearchNormalOption(token, lang, region), observer);
    }


    //Get Nearby List
    public void getNearbyList(Observer<NearbyListModel> observer, HashMap<String, String> map){
        ApiSubscribe(HttpMethod.getRunningGoldService().getNearbyList(map), observer);
    }


    //Get Collections
    public void getCollections(Observer<CollectionsModel> observer, String token, int lang, int region, int page){
        ApiSubscribe(HttpMethod.getRunningGoldService().getCollections(token, lang, region, page), observer);
    }


    //Add or Remove Mission to/from Fav
    public void addMissionToFav(Observer<StandardModel> observer, String token, int id){
        ApiSubscribe(HttpMethod.getRunningGoldService().addMissionToFav(token, id), observer);
    }


    //Get Shift Options
    public void getShiftOption(Observer<ShiftOptionModel> observer, int lang){
        ApiSubscribe(HttpMethod.getRunningGoldService().getShiftOption(lang), observer);
    }


    //Get Map (Mission, Taker, Mission and Taker)
    public void getMap(Observer<MapModel> observer, HashMap<String, String> map){
        ApiSubscribe(HttpMethod.getRunningGoldService().getMap(map), observer);
    }


    //Get Map District (District)
    public void getMapDistrict(Observer<MapDistrictModel> observer, HashMap<String, String> map){
        ApiSubscribe(HttpMethod.getRunningGoldService().getMapDistrict(map), observer);
    }
}
