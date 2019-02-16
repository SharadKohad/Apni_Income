package com.logicaltech.mydemoapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.logicaltech.mydemoapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.Account_Adapter;
import adapter.Recharge_History_Adapter;
import model.Account_Model;
import model.Recharge_History_Model;
import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class RechargeHistoryActivity extends AppCompatActivity
{
    RecyclerView RecyclerView_rechargeHistory;
    GridLayoutManager mGridLayoutManagerBrand;
    ArrayList<Recharge_History_Model> arrayList =new ArrayList<>();
    SessionManeger sessionManeger;
    ImageView imageView_back_arrow;
    String memberId;
    Button btn_recharge;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_history);
        RecyclerView_rechargeHistory = (RecyclerView) findViewById(R.id.recycler_view_rechange_history);
        imageView_back_arrow = (ImageView) findViewById(R.id.img_back_arrow_recharge_report_history);
        btn_recharge = (Button) findViewById(R.id.button_recharge);
        mGridLayoutManagerBrand = new GridLayoutManager(RechargeHistoryActivity.this, 1);
        RecyclerView_rechargeHistory.setLayoutManager(mGridLayoutManagerBrand);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);
        rechargeHistory(memberId);
        imageView_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        btn_recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RechargeHistoryActivity.this,RechargeActivity.class);
                intent.putExtra("token","0");
                startActivity(intent);
            }
        });
    }

    public void rechargeHistory(final String memberId)
    {
        String url = Constant.URL+"getRechargeReport?MemberID="+memberId;
        JsonArrayRequest MyStringRequest = new JsonArrayRequest(Request.Method.POST, url, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    arrayList.clear();
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject jsonObject2 = response.getJSONObject(i);
                        String Member_ID = jsonObject2.getString("Member_ID");
                        String Rech_Type = jsonObject2.getString("Rech_Type");
                        String Operator = jsonObject2.getString("Operator");
                        String Mobile_Service_No = jsonObject2.getString("Mobile_Service_No");
                        String Rech_Amount = jsonObject2.getString("Rech_Amount");
                        String request_no = jsonObject2.getString("request_no");
                        String rech_status = jsonObject2.getString("rech_status");
                        String Created_Dt = jsonObject2.getString("Created_Dt");

                        Recharge_History_Model model = new Recharge_History_Model();
                        model.setMember_ID(Member_ID);
                        model.setRech_Type(Rech_Type);
                        model.setOperator(Operator);
                        model.setMobile_Service_No(Mobile_Service_No);
                        model.setRech_Amount(Rech_Amount);
                        model.setRequest_no(request_no);
                        model.setRech_status(rech_status);
                        model.setCreated_Dt(Created_Dt);
                        arrayList.add(model);
                    }
                    Recharge_History_Adapter recharge_history_adapter = new Recharge_History_Adapter(arrayList,getApplicationContext());
                    RecyclerView_rechargeHistory.setAdapter(recharge_history_adapter);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error)
            {
                System.out.println("error...");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<>();
                return headers;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(MyStringRequest);
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
