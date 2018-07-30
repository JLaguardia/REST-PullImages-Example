package com.prismsoftworks.pullimagesviarestexample.list;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.prismsoftworks.pullimagesviarestexample.ListType;
import com.prismsoftworks.pullimagesviarestexample.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.prismsoftworks.pullimagesviarestexample.models.PullItem;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = ListActivity.class.getSimpleName();
    private static final String JSONRESPONSE_ARRAY_NAME = "pullItem";//response name for array
    private static final String JSONRESPONSE_CONTENT_NAME = "content";//response name for url

    public static final String LIST_TYPE = "extra:listType";
    private RecyclerView recyclerView;
    private ListType listType;
    private PullImagesAdapter adapter;
    public static Bitmap errImg; //fallback image if site times out or throws an exception
    private static List<Item> itemList = null; //cached item com.prismsoftworks.pullimagesviarestexample.list for images
    private int configMode; //vertical or horizontal orientation based on device configuration



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        if(itemList == null) {
            itemList = new ArrayList<>();
        }
        recyclerView = findViewById(R.id.recyclerView);
        listType = (ListType) getIntent().getSerializableExtra(LIST_TYPE);

        configMode = (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT ? LinearLayoutManager.VERTICAL :
                LinearLayoutManager.HORIZONTAL);
        init();
        errImg = BitmapFactory.decodeResource (getResources(), android.R.drawable.ic_delete);
    }

    /**
     * Obligatory initialize method
     */
    private void init(){
        if(itemList.size() == 0){
            try {
                JsonReader jsonReader = new JsonReader(new InputStreamReader(getAssets().open(
                        "response.json"), "UTF-8"));
                while (jsonReader.hasNext() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    parseObj(jsonReader, itemList);
                }
                jsonReader.close();
            } catch (IOException e) {
                Log.v(TAG, e.getMessage());
            }
        }

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new PullImagesAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, configMode, false));
        adapter.setPullItems(itemList, listType);
        recyclerView.setAdapter(adapter);
    }

    /**
     * When the back button is pressed, it is important to stop all async tasks (given this device,
     * only 1 async task may run at a time, so this method is probably redundant.
     */
    @Override
    public void onBackPressed() {
//        for(AsyncTask task : PullImagesAdapter.threadList){
//            if(task.getStatus() != AsyncTask.Status.FINISHED){
//                task.cancel(true);
//            }
//        }
//
//        PullImagesAdapter.threadList.clear();
        itemList.clear();
        super.onBackPressed();
    }

    /**
     *
     * Method to parse response.json. This can be optimized I think, maybe a recursion would be
     * better...
     * @param json JsonReader being parsed
     * @param list the com.prismsoftworks.pullimagesviarestexample.list to populate
     * @throws IOException
     */
    private void parseObj(JsonReader json, final List<Item> list) throws IOException {
        json.beginObject();
        while(json.hasNext()){
            switch(json.peek()){
                case NAME:
                    String name = json.nextName();
                    if(name.equals(JSONRESPONSE_ARRAY_NAME)){
                        json.beginArray();
                        json.beginObject();
                    } else if(name.equals(JSONRESPONSE_CONTENT_NAME)){
                        list.add(new PullItem(json.nextString()));
                    } else{
                        json.skipValue();
                    }

                    break;
                case BEGIN_OBJECT:
                    json.beginObject();
                    break;
                case END_OBJECT:
                    json.endObject();
                    break;
                case BEGIN_ARRAY:
                    json.beginArray();
                    break;
                case END_ARRAY:
                    json.endArray();
                    break;
            }
        }
        json.endObject();
    }
}
