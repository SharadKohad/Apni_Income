package com.logicaltech.mydemoapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.logicaltech.mydemoapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class ProfileDetailActivity extends AppCompatActivity
{
    SessionManeger sessionManeger;
    EditText ET_Name,ET_Email,ET_MobileNo,ET_City,ET_Address,ET_fatherName;
    Button Btn_Profile_Save,Btn_Change_Password;
    private String userId,userMobile,userName,userEmail,city,memberId;
    ImageView IV_Back_Arrow;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonGender;
    String gender="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        sessionManeger = new SessionManeger(getApplicationContext());

        init();

        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        userId = hashMap.get(SessionManeger.KEY_ID);
        userMobile = hashMap.get(SessionManeger.KEY_PHONE);
        userName = hashMap.get(SessionManeger.KEY_NAME);
        userEmail = hashMap.get(SessionManeger.KEY_EMAIL);
        city = hashMap.get(SessionManeger.CITY);
        memberId = hashMap.get(SessionManeger.MEMBER_ID);

        ET_Name.setText(userName);
        ET_Email.setText(userEmail);
        ET_MobileNo.setText(userMobile);
        ET_City.setText(city);


    }

    public void init()
    {
        ET_Name = (EditText) findViewById(R.id.EditText_ProfileName);
        ET_Email = (EditText) findViewById(R.id.EditText_ProfileEmailId);
        ET_MobileNo = (EditText) findViewById(R.id.EditText_PhoneNumber);
        ET_City = (EditText) findViewById(R.id.EditText_City);
        Btn_Profile_Save = (Button) findViewById(R.id.button_profile_save);
        Btn_Change_Password = (Button) findViewById(R.id.button_change_password);
        ET_Address = (EditText) findViewById(R.id.EditText_Address);
        ET_fatherName = (EditText) findViewById(R.id.EditText_fatherName);
        IV_Back_Arrow = (ImageView) findViewById(R.id.img_back_arrow_profile_detail);

        radioGroupGender = (RadioGroup) findViewById(R.id.rediogroupplace);

        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                radioButtonGender=(RadioButton)group.findViewById(checkedId);
                if (null != radioButtonGender && checkedId > -1)
                {
                    gender = radioButtonGender.getText().toString();                }
            }
        });

        Btn_Profile_Save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                putEditProfile(memberId,ET_Name.getText().toString(),gender,"India",ET_Email.getText().toString(),ET_City.getText().toString(),"Y",ET_Address.getText().toString(),ET_fatherName.getText().toString());
            }
        });

        IV_Back_Arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        Btn_Change_Password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ProfileDetailActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void putEditProfile(final String memberId,final String name,final String gender,final String country,final String emailId,final String city,final String flag,final String Address1,final String father_name)
    {
        String url = Constant.URL+"editProfile";
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
                        Toast.makeText(ProfileDetailActivity.this,"Profile Update",Toast.LENGTH_SHORT).show();
                        sessionManeger.createSession(userId,userName,userEmail,userMobile,memberId,city);
                    }
                    else
                    {
                        Toast.makeText(ProfileDetailActivity.this," "+message,Toast.LENGTH_SHORT).show();
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
                params.put("update_flag",flag);
                params.put("userFile","");
                params.put("Address1",Address1);
                params.put("EmailID", emailId);
                params.put("Memb_Name", name);
                params.put("Gender", gender);
                params.put("M_COUNTRY", country);
                params.put("father_name",father_name);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }
}
