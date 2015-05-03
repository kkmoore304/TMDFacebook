package com.tellmedoctor.tmdfacebook.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;

import com.tellmedoctor.tmdfacebook.R;
import com.tellmedoctor.tmdfacebook.model.questionItem;
import com.tellmedoctor.tmdfacebook.utils.LogUtils;
import com.tellmedoctor.tmdfacebook.utils.PrefsUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmoore on 5/2/15.
 */
public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = QuestionAdapter.class.getSimpleName();

    // private static final int QUESTIONFRAGMENT = 0;
    //    RecyclerView.Adapter<HomeAdapter.DataSummaryViewHolder>{
    private final Context _context;
    private List<questionItem> items;
    private View mEmptyView;

    //private final int layout_id;
    private OnQuestionItemClickListener mItemClickListener;
    private View itemView;
    private int position;
    private QuestionViewHolder holder;
    private OnItemSelectedListener mDataRangeItemSelectedListener;
    private ViewGroup parent;
    private int HomScreenLogBookRecordCount;
    private RecyclerView rootView;
    private int lastPosition;
    private boolean rootdis;

    public QuestionAdapter(List<questionItem> questions_list, int fragment_question_item, Context context, View view) {
        this._context = context;

        items = questions_list;
        itemView = view;
    }

   /* public QuestionAdapter(List<questionItem> question_list,
                           Context QuestionFragment) {
        this.items = question_list;
        this._context = QuestionFragment;

    }*/


    /**
     * Save list to the shared preferences
     */
    private void updatedPrefs() {
        if (items != null) {
            Gson gson = new Gson();
            String json = gson.toJson(items);
            PrefsUtils.setQuestions(_context, json);
        }

    }

    public void add(questionItem routineItem, int position) {
        if (items == null)
            items = new ArrayList<questionItem>();

        items.add(position, routineItem);
        updatedPrefs();
        notifyItemInserted(position);

    }

    public void remove(int position) {
        if (items != null) {
            items.remove(position);
            updatedPrefs();
            //   notifyItemRemoved(position);
        }
    }

    //TODO Consider add type t0 db
    /*@Override
    public int getItemViewType(int position) {
        //Implement your logic here
        HomeItem vo = items.get(position);
        return vo.getType();
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        LogUtils.LOGD(TAG, "QuestionAdapter.onCreateViewHolder(" + parent.getId() + ", " + viewType + ")");
        // HomeItem h = items.get(LogbookViewHolder(itemView).getAdapterPosition());
        // switch (viewType) {
        //     case QUESTIONFRAGMENT: // intro
        itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_question_item, parent, false);

        return new QuestionViewHolder(itemView);


        //}
        //return null;
    }


    @Override
    public int getItemViewType(int position) {
        questionItem h = null;
        if (items != null)
            h = items.get(position);


        int viewType = 0;


        return viewType;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        this.position = position;
        questionItem q = items.get(position);



        ((QuestionViewHolder) holder).question.setText(q.getPost_txt());
        ((QuestionViewHolder) holder).specialty.setText(q.getContentTitle());

// Display contact image
        if (q.getImageUrl()!= null) {
            File finalFile = new File(q.getImageUrl());
            Bitmap bm = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
            ((QuestionViewHolder) holder).img
                    .setScaleType(ImageView.ScaleType.CENTER_CROP);
            ((QuestionViewHolder) holder).img.setImageBitmap(Bitmap.createScaledBitmap(bm, 200, 150, false));
            //	.createScaledBitmap(bm, 200, 150, false));
        }

       /* if (emerg.getImage() != null) {
            File finalFile = new File(emerg.getImage());
            Bitmap bm = BitmapFactory.decodeFile(finalFile
                    .getAbsolutePath());
            emergencyViewHolder.emergency_icon
                    .setScaleType(ImageView.ScaleType.CENTER_CROP);
            emergencyViewHolder.emergency_icon.setImageBitmap(Bitmap
                    .createScaledBitmap(bm, 200, 150, false));
        }*/
    }

    /**
     * Get the index fromt he spinner list
     *
     * @param spinner
     * @param myString
     * @return
     */
    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase((myString))) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        else
            return 0;
    }

    public void setDateRange(String date_range_value) {

    }


    public boolean contains(questionItem f2) {
        // if(items.contains(f2))
        return items.contains(f2);
    }


    public class QuestionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // private TextView copyright;
        private TextView question;
        private LinearLayout row;
        private ImageView img;
        private TextView specialty;


        public QuestionViewHolder(View itemView) {
            super(itemView);
            specialty = (TextView) itemView.findViewById(R.id.specialty);
            img = (ImageView) itemView.findViewById(R.id.questionicon);
            row = (LinearLayout) itemView.findViewById(R.id.row);
            //  copyright = (TextView) itemView.findViewById(R.id.release_info);
            question = (TextView) itemView.findViewById(R.id.question);

            if (row != null)
                row.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                try {
                    mItemClickListener.onItemClick(v, getPosition());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public interface OnItemSelectedListener {
        void onItemSelected(AdapterView<?> parent, View view, int position);
    }


    public interface OnQuestionItemClickListener {
        void onItemClick(View view, int position) throws URISyntaxException;
    }

    public void SetOnQuestionItemClickListener(OnQuestionItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}

