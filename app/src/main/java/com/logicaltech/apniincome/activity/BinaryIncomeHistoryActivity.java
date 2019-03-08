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

import adapter.BinaryIncome_Adapter;
import model.BinaryIncome_Model;
import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class BinaryIncomeHistoryActivity extends AppCompatActivity
{
    RecyclerView recyclerView_binary_income;
    GridLayoutManager mGridLayoutManagerBrand;
    SessionManeger sessionManeger;
    String memberId;
    ArrayList<BinaryIncome_Model> arrayList =new ArrayList<>();
    ImageView imageView_back_Arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary_income_history);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);
        imageView_back_Arrow = (ImageView) findViewById(R.id.imageview_back_arrow_binary_income);
        recyclerView_binary_income = (RecyclerView) findViewById(R.id.recycler_view_binaryIncome);
        mGridLayoutManagerBrand = new GridLayoutManager(BinaryIncomeHistoryActivity.this, 1);
        recyclerView_binary_income.setLayoutManager(mGridLayoutManagerBrand);

        getBinaryIncome(memberId);

        imageView_back_Arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getBinaryIncome(final String memberId)
    {
        String url = Constant.URL+"getBinaryIncomeHist?membercode="+memberId;
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
                        String Payout_Date = jsonObject2.getString("Payout_Date");
                        String Memb_Code = jsonObject2.getString("Memb_Code");
                        String PLEFT = jsonObject2.getString("PLEFT");
                        String PRIGHT = jsonObject2.getString("PRIGHT");
                        String PAIR = jsonObject2.getString("PAIR");
                        String PAID_PAIR = jsonObject2.getString("PAID_PAIR");
                        String CLEFT = jsonObject2.getString("CLEFT");
                        String CRIGHT = jsonObject2.getString("CRIGHT");
                        String AMOUNT = jsonObject2.getString("AMOUNT");
                        String PAID_AMT = jsonObject2.getString("PAID_AMT");
                        String LEFT_JOIN = jsonObject2.getString("LEFT_JOIN");
                        String RIGHT_JOIN = jsonObject2.getString("RIGHT_JOIN");
                        String MEMB_NAME = jsonObject2.getString("MEMB_NAME");

                        BinaryIncome_Model model = new BinaryIncome_Model();
                        model.setPayout_Date(Payout_Date);
                        model.setMemb_Code(Memb_Code);
                        model.setPLEFT(PLEFT);
                        model.setPRIGHT(PRIGHT);
                        model.setPAIR(PAIR);
                        model.setPAID_PAIR(PAID_PAIR);
                        model.setCLEFT(CLEFT);
                        model.setCRIGHT(CRIGHT);
                        model.setAMOUNT(AMOUNT);
                        model.setPAID_AMT(PAID_AMT);
                        model.setLEFT_JOIN(LEFT_JOIN);
                        model.setRIGHT_JOIN(RIGHT_JOIN);
                        model.setMEMB_NAME(MEMB_NAME);

                        arrayList.add(model);
                    }
                    BinaryIncome_Adapter binaryIncome_adapter = new BinaryIncome_Adapter(arrayList,getApplicationContext());
                    recyclerView_binary_income.setAdapter(binaryIncome_adapter);
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
