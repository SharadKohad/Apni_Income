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


import model.Recharge_History_Model;

public class Recharge_History_Adapter extends RecyclerView.Adapter<Recharge_History_Adapter.RecyclerViewHolder>
{
    public ArrayList<Recharge_History_Model> orderList;
    public Context mContext;
    public Recharge_History_Adapter(ArrayList<Recharge_History_Model> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recharge_history_child, parent, false);
        return new Recharge_History_Adapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final Recharge_History_Model account_model = orderList.get(position);

        holder.TV_Recharge_Type.setText(account_model.getRech_Type());
        holder.TV_Recharge_Operator.setText(account_model.getOperator());
        holder.TV_Mobile_Name.setText(account_model.getMobile_Service_No());
     //   holder.TV_Branch_Name.setText("Bank Name: "+account_model.getMobile());
        holder.TV_Recharge_Amount.setText(account_model.getRech_Amount());

   /*     holder.relativeLayout_Account_List.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(mContext,AccountDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtra("token","1");
                intent.putExtra("accoun_name",orderList.get(position).getAc_name());
                intent.putExtra("accoun_no",orderList.get(position).getAc_no());
                intent.putExtra("accoun_type",orderList.get(position).getAc_type());
                intent.putExtra("bank_name",orderList.get(position).getBk_name());
                intent.putExtra("branck_name",orderList.get(position).getBk_branch());
                intent.putExtra("ifsc_code",orderList.get(position).getBk_ifsc());
                intent.putExtra("mobile_no",orderList.get(position).getBank_mobile_no());

                mContext.getApplicationContext().startActivity(intent);
            }
        });*/
    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_Recharge_Type,TV_Recharge_Operator,TV_Mobile_Name,TV_Recharge_Amount;
        RelativeLayout relativeLayout_Account_List;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_Recharge_Type = (TextView) itemView.findViewById(R.id.text_view_recharg_type_history);
            TV_Recharge_Operator = (TextView) itemView.findViewById(R.id.text_view_recharg_operator_history);
            TV_Mobile_Name = (TextView) itemView.findViewById(R.id.text_view_mobile_no_recharge_history);
         //   TV_Branch_Name = (TextView) itemView.findViewById(R.id.text_view_branch_name);
            TV_Recharge_Amount = (TextView) itemView.findViewById(R.id.text_view_recharge_amount);
            relativeLayout_Account_List = (RelativeLayout)itemView.findViewById(R.id.relative_layout_account_list);
        }
    }
}
