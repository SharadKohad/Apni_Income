package com.logicaltech.mydemoapplication.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.logicaltech.mydemoapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class WalletActivity extends AppCompatActivity
{
    ImageView back_arrow;
    LinearLayout linearLayout_rwallet_request,linearLayout_rwallet_request_history,linearLayout_transfer_ewallet_to_rwallet,linearLayout_transfer_ewallet_to_rwallet_history;
    Intent intent;
    TextView textView_total_balance;
    SessionManeger sessionManeger;
    String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);

        back_arrow = (ImageView) findViewById(R.id.imageview_back_arrow_rwallet);
        linearLayout_rwallet_request = (LinearLayout) findViewById(R.id.linear_layout_rwallet_request);
        linearLayout_rwallet_request_history = (LinearLayout) findViewById(R.id.linear_layout_rwallet_request_history);
        linearLayout_transfer_ewallet_to_rwallet = (LinearLayout) findViewById(R.id.linear_layout_transfer_ewallet_to_rwallet);
        linearLayout_transfer_ewallet_to_rwallet_history = (LinearLayout) findViewById(R.id.linear_layout_transfer_ewallet_to_rwallet_history);
        textView_total_balance = (TextView) findViewById(R.id.text_view_RWtotal_balance);

        textView_total_balance.setText(Constant.TOTAL_BALANCE+" Rs");

        back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        linearLayout_rwallet_request.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(WalletActivity.this,RWFundRequestActivity.class);
                startActivity(intent);
            }
        });

        linearLayout_rwallet_request_history.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(WalletActivity.this,RWallet_History_Activity.class);
                intent.putExtra("flag","0");
                startActivity(intent);
            }
        });
        linearLayout_transfer_ewallet_to_rwallet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showCustomDialog();
            }
        });

        linearLayout_transfer_ewallet_to_rwallet_history.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(WalletActivity.this,RWallet_History_Activity.class);
                intent.putExtra("flag","1");
                startActivity(intent);
            }
        });

    }

    private void showCustomDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.transfer_ewallet_to_rwallet);
        dialog.setCancelable(true);
        final EditText editText_amount;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        editText_amount = (EditText) dialog.findViewById(R.id.EditText_RW_amount);

        ((ImageView) dialog.findViewById(R.id.imageview_close)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.btn_transferetr)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String amount = editText_amount.getText().toString();
                if (amount.equals(""))
                {
                    Toast.makeText(getApplicationContext(), " Please enter the transfer amount: ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    transferfundEwalltoRwallet(memberId,amount);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void transferfundEwalltoRwallet(final String memberId,final String amount) {
        String url = Constant.URL+"transferfundEwalltoRwallet";
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
                        Toast.makeText(WalletActivity.this," "+msg,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(WalletActivity.this," "+msg,Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }
}
