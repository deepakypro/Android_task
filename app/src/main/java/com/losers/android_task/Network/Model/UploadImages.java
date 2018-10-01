package com.losers.android_task.Network.Model;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.UploadTask.TaskSnapshot;

public class UploadImages {

  private ImageUploadListener mImageUploadListener;

  public UploadImages(ImageUploadListener mImageUploadListener) {
    this.mImageUploadListener = mImageUploadListener;
  }

  public void uploadFileToFireBase(Uri path) {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    final StorageReference storageRef = storage.getReference();
//    Uri file = Uri.fromFile(new File(path));
    final StorageReference riversRef = storageRef.child("images/" + path.getLastPathSegment());
    riversRef.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
      @Override
      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
          @Override
          public void onSuccess(Uri uri) {
            Log.d(TAG, "onSuccess: uri= " + uri.toString());
            if (mImageUploadListener != null) {
              mImageUploadListener.onSucess(uri.toString());
            }
          }
        });
      }
    });

//// Register observers to listen for when the download is done or if it fails
//    uploadTask.addOnFailureListener(new OnFailureListener() {
//      @Override
//      public void onFailure(@NonNull Exception exception) {
//        // Handle unsuccessful uploads
//
//        if (mImageUploadListener != null) {
////          mImageUploadListener.onFailure(exception);
//        }
//      }
//    }).addOnSuccessListener(new OnSuccessListener<TaskSnapshot>() {
//      @Override
//      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//        // ...
////        taskSnapshot.getMetadata().ge
//        if (mImageUploadListener != null) {
////          mImageUploadListener.onSucess(taskSnapshot.getMetadata().getPath());
//        }
//
//      }
//    });
//
//
//    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<TaskSnapshot, Task<Uri>>() {
//      @Override
//      public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//        if (!task.isSuccessful()) {
//          throw task.getException();
//        }
//
//        // Continue with the task to get the download URL
//        return storageRef.getDownloadUrl();
//      }
//    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//      @Override
//      public void onComplete(@NonNull Task<Uri> task) {
//        if (task.isSuccessful()) {
//          Uri downloadUri = task.getResult();
//          if (mImageUploadListener != null) {
//            mImageUploadListener.onSucess(downloadUri);
//          }
//        } else {
//          if (mImageUploadListener != null) {
//            mImageUploadListener.onFailure();
//          }
//        }
//      }
//    });

  }

  public interface ImageUploadListener {

    void onSucess(String downloadUri);

    void onFailure();
  }
}
