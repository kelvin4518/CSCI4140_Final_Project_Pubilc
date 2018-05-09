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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.csci4140.culife.R;

/**
 * Created by maoyuxuan(Michael Mao) on 13/04/2018.
 */

public class SearchMissionResultAdapter extends RecyclerView.Adapter<SearchMissionResultAdapter.SearchMissionResultHolder> {

    private static final String TAG = "SearchMissionResultAdapter";

    public interface OnMissionResultItemClickListener {
        void onLikeClick(View v, int position);
        void onItemClick(View v, int position);
    }


    private Context mContext;
    private OnMissionResultItemClickListener listener;
    //TODO: result Item data type
    private List<?> resultItems;


    public SearchMissionResultAdapter(Context mContext){

        this.mContext = mContext;
        resultItems = new ArrayList<>();
    }


    public SearchMissionResultAdapter(Context mContext, List<?> resultItems){

        this.mContext = mContext;
        this.resultItems = resultItems;
    }


    public List<?> getMissionResultItems() {
        return resultItems;
    }


    public void setMissionResultItems(List<?> resultItems) {
        this.resultItems = resultItems;
    }




    public OnMissionResultItemClickListener getListener() {
        return listener;
    }


    public void setOnMissionResultItemClickListener(OnMissionResultItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public SearchMissionResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mission_card_view, parent, false);
        final SearchMissionResultHolder holder = new SearchMissionResultHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(SearchMissionResultHolder holder, final int position) {
        //TODO: set data
        holder.mPrepaidContainer.setVisibility(View.GONE);
        holder.mTimeContainer.setVisibility(View.GONE);
        holder.mProofPhotoContainer.setVisibility(View.GONE);
        holder.mFinishedContainer.setVisibility(View.GONE);
        holder.mCancelFinishButton.setVisibility(View.GONE);

        //Set Click listener on a item
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick(view, position);
                }
            }
        });

        //Set Click listener on like icon
        holder.mLikeElapsedContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onLikeClick(view, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return resultItems.size();
    }


    @Override
    public long getItemId(int position) {

        return 0;
    }


    final static class SearchMissionResultHolder extends RecyclerView.ViewHolder {

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


        SearchMissionResultHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
