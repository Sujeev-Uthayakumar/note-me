package com.sujeevuthayakumar.noteme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomBaseAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<NoteModel> noteModelList;
    List<NoteModel> mOriginalValues;
    LayoutInflater inflater;
    DataBaseHelper dataBaseHelper;

    public CustomBaseAdapter(Context ctx, List<NoteModel> noteModelList) {
        this.context = ctx;
        this.noteModelList = noteModelList;
        inflater = LayoutInflater.from(ctx);
        this.dataBaseHelper = new DataBaseHelper(ctx);
    }

    @Override
    public int getCount() {
        return noteModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return noteModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return noteModelList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_custom_list_view, null);
        styleNote(noteModelList.get(i), view);
        TextView titleView = (TextView) view.findViewById(R.id.list_title);
        titleView.setText(noteModelList.get(i).getTitle());
        TextView subtitleView = (TextView) view.findViewById(R.id.list_subtitle);
        subtitleView.setText(noteModelList.get(i).getSubTitle());
        TextView noteView = (TextView) view.findViewById(R.id.list_note);
        noteView.setText(noteModelList.get(i).getNote());

        ImageView imageView = view.findViewById(R.id.delete_image);
        imageView.setOnClickListener(v -> {
            long noteId = getItemId(i);
            boolean success = dataBaseHelper.deleteOne((int) noteId);
            if (success) {
                this.noteModelList.remove(i);
                notifyDataSetChanged();
                Toast.makeText(this.context, "Note Successfully Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void styleNote(NoteModel noteModel, View view) {
        if (noteModel.getNoteColor().equals("blue")) {
            view.findViewById(R.id.list).setBackgroundColor(context.getResources().getColor(R.color.lightblue));
        } else if (noteModel.getNoteColor().equals("red")) {
            view.findViewById(R.id.list).setBackgroundColor(context.getResources().getColor(R.color.lightred    ));
        } else if (noteModel.getNoteColor().equals("yellow")) {
            view.findViewById(R.id.list).setBackgroundColor(context.getResources().getColor(R.color.lightyellow));

        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                noteModelList = (List<NoteModel>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<NoteModel> FilteredArrList = new ArrayList<NoteModel>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<NoteModel>(noteModelList); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                }
                else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        NoteModel data = mOriginalValues.get(i);
                        if (data.getTitle().toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
