package com.losers.android_task.Fragment;

import static com.losers.android_task.Utils.Constants.FIREBASE_USER_DATA_INSTANCE;
import static com.losers.android_task.Utils.Constants.IMAGE;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.losers.android_task.Adapter.ImageListAdapter;
import com.losers.android_task.Adapter.ImageListAdapter.RecyclerViewListener;
import com.losers.android_task.Network.Model.ImagesResponse;
import com.losers.android_task.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageListFragment extends Fragment implements RecyclerViewListener {

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";
  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;
  Unbinder unbinder;

  private ProgressDialog mProgressDialog;
  private List<ImagesResponse> mImagesResponses = new ArrayList<>();


  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public ImageListFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment ImageListFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static ImageListFragment newInstance(String param1, String param2) {
    ImageListFragment fragment = new ImageListFragment();
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
    View view = inflater.inflate(R.layout.fragment_image_list, container, false);
    unbinder = ButterKnife.bind(this, view);
    mProgressDialog = new ProgressDialog(getContext());
    LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(),
        LinearLayoutManager.VERTICAL, false);

    mRecyclerView.setLayoutManager(horizontalLayoutManagaer);
    mRecyclerView.setHasFixedSize(true);
    getDataFromFirebase();
    return view;
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
//      throw new RuntimeException(context.toString()
//          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void getDataFromFirebase() {

    showProgressBar();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference docRef = db.collection(FIREBASE_USER_DATA_INSTANCE);
    //Query mQuery = docRef.ord
    docRef.get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {

            if (task.isSuccessful()) {
              HashMap<String, Object> mHashMap = new LinkedHashMap<>();
              for (DocumentSnapshot document : task.getResult()) {

                mHashMap.put(document.getId(), document.getData());
              }
              solveHashMap(mHashMap);
            } else {
              hideProgressBar();
              // mLeadershipProgressBar.setVisibility(View.INVISIBLE);
              //getDataFromRealm();
              Log.d("TFG", "Error getting documents: ", task.getException());
            }
          }
        });

  }

  private void solveHashMap(HashMap<String, Object> mHashMap) {

    for (Entry<String, Object> entry : mHashMap.entrySet()) {
      Map<String, Object> map = (Map<String, Object>) entry.getValue();
      ImagesResponse mImagesResponse = new ImagesResponse();

      mImagesResponse.setImage((String) map.get(IMAGE));
      mImagesResponse.setId("12");
      mImagesResponses.add(mImagesResponse);
    }

    setAdapter();
  }


  private void setAdapter() {

    hideProgressBar();
    if (mImagesResponses.size() > 0 && !mImagesResponses.isEmpty()) {
      ImageListAdapter mHorizontalAdapter = new ImageListAdapter(getContext(), mImagesResponses,this);
      mRecyclerView.setAdapter(mHorizontalAdapter);

    }
  }

  @Override
  public void onButtonClick(String id) {

  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {

    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }

  public void showProgressBar() {

    mProgressDialog.setTitle(Html.fromHtml("<font color='#000000'> Please wait</font>"));
    mProgressDialog.setMessage("Loading data..");

    mProgressDialog.setCancelable(false);
    mProgressDialog.setIndeterminate(true);
    mProgressDialog.show();
  }

  public void hideProgressBar() {

    if (mProgressDialog.isShowing()) {
      mProgressDialog.dismiss();
    }

  }
}

