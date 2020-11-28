package com.example.shifumi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store the list of placement
    private final String[] placementArray;

    //to store the list of names
    private final String[] nameArray;

    //to store the list of info
    private final String[] infoArray;

    public CustomListAdapter(Activity context, String[] placementArrayParam,String[] nameArrayParam, String[] infoArrayParam) {

        super(context, R.layout.listview_row, placementArrayParam);

        this.context=context;
        this.placementArray = placementArrayParam;
        this.nameArray = nameArrayParam;
        this.infoArray = infoArrayParam;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView placementTextField = (TextView) rowView.findViewById(R.id.row_position_label);
        TextView nameTextField = (TextView) rowView.findViewById(R.id.nameTextViewID);
        TextView infoTextField = (TextView) rowView.findViewById(R.id.infoTextViewID);

        //this code sets the values of the objects to values from the arrays
        placementTextField.setText(placementArray[position]);
        nameTextField.setText(nameArray[position]);
        infoTextField.setText(infoArray[position]);

        return rowView;

    };


}
