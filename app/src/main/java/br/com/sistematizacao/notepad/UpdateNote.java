package br.com.sistematizacao.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Objects;

import br.com.sistematizacao.notepad.model.Note;
import br.com.sistematizacao.notepad.database.NoteDatabase;

public class UpdateNote extends AppCompatActivity {

    Toolbar toolbar;
    EditText editTextTextUp, editTextTextMultiLineUp;
    String todaysDate, currentTime;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("noteTitle");
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextTextUp = findViewById(R.id.editTextTextUp);
        editTextTextMultiLineUp = findViewById(R.id.editTextTextMultiLineUp);

        Intent intent = getIntent();
        if (intent != null) {
            Long noteId = intent.getLongExtra("noteId", -1L);
            String noteTitle = intent.getStringExtra("noteTitle");
            String noteDescription = intent.getStringExtra("noteDescription");

            // Defina os valores nos campos
            editTextTextUp.setText(noteTitle);
            editTextTextMultiLineUp.setText(noteDescription);
        }

        //get current date and time
        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        currentTime = pad(c.get(Calendar.HOUR_OF_DAY)) + ":" + pad(c.get(Calendar.MINUTE));

        Log.d("date", todaysDate);
        Log.d("time", currentTime);
    }

    private String pad(int i) {
        if(i < 10)
            return "0" + i;
        return String.valueOf(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_delete_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NoteDatabase db = new NoteDatabase(this);

        int itemId = item.getItemId();

        if (itemId == R.id.edit) {
            if (editTextTextUp.getText().toString().isEmpty()) {
                editTextTextUp.setError("Title cannot be empty");
                return false;
            }
            if (editTextTextMultiLineUp.getText().toString().isEmpty()) {
                editTextTextMultiLineUp.setError("Description cannot be empty");
                return false;
            }
            editTextTextUp.setEnabled(true);
            editTextTextMultiLineUp.setEnabled(true);

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to update this note?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        long noteId = getIntent().getLongExtra("noteId", -1L);

                        Note note = new Note();
                        note.setID(noteId);
                        note.setTitle(editTextTextUp.getText().toString());
                        note.setDescription(editTextTextMultiLineUp.getText().toString());
                        note.setDate(todaysDate);
                        note.setTime(currentTime);
                        db.updateNote(note);
                        goToMain();
                        Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        goToMain(); // Chamar goToMain() aqui
                    }).show();


            //goToMain();
        } else if (itemId == R.id.delete) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        long position = getIntent().getLongExtra("noteId", -1L);
                        Note note = new Note();
                        note.setID(position);
                        db.deleteNote(position); // Certifique-se de ter a posição correta
                        goToMain();
                        Toast.makeText(this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        goToMain(); // Chamar goToMain() aqui
                    }).show();

        } else {
            return super.onOptionsItemSelected(item);
        }
        return true; // Retorne true para indicar que o item foi processado
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

}