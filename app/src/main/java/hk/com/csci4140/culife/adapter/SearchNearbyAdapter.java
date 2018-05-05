package hk.com.csci4140.culife.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.csci4140.culife.Constant;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.model.MissionDetailModel;
import hk.com.csci4140.culife.model.NearbyListModel;

/**
 * Created by maoyuxuan(Michael Mao) on 13/04/2018.
 */

public class SearchNearbyAdapter extends RecyclerView.Adapter<SearchNearbyAdapter.SearchNearbyResultHolder> {

    private static final String TAG = "SearchNearbyAdapter";

    public interface OnNearbyResultItemClickListener {
        void onLikeClick(View v, int position, List<Boolean> likeItems);
        void onItemClick(View v, int position);
    }

    public void setOnNearbyItemClickListener(OnNearbyResultItemClickListener listener){
        this.listener = listener;
    }

    private Context mContext;
    private OnNearbyResultItemClickListener listener;
    private List<NearbyListModel.Result.MissionList> items;
    private List<Boolean> likeItems;


    public SearchNearbyAdapter(Context mContext){

        this.mContext = mContext;
        items = new ArrayList<>();
        likeItems = new ArrayList<>();
    }


    public SearchNearbyAdapter(Context mContext, List<NearbyListModel.Result.MissionList> items){

        this.mContext = mContext;
        this.items = items;
        this.likeItems = new ArrayList<>();
    }


    public List<NearbyListModel.Result.MissionList> getItems() {
        return items;
    }


    public void setItems(List<NearbyListModel.Result.MissionList> items) {
        this.items = items;
    }




    public void setListener(OnNearbyResultItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public SearchNearbyResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission_card_view, parent, false);
        final SearchNearbyResultHolder holder = new SearchNearbyResultHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(final SearchNearbyResultHolder holder, final int position) {

        MissionDetailModel detail = items.get(position).getMissionDetail();

        //Set Click listener on a item
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick(view, position);
                }
            }
        });

        //Invisible the useless part
        holder.mPrepaidContainer.setVisibility(View.GONE);
        holder.mTimeContainer.setVisibility(View.GONE);
        holder.mProofPhotoContainer.setVisibility(View.GONE);
        holder.mFinishedContainer.setVisibility(View.GONE);
        holder.mCancelFinishButton.setVisibility(View.GONE);

        holder.mMissionTitle.setText(detail.getTitle());

        String passDateFormat = String.format(mContext.getString(R.string.mission_detail_pass_date_format), detail.getPassedDate());
        holder.mMissionElapsedTime.setText(passDateFormat);
        if(detail.getIcon() != null && !detail.getIcon().equals("")){
            Glide.with(mContext).load(detail.getIcon()).into(holder.mMissionIcon);
        }
        holder.mMissionLocation.setText(detail.getAddress());
        float salary = Float.parseFloat(detail.getSalary());
        holder.mMissionSalary.setText(String.format(mContext.getString(R.string.post_mission_two_salary_format), salary));

        holder.mLikeElapsedContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onLikeClick(holder.mLikeIcon, position, likeItems);
                }
            }
        });
        if(position >= likeItems.size()){
            likeItems.add(detail.getIsFav().equals(Constant.TRUE));
        }
        if(likeItems.get(position)){
            holder.mLikeIcon.setImageResource(R.drawable.ic_star_like);
        }
        else {
            holder.mLikeIcon.setImageResource(R.drawable.ic_star_unlike);
        }

    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public long getItemId(int position) {
        return items.get(position).getMissionDetail().getId();
    }


    final static class SearchNearbyResultHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_mission_view)
        CardView mContainer;

        @BindView(R.id.card_mission_prepaid_container)
        LinearLayout mPrepaidContainer;

        @BindView(R.id.card_mission_time_container)
        LinearLayout mTimeContainer;

        @BindView(R.id.card_mission_proof_photo_container)
        LinearLayout mProofPhotoContainer;

        @BindView(R.id.card_mission_finished_container)
        LinearLayout mFinishedContainer;

        @BindView(R.id.card_mission_button)
        Button mCancelFinishButton;

        @BindView(R.id.card_mission_like_elapsed_container)
        LinearLayout mLikeElapsedContainer;

        @BindView(R.id.card_mission_like_icon)
        ImageView mLikeIcon;

        @BindView(R.id.card_mission_icon)
        ImageView mMissionIcon;

        @BindView(R.id.card_mission_title)
        TextView mMissionTitle;

        @BindView(R.id.card_mission_salary)
        TextView mMissionSalary;

        @BindView(R.id.card_mission_elapsed_time)
        TextView mMissionElapsedTime;

        @BindView(R.id.card_mission_location)
        TextView mMissionLocation;

        //TODO: Date and video

        SearchNearbyResultHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
