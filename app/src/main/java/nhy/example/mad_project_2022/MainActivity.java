package nhy.example.mad_project_2022;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private ListView listView;
    SimpleAdapter adapter;
    static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
        }



    listView = (ListView) findViewById(R.id.listview);
        JsonHandlerThread jsonHandlerThread = new JsonHandlerThread();
        jsonHandlerThread.start();
        try {
            jsonHandlerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter = new SimpleAdapter(
                this,
                SchoolInfo.schoolList,
                R.layout.list_view_layout,
                new String[] { SchoolInfo.NAME,SchoolInfo.DISTRICT, SchoolInfo.LEVEL, SchoolInfo.GENDER },
                new int[] { R.id.name,R.id.district ,R.id.level, R.id.gender }
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        HashMap<String, String> contact = SchoolInfo.schoolList.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(contact.get(SchoolInfo.NAME));
                        builder.setMessage("Mobile: "+ contact.get(SchoolInfo.MOBILE) + "\nAddress: " + contact.get(SchoolInfo.ADDRESS));
                        builder.setPositiveButton("Back",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }});
                        builder.setNegativeButton("Search",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                showMap(contact.get(SchoolInfo.NAME));
                        }});
                        builder.setNegativeButtonIcon(getDrawable(R.drawable.ic_baseline_map_24));





                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
        );

    }

    public void showMap(String address) {
        String geo = "http://maps.google.com/maps?q=" + address + ", Hong Kong";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geo));
        startActivity(intent);
    }


    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    String msg = "Please run the app again and grant the required permission.";
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.search_menu, menu);

            MenuItem searchMenuItem = menu.findItem(R.id.app_bar_search);
            SearchView searchView = (SearchView) searchMenuItem.getActionView();
;
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if(SchoolInfo.schoolList.contains(query)){
                        adapter.getFilter().filter(query);
                    }else{
                        Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                    }
                    return false;

                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
            return super.onCreateOptionsMenu(menu);
        }
}

