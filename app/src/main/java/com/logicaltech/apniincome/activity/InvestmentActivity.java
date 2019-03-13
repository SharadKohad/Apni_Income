package com.logicaltech.apniincome.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.logicaltech.apniincome.R;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import utility.Constant;
import utility.MySingalton;
import utility.SessionManeger;

public class InvestmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    String[] amount = { "1000", "2000", "3000", "5000", "10000", "15000", "25000", "50000","100000","200000"};
    String investment_amount = "",paidamount="",memberId,investmentBy="",base64Sting,filePath;
    TextView textView_paidAmount;
    Button btn_file_select,btn_proccess,btn_investment_history;
    SessionManeger sessionManeger;
    private RadioGroup radioGroupInveBy;
    private RadioButton radioButtonInveBy;
    ImageView imageView_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinnerAmont);
        textView_paidAmount = (TextView) findViewById(R.id.textviewPaidAmont);
        btn_file_select = (Button) findViewById(R.id.btn_choose_file);
        btn_proccess = (Button) findViewById(R.id.button_investment_submit);
        btn_investment_history = (Button) findViewById(R.id.button_investment_history);
        imageView_back_arrow = (ImageView) findViewById(R.id.reacharg_back_arrow_make_investment);

        radioGroupInveBy = (RadioGroup) findViewById(R.id.rediogroupplace);

        radioGroupInveBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                radioButtonInveBy=(RadioButton)group.findViewById(checkedId);
                if (null != radioButtonInveBy && checkedId > -1)
                {
                    investmentBy = radioButtonInveBy.getText().toString();
                }
            }
        });

        imageView_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);
        spin.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,amount);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        btn_investment_history.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(InvestmentActivity.this,InvestmentHistoryActivity.class);
                startActivity(intent);
            }
        });

        btn_proccess.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                addInvestment(memberId,investment_amount,investmentBy,Constant.IMG_URL);
            }
        });

        btn_file_select.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    pickImage();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),amount[position] , Toast.LENGTH_LONG).show();
        investment_amount = amount[position];
        int amt = Integer.parseInt(investment_amount);
        int totalamount = ((amt*118)/100);
        textView_paidAmount.setText(""+totalamount);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
        {
            return;
        }
        if (requestCode == 1)
        {
            final Bundle extras = data.getExtras();
            if (extras != null)
            {
                //Get image
                Bitmap newProfilePic = extras.getParcelable("data");
            }
            filePath = data.toString();

            getBase64();

        }
    }

    public void addInvestment(final String membercode, final String USD_Amount,final String BTC_Type, final String attachment) {
        String url = Constant.URL+"addInvestment";
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
                        Toast.makeText(InvestmentActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(InvestmentActivity.this,""+msg,Toast.LENGTH_SHORT).show();
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
                params.put("membercode", membercode);
                params.put("USD_Amount",USD_Amount);
                params.put("BTC_Type", BTC_Type);
                params.put("attachment",attachment);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }

    private String getBase64() {
        File file = new File(filePath);  //file Path
        byte[] b = new byte[(int) file.length()];
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            for (int j = 0; j < b.length; j++)
            {
                System.out.print((char) b[j]);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File Not Found.");
            e.printStackTrace();
        }
        catch (IOException e1)
        {
            System.out.println("Error Reading The File.");
            e1.printStackTrace();
        }

        byte[] byteFileArray = new byte[0];
        try
        {
            byteFileArray = FileUtils.readFileToByteArray(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        String base64String = "";
        if (byteFileArray.length > 0)
        {
            base64String = android.util.Base64.encodeToString(byteFileArray, android.util.Base64.NO_WRAP);
            Log.i("File Base64 string", "IMAGE PARSE ==>" + base64String);
        }
        System.out.print(" My URL"+base64String);
        System.out.print(" My URL1"+base64Sting);
        return base64Sting;

    }
}
