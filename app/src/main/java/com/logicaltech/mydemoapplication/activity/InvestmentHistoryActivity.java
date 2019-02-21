package com.logicaltech.mydemoapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.logicaltech.mydemoapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.Investment_His_Adapter;
import adapter.MyNetwork_Adapter;
import model.DirectNetworkModel;
import model.InvestmentModel;
import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class InvestmentHistoryActivity extends AppCompatActivity
{
    ImageView imageView_back_arrow;
    RecyclerView recyclerView_investmenthistory;
    GridLayoutManager mGridLayoutManagerBrand;
    SessionManeger sessionManeger;
    String memberId;
    ArrayList<InvestmentModel> arrayList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_history);
        imageView_back_arrow = (ImageView) findViewById(R.id.reacharg_back_arrow_make_investment_his);
        recyclerView_investmenthistory = (RecyclerView) findViewById(R.id.recycler_view_investmentHistory);
        mGridLayoutManagerBrand = new GridLayoutManager(InvestmentHistoryActivity.this, 1);
        recyclerView_investmenthistory.setLayoutManager(mGridLayoutManagerBrand);

        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);

        imageView_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        getInvestmentHistory(memberId);
    }

    public void getInvestmentHistory(final String memberId)
    {
        String url = Constant.URL+"getInvestmentHist?membercode="+memberId;
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
                        String commit_no = jsonObject2.getString("commit_no");
                        String ttime = jsonObject2.getString("ttime");
                        String amount = jsonObject2.getString("amount");
                        String Provide_Status = jsonObject2.getString("Provide_Status");
                        String BTC_Type = jsonObject2.getString("BTC_Type");

                        InvestmentModel model = new InvestmentModel();
                        model.setCommit_no(commit_no);
                        model.setTime(ttime);
                        model.setAmount(amount);
                        model.setProvide_Status(Provide_Status);
                        model.setBTC_Type(BTC_Type);

                        arrayList.add(model);
                    }
                    Investment_His_Adapter investment_his_adapter = new Investment_His_Adapter(arrayList,getApplicationContext());
                    recyclerView_investmenthistory.setAdapter(investment_his_adapter);
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
