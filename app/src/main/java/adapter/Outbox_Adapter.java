package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logicaltech.apniincome.R;

import java.util.ArrayList;

import model.RWallet_Model;

public class Outbox_Adapter extends RecyclerView.Adapter<Outbox_Adapter.RecyclerViewHolder>
{
    public ArrayList<RWallet_Model> orderList;
    public Context mContext;
    public Outbox_Adapter(ArrayList<RWallet_Model> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outbox_history_layout, parent, false);
        return new Outbox_Adapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final RWallet_Model investmentModel = orderList.get(position);
        holder.TV_subject.setText("Subject: "+investmentModel.getSubject());
        holder.TV_message.setText(investmentModel.getMessage());
        holder.TV_date.setText(investmentModel.getRequest_date());
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_subject,TV_message,TV_date;
        RelativeLayout relativeLayout_Account_List;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_subject = (TextView) itemView.findViewById(R.id.textview_outbox_history_subject);
            TV_message = (TextView) itemView.findViewById(R.id.textview_outbox_history_message);
            TV_date = (TextView) itemView.findViewById(R.id.textview_outbox_date);

            relativeLayout_Account_List = (RelativeLayout)itemView.findViewById(R.id.relative_layout_account_list);
        }
    }
}
