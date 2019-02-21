package com.logicaltech.mydemoapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import adapter.MyNetwork_Adapter;
import model.DirectNetworkModel;
import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class MyNetworkList_Activity extends AppCompatActivity
{
    SessionManeger sessionManeger;
    String memberId;
    ImageView imageView_Back_Arrow;
    TextView textView_title;
    RecyclerView recyclerView_networkList;
    GridLayoutManager mGridLayoutManagerBrand;
    ArrayList<DirectNetworkModel> arrayList =new ArrayList<>();
    String flag="0";
    String url1;
    Button btn_home;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_network_list_);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);

        imageView_Back_Arrow = (ImageView) findViewById(R.id.img_back_arrow_mynetwork_list);
        textView_title = (TextView) findViewById(R.id.textview_networklist_title);
        recyclerView_networkList = (RecyclerView) findViewById(R.id.recycler_view_networklist);
        btn_home = (Button) findViewById(R.id.button_home);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MyNetworkList_Activity.this,DashBoardActivity.class);
                startActivity(intent);
            }
        });
        mGridLayoutManagerBrand = new GridLayoutManager(MyNetworkList_Activity.this, 1);
        recyclerView_networkList.setLayoutManager(mGridLayoutManagerBrand);

        flag = getIntent().getExtras().getString("flag");
        if (flag.equals("0"))
        {
            textView_title.setText("Direct Network");
            url1 = "getDirectNetwork?membercode=";
        }
        else
        {
            textView_title.setText("Downline Team");
            url1 = "getDownlineTeam?membercode=";
        }

        imageView_Back_Arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        getDirectNetwork(memberId);
    }

    public void getDirectNetwork(final String memberId)
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
                        String reg_date = jsonObject2.getString("reg_date");
                        String memb_name = jsonObject2.getString("memb_name");
                        String username = jsonObject2.getString("username");
                        String m_country = jsonObject2.getString("m_country");
                        String email = jsonObject2.getString("email");
                        String level_no = jsonObject2.getString("level_no");
                        String confirm_ph = jsonObject2.getString("confirm_ph");
                        String Place = jsonObject2.getString("Place");
                        String Total_Business = jsonObject2.getString("Total_Business");

                        DirectNetworkModel model = new DirectNetworkModel();
                        model.setReg_date(reg_date);
                        model.setMemb_name(memb_name);
                        model.setUsername(username);
                        model.setM_country(m_country);
                        model.setEmail(email);
                        model.setLevel_no(level_no);
                        model.setConfirm_ph(confirm_ph);
                        model.setPlace(Place);
                        model.setTotal_Business(Total_Business);
                        arrayList.add(model);
                    }
                    MyNetwork_Adapter myNetwork_adapter = new MyNetwork_Adapter(arrayList,getApplicationContext());
                    recyclerView_networkList.setAdapter(myNetwork_adapter);
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
