package com.logicaltech.apniincome.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import adapter.Earning_Report_Adapter;
import model.EarningReportModel;
import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class EarningReportHistoryActivity extends AppCompatActivity
{
    ImageView imageView_back_arrow;
    SessionManeger sessionManeger;
    String memberId;
    ArrayList<EarningReportModel> arrayList =new ArrayList<>();
    RecyclerView recyclerView_investmenthistory;
    GridLayoutManager mGridLayoutManagerBrand;
    String flag="0";
    String url1="";
    TextView textView_title;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning_report_history);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);

        imageView_back_arrow = (ImageView) findViewById(R.id.imageview_back_arrow_earning_report);
        recyclerView_investmenthistory = (RecyclerView) findViewById(R.id.recycler_view_earningreportHistory);
        textView_title = (TextView) findViewById(R.id.textView_earning_report_title);
        mGridLayoutManagerBrand = new GridLayoutManager(EarningReportHistoryActivity.this, 1);
        recyclerView_investmenthistory.setLayoutManager(mGridLayoutManagerBrand);
        flag = getIntent().getExtras().getString("flag");

        if (flag.equals("0"))
        {
            url1= "getROIHist?membercode=";
            textView_title.setText("Generated ROI");
        }
        else if(flag.equals("1"))
        {
            url1= "getDirectIncomeHist?membercode=";
            textView_title.setText("Direct Income");
        }
        else if(flag.equals("2"))
        {
            url1= "getAllTransaction?membercode=";
            textView_title.setText("All Income");
        }

        getEarningReportHistory(memberId);

        imageView_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public void getEarningReportHistory(final String memberId)
    {
        String url = Constant.URL+url1+memberId;
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
                        String flag = jsonObject2.getString("flag");
                        String created_at = jsonObject2.getString("created_at");
                        String provide_id = jsonObject2.getString("provide_id");
                        String amount = jsonObject2.getString("amount");
                        String Package = jsonObject2.getString("Package");

                        EarningReportModel model = new EarningReportModel();
                        model.setFlag(flag);
                        model.setCreated_at(created_at);
                        model.setProvide_id(provide_id);
                        model.setAmount(amount);
                        model.setEarning_package(Package);

                        arrayList.add(model);
                    }
                    Earning_Report_Adapter earning_report_adapter = new Earning_Report_Adapter(arrayList,getApplicationContext());
                    recyclerView_investmenthistory.setAdapter(earning_report_adapter);
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
