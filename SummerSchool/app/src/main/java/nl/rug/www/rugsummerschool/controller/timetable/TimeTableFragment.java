package nl.rug.www.rugsummerschool.controller.timetable;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import nl.rug.www.rugsummerschool.R;
import nl.rug.www.rugsummerschool.controller.ContentsLab;
import nl.rug.www.rugsummerschool.model.Event;
import nl.rug.www.rugsummerschool.model.EventsPerDay;
import nl.rug.www.rugsummerschool.networking.NetworkingService;

/**
 * This class is time table fragment on main pager activity.
 * It shows days of week from Monday, ... Sunday.
 * Each of day is a expandable drawer, so after clicking each item, it shows details of the day.
 *
 * @since 13/04/2017
 * @author Jeongkyun Oh
 */

public class TimeTableFragment extends Fragment {

    private RecyclerView mTimeTableRecyclerView;
    private TimeTableExpandableAdapter mTimeTableExpandableAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mWeek = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);

        TextView section = (TextView)view.findViewById(R.id.section_name);
        section.setText(R.string.time_table);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchTimeTablesTask().execute();
            }
        });

        Button previousWeekButton = (Button)view.findViewById(R.id.previous_week_button);
        previousWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeek--;
                new FetchTimeTablesTask().execute();
            }
        });

        Button nextWeekButton = (Button)view.findViewById(R.id.next_week_button);
        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeek++;
                new FetchTimeTablesTask().execute();
            }
        });

        mTimeTableRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mTimeTableRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        new FetchTimeTablesTask().execute();
        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mTimeTableExpandableAdapter = new TimeTableExpandableAdapter(getActivity(), generateTimeTableWeek());
            mTimeTableExpandableAdapter.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
            mTimeTableExpandableAdapter.setParentClickableViewAnimationDuration(ExpandableRecyclerAdapter.DEFAULT_ROTATE_DURATION_MS);
            mTimeTableExpandableAdapter.setParentAndIconExpandOnClick(true);
            mTimeTableRecyclerView.setAdapter(mTimeTableExpandableAdapter);
        }
    }

    private class TimeTableParentViewHolder extends ParentViewHolder {

        private TextView mTimeTableTitleTextView;

        private TimeTableParentViewHolder(View itemView) {
            super(itemView);

            mTimeTableTitleTextView = (TextView) itemView.findViewById(R.id.parent_list_item_timetable_text_view);
        }
    }

    private class TimeTableChildViewHolder extends ChildViewHolder {

        private TextView mTimeTextView;
        private TextView mSubjectTextView;

        private TimeTableChildViewHolder(View itemView) {
            super(itemView);

            mTimeTextView = (TextView) itemView.findViewById(R.id.time_text_view);
            mSubjectTextView = (TextView) itemView.findViewById(R.id.subject_description);
        }
    }

    private class TimeTableExpandableAdapter extends ExpandableRecyclerAdapter<TimeTableParentViewHolder, TimeTableChildViewHolder> {

        private LayoutInflater mInflater;

        private TimeTableExpandableAdapter(Context context, List<ParentObject> parentItemList) {
            super(context, parentItemList);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public TimeTableParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
            View view = mInflater.inflate(R.layout.list_item_timetable_parent, viewGroup, false);
            return new TimeTableParentViewHolder(view);
        }

        @Override
        public TimeTableChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
            View view = mInflater.inflate(R.layout.list_item_timetable_child, viewGroup, false);
            return new TimeTableChildViewHolder(view);
        }

        @Override
        public void onBindParentViewHolder(TimeTableParentViewHolder timeTableParentViewHolder, int i, Object o) {
            EventsPerDay eventsPerDay = (EventsPerDay)o;
            timeTableParentViewHolder.mTimeTableTitleTextView.setText(eventsPerDay.getDayOfWeek());
        }

        @Override
        public void onBindChildViewHolder(TimeTableChildViewHolder timeTableChildViewHolder, int i, Object o) {
            Event event = (Event)o;
            timeTableChildViewHolder.mSubjectTextView.setText(event.getTitle());
            Date start = new DateTime(event.getStartDate()).toDate();

            Date end = new DateTime(event.getEndDate()).toDate();
            SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.getDefault());
            time.setTimeZone(TimeZone.getTimeZone("UTC"));

            timeTableChildViewHolder.mTimeTextView.
                    setText(time.format(start)+" - "+time.format(end));
        }
    }

    private ArrayList<ParentObject> generateTimeTableWeek() {
        ContentsLab contentsLab = ContentsLab.get();
        ArrayList<ParentObject> parentObjects = new ArrayList<>();
        List<EventsPerDay> eventsPerDays = contentsLab.getEventsPerDays();
        for (EventsPerDay t : eventsPerDays) {
            parentObjects.add(t);
        }
        return parentObjects;
    }

    private class FetchTimeTablesTask extends AsyncTask<Void, Void, List<EventsPerDay>> {

        List<EventsPerDay> mItems;

        @Override
        protected List<EventsPerDay> doInBackground(Void... params) {
            return new NetworkingService().fetchTimeTables(mWeek);
        }

        @Override
        protected void onPostExecute(List<EventsPerDay> timeTables) {
            mItems = timeTables;
            ContentsLab.get().updateTimeTableWeeks(mItems);
            setupAdapter();
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

}
