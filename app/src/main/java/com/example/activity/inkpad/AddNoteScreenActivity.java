package com.example.activity.inkpad;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Calendar;

public class AddNoteScreenActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar detailToolbar;
    ImageButton btnBack;
    ImageButton btnDelete;
    ImageButton btnShare;
    ImageButton btnAdd;
    EditText edtContent, edtTitle;
    int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnDelete = (ImageButton) findViewById(R.id.btnDelete);
        btnShare = (ImageButton) findViewById(R.id.btnExport);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        edtContent = (EditText) findViewById(R.id.edtContent);
        edtTitle = (EditText) findViewById(R.id.edtTitle);

        btnBack.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        detailToolbar = (Toolbar) findViewById(R.id.toolbarDetail);
        detailToolbar.setLogo(R.drawable.rsz_ic_notead);
        setSupportActionBar(detailToolbar);
        // change color for back button on toolbar
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.whileColor), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get mode insert or update
        Bundle b = getIntent().getExtras();
        mode = b.getInt("MODE");

        if (mode == NoteListScreenActivity.MODE_INSERT) {
            // if insert, disable button delete, add, share
            btnDelete.setEnabled(false);
            btnAdd.setEnabled(false);
            btnShare.setEnabled(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    String content = "";
    String title = "";
    String time = "";

    private String getTime(){
        Calendar c = Calendar.getInstance();
        String timeUpdate = c.HOUR_OF_DAY + ":" + c.MINUTE;
        return timeUpdate;
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnBack:
                // get content, timeUpdate and title
                time = getTime();
                content =  edtContent.getText().toString();
                title = edtTitle.getText().toString();
                insertUpdate();
                break;
            case R.id.btnDelete:

                break;
            case R.id.btnExport:

                break;
            case R.id.btnAdd:

                break;
        }
    }

    NoteHelper noteHelper = new NoteHelper(this);

    // the class insert or update when press button back
    public void insertUpdate() {
        Note n = new Note();
        n.setName(title);
        n.setTime(time);
        n.setContent(content);
        if (mode == NoteListScreenActivity.MODE_INSERT) {
            // title blank and content is not blank
            if (title.equals("") && !content.equals("")) {
                // insert with title is first paragraph of content
                n.setName(getFirstParagraph(content));
                noteHelper.insert(n);
                Log.d("INSERT", " title blank and content NOT blank");
            } else if (!title.equals("") && content.equals("")) { // title not blank and content blank
                // insert normal
                noteHelper.insert(n);
                Log.d("INSERT", " title not blank and content blank");
            } else if (!title.equals("") && !content.equals("")) { // title and content is not blank
                // insert normal
                noteHelper.insert(n);
                Log.d("INSERT", " title and content not blank");
            }
             //if title and content are blank do  not save anything
            // go back list activity
            Intent intentBack = new Intent(AddNoteScreenActivity.this, NoteListScreenActivity.class);
            startActivity(intentBack);
        }
    }

    // get first line in content
    public String getFirstParagraph(String content) {
//        String titleTemp = content.substring(0, 17);
        String[] lines = content.split("\\n");
        String titleTemp = lines[0];
        return titleTemp;
    }
}
