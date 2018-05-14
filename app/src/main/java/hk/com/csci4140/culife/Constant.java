package hk.com.csci4140.culife;

/**
 * Created by Created by gechen(Ge Chen) on 05/04/2018.
 */

public interface Constant {

    String API_REPORT_TAG = "API_REPORT";
    String API_BASE_URL = "http://ec2-54-251-167-117.ap-southeast-1.compute.amazonaws.com:8000/api/";



    String USER_GPS_INFO = "GPS_INFO";
    String GPS_RECORDING_DATE = "RECORDING_DATE";
    String GPS_TIME = "GPS_TIME";
    String GPS_LAT = "GPS_LAT";
    String GPS_LNG = "GPS_LNG";





    String USERID = "USERID";
    String USERNAME = "username";
    String USER_CHAT_LIST  = "user_chat_list";
    String USER_CHAT_LIST_OTHER_USER_ID  = "chat_other_user_ID";
    String USER_CHAT_LIST_ICON_LINK  = "chat_icon_link";
    String USER_CHAT_LIST_NAME  = "chat_other_user_name";
    String USER_CHAT_LIST_LAST_MESSAGE  = "chat_last_message";
    String USER_CHAT_LIST_LAST_DATE  = "chat_last_date";
    String USER_CHAT_IS_NOT_READ = "chat_is_not_read";

    String USER_CHATTING_DATABASE = "chat_current_using_database";
    /**
     * associated with read / unread
     * set to "" when welcome
     * set to <OtherUserID> when chat with other user
     * set to "" when exit
     * */



    // FriendListFragment Mode:
    String FRIEND_LIST_FRAGMENT_INVITE_MODE = "use_friend_list_to_invite_friend_to_habit";
    String FRIEND_LIST_FRAGMENT_START_CHAT_MODE = "use_friend_list_to_start_a_chat";
    String FRIEND_LIST_FRAGMENT_VIEW_FRIEND_LIST_MODE = "use_friend_list_to_view_my_friend_and_see_their_profile";



    //General String Value
    String ACTIVITY_BUNDLE = "activity_bundle";
    String TRUE = "1";
    String FALSE = "0";

    //Setting Page
    String LANGUAGE_SETTING = "language_setting";

    //Login Fragment
    String IS_LOGIN = "is_login";
    String IS_REMEMBER = "is_remember";
    String TOKEN    = "token";
    String ICON_URL  = "icon_url";
    String REGION   = "region";
    String SHOW_LOCATION = "show_location";

    //Register by Phone
    String REGISTER_PHONE_NUM = "register_phone_num";
    String REGISTER_PASSWORD  = "register_password";
    String REGISTER_REGION  = "register_region";
    String REGISTER_SHOW_LOCATION = "register_show_location";

    //Edit Profile Fragment
    String EDIT_INDUSTRY_LIST = "edit_industry_list";
    String EDIT_SELECTED_INDUSTRY_LIST = "edit_selected_industry_list";

    //Home Shift Fragment
    String HOME_SHIFT_OPTION_VALUE = "home_shift_option_value";
    String HOME_SHIFT_TYPE = "home_shift_type";
    String HOME_SHIFT_SALARY = "home_shift_salary";
    String HOME_SHIFT_DATE_START = "home_shift_date_start";
    String HOME_SHIFT_DATE_END = "home_shift_date_end";

    //Post Mission Step One
    String MISSION_MAP_PARAMETER = "mission_map_parameter";

    //Post Mission Finished
    String MISSION_POST_FINISHED_TOTAL_FEE = "mission_post_finished_arguments";
    String MISSION_POST_FINISHED_END_DATE = "mission_post_finished_end_date";
    String MISSION_POST_FINISHED_NUM_NEEDED = "mission_post_finished_num_needed";
    String MISSION_POST_FINISHED_SERVICE_FEE = "mission_post_finished_service_fee";
    String MISSION_POST_FINISHED_MISSION_ID = "mission_post_finished_mission_id";

