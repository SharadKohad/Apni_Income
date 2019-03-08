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

public class RWallet_History_Adapter extends RecyclerView.Adapter<RWallet_History_Adapter.RecyclerViewHolder>
{
    public ArrayList<RWallet_Model> orderList;
    public Context mContext;
    public RWallet_History_Adapter(ArrayList<RWallet_Model> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rwallet_history_layout, parent, false);
        return new RWallet_History_Adapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final RWallet_Model investmentModel = orderList.get(position);
        holder.TV_Bank_Name.setText("Bank Name: "+investmentModel.getBank_name());
        holder.TV_Accont_No.setText("Acc No: "+investmentModel.getAccont_no());
        holder.TV_Trancation_no.setText("Trac No: "+investmentModel.getTransaction_no());
        holder.TV_Amount.setText(investmentModel.getAmount());
        holder.TV_Date.setText(investmentModel.getRequest_date());
        holder.TV_Status.setText(investmentModel.getStatus());
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Bank_Name,TV_Accont_No,TV_Trancation_no,TV_Amount,TV_Date,TV_Status;
        RelativeLayout relativeLayout_Account_List;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Bank_Name = (TextView) itemView.findViewById(R.id.textview_rwallet_bank_name);
            TV_Accont_No = (TextView) itemView.findViewById(R.id.textview_rwallet_account_no);
            TV_Trancation_no = (TextView) itemView.findViewById(R.id.textview_rwallet_trancation_no);
            TV_Amount = (TextView) itemView.findViewById(R.id.textview_rwallet_amount);
            TV_Date = (TextView) itemView.findViewById(R.id.textview_rwallet_date);
            TV_Status = (TextView) itemView.findViewById(R.id.textview_rwallet_status);
            relativeLayout_Account_List = (RelativeLayout)itemView.findViewById(R.id.relative_layout_account_list);
        }
    }
}
