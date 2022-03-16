package com.example.nutritionfactsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    Context context;
    private ArrayList<String> listName, description;

    public ListAdapter(Context lContext, ArrayList<String> lName, ArrayList<String> lDescription) { //Constructor to pass all the data
        context = lContext;
        this.listName = lName;
        this.description = lDescription;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_view_card, parent, false); // view in the recycler view
        return new ListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.listName.setText(String.valueOf(listName.get(position)));
        holder.description.setText(String.valueOf(description.get(position)));

        holder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DisplayCustomListItem.class);
                intent.putExtra("listName", listName.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listName.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView listName, description;
        ConstraintLayout listLayout;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            listName = itemView.findViewById(R.id.listName); //Get id from text views
            description = itemView.findViewById(R.id.listDescription);

            listLayout = itemView.findViewById(R.id.listLayout);

        }
    }
}
