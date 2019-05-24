package com.app.pipelinesurvey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.base.BaseActivity;

public class TemplateSelectActivity extends BaseActivity {
    private Spinner spModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_select);
        spModel = (Spinner) findViewById(R.id.spModel);
        findViewById(R.id.tvEnter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TemplateSelectActivity.this,ProjectListActivity.class)
                        .putExtra("model",spModel.getSelectedItem().toString()));
            }
        });
    }
}
