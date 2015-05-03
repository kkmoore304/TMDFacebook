package com.tellmedoctor.tmdfacebook.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.tellmedoctor.tmdfacebook.R;
import com.tellmedoctor.tmdfacebook.model.questionItem;
import com.tellmedoctor.tmdfacebook.ui.activities.MainActivity;
import com.tellmedoctor.tmdfacebook.utils.ImageUtils;
import com.tellmedoctor.tmdfacebook.utils.PrefsUtils;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.facebook.messenger.MessengerUtils;
import com.facebook.messenger.MessengerThreadParams;
import com.facebook.messenger.ShareToMessengerParams;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AskAQuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AskAQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AskAQuestionFragment extends Fragment {

    protected static final int ACTION_REQUEST_GALLERY = 100;
    protected static final int ACTION_REQUEST_CAMERA = 200;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE_SHARE_TO_MESSENGER = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View rootView;
    private ImageButton fab_newquestion;
    private Activity _context;
    private EditText newquestion;
    private Uri outputFileUri;
    private String path;
    private ImageView questionImage;
    private Uri tempUri;
    private questionItem newone;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AskAQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AskAQuestionFragment newInstance(String param1, String param2) {
        AskAQuestionFragment fragment = new AskAQuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AskAQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        _context = getActivity();
        FacebookSdk.sdkInitialize(_context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ask_aquestion, container, false);
        fab_newquestion = (ImageButton) rootView.findViewById(R.id.fab_newquestion);

        newquestion = (EditText) rootView.findViewById(R.id.newquestion);
        fab_newquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<questionItem> quest = PrefsUtils.getQuestions(_context);
                if (newone == null)
                    newone = new questionItem("2", newquestion.getText().toString(), "3");
                if (quest == null)
                    quest = new ArrayList<questionItem>();
                //newone.setImageUrl(tempUri.getPath().toString());//set the image
                newone.setContentTitle(newquestion.getText().toString());
                if (newquestion.getText().toString().length() > 5)
                    newone.setPost_txt("Oncology");
                else
                    newone.setPost_txt("Orthopedics");
                quest.add(newone);
            /*    String mimeType = "image/jpeg";

                // contentUri points to the content being shared to Messenger
                ShareToMessengerParams shareToMessengerParams =
                        ShareToMessengerParams.newBuilder(tempUri, mimeType)
                                .build();

                // Sharing from an Activity
                MessengerUtils.shareToMessenger(
                        _context,
                        REQUEST_CODE_SHARE_TO_MESSENGER,
                        shareToMessengerParams);
*/

                if (quest != null) {

                    Gson gson = new Gson();
                    String json = gson.toJson(quest);
                    PrefsUtils.setQuestions(_context, json);

                    Intent returnIntent = new Intent(getActivity(), MainActivity.class);
                    returnIntent.putExtra("fragment", "Question");
                    //getActivity().setResult(getActivity().RESULT_OK, returnIntent);
                    startActivity(returnIntent);
                    getActivity().finish();

                }
            }
        });
        Button btnaddimage = (Button) rootView.findViewById(R.id.btnaddimage);

        btnaddimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challengeImageGallery();
            }
        });
        questionImage = (ImageView) rootView.findViewById(R.id.questionImage);


        return rootView;
    }


    /**
     * Check for images or photo in the phones gallery
     */
    protected void challengeImageGallery() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Your Image From...");
        builder.setItems(
                new CharSequence[]{"Image Gallery", "Phone Camera"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        switch (which) {
                            case 0:

                                // To open up a gallery browser
                                intent = new Intent();

                                // intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);

                                intent.setType("image/*");

                                // intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                                intent.setAction(Intent.ACTION_GET_CONTENT);

                                // intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                                startActivityForResult(Intent.createChooser(intent,
                                                "Select Your Emergency Contact's Picture"),
                                        ACTION_REQUEST_GALLERY);

                                break;

                            case 1:
                                // Guarantee unique
                                File file = new File(Environment
                                        .getExternalStorageDirectory(), "inscard_"
                                        + ImageUtils.random(5) + ".jpg");

                                outputFileUri = Uri.fromFile(file);

                                // Generate the intent
                                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        outputFileUri);

                                // Launch the camera app.
                                startActivityForResult(intent,
                                        ACTION_REQUEST_CAMERA);

                                break;

                            default:
                                break;
                        }
                    }
                });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            // we requested the camera option
            if (requestCode == ACTION_REQUEST_CAMERA) {
                // check if the results includes a thumb nail
                if (data != null) {
                    if (data.hasExtra("data")) {


                        Bitmap thumbnail = data.getParcelableExtra("data");

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = ImageUtils.getImageUri(_context,
                                thumbnail);

                        path = ImageUtils.getRealPathFromURI(_context, tempUri,
                                MediaStore.Images.ImageColumns.DATA);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        // File finalFile = new File(path);
                        questionImage.setImageBitmap(thumbnail);
                    }
                } else {
                    // if there is no thumb nail image data, the image
                    // will have been stored in the target folder output URI.
                    path = outputFileUri.getPath();
                    Bitmap bm = null;
                    try {
                        bm = MediaStore.Images.Media.getBitmap(
                                _context.getContentResolver(), outputFileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    questionImage.setImageBitmap(bm);
                }
                if (newone == null)
                    newone = new questionItem();
                newone.setImageUrl(path);
            }

            // we requested image from the gallery
            if (requestCode == ACTION_REQUEST_GALLERY) {
                String selectedImagePath;
                Uri selectedImageUri = data.getData();
                Bitmap lbitmap = null;
                if (Build.VERSION.SDK_INT < 19) {
                    selectedImagePath = selectedImageUri.getPath();
                    lbitmap = BitmapFactory.decodeFile(selectedImagePath);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = ImageUtils.getImageUri(_context, lbitmap);

                    path = ImageUtils.getRealPathFromURI(_context, tempUri,
                            MediaStore.Images.ImageColumns.DATA);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    // File finalFile = new File(path);

                } else {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {

                        parcelFileDescriptor = _context.getContentResolver()
                                .openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor
                                .getFileDescriptor();
                        lbitmap = BitmapFactory
                                .decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();

                        // InnerProfileBtn.setImageBitmap(image);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // set the image to the bitmap
                    questionImage.setImageBitmap(lbitmap);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    // Uri
                    tempUri = ImageUtils.getImageUri(_context, lbitmap);

                    path = ImageUtils.getRealPathFromURI(_context, tempUri,
                            MediaStore.Images.ImageColumns.DATA);

                }
            }
            if (newone == null)
                newone = new questionItem();
            newone.setImageUrl(path);

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
