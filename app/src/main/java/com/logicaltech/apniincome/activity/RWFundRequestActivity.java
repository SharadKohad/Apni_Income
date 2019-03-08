package com.logicaltech.apniincome.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.logicaltech.apniincome.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class RWFundRequestActivity extends AppCompatActivity
{
    ImageView imageView_back_arrow;
    EditText editText_amount,editText_bank_name,editText_account_no,editText_trancation_no;
    Button btn_submit;
    SessionManeger sessionManeger;
    String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rwfund_request);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);
        imageView_back_arrow =  (ImageView) findViewById(R.id.imageview_back_arrow_rwallet_fund_request);
        editText_amount = (EditText) findViewById(R.id.EditText_RW_amount);
        editText_bank_name = (EditText) findViewById(R.id.edit_text_RWbank_name);
        editText_account_no = (EditText) findViewById(R.id.edit_text_RWaccount_no);
        editText_trancation_no = (EditText) findViewById(R.id.edit_text_trancastion_no);
        btn_submit = (Button) findViewById(R.id.button_RW_submit);

        imageView_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = editText_amount.getText().toString();
                if (amount.equals(""))
                {
                    Toast.makeText(RWFundRequestActivity.this,"Please Enter Amount: ",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String bank_name = editText_bank_name.getText().toString();
                    if (bank_name.equals(""))
                    {
                        Toast.makeText(RWFundRequestActivity.this,"Please Enter Bank Name: ",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String account_no = editText_account_no.getText().toString();
                        if (account_no.equals(""))
                        {
                            Toast.makeText(RWFundRequestActivity.this,"Please Enter Account No: ",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String trancation_no = editText_trancation_no.getText().toString();
                            if (trancation_no.equals(""))
                            {
                                Toast.makeText(RWFundRequestActivity.this,"Please Enter Trancation No: ",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                addRwalletFundReq(memberId,amount,bank_name,account_no,trancation_no);
                            }
                        }
                    }
                }
            }
        });
    }


    public void addRwalletFundReq(final String memberId,final String amount,final String fundbank_name,final String fundaccount_no,final String fundTransaction_No)
    {
        String url = Constant.URL+"addRwalletFundReq";
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
                        Toast.makeText(RWFundRequestActivity.this," "+msg,Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(RWFundRequestActivity.this," "+msg,Toast.LENGTH_SHORT).show();
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
                params.put("Amount",amount);
                params.put("fundbank_name", fundbank_name);
                params.put("fundaccount_no",fundaccount_no);
                params.put("fundTransaction_No", fundTransaction_No);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }
}
