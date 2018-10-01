package com.losers.android_task.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import com.losers.android_task.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MiscUtils {

  public static void setImageToPicasso(ImageView imageToPicasso, String id, Context context) {
    Log.d("TAF",id+"");
    Picasso.with(context)
        .load(id)
        .into(imageToPicasso);

  }
}
