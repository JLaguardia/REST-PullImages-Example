package com.prismsoftworks.pullimagesviarestexample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.prismsoftworks.pullimagesviarestexample.list.ListActivity;

public class MainActivity extends AppCompatActivity {
    private Button imageButton;
    private Button textButton;
    private Button bothButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Example getting images by James Laguardia",
                Toast.LENGTH_SHORT).show();
        imageButton = findViewById(R.id.imageButton);
        textButton = findViewById(R.id.textButton);
        bothButton = findViewById(R.id.bothButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListActivity(ListType.IMAGE);
            }
        });

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListActivity(ListType.TEXT);
            }
        });

        bothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToListActivity(ListType.COMPOSITE);

            }
        });
    }

    private void goToListActivity(ListType listType){
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(ListActivity.LIST_TYPE, listType);
        if(listType != ListType.TEXT) {
            Toast.makeText(this, "The images from lorempixel.com might be slow to " +
                    "download...", Toast.LENGTH_SHORT).show();
        }

        startActivity(intent);
    }
}
