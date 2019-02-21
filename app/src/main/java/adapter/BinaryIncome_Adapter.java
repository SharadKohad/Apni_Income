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

import model.BinaryIncome_Model;

public class BinaryIncome_Adapter extends RecyclerView.Adapter<BinaryIncome_Adapter.RecyclerViewHolder>
{
    public ArrayList<BinaryIncome_Model> orderList;
    public Context mContext;
    public BinaryIncome_Adapter(ArrayList<BinaryIncome_Model> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.binary_income_history_layout, parent, false);
        return new BinaryIncome_Adapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final BinaryIncome_Model binaryIncome_model = orderList.get(position);
        holder.TV_Pleft.setText(binaryIncome_model.getPLEFT());
        holder.TV_Pright.setText(binaryIncome_model.getPRIGHT());
        holder.TV_Cleft.setText(binaryIncome_model.getCLEFT());
        holder.TV_Cright.setText(binaryIncome_model.getCRIGHT());
        holder.TV_Left_Join.setText(binaryIncome_model.getLEFT_JOIN());
        holder.TV_Right_Join.setText(binaryIncome_model.getRIGHT_JOIN());
        holder.TV_Amont.setText(binaryIncome_model.getAMOUNT());
        holder.TV_PaidAmount.setText(binaryIncome_model.getPAID_AMT());
        holder.TV_payoutdate.setText(binaryIncome_model.getPayout_Date());
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Pleft,TV_Pright,TV_Cleft,TV_Cright,TV_Left_Join,TV_Right_Join,TV_Amont,TV_PaidAmount,TV_payoutdate;
        RelativeLayout relativeLayout_Account_List;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Pleft = (TextView) itemView.findViewById(R.id.textview_pleft);
            TV_Pright = (TextView) itemView.findViewById(R.id.textview_pright);
            TV_Cleft = (TextView) itemView.findViewById(R.id.textview_cleft);
            TV_Cright = (TextView) itemView.findViewById(R.id.textview_cright);
            TV_Left_Join = (TextView) itemView.findViewById(R.id.textview_left_join);
            TV_Right_Join = (TextView) itemView.findViewById(R.id.textview_right_join);
            TV_Amont = (TextView) itemView.findViewById(R.id.textview_amount);
            TV_PaidAmount = (TextView) itemView.findViewById(R.id.textview_paid_amount);
            TV_payoutdate = (TextView) itemView.findViewById(R.id.textview_payoutdate);
            relativeLayout_Account_List = (RelativeLayout)itemView.findViewById(R.id.relative_layout_account_list);
        }
    }
}
