package com.example.avivmoyal.minsta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avivmoyal.minsta.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private ArrayList<Post> mDataset;
    private FeedActivity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextuser;
        public TextView mTextView;
        public ImageView mImageView;
        public Button mLikeButton;

        public ViewHolder(View v) {
            super(v);
            mTextuser = v.findViewById(R.id.textView2);
            mTextView = v.findViewById(R.id.textView);
            mImageView = v.findViewById(R.id.imageView);
            mLikeButton = v.findViewById(R.id.likeButton);
        }
    }

    public ImageAdapter(ArrayList<Post> myDataset, FeedActivity activity) {
        mDataset = myDataset;
        mActivity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Post post = (Post) mDataset.get(position);
        if (post.user != null) {
            holder.mTextuser.setText(post.user.displayName);
        }
        if (post.text!= null) {
            holder.mTextView.setText(post.text);
        }
        Picasso.get().load(post.downloadUrl).into(holder.mImageView);

        holder.mLikeButton.setText("Like (" + post.likes + ")");
        if(post.hasLiked) {
            holder.mLikeButton.setBackgroundColor(mActivity.getResources().getColor(R.color.colorAccent));
        } else {
            holder.mLikeButton.setBackgroundColor(mActivity.getResources().getColor(R.color.colorPrimary));
        }
        holder.mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setLiked(post);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addPost(Post post) {
        mDataset.add(0, post);
        notifyDataSetChanged();
    }
}