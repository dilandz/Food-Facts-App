package com.example.nutritionfactsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// This class is to view each item from the main list.
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    Context context;
    private ArrayList<String> titles; // array for the products names
    private ArrayList<String> grades;
    private ArrayList<String> imagesURL;
    private ArrayList<String> ingredients;
    private ArrayList<String> nova_groups;
    private ArrayList<String> barcodes;
    private ArrayList<String> nutrients;

    Animation translate_anim;

    public  ItemAdapter(Context pContext, ArrayList<String> pTitles, ArrayList<String> pGrades, ArrayList<String> pIngredients,
                        ArrayList<String> pNova_groups, ArrayList<String> pBarcodes, ArrayList<String> pNutrients,ArrayList<String> pImages){
        context = pContext;
        this.titles = pTitles;
        this.grades = pGrades;
        this.ingredients = pIngredients;
        this.nova_groups = pNova_groups;
        this.barcodes = pBarcodes;
        this.nutrients= pNutrients;
        this.imagesURL = pImages;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.itemview, parent,false);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.title.setText(String.valueOf(titles.get(position))); // get data from array
        holder.grade.setText(String.valueOf(grades.get(position)));

        Picasso.get().load(imagesURL.get(position)).into(holder.image);



        holder.mainLayout.setOnClickListener(new View.OnClickListener() { // when click on each item directed to details screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DisplayingItem.class);
                intent.putExtra("titles", titles.get(position)); // Gets data from activity one
                intent.putExtra("grades", grades.get(position));
                intent.putExtra("images", imagesURL.get(position));
                intent.putExtra("ingredients",ingredients.get(position));
                intent.putExtra("nova_groups",nova_groups.get(position));
                intent.putExtra("barcodes",barcodes.get(position));
                intent.putExtra("nutrients",nutrients.get(position));

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{


        TextView title, grade,ingredient,nova_group,barcode,nutrient;
        ImageView image;
        ConstraintLayout mainLayout;

        public ItemViewHolder(@NonNull  View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            grade = itemView.findViewById(R.id.grade);
            ingredient = itemView.findViewById(R.id.ingredient);
            nova_group = itemView.findViewById(R.id.nova_group);
            barcode = itemView.findViewById(R.id.barcode);
            nutrient = itemView.findViewById(R.id.nutrient);

            image = itemView.findViewById(R.id.image);

            mainLayout = itemView.findViewById(R.id.mainLayout);
            // Animation for the list
            translate_anim = AnimationUtils.loadAnimation(context,R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
