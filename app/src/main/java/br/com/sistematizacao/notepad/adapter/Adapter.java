package br.com.sistematizacao.notepad.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.sistematizacao.notepad.R;
import br.com.sistematizacao.notepad.UpdateNote;
import br.com.sistematizacao.notepad.model.Note;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


    LayoutInflater inflater;
    List<Note> notes;

    public Adapter(Context context, List<Note> notes) {
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        holder.noteTitle.setText(notes.get(position).getTitle());
        holder.nDate.setText(notes.get(position).getDate());
        holder.nTime.setText(notes.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noteTitle, nDate, nTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.noteTitle);
            nDate = itemView.findViewById(R.id.nDate);
            nTime = itemView.findViewById(R.id.nTime);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Note clickedNote = notes.get(position);
                Intent intent = new Intent(view.getContext(), UpdateNote.class);
                intent.putExtra("noteId", clickedNote.getID()); // Passe o ID ou outros dados da anotação
                intent.putExtra("noteTitle", clickedNote.getTitle());
                intent.putExtra("noteDescription", clickedNote.getDescription());
                view.getContext().startActivity(intent);
            }
        }

    }
}
