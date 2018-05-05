package hk.com.csci4140.culife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.model.EditProfileOptionModel;

/**
 * Created by maoyuxuan(Michael Mao) on 15/04/2018.
 */

public class IndustryListAdapter extends RecyclerView.Adapter<IndustryListAdapter.IndustryListViewHolder> {

    private static final String TAG = "FragmentAdapter";

    private ArrayList<String> industryList;
    private ArrayList<Integer> checkedPosition;
    private Context mContext;

    public IndustryListAdapter(Context context){
        industryList = new ArrayList<>();
        checkedPosition = new ArrayList<>();
        mContext = context;
    }


    public IndustryListAdapter(Context context, ArrayList<String> industryList, ArrayList<Integer> checkedPosition){
        this.industryList = industryList;
        this.checkedPosition = checkedPosition;
        mContext = context;
    }


    @Override
    public IndustryListAdapter.IndustryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_industry_list, parent, false);
        final IndustryListAdapter.IndustryListViewHolder holder = new IndustryListAdapter.IndustryListViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(IndustryListAdapter.IndustryListViewHolder holder, final int position) {

        holder.mTitle.setText(industryList.get(position));
        //in some cases, it will prevent unwanted situations
        holder.mCheckBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        for(int i : checkedPosition){
            if(i == position){
                holder.mCheckBox.setChecked(true);
                break;
            }
        }

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkedPosition.add(position);
                }
                else {
                    for(int i = 0; i < checkedPosition.size(); i ++){
                        if(checkedPosition.get(i) == position){
                            checkedPosition.remove(i);
                            break;
                        }
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return industryList.size();
    }


    @Override
    public long getItemId(int position) {

        //return packingItems.get(position).getId();
        return position;
    }


    final static class IndustryListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_industry_title)
        TextView mTitle;

        @BindView(R.id.item_industry_check_box)
        CheckBox mCheckBox;


        IndustryListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
