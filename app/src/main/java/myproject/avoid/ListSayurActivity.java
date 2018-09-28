package myproject.avoid;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import Adapter.RecycleAdapterListSayur;

public class ListSayurActivity extends AppCompatActivity {

    FloatingActionButton btnCreate;
    Intent i;
    RecyclerView recycler_listSayur;
    public static ProgressBar progressBar;
    TextView txtInfo;
    RecycleAdapterListSayur adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sayur);
        btnCreate = (FloatingActionButton) findViewById(R.id.btnCreate);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtInfo = (TextView) findViewById(R.id.txtInfo);

        recycler_listSayur = (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapter = new RecycleAdapterListSayur(this);
        recycler_listSayur.setAdapter(adapter);
        recycler_listSayur.setLayoutManager(new LinearLayoutManager(this));

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TambahSayurActivity.class);
                startActivity(i);
            }
        });
    }
}
