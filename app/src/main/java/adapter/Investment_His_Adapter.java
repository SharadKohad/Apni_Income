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
import model.InvestmentModel;

public class Investment_His_Adapter extends RecyclerView.Adapter<Investment_His_Adapter.RecyclerViewHolder>
{
    public ArrayList<InvestmentModel> orderList;
    public Context mContext;
    public Investment_His_Adapter(ArrayList<InvestmentModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.investment_history_layout, parent, false);
        return new Investment_His_Adapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final InvestmentModel investmentModel = orderList.get(position);
        holder.TV_Commit_no.setText("Commit No: "+investmentModel.getCommit_no());
        holder.TV_Time.setText("Time: "+investmentModel.getTime());
        holder.TV_Amount.setText(investmentModel.getAmount());
        holder.TV_BTC_Type.setText("BTC_Type: "+investmentModel.getBTC_Type());
        holder.TV_Status.setText(investmentModel.getProvide_Status());
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Commit_no,TV_Time,TV_Amount,TV_Status,TV_BTC_Type;
        RelativeLayout relativeLayout_Account_List;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Commit_no = (TextView) itemView.findViewById(R.id.text_view_commit_no);
            TV_Time = (TextView) itemView.findViewById(R.id.text_view_investment_his_time);
            TV_Amount = (TextView) itemView.findViewById(R.id.text_view_investment_his_amount);
            TV_Status = (TextView) itemView.findViewById(R.id.text_view_investment_status);
            TV_BTC_Type = (TextView) itemView.findViewById(R.id.text_view_member_btc_type);
            relativeLayout_Account_List = (RelativeLayout)itemView.findViewById(R.id.relative_layout_account_list);
        }
    }
}
