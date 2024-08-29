package com.example.divedeep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<StoredScore> {

    private int[] aImages;

    public CustomAdapter(Context context, ArrayList<StoredScore> items, int[] images) {
        super(context, R.layout.custom_view, items);
        this.aImages = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.custom_view, parent, false);

        StoredScore singleItem = getItem(position);
        TextView textView = customView.findViewById(R.id.customTextView);
        ImageView image = customView.findViewById(R.id.customImageView);

        // Set image based on position
        if (position < 3) {
            image.setImageResource(aImages[position]);
        } else {
            image.setImageResource(aImages[3]); // default image for positions other than 1st, 2nd, 3rd
        }

        // Set text with each variable on a new line
        String displayText = "Player: " + singleItem.getName()
                + "\nGold Mined: " + singleItem.getGoldMined()
                + "\nDeepest Dive: " + singleItem.getDeepestDive() + "m";
        textView.setText(displayText);

        return customView;
    }

}
