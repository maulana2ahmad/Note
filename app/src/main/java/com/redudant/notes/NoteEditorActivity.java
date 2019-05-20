package com.redudant.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity
{

    EditText etNotes;

    int noteId;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        etNotes = (EditText) findViewById(R.id.etNotes);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        //cek
        if (noteId != -1)
        {
            etNotes.setText(MainActivity.notes.get(noteId));
        }
        else
        {
            //
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() -1;
            MainActivity.arrayAdapter.notifyDataSetChanged(); //set ke daftar list
        }

        //text change
        etNotes.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                MainActivity.notes.set(noteId, String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                //permanent storage
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.redudant.notes", MODE_PRIVATE);

                HashSet<String> set = new HashSet<>(MainActivity.notes);

                sharedPreferences.edit().putStringSet("notes", set).apply();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }
}
