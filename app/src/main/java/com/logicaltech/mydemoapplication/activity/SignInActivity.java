package com.logicaltech.mydemoapplication.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.SignInButton;
import com.google.gson.JsonObject;
import com.logicaltech.mydemoapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class SignInActivity extends AppCompatActivity
{
    LinearLayout linearLayoutSignUp;
    FloatingActionButton fab_signin,fab_signin1;
    TextInputLayout textInputLayoutOtp;
    TextInputEditText TIET_email_id,TIET_password,TIET_Otp;
    ProgressBar progressBar;
    SessionManeger sessionManeger;
    TextView TVforgotpassword;
    Dialog dialog;
    WindowManager.LayoutParams lp;
    String ip= "255.255.255.0",otp,user_id,password;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sessionManeger = new SessionManeger(getApplicationContext());
        init();
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }
    public void init()
    {
        textInputLayoutOtp = (TextInputLayout) findViewById(R.id.textinputlayout_otp);
        linearLayoutSignUp = (LinearLayout)findViewById(R.id.llsign_up_for_account);
        TIET_email_id = (TextInputEditText)findViewById(R.id.tiet_userid_signin);
        TIET_password = (TextInputEditText)findViewById(R.id.tiet_password_signin);
        TIET_Otp = (TextInputEditText) findViewById(R.id.tiet_otp);
        fab_signin = (FloatingActionButton) findViewById(R.id.fab_signin);
        fab_signin1 = (FloatingActionButton) findViewById(R.id.fab_signin1);
        progressBar = (ProgressBar) findViewById(R.id.progress_barsignin);
        TVforgotpassword = (TextView) findViewById(R.id.forgotpassword);
        linearLayoutSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SignInActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        fab_signin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });

        fab_signin1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String user_otp = TIET_Otp.getText().toString();
                if (user_otp.equals(otp))
                {
                    signInVolly(user_id,password);
                    progressBar.setVisibility(View.VISIBLE);
                    fab_signin1.setAlpha(0f);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            progressBar.setVisibility(View.GONE);
                            fab_signin1.setAlpha(1f);
                        }
                    }, 2000);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Wrong OTP",Toast.LENGTH_SHORT).show();
                }

            }
        });
        TVforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
    }

    public void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        fab_signin.setAlpha(0f);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                progressBar.setVisibility(View.GONE);
                fab_signin.setAlpha(1f);
            }
        }, 2000);

        user_id = TIET_email_id.getText().toString();
        if (user_id.equals(""))
            {
                Toast.makeText(this,"Please enter valid email",Toast.LENGTH_SHORT).show();
            }
            else
            {
                password = TIET_password.getText().toString();
                if (password.equals(""))
                {
                    Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //signInVolly(email_id,password);
                    postOTP_Genrate(user_id);
                }
            }
    }

    public void signInVolly(final String userId, final String Password) {
        String url = Constant.URL+"getSignIn";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>()
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
                        String username = jsonObject.getString("username");
                        String email = jsonObject.getString("Email");
                        String mobileNo = jsonObject.getString("Mobile_No");
                        String userName = jsonObject.getString("Memb_Name");
                        String memberId = jsonObject.getString("membercode");
                        Constant.MEMBER_ID = memberId;
                        String city = jsonObject.getString("City");
                        sessionManeger.createSession(username,userName,email,mobileNo,memberId,city);
                        Intent intent=new Intent(SignInActivity.this,DashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SignInActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                    }
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
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
            }
        })
        {
            @Override
            public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", userId);
                params.put("Password",Password);
                params.put("client_ip", ip);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }

    private void showCustomDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.forgot_password);
        dialog.setCancelable(true);
        final TextInputEditText textInputEditTextEmail;

        lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        textInputEditTextEmail = (TextInputEditText) dialog.findViewById(R.id.tiet_password_forgot);

        ((AppCompatButton) dialog.findViewById(R.id.btn_forgot_password)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String forgotEmail = textInputEditTextEmail.getText().toString();
                if (forgotEmail.equals(""))
                {
                    Toast.makeText(SignInActivity.this,"Please enter valid email",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    forgotPassword(forgotEmail);
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void forgotPassword(final String userId) {
        String url = Constant.URL+"forgotPassword";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,url, new Response.Listener<String>()
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
                        Toast.makeText(SignInActivity.this," "+msg,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(SignInActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
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
                params.put("username", userId);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }

    public void postOTP_Genrate(final String userId) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"getOTP?username="+userId;
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("1"))
                    {
                        otp = jsonObject.getString("OTP");
                        System.out.println("OTP: "+otp);
                        textInputLayoutOtp.setVisibility(View.VISIBLE);
                        fab_signin.setVisibility(View.GONE);
                        fab_signin1.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Toast.makeText(SignInActivity.this,""+status,Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
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
                //This code is executed if there is an error.
            }
        }) {
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
