package com.losers.android_task.Network.Model;

import static com.losers.android_task.Utils.Constants.FIREBASE_USER_DATA_INSTANCE;
import static com.losers.android_task.Utils.Constants.ID;
import static com.losers.android_task.Utils.Constants.IMAGE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class UpdateImageLink {

  private UpdateImageLinkInterface mUpdateImageLinkInterface;

  public UpdateImageLink(
      UpdateImageLinkInterface updateImageLinkInterface) {
    mUpdateImageLinkInterface = updateImageLinkInterface;
  }

  public void UpdateLink(final String imageLink) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Map<String, Object> data = new HashMap<>();
    data.put(IMAGE, imageLink);
    data.put(ID, "12");

    db.collection(FIREBASE_USER_DATA_INSTANCE)
        .add(data)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
          @Override
          public void onSuccess(DocumentReference documentReference) {
            if (mUpdateImageLinkInterface != null) {
              mUpdateImageLinkInterface.onLinkSuccess();
            }
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            e.getStackTrace();
            if (mUpdateImageLinkInterface != null) {
              mUpdateImageLinkInterface.onLinkFailure(e);
            }

          }
        });
  }

  public interface UpdateImageLinkInterface {

    void onLinkSuccess();

    void onLinkFailure(Exception e);

  }

}
