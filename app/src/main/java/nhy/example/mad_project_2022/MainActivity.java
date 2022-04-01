package nhy.example.mad_project_2022;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        listView = (ListView) findViewById(R.id.listview);
        JsonHandlerThread jsonHandlerThread = new JsonHandlerThread();
        jsonHandlerThread.start();
        try {
            jsonHandlerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                SchoolInfo.schoolList,
                R.layout.list_view_layout,
                new String[] { SchoolInfo.NAME, SchoolInfo.DISTRICT, SchoolInfo.ADDRESS },
                new int[] { R.id.name, R.id.district, R.id.address }
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, String> contact = SchoolInfo.schoolList.get(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(contact.get(SchoolInfo.NAME));
                        builder.setMessage("Website: " + contact.get(SchoolInfo.WEBSITE));
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
        );

    }
}