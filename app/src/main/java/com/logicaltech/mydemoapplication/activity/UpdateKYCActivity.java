package com.logicaltech.mydemoapplication.activity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

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

public class UpdateKYCActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    String[] Detail = { "Adhar Detail", "Bank Detail"};
    Spinner spinner;
    ImageView imageView_back_arrow,imageView_pan_card,imageView_Address;
    Button btn_file_select,btn_submit,btn_address_file;
    EditText editText_pan_no,editText_doc_no;
    String address_type,memberId;
    SessionManeger sessionManeger;
    TextView textView_message,textView_pan_cart_attachment,textView_address_attachment;
    ArrayAdapter aa;
    String base64Sting,filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_kyc);
        sessionManeger = new SessionManeger(getApplicationContext());
        HashMap<String, String> hashMap = sessionManeger.getUserDetails();
        memberId = hashMap.get(SessionManeger.MEMBER_ID);

        spinner = (Spinner) findViewById(R.id.spinnerAddress);
        btn_file_select = (Button) findViewById(R.id.btn_choose_file_kyc);
        btn_address_file = (Button) findViewById(R.id.btn_choose_address_file_kyc);
        btn_submit = (Button) findViewById(R.id.button_kyc_submit);
        editText_pan_no = (EditText) findViewById(R.id.edittextPancartNo);
        editText_doc_no = (EditText) findViewById(R.id.textviewDocumentNo);
        textView_message = (TextView) findViewById(R.id.textview_message);
        textView_pan_cart_attachment = (TextView) findViewById(R.id.textview_pancard_attachment);
        textView_address_attachment = (TextView) findViewById(R.id.textview_address_attachment);
        imageView_pan_card = (ImageView) findViewById(R.id.imageViewPanCard);
        imageView_Address = (ImageView) findViewById(R.id.imageViewAddress);

        spinner.setOnItemSelectedListener(this);
        imageView_back_arrow = (ImageView) findViewById(R.id.reacharg_back_arrow_update_kyc);
        //Creating the ArrayAdapter instance having the country list
        aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Detail);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        postKYC_Detail(memberId);

        btn_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String pan_no = editText_pan_no.getText().toString();
                if (pan_no.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter Pan Card No",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (address_type.equals(""))
                    {
                        Toast.makeText(getApplicationContext(),"Please select Address",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String documen_no = editText_doc_no.getText().toString();
                        if (documen_no.equals(""))
                        {
                            Toast.makeText(getApplicationContext(),"Please Enter Document No",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            putUpdateKYC(memberId,pan_no,Constant.IMG_URL,Constant.IMG_URL,documen_no,address_type);
                        }
                    }
                }
            }
        });

        imageView_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
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

        btn_address_file.setOnClickListener(new View.OnClickListener()
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
        Toast.makeText(getApplicationContext(),Detail[position] , Toast.LENGTH_LONG).show();
        address_type = Detail[position];
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

    public void putUpdateKYC(final String membercode, final String pan_card_no,final String pan_attachment, final String attachment,final String document_no,final String Doc_Type) {
        String url = Constant.URL+"updateKYC";
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
                        Toast.makeText(UpdateKYCActivity.this,""+msg,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(UpdateKYCActivity.this,""+msg,Toast.LENGTH_SHORT).show();
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
                params.put("membercode", membercode);
                params.put("pan_card_no",pan_card_no);
                params.put("pan_attachment", pan_attachment);
                params.put("attachment",attachment);
                params.put("document_no",document_no);
                params.put("Doc_Type",Doc_Type);
                return params;
            }
        };
        MySingalton.getInstance(getApplicationContext()).addRequestQueue(jsonObjRequest);
    }

    public void postKYC_Detail(final String userId) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Constant.URL+"getKYCDetails?membercode="+userId;
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String flag = jsonObject.getString("flag");

                    if (flag.equals("R"))
                    {
                        String pan_card_no = jsonObject.getString("pan_card_no");
                        String pan_attachment = jsonObject.getString("pan_attachment");
                        String doc_type = jsonObject.getString("Doc_Type");
                        String document_no = jsonObject.getString("document_no");
                        String attachment = jsonObject.getString("attachment");

                        editText_pan_no.setText(""+pan_card_no);
                        editText_doc_no.setText(""+document_no);
                        textView_pan_cart_attachment.setText("File Upload: ");
                        textView_address_attachment.setText("File Upload: ");
                        Picasso.with(UpdateKYCActivity.this).load(pan_attachment).into(imageView_pan_card);
                        Picasso.with(UpdateKYCActivity.this).load(attachment).into(imageView_Address);
                        textView_message.setText("Your Previous details is rejected by Admin .Please Resubmit your details.");
                    }
                    else if (flag.equals("N"))
                    {
                        String pan_card_no = jsonObject.getString("pan_card_no");
                        String pan_attachment = jsonObject.getString("pan_attachment");
                        String doc_type = jsonObject.getString("Doc_Type");
                        String document_no = jsonObject.getString("document_no");
                        String attachment = jsonObject.getString("attachment");

                        editText_pan_no.setText(""+pan_card_no);
                        editText_doc_no.setText(""+document_no);
                        textView_pan_cart_attachment.setText("File Upload: ");
                        textView_address_attachment.setText("File Upload: ");
                        Picasso.with(UpdateKYCActivity.this).load(pan_attachment).into(imageView_pan_card);
                        Picasso.with(UpdateKYCActivity.this).load(attachment).into(imageView_Address);

                        if (doc_type.equals("Bank Details"))
                        {
                            int spinnerPosition = aa.getPosition(doc_type);
                            spinner.setSelection(spinnerPosition);
                        }

                        textView_message.setText("Your KYC Details is Submitted. Please wait for Admin Confirmation.");
                        btn_submit.setVisibility(View.GONE);
                    }
                    else if(flag.equals("Y"))
                    {
                        textView_message.setText("Your KYC Details is Approved by Admin.");
                        btn_submit.setVisibility(View.GONE);
                      //  Toast.makeText(UpdateKYCActivity.this,""+status,Toast.LENGTH_SHORT).show();
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
