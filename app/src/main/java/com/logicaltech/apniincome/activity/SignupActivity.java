package com.logicaltech.apniincome.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.logicaltech.apniincome.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class SignupActivity extends AppCompatActivity
{
    FloatingActionButton fab_signUp;
    TextInputEditText TIET_mobileNo,TIET_email_id,TIET_name,TIET_sponsorId;
    private RadioGroup radioGroupPlace;
    private RadioButton radioButtonPlace;
    String place="";
    CheckBox checkBox_SponserId;
    SessionManeger sessionManeger;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        sessionManeger = new SessionManeger(getApplicationContext());
        init();
        fab_signUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signUp();
            }
        });
    }

    public void init() {
        fab_signUp = (FloatingActionButton) findViewById(R.id.fab_signup);
        TIET_mobileNo = (TextInputEditText)findViewById(R.id.tiet_mobileno);
        TIET_email_id = (TextInputEditText)findViewById(R.id.tied_email_id);
        TIET_name =(TextInputEditText)findViewById(R.id.tiet_name);
        TIET_sponsorId = (TextInputEditText) findViewById(R.id.tiet_sponsorid);
        radioGroupPlace = (RadioGroup) findViewById(R.id.rediogroupplace);
        checkBox_SponserId = (CheckBox) findViewById(R.id.checkboxspoinerId);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_signup);

        radioGroupPlace.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                radioButtonPlace=(RadioButton)group.findViewById(checkedId);
                if (null != radioButtonPlace && checkedId > -1)
                {
                    place = radioButtonPlace.getText().toString();
                }
                }
        });

        checkBox_SponserId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                       TIET_sponsorId.setText("AP0000001");
                }
                else
                {
                    TIET_sponsorId.setText("");
                }
            }
        });
    }

    public void signUp() {
        String mobileNo = TIET_mobileNo.getText().toString();
        if (mobileNo.equals("")|| mobileNo.length()<10)
        {
            Toast.makeText(this,"Please enter valid Mobile Number",Toast.LENGTH_SHORT).show();
        }
        else
        {
            String email_id = TIET_email_id.getText().toString();
            if (!Constant.isValidEmail(email_id))
            {
                Toast.makeText(this,"Please enter valid email",Toast.LENGTH_SHORT).show();
            }
            else
            {
                String name = TIET_name.getText().toString();
                    if (name.equals(""))
                    {
                        Toast.makeText(this,"Please enter Name",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String sponsorId = TIET_sponsorId.getText().toString();
                        if (sponsorId.equals(""))
                        {
                            Toast.makeText(this,"please Enter Sponsor Id",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if (place.equals(""))
                            {
                                Toast.makeText(this,"please select any one place",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressBar.setVisibility(View.VISIBLE);
                                fab_signUp.setAlpha(0f);
                                new Handler().postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        fab_signUp.setAlpha(1f);
                                    }
                                }, 2000);

                                registration1(mobileNo,email_id,name,place,sponsorId,"255.255.255.0");
                            }
                        }
                    }
                }
            }
        }

    public void signInVolly(final String userId, final String Password) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"getSignIn?UserID="+userId+"&Password="+Password;
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("SUCCESS"))
                    {
                        String userId = jsonObject.getString("UserID");
                        String email = jsonObject.getString("Email");
                        String mobileNo = jsonObject.getString("Mobile_No");
                        String userName = jsonObject.getString("Memb_Name");
                        String memberId = jsonObject.getString("membercode");
                        Constant.MEMBER_ID = memberId;
                        String city = jsonObject.getString("City");
                        sessionManeger.createSession(userId,userName,email,mobileNo,memberId,city);
                        Intent intent=new Intent(SignupActivity.this,DashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Intent intent=new Intent(SignupActivity.this,SignInActivity.class);
                        startActivity(intent);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Content-Type","application/json");
                return headers;
            }
        };
        MyStringRequest.setRetryPolicy(new DefaultRetryPolicy(100000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyRequestQueue.add(MyStringRequest);
    }

    public void registration1(final String Mobileno, final String EmailId,final String name, final String place, final String sponserid, final String ip_address) {
        String url = Constant.URL+"addSignUp";
        StringRequest jsonObjRequest = new StringRequest(Request.Method.PUT,url, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (status.equals("1"))
                            {
                               // String userId = jsonObject.getString("username");
                              //  String password = jsonObject.getString("NewPass");
                                // signInVolly(userId,password);
                                showConfirmDialog();
                            }
                            else
                            {
                                Toast.makeText(SignupActivity.this," "+message,Toast.LENGTH_SHORT).show();
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
                params.put("Mobile_No", Mobileno);
                params.put("Email",EmailId);
                params.put("Memb_Name", name);
                params.put("sp_user", sponserid);
                params.put("Place", place);
                params.put("client_ip", ip_address);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }

    private void showConfirmDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Apni Income");
        builder.setMessage("Please check your email to get user_id And password");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Intent intent = new Intent(SignupActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        builder.show();
    }
}
