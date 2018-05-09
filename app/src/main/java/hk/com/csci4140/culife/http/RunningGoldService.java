package hk.com.csci4140.culife.http;

import java.util.Map;

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
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by linjiajie(August Lin) on 14/11/2017.
 */

public interface RunningGoldService {

    //1.Register Part
    //=========================== Start ===========================

    //Register(Phone)
    /**
     *
     * String   phone
     * Integer  region (1 = HK, 2 = China)
     */
    @FormUrlEncoded
    @POST("send_sms_reg")
    Observable<GetVerificationCodeModel> registerPhone(@Field("phone")String phone,
                                                       @Field("region")int region
    );


    //Verify Code ( Register Phone )
    /**
     *
     * String   phone
     * String   password
     * String   region (1 = HK, 2 = China)
     * String   verificationCode
     * String   device_token
     * String   device_type (IOS / Android)
     *
     */
    @FormUrlEncoded
    @POST("user_verification")
    Observable<RegisterVerifyPhoneModel> verifyByRegisterPhone(@FieldMap Map<String, String> map
    );
    //=========================== END ===========================





    //2.Login Part
    //=========================== Start ===========================

    //Login (By Phone)
    /**
     *
     * String   phone
     * String   password
     * String   device_token
     * String   device_type (IOS / Android)
     *
     */
    @FormUrlEncoded
    @POST("user_login")
    Observable<LoginModel> loginByPhone(@FieldMap Map<String, String> map
    );
    //===========================  END  ===========================

    //Login (By Email)
    /**
     *
     * String   email
     * String   password
     * String   device_token
     * String   device_type (IOS / Android)
     *
     */
    @FormUrlEncoded
    @POST("users/login")
    Observable<LoginModel> loginByEmail(@Field("user") Map<String, String> map
    );
    //===========================  END  ===========================





    //3.Forget Password Part
    //=========================== Start ===========================

    //Forget Password
    /**
     *
     * String   phone
     *
     */
    @FormUrlEncoded
    @POST("forget_psw")
    Observable<GetVerificationCodeModel> forgetPassword(@Field("phone") String phone
    );
    //===========================  END  ===========================





    //4.User Part
    //=========================== Start ===========================

    //Get the Edit Profile Options
    /**
     *
     * String        token
     *
     */
    @FormUrlEncoded
    @POST("create_profile_option")
    Observable<EditProfileOptionModel> getEditProfileOptions(@Field("token") String token,
                                                             @Field("lang") int lang
    );


    //Create(Edit) User Profile
    /**
     *
     * String        token
     * String        name
     * String        phone
     * String        email (Option)
     * String        age_range (Option, id)
     * String        self_intro (Option)
     * List<Integer> industry  (Option)
     * String        other_industry (Option)
     * File          icon_image (Option)
     *
     */
    @Multipart
    @POST("create_profile")
    Observable<StandardModel> createProfile(@PartMap() Map<String, RequestBody> partMap
    );

    @Multipart
    @POST("create_profile")
    Observable<StandardModel> createProfile(@PartMap() Map<String, RequestBody> partMap,
                                            @Part MultipartBody.Part file
    );


    //Set Region
    @FormUrlEncoded
    @POST("set_region")
    Observable<StandardModel> setRegion(@Field("token")String token,
                                        @Field("region")int region
    );


    //Set Show Location
    @FormUrlEncoded
    @POST("set_is_show_location")
    Observable<StandardModel> setIsShowLocation(@Field("token")String token,
                                                @Field("show_location")int showLocation
    );

    //===========================  END  ===========================





    //5.Mission Part
    //=========================== Start ===========================

    //Get the create mission Options
    /**
     *
     * String        token
     *
     */
    @FormUrlEncoded
    @POST("create_mission_option")
    Observable<CreateMissionOptionModel> getCreateMissionOptions(@Field("token") String token,
                                                                 @Field("lang")int lang
    );


    //Create mission
    @FormUrlEncoded
    @POST("create_mission")
    Observable<CreateMissionModel> createMission(@FieldMap Map<String, String> map
    );


    //Get Mission Detail
    /**
     *
     * @param missionId
     * @param region
     * @param lang
     *
     */
    @FormUrlEncoded
    @POST("get_mission_detail")
    Observable<GetMissionDetailModel> getMissionDetail(@Field("token")String token,
                                                       @Field("mission_id")int missionId,
                                                       @Field("region")int region,
                                                       @Field("lang")int lang
    );

    @FormUrlEncoded
    @POST("get_mission_detail")
    Observable<GetMissionDetailModel> getMissionDetail(@Field("mission_id")int missionId,
                                                       @Field("region")int region,
                                                       @Field("lang")int lang
    );
    //===========================  END  ===========================





    //6. Search Part
    //=========================== Start ===========================

    //Search Normal Options
    /**
     *
     * String token
     * int    region
     * int    lang
     *
     */
    @FormUrlEncoded
    @POST("search_option")
    Observable<SearchNormalOptionModel> getSearchNormalOption(@Field("token")String token,
                                                       @Field("lang")int lang,
                                                       @Field("region")int region
    );


    //Search Nearby
    /**
     *
     * String        token
     * String        lat
     * String        lng
     * String        lang
     * String        region
     *
     */
    @FormUrlEncoded
    @POST("get_nearby_list")
    Observable<NearbyListModel> getNearbyList(@FieldMap Map<String, String> map
    );


    //Search Collection
    /**
     *
     * String       token
     * int          lang
     * int          region
     *
     */
    @FormUrlEncoded
    @POST("get_fav_missions")
    Observable<CollectionsModel> getCollections(@Field("token")String token,
                                                @Field("lang")int lang,
                                                @Field("region")int region,
                                                @Field("page")int page
    );


    //Add/Remove Mission to/from Fav
    /**
     *
     * String   token
     * int      mission_id
     *
     */
    @FormUrlEncoded
    @POST("add_to_fav")
    Observable<StandardModel> addMissionToFav(@Field("token")String token,
                                              @Field("mission_id")int id
    );
    //===========================  END  ===========================





    //7. Map Part
    //=========================== Start ===========================

    //Get Nearby Options
    @FormUrlEncoded
    @POST("get_nearby_option")
    Observable<ShiftOptionModel> getShiftOption(@Field("lang")int lang
    );


    //Get Nearby (mission, taker, mission and taker)
    /**
     *
     * lat*
     * lng*
     * region*  (1 = HK, 2 = China)
     * lang* (1 = TC, 2 = SC)  - if (show_type = 1 or 3)
     * show_type* (1 = 顯示全部, 3 = 顯示任務, 4 ＝ 顯示委託人)
     * type_id  (get_nearby_option - mission_type) mission_type_sc
     * salary_range_id (get_nearby_option -salary_range ) mission_type_sc
     *
     */
    @FormUrlEncoded
    @POST("get_nearby")
    Observable<MapModel> getMap(@FieldMap Map<String, String> map
    );


    //Get Nearby (district)
    /**
     * lat*
     * lng*
     * region*  (1 = HK, 2 = China)
     * show_type* ( 2 = 顯示任務(地區))
     * type_id  (get_nearby_option - mission_type) mission_type_sc
     * salary_range_id (get_nearby_option -salary_range ) mission_type_sc
     *
     */
    @FormUrlEncoded
    @POST("get_nearby")
    Observable<MapDistrictModel> getMapDistrict(@FieldMap Map<String, String> map
    );

    //===========================  END  ===========================
}
