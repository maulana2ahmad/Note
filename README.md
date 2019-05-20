# Note
App Note

### Priview
![ezgif com-resize](https://user-images.githubusercontent.com/43386555/58025407-946ef800-7b3e-11e9-8a57-83b96ad3db32.gif)


### 1. activity_main.xml
      <ListView
              android:id="@+id/listView"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:layout_marginStart="0dp"
              android:layout_marginTop="8dp"
              android:layout_marginEnd="0dp"
              android:layout_marginBottom="8dp"
              app:layout_constraintBottom_toBottomOf="parent"
              app:layout_constraintEnd_toEndOf="parent"
              app:layout_constraintStart_toStartOf="parent"
              app:layout_constraintTop_toTopOf="parent">
          </ListView>

### 2. MainActivity.java
        ListView listView;

            static ArrayAdapter arrayAdapter;

            static ArrayList<String> notes = new ArrayList<>();

            public SharedPreferences sharedPreferences;


            @Override
            public boolean onCreateOptionsMenu(Menu menu) {

                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.add_note_menu, menu);

                return super.onCreateOptionsMenu(menu);
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                super.onOptionsItemSelected(item);

                if (item.getItemId() == R.id.add_note) {
                    Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);

                    startActivity(intent);

                    return true;

                }

                return false;
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                listView = (ListView) findViewById(R.id.listView);

                sharedPreferences = getApplicationContext().getSharedPreferences("com.redudant.notes", MODE_PRIVATE);

                HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

                if (set == null)
                {

                    notes.add("New Note");

                }
                else
                {
                    notes = new ArrayList(set);
                }

                arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

                listView.setAdapter(arrayAdapter);

                //jump to noteeditoractivity.java
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                        intent.putExtra("noteId", position);
                        startActivity(intent);

                    }
                });

                //alert delete list note
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
                {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        //add int to delete from int position method onItemLongClick
                        final int itemToDelete = position;

                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Are you sure?")
                                .setMessage("Do you want do delete this note?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {

                                                notes.remove(itemToDelete);
                                                arrayAdapter.notifyDataSetChanged();

                                                //permanent storage
                                                sharedPreferences = getApplicationContext().getSharedPreferences("com.redudant.notes", MODE_PRIVATE);

                                                HashSet<String> set = new HashSet<>(MainActivity.notes);

                                                sharedPreferences.edit().putStringSet("notes", set).apply();
                                            }
                                        }
                                )
                                .setNegativeButton("No", null)
                                .show();

                        return true;
                    }
                });

            }


### 3. activity_note_editor.xml
      <EditText
              android:background="@color/colorLemonChiffon"
              android:id="@+id/etNotes"
              android:layout_width="match_parent"
              android:layout_height="match_parent"
              android:layout_marginBottom="0dp"
              android:ems="10"
              android:gravity="start|top"
              android:inputType="textMultiLine"
              app:layout_constraintBottom_toBottomOf="parent"
              app:layout_constraintEnd_toEndOf="parent"
              app:layout_constraintStart_toStartOf="parent" />
              
 ### 4. NoreEditorActivity.java
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
            
            
