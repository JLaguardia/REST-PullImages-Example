package com.prismsoftworks.pullimagesviarestexample.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prismsoftworks.pullimagesviarestexample.ListType;
import com.prismsoftworks.pullimagesviarestexample.R;

import com.prismsoftworks.pullimagesviarestexample.models.PullItem;

public class PullImageViewHolder extends RecyclerView.ViewHolder {
    private TextView mTxtContent;
    private ImageView mImgContent;
    private ProgressBar mProgBar;
    private PullItem mPullItem;
    private ListType mListType;

    public PullImageViewHolder(View itemView, ListType listType) {
        super(itemView);
        mTxtContent = itemView.findViewById(R.id.textView);
        mImgContent = itemView.findViewById(R.id.imageView);
        mProgBar = itemView.findViewById(R.id.progressBar);
        mListType = listType;
    }

    public void bind(PullItem item){
        mPullItem = item;
        mTxtContent.setVisibility(mListType != ListType.IMAGE ? View.VISIBLE : View.GONE);
        mProgBar.setVisibility(mPullItem.getImageBmp() == null && mListType != ListType.TEXT ? View.VISIBLE : View.GONE);
        mImgContent.setVisibility(mProgBar.getVisibility() == View.GONE && mListType != ListType.TEXT ? View.VISIBLE : View.GONE);
        if(mListType != ListType.IMAGE)
            mTxtContent.setText(mPullItem.getContent());
        if(mListType != ListType.TEXT)
            mImgContent.setImageBitmap(mPullItem.getImageBmp());
    }
}
