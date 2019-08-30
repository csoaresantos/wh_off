package com.nsk.nsk_wh_off;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.nsk.nsk_wh_off.model.InventoryItem;
import com.nsk.nsk_wh_off.repository.DatabaseHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextInputEditText nameItem;
    TextInputEditText quantityItem;
    ListView listView;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nameItem = findViewById(R.id.txt_name_item);
        quantityItem = findViewById(R.id.txt_quantity_item);
        listView = findViewById(R.id.list);
        mContext = this;

        final DatabaseHandler db = new DatabaseHandler(this);

        final List<InventoryItem> inventoryItems = db.getInventoryItems();
        final List<String> nameItems = new ArrayList<>();

        for (InventoryItem item : inventoryItems) {
            nameItems.add("ID: " + item.get_name() + " NAME: " + item.get_id() + " QUANTITY: " + item.get_quantity());
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, nameItems);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != nameItem && null != quantityItem) {
                    String name = nameItem.getText().toString();
                    int quantity = Integer.parseInt(quantityItem.getText().toString());

                    db.addItem(new InventoryItem(name, quantity));
                    Snackbar.make(view, "Replace with your own action name" + nameItem.getText().toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        FloatingActionButton fabgf = findViewById(R.id.fab_generate_file);
        fabgf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskRunner myTask = new AsyncTaskRunner();
                myTask.execute(db);
            }
        });


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                try {
                    db.deleteInventoryItem(inventoryItems.get(position));
                    nameItems.remove(position);
                    adapter.notifyDataSetChanged();
                    Snackbar.make(view, "Replace with your own action name" + nameItem.getText().toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } catch (Exception e) {
                    Snackbar.make(view, "Falha ao deletar item", Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class AsyncTaskRunner extends AsyncTask<DatabaseHandler, String, Boolean> {

        ProgressDialog progressDialog;
        private String resp;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(DatabaseHandler... params) {
            //publishProgress("Sleeping..."); // Calls onProgressUpdate()
            Boolean result = false;
            try {
                final DatabaseHandler db = params[0];

                final List<InventoryItem> inventoryItems = db.getInventoryItems();
                final List<String> nameItems = new ArrayList<>();
                StringBuilder stringBuilder = new StringBuilder();

                for (InventoryItem item : inventoryItems) {
                    nameItems.add("ID: " + item.get_name() + " NAME: " + item.get_id() + " QUANTITY: " + item.get_quantity());
                    stringBuilder.append(item.get_name() + ";" + item.get_id() + ";" + item.get_quantity()+ ";\n\r");
                }

                //File path = mContext.getFilesDir();
                //File file = new File(path, "physical_account.csv");
                //FileOutputStream fOut = new FileOutputStream(file);
                //fOut.write(stringBuilder.toString().getBytes());
                //fOut.close();
/*
                File file = new File(mContext.getFilesDir(),"nskwhoff");
                if(!file.exists()){
                    file.mkdir();
                }

                File gpxfile = new File(file, "physical_account.csv");
                FileWriter writer = new FileWriter(gpxfile);
                writer.write(stringBuilder.toString());
                writer.flush();
                writer.close();
*/
/*
                File mdir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                File file = new File(mdir , "physical_account.txt");

                FileOutputStream fileOutputStream = new FileOutputStream(file);

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                bufferedWriter.write(stringBuilder.toString());

                bufferedWriter.flush();
                bufferedWriter.close();

                // Get /storage/emulated/0/Music folder.
                File musicPublicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

                // Create and return folder /storage/emulated/0/Android/data/com.dev2qa.example/files/Custom
                File customPrivateDir = mContext.getExternalFilesDir("Custom");
*/


// Save email_public.txt file to /storage/emulated/0/DCIM folder
                String publicDcimDirPath = ExternalStorageUtil.getPublicExternalStorageBaseDir(Environment.DIRECTORY_DCIM);

                File newFile = new File(publicDcimDirPath, "physical_count.csv");

                FileWriter fw = new FileWriter(newFile);

                fw.write(stringBuilder.toString());

                fw.flush();

                fw.close();


                result = true;
            } catch (Exception ex) {
                String teste = ex.getMessage();
                String teste2 = teste;
            }
            return result;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            // execution of result of Long time consuming operation
            //progressDialog.dismiss();
            //finalResult.setText(result);
        }


        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);

        }
    }
}
