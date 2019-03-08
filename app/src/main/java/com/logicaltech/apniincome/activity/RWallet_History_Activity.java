package com.logicaltech.apniincome.activity;

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
import com.logicaltech.apniincome.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.EWalletToRWallet;
import adapter.RWallet_History_Adapter;
import model.RWallet_Model;
import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class RWallet_History_Activity extends AppCompatActivity
{
    ImageView imageView_back_arrow;
    RecyclerView recyclerView_investmenthistory;
    GridLayoutManager mGridLayoutManagerBrand;
    ArrayList<RWallet_Model> arrayList =new ArrayList<>();
    SessionManeger sessionManeger;
    String memberId;
    String flag="0",url1="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rwallet__history_);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);
        imageView_back_arrow = (ImageView) findViewById(R.id.imageview_back_arrow_rwallet_history);
        recyclerView_investmenthistory = (RecyclerView) findViewById(R.id.recycler_view_ewallet_request_history);
        mGridLayoutManagerBrand = new GridLayoutManager(RWallet_History_Activity.this, 1);
        recyclerView_investmenthistory.setLayoutManager(mGridLayoutManagerBrand);

        flag = getIntent().getExtras().getString("flag");

        if (flag.equals("0"))
        {
            getRwalletReqHist(memberId);
        }
        else
        {
            getEWalltoRWallTranfHist(memberId);
        }

        imageView_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public void getRwalletReqHist(final String memberId)
    {
        String url = Constant.URL+"getRwalletReqHist?membercode="+memberId;
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
                        String Amount = jsonObject2.getString("Amount");
                        String fundbank_name = jsonObject2.getString("fundbank_name");
                        String fundaccount_no = jsonObject2.getString("fundaccount_no");
                        String fundTransaction_No = jsonObject2.getString("fundTransaction_No");
                        String Request_Date = jsonObject2.getString("Request_Date");
                        String Request_Status = jsonObject2.getString("Request_Status");

                        RWallet_Model model = new RWallet_Model();
                        model.setAmount(Amount);
                        model.setBank_name(fundbank_name);
                        model.setAccont_no(fundaccount_no);
                        model.setTransaction_no(fundTransaction_No);
                        model.setRequest_date(Request_Date);
                        model.setStatus(Request_Status);

                        arrayList.add(model);
                    }

                    RWallet_History_Adapter rWallet_history_adapter = new RWallet_History_Adapter(arrayList,getApplicationContext());
                    recyclerView_investmenthistory.setAdapter(rWallet_history_adapter);
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


    public void getEWalltoRWallTranfHist(final String memberId)
    {
        String url = Constant.URL+"getEWalltoRWallTranfHist?membercode="+memberId;
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
                        String Amount = jsonObject2.getString("Amount");
                        String Request_Date = jsonObject2.getString("Request_Date");

                        RWallet_Model model = new RWallet_Model();
                        model.setAmount(Amount);
                        model.setRequest_date(Request_Date);
                        arrayList.add(model);
                    }

                    EWalletToRWallet rWallet_history_adapter = new EWalletToRWallet(arrayList,getApplicationContext());
                    recyclerView_investmenthistory.setAdapter(rWallet_history_adapter);
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
