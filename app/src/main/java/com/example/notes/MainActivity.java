package com.example.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    static ArrayList<String> notes;
    static ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.list1);
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
        HashSet<String> set=(HashSet<String>)sharedPreferences.getStringSet("notes",null);
        if (set==null||set.isEmpty()) {
            notes = new ArrayList<>();
            notes.add("Example Note");
        }else {
            notes = new ArrayList<>(set);
        }

        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("noteId",position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this).
                        setIcon(android.R.drawable.ic_dialog_alert).
                        setMessage("Do you want to delete this note?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                adapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                                HashSet<String> set =new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes",set).apply();
                            }
                        }).setNegativeButton("No",null).show();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.item1){
            startActivity(new Intent(getApplicationContext(),Main2Activity.class));
        }
        return true;
    }
}
