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

public class EWalletToRWallet extends RecyclerView.Adapter<EWalletToRWallet.RecyclerViewHolder>
{
    public ArrayList<RWallet_Model> orderList;
    public Context mContext;
    public EWalletToRWallet(ArrayList<RWallet_Model> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ewallet_to_rwallet_history, parent, false);
        return new EWalletToRWallet.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final RWallet_Model investmentModel = orderList.get(position);
        holder.TV_Amount.setText(investmentModel.getAmount());
        holder.TV_Date.setText(investmentModel.getRequest_date());
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Amount,TV_Date;
        RelativeLayout relativeLayout_Account_List;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Amount = (TextView) itemView.findViewById(R.id.textview_rwallet_amount_history);
            TV_Date = (TextView) itemView.findViewById(R.id.textview_rwallet_request_date_history);
            relativeLayout_Account_List = (RelativeLayout)itemView.findViewById(R.id.relative_layout_account_list);
        }
    }
}
