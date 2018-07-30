package com.prismsoftworks.pullimagesviarestexample.list;

import android.support.v7.widget.RecyclerView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prismsoftworks.pullimagesviarestexample.ListType;
import com.prismsoftworks.pullimagesviarestexample.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import com.prismsoftworks.pullimagesviarestexample.models.PullItem;

public class PullImagesAdapter extends RecyclerView.Adapter<PullImageViewHolder> {
    private static final String TAG = PullImagesAdapter.class.getSimpleName();
    private List<Item> mPullItems;
//    public static List<FetchThread> threadList; //this is public for handling the back button in ListActivity
    private static ListType listType;

    public PullImagesAdapter() {
        this.mPullItems = new ArrayList<>();
//        threadList = new ArrayList<>();
    }

    public void setPullItems(List<Item> pullItems, ListType listTypeIn){
        this.mPullItems.clear();
        this.mPullItems.addAll(pullItems);
//        threadList.clear();
        listType = listTypeIn;
        if(listType != ListType.TEXT){
            for(final Item item : pullItems){
                if(item.getImageBmp() == null) {

                    Observable.just(item.getContent()).map(new Func1<String, Bitmap>() {

                        @Override
                        public Bitmap call(String content) {
                            try {
                                URL url = new URL(content);
                                InputStream streamIn = url.openStream(); //the site is slow, this is what makes it "freeze"

                                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                                byte[] data = new byte[1024];
                                int length;
                                try {
                                    while ((length = streamIn.read(data)) != -1) {
                                        dataStream.write(data, 0, length);
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                                dataStream.flush();
                                return BitmapFactory.decodeByteArray(
                                        dataStream.toByteArray(),0, dataStream.size());

                            } catch (IOException e) {
                                e.printStackTrace();
                                return ListActivity.errImg;
                            }
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Bitmap>() {
                                @Override
                                public void onCompleted() {
                                    Log.i(TAG, "Completed DL ");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, "Error: " + e.getMessage());
                                }

                                @Override
                                public void onNext(Bitmap bitmap) {
                                    ((PullItem)item).setImageBmp(bitmap);
                                    notifyDataSetChanged();
                                }
                            });

//                    threadList.add(new FetchThread((PullItem) item));
                }
            }

//            for(FetchThread task: threadList){
//                task.execute();
//            }
        }

        notifyDataSetChanged();
    }

    @Override
    public PullImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_item, parent,
                false);
        return new PullImageViewHolder(v, listType);
    }

    @Override
    public void onBindViewHolder(PullImageViewHolder holder, int position) {
        holder.bind((PullItem)mPullItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mPullItems.size();
    }

    /**
     * ASynchronous thread to fetch image from the content in pullItems

    private class FetchThread extends AsyncTask<Void, Void, Void> {
        final PullItem item;

        public FetchThread(PullItem item){
            this.item = item;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(item.getContent());
                InputStream streamIn = url.openStream(); //the site is slow, this is what makes it "freeze"

                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                int length;
                try {
                    while ((length = streamIn.read(data)) != -1) {
                        dataStream.write(data, 0, length);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                dataStream.flush();
                item.setImageBmp(BitmapFactory.decodeByteArray(
                        dataStream.toByteArray(),0, dataStream.size()));

            } catch (IOException e) {
                e.printStackTrace();
                item.setImageBmp(ListActivity.errImg);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onpostExecute");
            notifyDataSetChanged();
            threadList.remove(this);
        }
    } */
}
