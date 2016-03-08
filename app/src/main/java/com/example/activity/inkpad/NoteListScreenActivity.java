package com.example.activity.inkpad;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class NoteListScreenActivity extends AppCompatActivity {
    Toolbar toolbar;
    NoteAdapter adapter;
    ListView lvNote;
    SearchView mSearchview;
    public static final int MODE_INSERT = 0;
    public static final int MODE_UPDATE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lvNote = (ListView) findViewById(R.id.lvNote);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.rsz_ic_notead);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start detail activity with content and title are empty
                Intent intentAdd = new Intent(NoteListScreenActivity.this, DetailNoteScreenActivity.class);
                // put the mode insert
                intentAdd.putExtra("MODE", MODE_INSERT);
                startActivity(intentAdd);
            }
        });
        // set background for fab
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fabCorlor)));

        final NoteHelper noteHelper = new NoteHelper(this);
        final List<Note> notes = noteHelper.getAll();

        adapter = new NoteAdapter(this, R.layout.row_note, notes);
        lvNote.setAdapter(adapter);


        // when click, load content and title, start activity detail
        lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // load content and title from listNote
                String content = notes.get(position).getContent();
                String title = notes.get(position).getName();
                int _id = notes.get(position).getId();
                // Create new intent and put content, title, id
                Intent intentDetail = new Intent(NoteListScreenActivity.this, DetailNoteScreenActivity.class);
                intentDetail.putExtra(Note.NOTE_CONTENT, content);
                intentDetail.putExtra(Note.NOTE_NAME, title);
                intentDetail.putExtra(Note.ID, _id);
                // put mode update
                intentDetail.putExtra("MODE", MODE_UPDATE);
                startActivity(intentDetail);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // find action Search
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        // get searchview
        mSearchview = (SearchView) MenuItemCompat.getActionView(itemSearch);
        // listen when search
//        mSearchview.setBackground();
        mSearchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // when the text on the searchview change, call getFilter in Adapter
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }

}
