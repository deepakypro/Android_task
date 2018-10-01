package com.losers.android_task.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.github.chrisbanes.photoview.PhotoView;
import com.losers.android_task.Network.Model.ImagesResponse;
import com.losers.android_task.R;
import com.losers.android_task.Utils.MiscUtils;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

  private List<ImagesResponse> horizontalList;
  private Context context;
  RecyclerViewListener mRecyclerViewListener;

  public class MyViewHolder extends RecyclerView.ViewHolder {

    PhotoView mImageView;

    public MyViewHolder(View view) {
      super(view);
      mImageView = (PhotoView) view.findViewById(R.id.imageview);

    }
  }

  public ImageListAdapter(Context context, List<ImagesResponse> horizontalList,
      RecyclerViewListener mRecyclerViewListener) {
    this.context = context;
    this.horizontalList = horizontalList;
    this.mRecyclerViewListener = mRecyclerViewListener;
  }

  @Override
  public ImageListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater
        .from(parent.getContext()).inflate(R.layout.cardview_image_list, parent, false);
    return new ImageListAdapter.MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(final ImageListAdapter.MyViewHolder holder, final int position) {
    final ImagesResponse detail = horizontalList.get(position);

//    holder.mImageView.setI(detail.getImage());

    MiscUtils mMiscUtils = new MiscUtils();
    mMiscUtils.setImageToPicasso(holder.mImageView, detail.getImage(), context);
//    holder.mImageView.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        if (mRecyclerViewListener != null) {
//
//          mRecyclerViewListener.onButtonClick("");
//        }
//      }
//    });

  }


  public interface RecyclerViewListener {

    void onButtonClick(String id);
  }

  @Override
  public int getItemCount() {
    return horizontalList.size();
  }
}