    //Mission Detail Fragment
    String MISSION_DETAIL_MISSION_ID = "mission_detail_mission_id";

    //Setting Fragment
    String IS_SHOW_LOCATION = "is_show_location";


    //Region Array Position
    int NON_SELECT = 0;
    int HONG_KONG = 1;
    int CHINA = 2;

    //Area Code
    String HONG_KONG_AREA_CODE = "+852";
    String CHINA_AREA_CODE = "+86";

    //Phone Length
    int HONGKONG_PHONE_LENGTH = 8;
    int CHINA_PHONE_LENGTH = 11;


    //API Part ========================== START ==========================
    //General Value
    String DEVICE_TYPE     = "Android";
    String CONNECT_SUCCESS = "1";
    String CONNECT_FAILED  = "0";

    //Login (By phone)
    String LOGIN_PHONE_PASSWORD = "password";
    String LOGIN_PHONE_PHONE    = "phone";
    String LOGIN_PHONE_DEVICE_TYPE = "device_type";
    String LOGIN_PHONE_DEVICE_TOKEN = "device_token";


    //Verify Code ( Register Phone )
    String VERIFY_PASSWORD = "password";
    String VERIFY_PHONE    = "phone";
    String VERIFY_REGION   = "region";
    String VERIFY_CODE     = "verificationCode";
    String VERIFY_DEVICE_TYPE = "device_type";
    String VERIFY_DEVICE_TOKEN = "device_token";


    //Edit Profile
    String PROFILE_NAME = "name";
    String PROFILE_PHONE = "phone";
    String PROFILE_EMAIL = "email";
    String PROFILE_AGE_RANGE = "age_range";
    String PROFILE_SELF_INTRO = "self_intro";
    String PROFILE_OTHER_INDUSTRY = "other_industry";
    String PROFILE_ABILITY = "ability";
    String PROFILE_ICON    = "icon_image";

    //Post Mission
    String MISSION_TITLE = "title";
    String MISSION_TYPE_ID = "type_id";
    String MISSION_TYPE  = "mission_type";
    String MISSION_NO_SPEC_DATE = "no_spec_date";
    String MISSION_DATE = "date";
    String MISSION_START_TIME = "start_time";
    String MISSION_END_TIME = "end_time";
    String MISSION_NUM_NEEDED = "num_of_need";
    String MISSION_POST_METHOD = "post_method";
    String MISSION_POST_START = "post_start";
    String MISSION_POST_END = "post_end";
    String MISSION_POST_PERIOD = "mission_post_period";
    String MISSION_CONTENT = "content";
    String MISSION_POST_HABBIT_CONTENT = "content";
    String MISSION_REQUIREMENT = "requirement";
    String MISSION_REGION = "region";

    //Get Nearby List
    String NEARBY_LAT = "lat";
    String NEARBY_LNG = "lng";
    String NEARBY_LANGUAGE = "lang";
    String NEARBY_REGION = "region";
    String NEARBY_PAGE = "page";


    //Home Fragment
    String HOME_LAT = "lat";
    String HOME_LNG = "lng";
    String HOME_REGION = "region";
    String HOME_LANG = "lang";
    String HOME_SHOW_TYPE = "show_type";
    String HOME_SHOW_ALL = "1";
    String HOME_SHOW_MISSION_DISTRICT = "2";
    String HOME_SHOW_MISSION = "3";
    String HOME_SHOW_TAKER = "4";
    String HOME_TYPE_ID = "type_id";
    String HOME_SALARY_ID = "salary_id";
    String HOME_START_DATE = "start_date";
    String HOME_END_DATE = "end_date";
    String HOME_MARKER_POSITION = "home_marker_position";
    String HOME_MARKER_TYPE = "home_marker_type";
    String HOME_MARKER_MISSION = "0";
    String HOME_MARKER_TAKER = "1";


    //API Part ==========================  END  ==========================

}
