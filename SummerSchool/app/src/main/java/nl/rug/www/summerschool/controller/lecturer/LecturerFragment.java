package nl.rug.www.summerschool.controller.lecturer;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nl.rug.www.summerschool.controller.ContentsLab;
import nl.rug.www.summerschool.R;
import nl.rug.www.summerschool.model.Lecturer;

/**
 * Lecturer fragment is to show the details of lecturer information
 * when any item is clicked on LecturerListFragment.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class LecturerFragment extends Fragment {

    private static final String ARG_LECTURER_ID = "lecturer_id";

    private Lecturer mLecturer;

    public static LecturerFragment newInstance(String lecturerId) {
        Bundle args = new Bundle();
        args.putString(ARG_LECTURER_ID, lecturerId);

        LecturerFragment fragment = new LecturerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String lecturerId = getArguments().getString(ARG_LECTURER_ID);
        mLecturer = ContentsLab.get().getLecturer(lecturerId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lecturer, container, false);

        TextView mTitle = (TextView)view.findViewById(R.id.lecturer_name_text_view);
        mTitle.setText(mLecturer.getTitle());
        TextView mDescription = (TextView)view.findViewById(R.id.lecturer_decription_text_view);
        mDescription.setText(mLecturer.getDescription());
        ImageView mLecturerImageView = (ImageView)view.findViewById(R.id.lecturer_image_view);
        Drawable drawable = mLecturer.getProfilePicture();
        if (drawable != null)
            mLecturerImageView.setImageDrawable(drawable);
        else
            mLecturerImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.profile));

        return view;
    }
}