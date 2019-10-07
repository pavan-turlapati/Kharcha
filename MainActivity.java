package com.nocomp.kharcha;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public int PERMISSION_STATUS=1;
    EditText editText;
    Button button;
    TextView textView;
    View view;
    ArrayAdapter <String> arrayAdapter;
    ArrayList<String> content = new ArrayList<>();
    ListView listView;
    SQLiteDatabase database;
    String toInsert="",x="";
    String msg;
    int total;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        database = this.openOrCreateDatabase("Kharche",MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS kharche (msg VARCHAR)");

        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, content);

        listView.setAdapter(arrayAdapter);

        if(item.getItemId() == R.id.del_note) {

            database.execSQL("DELETE FROM kharche");
            content.clear();

            arrayAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {


            /*public void onClick (View view)
            {
                int n1 = Integer.parseInt(editText.getText().toString());

                //Toast.makeText(this, n1+ "", Toast.LENGTH_LONG).show();
                //startActivity(new Intent(MainActivity.this, DusraActivity.class));
                //Bundle extras = getIntent().getExtras();
                Toast.makeText(this, "Ready!", Toast.LENGTH_SHORT).show();

            }*/
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, content);

        listView.setAdapter(arrayAdapter);

        textView = (TextView)findViewById(R.id.textView);

        database = this.openOrCreateDatabase("Kharche",MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS kharche (msg INT)");
        updateListView();

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            msg = extras.getString("Message");
            //textView.setText(msg);
            try {

                if (msg.contains("Paid Rs.")) {
                    String segment[] = msg.split("Paid Rs.");
                    x = segment[segment.length - 1];
                    String y[] = x.split(" ");
                    toInsert = y[0];

                    int i = Integer.parseInt(toInsert);

                    String sql = ("INSERT INTO kharche (msg) VALUES(?)");

                    SQLiteStatement statement = database.compileStatement(sql);
                    statement.bindLong(1, i);

                    statement.execute();

                    updateListView();
                }

            }catch(NullPointerException e){
                e.printStackTrace();
                Toast.makeText(this, "Welcome to Kharcha!", Toast.LENGTH_SHORT).show();
            }


        }


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},1);
        }

    }
    public void updateListView()
    {
        Cursor c = database.rawQuery("SELECT * FROM kharche",null);

        int contentIndex = c.getColumnIndex("msg");
       // c.moveToFirst();
        if(c.moveToFirst()) {
            content.clear();

            do {
                content.add(c.getString(contentIndex));
            } while (c.moveToNext());

            Cursor cursor = database.rawQuery("SELECT SUM(msg) as Total FROM kharche", null);

            if (cursor.moveToFirst()) {

                total = cursor.getInt(cursor.getColumnIndex("Total"));
            }// get final total

            textView.setText("Expenditure this week : "+Integer.toString(total));

            arrayAdapter.notifyDataSetChanged();
        }
    }

}
