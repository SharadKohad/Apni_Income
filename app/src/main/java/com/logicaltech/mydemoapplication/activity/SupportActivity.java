package com.logicaltech.mydemoapplication.activity;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.logicaltech.mydemoapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapter.Outbox_Adapter;
import model.RWallet_Model;
import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class SupportActivity extends AppCompatActivity
{
    ImageView imageView_back_arrow;
    EditText ET_subject,Et_message;
  //  Button btn_submit;
    SessionManeger sessionManeger;
    String memberId;
    RecyclerView recyclerView_investmenthistory;
    GridLayoutManager mGridLayoutManagerBrand;
    ArrayList<RWallet_Model> arrayList =new ArrayList<>();
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);

        imageView_back_arrow = (ImageView) findViewById(R.id.imageview_back_arrow_compose);
        recyclerView_investmenthistory = (RecyclerView) findViewById(R.id.recycler_view_getoutboxhist);
        mGridLayoutManagerBrand = new GridLayoutManager(SupportActivity.this, 1);
        recyclerView_investmenthistory.setLayoutManager(mGridLayoutManagerBrand);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add_compont);
      //  btn_submit = (Button) findViewById(R.id.button_submit);

        imageView_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        getRwalletReqHist(memberId);

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    showCustomDialog();
            }
        });

    }

    public void getRwalletReqHist(final String memberId)
    {
        String url = Constant.URL+"getOutboxHist?membercode="+memberId;
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
                        String ttime = jsonObject2.getString("ttime");
                        String msg = jsonObject2.getString("msg");
                        String sub = jsonObject2.getString("sub");


                        RWallet_Model model = new RWallet_Model();
                        model.setRequest_date(ttime);
                        model.setMessage(msg);
                        model.setSubject(sub);

                        arrayList.add(model);
                    }

                    Outbox_Adapter rWallet_history_adapter = new Outbox_Adapter(arrayList,getApplicationContext());
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


    private void showCustomDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_post);
        dialog.setCancelable(true);
        ImageView imageView_close;
        Button btn_post;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        ET_subject = (EditText) dialog.findViewById(R.id.EditText_subject);
        Et_message = (EditText) dialog.findViewById(R.id.EditText_message);
        imageView_close = (ImageView) dialog.findViewById(R.id.image_view_close);
        btn_post = (Button) dialog.findViewById(R.id.bt_post);

        imageView_close.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String subject = ET_subject.getText().toString();
                if (subject.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter the subject",Toast.LENGTH_SHORT);
                }
                else
                {
                    String message = Et_message.getText().toString();
                    if (message.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please enter the Message",Toast.LENGTH_SHORT);
                    }
                    else
                    {
                        addCompose(memberId,subject,message);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Post Submitted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void addCompose(final String memberId,final String subject,final String message)
    {
        String url = Constant.URL+"addCompose";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1"))
                    {
                        Toast.makeText(SupportActivity.this," "+msg,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SupportActivity.this," "+msg,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                    }
                }) {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("membercode", memberId);
                params.put("sub",subject);
                params.put("message",message);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }


}
