package com.example.activity.inkpad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Tien on 05-Mar-16.
 */
public class NoteAdapter extends ArrayAdapter {
    Context context;
    int layout;
    List<Note> notes;
    List<Note> noteOriginal;
    public NoteAdapter(Context context, int layout, List<Note> notes) {
        super(context, layout, notes);
        this.context = context;
        this.layout = layout;
        this.notes = notes;
        // create another listnote as notes help when search
        noteOriginal = new ArrayList<>(notes);
    }
// the method query by name
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                charSequence = charSequence.toString().trim();
                // create a FilterResult
                FilterResults filterResults = new FilterResults();
                // create a list temp to show when search
                if (!charSequence.equals("")) {
                    List<Note> listTemp = new ArrayList<>();
                    String text = charSequence.toString().toUpperCase();
                    for (Note s : noteOriginal) {
                        if (s.getName().toUpperCase().contains(text)) {
                            listTemp.add(s);
                        }
                    }
                    filterResults.values = listTemp;
                }
                return filterResults;
            }
            // when filter compelete, update notes to show on listview
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notes.clear();
                if (filterResults.values != null) {
                    notes.addAll((Collection<? extends Note>) filterResults.values);
                } else {
                    notes.addAll(noteOriginal);
                }
                notifyDataSetChanged();
            }
        };
    }

    public void reload() {
        notes.clear();
        notes.addAll(noteOriginal);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoler holer;
        // create new row if convertview == null
        if (convertView == null) {
            // inflat change layout xml to view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_note, null);
            holer = new ViewHoler(convertView);
            // save the view to hconvertview
            convertView.setTag(holer);
        } else { // getTag in convertview if convertview != null
            holer = (ViewHoler) convertView.getTag();
        }
        // set Text for textviews
        holer.tvName.setText(notes.get(position).getName());
        holer.tvTime.setText(notes.get(position).getTime());
        return convertView;
    }

    // the class wrap 2 textview to a row
    class ViewHoler {
        public TextView tvName;
        public TextView tvTime;

        public ViewHoler(View rootView) {
            // find widget on the row
            tvName = (TextView) rootView.findViewById(R.id.tvNameOnLV);
            tvTime = (TextView) rootView.findViewById(R.id.tvTimeOnLV);
        }
    }
}
