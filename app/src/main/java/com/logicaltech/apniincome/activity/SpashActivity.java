package com.logicaltech.apniincome.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.logicaltech.apniincome.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utility.Constant;
import utility.SessionManeger;

public class SpashActivity extends AppCompatActivity
{
    Handler handler;
    LinearLayout li1,li2;
    Animation uptodown,downtoup;
    SessionManeger sessionManeger;
    String version;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        li1 = (LinearLayout) findViewById(R.id.li1);
        li2 = (LinearLayout) findViewById(R.id.li2);
        sessionManeger = new SessionManeger(getApplicationContext());

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        li1.setAnimation(uptodown);
        li2.setAnimation(downtoup);

        versionControl();

       /* handler=new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (sessionManeger.checkLogin())
                {
                    Intent intent=new Intent(SpashActivity.this,SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(SpashActivity.this,DashBoardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },1000);*/

    }

    public void versionControl()
    {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"getVersionDetails";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    version = jsonObject.getString("version");
                    Toast.makeText(SpashActivity.this,"Version "+version,Toast.LENGTH_SHORT).show();

                    if (version.equals("1.0"))
                         {
                             handler=new Handler();
                             handler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                        {
                                            if (sessionManeger.checkLogin())
                                                {
                                                    Intent intent=new Intent(SpashActivity.this,SignInActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            else
                                                {
                                                    Intent intent=new Intent(SpashActivity.this,DashBoardActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                        }
                                        },1000);
                         }
        else
        {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            }
            catch (android.content.ActivityNotFoundException anfe)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                String message= "";
                if (error instanceof ServerError)
                {
                    message = "The server could not be found. Please try again after some time!!";
                }
                else if (error instanceof TimeoutError)
                {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                System.out.println("error........"+error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Content-Type","application/json");
                return headers;
            }
        };
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(MyStringRequest);
    }

}
