package com.baishakhee.firbasewithrecyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.baishakhee.firbasewithrecyclerview.adapter.RecyclerViewDataAdapter;
import com.baishakhee.firbasewithrecyclerview.databinding.ActivityMainBinding;
import com.baishakhee.firbasewithrecyclerview.model.DataModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DatabaseReference mDatabase;
    RecyclerViewDataAdapter recyclerViewDataAdapter;

    private String currentSortCriterion = "default"; // Initially, no sorting

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mDatabase = FirebaseDatabase.getInstance().getReference();


        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        getData();

        binding.search.setQueryHint("Search by name");
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText(newText);

                return false;
            }
        });

    }

    private void getData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerview.setVisibility(View.GONE);
        FirebaseRecyclerOptions<DataModel> options =
                new FirebaseRecyclerOptions.Builder<DataModel>()
                        .setQuery(mDatabase.child("data"), DataModel.class)
                        .build();
        Log.d("Main...", options.toString());
        recyclerViewDataAdapter = new RecyclerViewDataAdapter(options);
        binding.recyclerview.setAdapter(recyclerViewDataAdapter);
        binding.recyclerview.setVisibility(View.VISIBLE);

        binding.progressBar.setVisibility(View.GONE);

    }

    private void searchText(String name) {
        FirebaseRecyclerOptions<DataModel> options = null;
        options = new FirebaseRecyclerOptions.Builder<DataModel>()
                .setQuery(mDatabase.child("data").orderByChild("name").startAt(name).endAt(name + "~"), DataModel.class)
                .build();
        recyclerViewDataAdapter = new RecyclerViewDataAdapter(options);
        recyclerViewDataAdapter.startListening();

        binding.recyclerview.setAdapter(recyclerViewDataAdapter);
    }

    private void updateQuery(String sortCriterion) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerview.setVisibility(View.GONE);

        FirebaseRecyclerOptions<DataModel> options = null;

        if (sortCriterion.equals("default")) {
            options = new FirebaseRecyclerOptions.Builder<DataModel>()
                    .setQuery(mDatabase.child("data"), DataModel.class)
                    .build();
        } else if (sortCriterion.equals("name")) {
            // Sort by name
            options = new FirebaseRecyclerOptions.Builder<DataModel>()
                    .setQuery(mDatabase.child("data").orderByChild("name"), DataModel.class)
                    .build();

        } else if (sortCriterion.equals("age")) {
            // Sort by age
            options = new FirebaseRecyclerOptions.Builder<DataModel>()
                    .setQuery(mDatabase.child("data").orderByChild("age"), DataModel.class)
                    .build();

        } else if (sortCriterion.equals("city")) {
            // Sort by city
            options = new FirebaseRecyclerOptions.Builder<DataModel>()
                    .setQuery(mDatabase.child("data").orderByChild("city"), DataModel.class)
                    .build();


        }

        recyclerViewDataAdapter = new RecyclerViewDataAdapter(options);
        recyclerViewDataAdapter.startListening();

        binding.recyclerview.setAdapter(recyclerViewDataAdapter);
        binding.recyclerview.setVisibility(View.VISIBLE);

        // Hide the ProgressBar when data is loaded
        binding.progressBar.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerViewDataAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerViewDataAdapter.stopListening();
    }

    public void menuDialog(View view) {

        showPopupMenu(view);
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sortByName:
                        currentSortCriterion = "name";
                        updateQuery(currentSortCriterion);
                        return true;
                    case R.id.sortByAge:
                        // Handle item 2 click
                        currentSortCriterion = "age";
                        updateQuery(currentSortCriterion);
                        return true;
                    case R.id.sortByCity:
                        // Handle item 3 click
                        currentSortCriterion = "city";
                        updateQuery(currentSortCriterion);
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Show the popup menu
        popupMenu.show();
    }


    public void addData(View view) {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_add_people);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        EditText edtName = dialog.findViewById(R.id.etName);
        EditText edtAge = dialog.findViewById(R.id.etage);
        EditText edtCity = dialog.findViewById(R.id.etCity);
        ImageView close = dialog.findViewById(R.id.close);

        Button btnAdd = dialog.findViewById(R.id.btn_add);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().toString().trim().isEmpty()) {
                    edtName.setError("Please enter Name");
                    edtName.requestFocus();
                } else if (edtAge.getText().toString().trim().isEmpty()) {
                    edtAge.setError("Please enter Age");
                    edtAge.requestFocus();
                } else if (edtCity.getText().toString().trim().isEmpty()) {
                    edtCity.setError("Please enter City");
                    edtCity.requestFocus();
                } else {
                    dialog.dismiss();
                    insertData(edtName.getText().toString().trim(), edtAge.getText().toString().trim(), edtCity.getText().toString().trim());
                }
            }
        });
        dialog.show();

    }

    private void insertData(String name, String age, String city) {

        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("age", Integer.valueOf(age));
        map.put("city", city);
        mDatabase.child("data").push().setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Data Added Successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                    }
                });

    }


}