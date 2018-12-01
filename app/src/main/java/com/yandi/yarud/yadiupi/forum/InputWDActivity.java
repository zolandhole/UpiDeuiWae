package com.yandi.yarud.yadiupi.forum;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.yandi.yarud.yadiupi.R;

import java.util.Objects;

public class InputWDActivity extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener {

    private Button input_wd_btn_batal;
    private ImageButton input_btn_next;
    private EditText editTextDiskusiJudul;
    private GestureDetectorCompat detector;
    private String username, password, idmk, mk, getIsi, getJudul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_wd);
        initView();
        initListener();
        detector = new GestureDetectorCompat(this,this);
    }

    private void initListener() {
        username = Objects.requireNonNull(getIntent().getExtras()).getString("USERID");
        idmk = getIntent().getExtras().getString("IDMK");
        password = getIntent().getExtras().getString("PASSWORD");
        mk = getIntent().getExtras().getString("MK");
        getJudul = getIntent().getExtras().getString("JUDUL");
        getIsi = getIntent().getExtras().getString("ISI");

        editTextDiskusiJudul.setText(getJudul);
        input_wd_btn_batal.setOnClickListener(this);
        input_btn_next.setOnClickListener(this);
    }

    private void initView() {
        input_wd_btn_batal = findViewById(R.id.input_wd_btn_batal);
        input_btn_next = findViewById(R.id.input_btn_next);
        editTextDiskusiJudul = findViewById(R.id.editTextDiskusiJudul);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.input_wd_btn_batal:
                Intent intentDataForum = new Intent(InputWDActivity.this, DataForumActivity.class);
                intentDataForum.putExtra("IDMK",idmk);
                intentDataForum.putExtra("MK",mk);
                startActivity(intentDataForum);
                finish();
                break;
            case R.id.input_btn_next:
                keInputIsiDiskusi();
                break;
        }
    }

    private void keInputIsiDiskusi() {
        if (TextUtils.isEmpty(editTextDiskusiJudul.getText())){
            Toast.makeText(this, "Judul Diskusi harus dimasukan", Toast.LENGTH_SHORT).show();
        } else {
            Intent intentID = new Intent(InputWDActivity.this, InputDiskusiActivity.class);
            intentID.putExtra("USERID",username);
            intentID.putExtra("IDMK", idmk);
            intentID.putExtra("PASSWORD", password);
            intentID.putExtra("MK",mk);
            intentID.putExtra("JUDUL", editTextDiskusiJudul.getText().toString().trim());
            intentID.putExtra("ISI", getIsi);
            startActivity(intentID);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        keInputIsiDiskusi();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}