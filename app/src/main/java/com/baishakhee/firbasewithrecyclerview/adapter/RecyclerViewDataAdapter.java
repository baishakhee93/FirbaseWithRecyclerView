package com.baishakhee.firbasewithrecyclerview.adapter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baishakhee.firbasewithrecyclerview.R;
import com.baishakhee.firbasewithrecyclerview.model.DataModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewDataAdapter extends FirebaseRecyclerAdapter<DataModel, RecyclerViewDataAdapter.ViewHolder> {

    Context context;
    FirebaseRecyclerOptions<DataModel> options; // Add this member variable

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public RecyclerViewDataAdapter(@NonNull FirebaseRecyclerOptions<DataModel> options) {
        super(options);
        this.options = options; // Initialize options

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull DataModel dataModels) {
        holder.name.setText(dataModels.getName());
        holder.age.setText(String.valueOf(dataModels.getAge()));
        holder.city.setText(dataModels.getCity());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.name.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_add_people))
                        .setExpanded(true, 800).create();


                EditText edtName = (EditText) dialogPlus.findViewById(R.id.etName);
                EditText edtAge =(EditText) dialogPlus.findViewById(R.id.etage);
                EditText edtCity =(EditText) dialogPlus.findViewById(R.id.etCity);
                TextView tvAdd = (TextView) dialogPlus.findViewById(R.id.tv_Add);
                ImageView close = (ImageView) dialogPlus.findViewById(R.id.close);
                Button btnUpdate= (Button) dialogPlus.findViewById(R.id.btn_add);
                tvAdd.setText("Update Data");
                btnUpdate.setText("Update");
                edtName.setText(dataModels.getName());
                edtAge.setText(String.valueOf(dataModels.getAge()));
                edtCity.setText(dataModels.getCity());

                dialogPlus.show();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogPlus.dismiss();
                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("data");

                        Map<String, Object> map = new HashMap<>();
                        map.put("name", holder.name.getText().toString());
                        map.put("age", Integer.valueOf(holder.age.getText().toString()));
                        map.put("city", holder.city.getText().toString());
                        String dataKey = getRef(position).getKey();

                        dataRef.child(dataKey)
                                .updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(holder.name.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.name.getContext(), "Failed", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                });

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, age, city;
        ImageView edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            city = itemView.findViewById(R.id.city);
            edit = itemView.findViewById(R.id.edit);
        }
    }

}
