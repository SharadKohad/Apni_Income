package com.logicaltech.apniincome.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logicaltech.apniincome.R;

import utility.Constant;

public class EarningReportActivity extends AppCompatActivity
{
    ImageView imageView_back_arrow;
    TextView textView_total_income,TV_Direct_Income,TV_Binary_Income;
    LinearLayout linearLayout_generated_ro_history,linearLayout_direct_income,linearLayout_binary_income,linearLayout_all_trancation;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning_report);
        imageView_back_arrow = (ImageView) findViewById(R.id.imageview_back_arrow_earning_report);
        textView_total_income = (TextView) findViewById(R.id.text_view_total_income);
        TV_Direct_Income = (TextView) findViewById(R.id.textview_DirectIncome);
        TV_Binary_Income = (TextView) findViewById(R.id.textview_Binary_Income);
        linearLayout_generated_ro_history = (LinearLayout) findViewById(R.id.linear_layout_generated_ro_history);
        linearLayout_direct_income = (LinearLayout) findViewById(R.id.linear_layout_direct_incomeing);
        linearLayout_binary_income = (LinearLayout) findViewById(R.id.linear_layout_binary_income_history);
        linearLayout_all_trancation = (LinearLayout) findViewById(R.id.linear_layout_all_trancation);

        textView_total_income.setText(Constant.TOTAL_BALANCE);
        TV_Direct_Income.setText(Constant.DIRECT_INCOME+" Rs");
        TV_Binary_Income.setText(Constant.TOTAL_BINARY+" Rs");
        imageView_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        linearLayout_generated_ro_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EarningReportActivity.this,EarningReportHistoryActivity.class);
                intent.putExtra("flag","0");
                startActivity(intent);
            }
        });

        linearLayout_direct_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EarningReportActivity.this,EarningReportHistoryActivity.class);
                intent.putExtra("flag","1");
                startActivity(intent);
            }
        });

        linearLayout_binary_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EarningReportActivity.this,BinaryIncomeHistoryActivity.class);
                startActivity(intent);
            }
        });
        linearLayout_all_trancation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(EarningReportActivity.this,EarningReportHistoryActivity.class);
                intent.putExtra("flag","2");
                startActivity(intent);
            }
        });
    }
}
