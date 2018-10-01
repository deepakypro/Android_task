package com.losers.android_task.Fragment;


import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.losers.android_task.Network.Model.UpdateImageLink;
import com.losers.android_task.Network.Model.UpdateImageLink.UpdateImageLinkInterface;
import com.losers.android_task.R;
import com.losers.android_task.Network.Model.UploadImages;
import com.losers.android_task.Network.Model.UploadImages.ImageUploadListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFragment extends Fragment implements ImageUploadListener,
    UpdateImageLinkInterface {
  private static final int PERMISSION_REQUEST_CODE = 200;
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  @BindView(R.id.upload_image_rtl)
  RelativeLayout mUploadImageRtl;
  Unbinder unbinder;

  UpdateImageLink mUpdateLink;
  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;
  private UploadImages mUploadImages;

  private ProgressDialog mProgressDialog;

  public UploadFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment UploadFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static UploadFragment newInstance(String param1, String param2) {
    UploadFragment fragment = new UploadFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_upload, container, false);
    unbinder = ButterKnife.bind(this, view);
    mUploadImages = new UploadImages(this);
    mProgressDialog = new ProgressDialog(getContext());
    mUpdateLink = new UpdateImageLink(this);

    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }


  @OnClick({R.id.upload_image_imgbtn, R.id.upload_image_btn, R.id.upload_image_rtl})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.upload_image_imgbtn:
        break;
      case R.id.upload_image_btn:

        if (!checkPermission()) {
          requestPermission();
        } else {
          CropImage.activity()
              .setOutputCompressQuality(60)
              .setGuidelines(CropImageView.Guidelines.ON)
              .start(getContext(), this);
        }
        break;
      case R.id.upload_image_rtl:
        break;
    }
  }


  public boolean checkPermission() {
    int result = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);

    return result == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermission() {

    ActivityCompat
        .requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE},
            PERMISSION_REQUEST_CODE);

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[],
      int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_REQUEST_CODE:
        if (grantResults.length > 0) {

          boolean readexternalstorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;


          if (readexternalstorage) {

            CropImage.activity()
                .setOutputCompressQuality(60)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(), this);
          }
          // Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
          else {

            //Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                showMessageOKCancel("We suggest you to connect your gallery with the app.This will help in setting up your profile and asking doubts as well.",
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                          requestPermissions(
                              new String[]{WRITE_EXTERNAL_STORAGE},
                              PERMISSION_REQUEST_CODE);
                        }
                      }
                    });
                return;
              }
            }

          }
        }

        break;
    }
  }

  private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(getContext())
        .setMessage(Html.fromHtml("<font color='#000000'> " + message + "</font>"))
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .create()
        .show();
  }

  public void showProgressBar() {

    mProgressDialog.setTitle(Html.fromHtml("<font color='#000000'> Please wait</font>"));
    mProgressDialog.setMessage("Updating response to the server..");

    mProgressDialog.setCancelable(false);
    mProgressDialog.setIndeterminate(true);
    mProgressDialog.show();
  }

  public void hideProgressBar() {

    if (mProgressDialog.isShowing()) {
      mProgressDialog.dismiss();
    }

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);

      if (resultCode == RESULT_OK) {
        Uri resultUri = result.getUri();
        showProgressBar();
        mUploadImages.uploadFileToFireBase(resultUri);

        try {
          Bitmap bitmap = MediaStore.Images.Media
              .getBitmap(getActivity().getContentResolver(), resultUri);
        } catch (IOException e) {
          e.printStackTrace();
        }
//

      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        Exception error = result.getError();
        error.getStackTrace();
      }
    }
  }


  @Override
  public void onSucess(String downloadUri) {
    mUpdateLink.UpdateLink(downloadUri);
  }

  @Override
  public void onFailure() {
    hideProgressBar();

  }

  @Override
  public void onLinkSuccess() {
    hideProgressBar();
  }

  @Override
  public void onLinkFailure(Exception e) {
    hideProgressBar();
  }
}
