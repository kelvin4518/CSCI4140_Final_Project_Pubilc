package hk.com.csci4140.culife.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cazaea.sweetalert.SweetAlertDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.roger.catloadinglibrary.CatLoadingView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.http.HttpMethod;
import hk.com.csci4140.culife.model.BlogModel;
import hk.com.csci4140.culife.model.GetVerificationCodeModel;
import hk.com.csci4140.culife.model.HabitModel;
import hk.com.csci4140.culife.model.RegisterVerifyPhoneModel;
import hk.com.csci4140.culife.model.UserModel;
import hk.com.csci4140.culife.observer.ObserverOnNextListener;
import hk.com.csci4140.culife.observer.ProgressObserver;
import hk.com.csci4140.culife.utility.Utility;

/**
 * Created by zhenghao(Kelvin Zheng) on 07/04/2018.
 */

public class ShowBlogFragment extends BaseFragment {

    @BindView(R.id.blog_list)
    RecyclerView mBlogrecycle;

    private static final String TAG = "RegisterVerifyPhoneFrag";

    CatLoadingView mCatLoadingView;

    private String mTitle;

    public int blog_habit_id;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Set the title of this fragment, and set the prev title
        if(mTitle == null) {
            mTitle = getString(R.string.register_verify_phone_title);
        }
        setToolbarTitle(mTitle);

        JSONObject jsonParams = new JSONObject();
        JSONObject outerJsonParams = new JSONObject();
        try {
            jsonParams.put("habitid", blog_habit_id);
            outerJsonParams.put("blog", jsonParams);
            StringEntity entity = new StringEntity(outerJsonParams.toString());
            callAPItoGetBlog(entity);
        }
        catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume(){
        super.onResume();

        //Set the Go back Icon
        setGoBackIcon();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View mView = inflater.inflate(R.layout.show_blog, container, false);
        ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    public void callAPItoGetBlog(StringEntity params){
        AsyncHttpClient client = new AsyncHttpClient();
        String AuthorizationToken = "Token "+ UserModel.token;
        client.addHeader("Authorization","Token "+UserModel.token);
        mCatLoadingView = new CatLoadingView();

        mCatLoadingView.show(getFragmentManager(), "");

        client.post(getContext(), Constant.API_BASE_URL+"habits/blog_show",params, ContentType.APPLICATION_JSON.getMimeType(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //ArrayList<HomeFragmentModel> localmList = new ArrayList<HomeFragmentModel>();
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onSuccess: login");
                Log.d("API_REPORT", "onSuccess: Blogstatus : "+statusCode);
                Log.d("API_REPORT", "onSuccess: Blogresponse: "+response);
                int all = 0;
                JSONArray blog_list = new JSONArray();
                try{
                    all = response.getInt("blogsCount");
                    blog_list = response.getJSONArray("blogs");
                }
                catch (JSONException e){

                }
                ArrayList<BlogModel> BlogModel_list = new ArrayList<BlogModel>();
                for (Integer i = 0 ; i < all;i++){
                    BlogModel bm = new BlogModel();
                    JSONObject blogobject= new JSONObject();
                       try{
                           //blogobject =
                            blogobject = blog_list.getJSONObject(i);
                       }
                       catch(JSONException e){}
                        bm.initBlogModel(blogobject);
                       BlogModel_list.add(bm);
                }
                mBlogrecycle.setHasFixedSize(false);
                mBlogrecycle.setItemAnimator(new DefaultItemAnimator());
                mBlogrecycle.setLayoutManager(new LinearLayoutManager(getContext()));

                mBlogrecycle.setAdapter(
                        new CommonAdapter<BlogModel>(getContext(), R.layout.item_show_blog, BlogModel_list) {
                            @Override
                            public void convert(ViewHolder holder, BlogModel s, int pos) {
                                //Log.d(TAG,"latestname"+s.name);
                                holder.setText(R.id.blog_like, "Liked by "+s.like+" people");
                                holder.setText(R.id.blog_habit_owner, s.username);
                                holder.setText(R.id.blog_content, s.blog);
                            }

                            @Override
                            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                                super.onViewHolderCreated(holder, itemView);
                                ButterKnife.bind(this,itemView);
                            }
                        });
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                mCatLoadingView.dismiss();
                Log.d("API_REPORT", "onFailure: login");
                Log.d("API_REPORT", "onFailure: status : "+statusCode);
                Log.d("API_REPORT", "onFailure: response : "+response);
                showBottomSnackBar("get blog fail!");
            }
        });
    }

}
