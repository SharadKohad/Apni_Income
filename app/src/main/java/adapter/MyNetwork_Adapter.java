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
import model.DirectNetworkModel;

public class MyNetwork_Adapter extends RecyclerView.Adapter<MyNetwork_Adapter.RecyclerViewHolder>
{
    public ArrayList<DirectNetworkModel> orderList;
    public Context mContext;
    public MyNetwork_Adapter(ArrayList<DirectNetworkModel> orderList , Context context)
    {
        this.orderList = orderList;
        mContext = context;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_netowrk_model, parent, false);
        return new MyNetwork_Adapter.RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)
    {
        final DirectNetworkModel directNetworkModel = orderList.get(position);

        System.out.print(" MEri Income:"+directNetworkModel.getReg_date());

        holder.TV_RegistrationDate.setText("Date: "+directNetworkModel.getReg_date());
        holder.TV_Member_Name.setText("Name: "+directNetworkModel.getMemb_name());
        holder.TV_Member_Email_id.setText("Email: "+directNetworkModel.getEmail());
        holder.TV_Member_Place.setText(directNetworkModel.getPlace());
        holder.TV_Total_Amount.setText(directNetworkModel.getTotal_Business());
        holder.TV_Status.setText(directNetworkModel.getConfirm_ph());

    }
    @Override
    public int getItemCount()
    {
        return orderList.size();
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView TV_RegistrationDate,TV_Member_Name,TV_Member_Email_id,TV_Member_Place,TV_Total_Amount,TV_Status;
        RelativeLayout relativeLayout_Account_List;
        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            TV_RegistrationDate = (TextView) itemView.findViewById(R.id.text_view_member_place);
            TV_Member_Name = (TextView) itemView.findViewById(R.id.text_view_member_name);
            TV_Member_Email_id = (TextView) itemView.findViewById(R.id.text_view_member_name_email);
            TV_Member_Place = (TextView) itemView.findViewById(R.id.text_view_reg_date);
            TV_Total_Amount = (TextView) itemView.findViewById(R.id.text_view_member_total_business1);
            TV_Status = (TextView) itemView.findViewById(R.id.textview_rwallet_status);
            relativeLayout_Account_List = (RelativeLayout)itemView.findViewById(R.id.relative_layout_account_list);
        }
    }
}

