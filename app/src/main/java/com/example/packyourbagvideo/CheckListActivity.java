package com.example.packyourbagvideo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.packyourbagvideo.Adapter.CheckAdapter;
import com.example.packyourbagvideo.Data.AppData;
import com.example.packyourbagvideo.Database.RoomDB;
import com.example.packyourbagvideo.Models.Items;
import com.example.packyourbagvideo.constants.MConstants;

import java.util.ArrayList;
import java.util.List;

public class CheckListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CheckAdapter checkAdapter;
    RoomDB database;
    List<Items> itemsList = new ArrayList<>();

    String header, show;
    EditText txtAdd;
    Button btnAdd;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        header = intent.getStringExtra(MConstants.HEADER_SMALL);
        show = intent.getStringExtra(MConstants.SHOW_SMALL);
        getSupportActionBar().setTitle(header);

        txtAdd = findViewById(R.id.txtAdd);
        btnAdd = findViewById(R.id.btnAdd);

        linearLayout = findViewById(R.id.liniearLayout);
        recyclerView = findViewById(R.id.recyclerView);

        database = RoomDB.getInstance(this);

        if (MConstants.FALSE_STRING.equals(show)) {
            linearLayout.setVisibility(View.GONE);
            itemsList = database.mainDao().getALlSelected(true);
        } else {
            itemsList = database.mainDao().getAll(header);
        }
        updateRecycler(itemsList);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = txtAdd.getText().toString();
                if (itemName != null && !itemName.isEmpty()) {
                    addNewItem(itemName);

                    Toast.makeText(CheckListActivity.this, "Item is Added", Toast.LENGTH_SHORT).show();
                    txtAdd.setText("");
                } else {
                    Toast.makeText(CheckListActivity.this, "Empty cannot be added..", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_one, menu);

        if (MConstants.MY_SELECTIONS.equals(header)) {
            menu.getItem(0).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        } else if (MConstants.MY_LIST_CAMEL_CASE.equals(header)) {
            menu.getItem(1).setVisible(false);
        }

        MenuItem menuItem = menu.findItem(R.id.btnSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Items> mFinalList = new ArrayList<>();
                for (Items items : itemsList) {
                    if (items.getItemname().toLowerCase().startsWith(newText.toLowerCase())) {
                        mFinalList.add(items);
                    }
                }
                updateRecycler(mFinalList);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, CheckListActivity.class);
        AppData appData = new AppData(database, this);

        if (item.getItemId() == R.id.btnMySelections) {
            intent.putExtra(MConstants.HEADER_SMALL, MConstants.MY_SELECTIONS);
            intent.putExtra(MConstants.SHOW_SMALL, MConstants.FALSE_STRING);
            startActivityForResult(intent, 101);
            return true;
        } else if (item.getItemId() == R.id.btnCustomList) {
            intent.putExtra(MConstants.HEADER_SMALL, MConstants.MY_LIST_CAMEL_CASE);
            intent.putExtra(MConstants.SHOW_SMALL, MConstants.TRUE_STRING);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.btnDeleteDefault) {

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Delete default data")
                    .setMessage("Are you sure ? \n\nAs this will delete the data provided by (Pack your bag ) while installing.")
                    .setPositiveButton("Confirme", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            appData.persistDataByCategory(header, false);
                            itemsList = database.mainDao().getAll(header);
                            updateRecycler(itemsList);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(R.drawable.baseline_warning_24)
                    .show();
            return true;
        } else if (item.getItemId() == R.id.btnReset) {

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Reset to default")
                    .setMessage("Are you sure ? \n \nAs this will load the default data provided by (Pack your bag)" +
                            "and will delete the custom data you have added in ()" + header)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            appData.persistDataByCategory(header, true);
                            itemsList = database.mainDao().getAll(header);
                            updateRecycler(itemsList);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    })
                    .setIcon(R.drawable.baseline_warning_24)
                    .show();
            return true;
        } else if (item.getItemId() == R.id.btnAboutUs) {
            intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.btnExit) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//                          Intent intent = new Intent(CheckListActivity.this, MainActivity.class);
//                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                          intent.putExtra("Exit", true);
//                          startActivity(intent);
//                          finish();
                            CheckListActivity.this.finishAffinity();
                            Toast.makeText(CheckListActivity.this, "Pack your bag \nExit completed ", Toast.LENGTH_SHORT).show();


                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(R.drawable.baseline_exit_to_app_24)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101)
        {
            itemsList=database.mainDao().getAll(header);
            updateRecycler(itemsList);
        }
    }

    private void addNewItem(String itemName) {
        Items items = new Items();
        items.setChecked(false);
        items.setCategory(header);
        items.setItemname(itemName);
        items.setAddedby(MConstants.USER_SMALL);
        database.mainDao().saveItem(items);
        itemsList = database.mainDao().getAll(header);
        updateRecycler(itemsList);
        recyclerView.scrollToPosition(checkAdapter.getItemCount() - 1);

    }

    private void updateRecycler(List<Items> itemsList) {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        checkAdapter = new CheckAdapter(CheckListActivity.this, itemsList, database, show);
        recyclerView.setAdapter(checkAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}