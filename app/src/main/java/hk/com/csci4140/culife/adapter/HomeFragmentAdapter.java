package hk.com.csci4140.culife.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hk.com.csci4140.culife.R;
import hk.com.csci4140.culife.fragment.HabitDetailFragment;
import hk.com.csci4140.culife.fragment.SettingFragment;
import hk.com.csci4140.culife.model.HomeFragmentModel;


public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.MyViewHolder> {

    private String TAG = "Home Fragment Adapter";

    private SparseArray<Integer> mTextStateList;//保存文本状态集合

    private Context mContext;

    List<HomeFragmentModel> mList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView owner;
        public TextView title;
        public TextView delete;
        public TextView Time;
        public TextView Identity;

        public MyViewHolder(View itemView) {
            super(itemView);
            owner = (TextView) itemView.findViewById(R.id.owner);
            title = (TextView) itemView.findViewById(R.id.other_habbit_title);
            //delete = (TextView) itemView.findViewById(R.id.habbit_delete);
            //expandOrFold = (TextView) itemView.findViewById(R.id.expand_or_fold);
            Time = (TextView) itemView.findViewById(R.id.time);
            Identity = (TextView) itemView.findViewById(R.id.owner);
        }
    }

    public HomeFragmentAdapter(List<HomeFragmentModel> list, Context context) {
//        mContent = context;
        this.mList = list;
        mTextStateList = new SparseArray<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new MyViewHolder(mContent.getLayoutInflater().inflate(R.layout.item_expand_fold_text, parent, false));
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_habbit_list, parent, false);
        final HomeFragmentAdapter.MyViewHolder holder = new HomeFragmentAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final HomeFragmentAdapter.MyViewHolder holder, final int position) {
        holder.title.setText(mList.get(position).getTitle());
        holder.Time.setText(mList.get(position).getTime());
        holder.Identity.setText(mList.get(position).getIdentity());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mContext.replaceFragment(new HabitDetailFragment(), null);
//                ((FragmentActivity)mContext).getFragmentManager().beginTransaction().replace(R.id.owner,new HabitDetailFragment()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
