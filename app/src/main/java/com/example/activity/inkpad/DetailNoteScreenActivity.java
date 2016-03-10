package com.example.activity.inkpad;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.Date;

public class DetailNoteScreenActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar detailToolbar;
    ImageButton btnBack;
    ImageButton btnDelete;
    ImageButton btnShare;
    ImageButton btnAdd;
    EditText edtContent, edtTitle;
    int mode;
    int noteId; // id for update
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
        // set when press back button on toolbar
        detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(DetailNoteScreenActivity.this, "back pressed", Toast.LENGTH_SHORT).show();
                insertUpdate();
                // go back list activity
                goBack();
            }
        });

        // get mode insert or update
        Bundle b = getIntent().getExtras();
        mode = b.getInt("MODE");

        String content;
        String title;


        if (mode == NoteListScreenActivity.MODE_INSERT) {
            // if insert, disable button delete, add, share
            btnDelete.setEnabled(false);
            btnAdd.setEnabled(false);
            btnShare.setEnabled(false);
        } else if (mode == NoteListScreenActivity.MODE_UPDATE) {
            // load content and title
            content = b.getString(Note.NOTE_CONTENT);
            title = b.getString(Note.NOTE_NAME);
            noteId = b.getInt(Note.ID);
            edtContent.setText(content);
            edtTitle.setText(title);
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
//        Calendar c = Calendar.getInstance();
//        String timeUpdate = c.HOUR_OF_DAY + ":" + c.MINUTE;
        Date d = new Date();
        String timeUpdate = String.valueOf(d.getHours()) + ":" + String.valueOf(d.getMinutes());
        return timeUpdate;
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnBack:
                insertUpdate();
                // go back list activity
                goBack();
                break;
            case R.id.btnDelete:
                // delete by id
               /* noteHelper.delete(noteId);
                // go back to List activity
                goBack();*/
                showDialog();
                break;
            case R.id.btnExport:
                share();
                break;
            case R.id.btnAdd:
                // save the current note
                insertUpdate();
                // go to activity detail empty
                Intent intentAdd = new Intent(DetailNoteScreenActivity.this, DetailNoteScreenActivity.class);
                intentAdd.putExtra("MODE", NoteListScreenActivity.MODE_INSERT);
                startActivity(intentAdd);
                break;
        }
    }

    Note n = new Note();

    NoteHelper noteHelper = new NoteHelper(this);

    // the class insert or update when press button back
    public void insertUpdate() {
        // get content, timeUpdate and title
        time = getTime();
        content =  edtContent.getText().toString();
        content = content.trim();
        title = edtTitle.getText().toString();
        title = title.trim();

        n.setName(title);
        n.setTime(time);
        n.setContent(content);
        // case insert
        if (mode == NoteListScreenActivity.MODE_INSERT) {
            // title blank and content is not blank
            if (title.equals("") && !content.equals("")) {
                // insert with title is first paragraph of content
                n.setName(getFirstParagraph(content));
                noteHelper.insert(n);
                Log.d("INSERT", " title blank and content NOT blank");
            } else if (title.equals("") && content.equals("")) { // title and content are blank
                // not do nothing
                Log.d("INSERT", "nothing happen on DB");
            } else { // title not blank and content blank OR title and content are not blank
                // insert normal
                noteHelper.insert(n);
                Log.d("INSERT", " title not blank and content blank OR title and content are not blank");
            }
            //if title and content are blank do  not save anything
        } else if (mode == NoteListScreenActivity.MODE_UPDATE) { // case update
            // set id for Note n
            n.setId(noteId);
            if (title.equals("") && content.equals("")) { // title and content are blank
                noteHelper.delete(noteId);
                Log.d("UPDATE", "update delete");
            } else if (title.equals("") && !content.equals("")) { //title balnk and content is not blank
                n.setName(getFirstParagraph(content));
                noteHelper.update(n);
                Log.d("UPDATE", "update with title first");
            }  else { //title not blank, content blank OR title and content are not blank
                noteHelper.update(n);
                Log.d("UPDATE", "update normal");
            }
        }
    }

    public void goBack() {
        Intent intentBack = new Intent(DetailNoteScreenActivity.this, NoteListScreenActivity.class);
        startActivity(intentBack);
    }

    // show dialog when press button delete
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message);
        builder.setNegativeButton("Yes, Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noteHelper.delete(noteId);
                goBack();
            }
        });
        builder.setPositiveButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, edtTitle.getText().toString());
        shareIntent.putExtra(Intent.EXTRA_TEXT, edtContent.getText().toString());
        startActivity(Intent.createChooser(shareIntent, "Share InkPad note"));
    }

    // get first line in content
    public String getFirstParagraph(String content) {
        String titleTemp = "";
        if (content.length() > 20) {
            titleTemp = content.substring(0, 20);
        } else {
            titleTemp = content;
        }
        return titleTemp;
    }
}
