package com.akshay.workmanagerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_COUNT_VALUE = "key_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        final TextView textView= (TextView) findViewById(R.id.textView);

        final Data data = new Data.Builder()
                .putInt(KEY_COUNT_VALUE, 1750).build();

        /*Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true).build();*/

        final OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest
                .Builder(DemoWorker.class)
                .setInputData(data)
                //.setConstraints(constraints)
                .build();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);

            }
        });

        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null) {
                            textView.setText(workInfo.getState().name());

                            if(workInfo.getState().isFinished()){
                                Data data1 = workInfo.getOutputData();
                                String message = data1.getString(DemoWorker.KEY_WORKER);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        }
                        }
                });
    }
}