package hk.com.csci4140.culife.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.csci4140.culife.R;

/**
 * Created by maoyuxuan(Michael Mao) on 13/04/2018.
 */

public class SearchTakerResultAdapter extends RecyclerView.Adapter<SearchTakerResultAdapter.SearchTakerResultHolder> {

    private static final String TAG = "SearchTakerResultAdapter";

    public interface OnTakerResultItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnTakerResultItemClickListener(OnTakerResultItemClickListener listener){
        this.listener = listener;
    }

    private Context mContext;
    private OnTakerResultItemClickListener listener;
    //TODO: result Item data type
    private List<?> resultItems;


    public SearchTakerResultAdapter(Context mContext){
        this.mContext = mContext;
        resultItems = new ArrayList<>();
    }


    public SearchTakerResultAdapter(Context mContext, List<?> resultItems){

        this.mContext = mContext;
        this.resultItems = resultItems;
    }


    public List<?> getTakerResultItems() {
        return resultItems;
    }


    public void setTakerResultItems(List<?> resultItems) {
        this.resultItems = resultItems;
    }


    public OnTakerResultItemClickListener getListener() {
        return listener;
    }


    @Override
    public SearchTakerResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_taker_card_view, parent, false);
        final SearchTakerResultHolder holder = new SearchTakerResultHolder(view);


        return holder;
    }


    @Override
    public void onBindViewHolder(SearchTakerResultHolder holder, final int position) {

        //TODO: set data
        holder.mRangeContainer.setVisibility(View.GONE);
        holder.mCommissionedButton.setVisibility(View.GONE);
        holder.mEvaluateContentContainer.setVisibility(View.GONE);

        //Set Click listener on a item
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onItemClick(view, position);
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

        //return packingItems.get(position).getId();
        return 0;
    }


    final static class SearchTakerResultHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_taker_container)
        CardView mContainer;

        @BindView(R.id.card_taker_range_container)
        RelativeLayout mRangeContainer;

        @BindView(R.id.card_taker_commissioned_button)
        Button mCommissionedButton;

        @BindView(R.id.card_taker_evaluate_content_container)
        LinearLayout mEvaluateContentContainer;


        SearchTakerResultHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}