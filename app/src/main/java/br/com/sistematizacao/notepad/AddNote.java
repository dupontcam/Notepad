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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

import br.com.sistematizacao.notepad.database.NoteDatabase;
import br.com.sistematizacao.notepad.model.Note;

public class AddNote extends AppCompatActivity {

    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    String todaysDate, currentTime;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        noteTitle = findViewById(R.id.noteTitle);
        noteDetails = findViewById(R.id.noteDetails);

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
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        NoteDatabase db = new NoteDatabase(this);
        Note note = new Note();

        if (item.getItemId() == R.id.save) {
            if (noteTitle.getText().toString().isEmpty()) {
                noteTitle.setError("O título não pode estar vazio.");
                return false;
            }
            if (noteDetails.getText().toString().isEmpty()) {
                noteDetails.setError("A descrição não pode estar vazia.");
                return false;
            }
            note.setTitle(noteTitle.getText().toString());
            note.setDescription(noteDetails.getText().toString());
            note.setDate(todaysDate);
            note.setTime(currentTime);
            note = new Note(noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate, currentTime);
            db.addNote(note);
            Toast.makeText(this, "Anotação salva.", Toast.LENGTH_SHORT).show();
            goToMain();
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
    }
}