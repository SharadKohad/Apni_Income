package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logicaltech.mydemoapplication.R;

import java.util.ArrayList;

import model.EarningReportModel;

public class Earning_Report_Adapter extends RecyclerView.Adapter<Earning_Report_Adapter.RecyclerViewHolder>
{
    public ArrayList<EarningReportModel> orderList;
    public Context mContext;
    public Earning_Report_Adapter(ArrayList<EarningReportModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_history_layout, parent, false);
        return new Earning_Report_Adapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final EarningReportModel investmentModel = orderList.get(position);
        holder.TV_Create_Date.setText(investmentModel.getCreated_at());
        holder.TV_Provide_Id.setText(investmentModel.getProvide_id());
        holder.TV_Amount.setText(investmentModel.getAmount());
        holder.TV_Package.setText(investmentModel.getEarning_package());
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Create_Date,TV_Provide_Id,TV_Amount,TV_Package;
        RelativeLayout relativeLayout_Account_List;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Create_Date = (TextView) itemView.findViewById(R.id.text_view_created_at);
            TV_Provide_Id = (TextView) itemView.findViewById(R.id.text_view_earning_his_provide_id);
            TV_Amount = (TextView) itemView.findViewById(R.id.text_view_investment_his_amount);
            TV_Package = (TextView) itemView.findViewById(R.id.text_view_earning_his_package);
            relativeLayout_Account_List = (RelativeLayout)itemView.findViewById(R.id.relative_layout_account_list);
        }
    }
}
