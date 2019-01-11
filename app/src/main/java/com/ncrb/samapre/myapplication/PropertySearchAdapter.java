package com.ncrb.samapre.myapplication;

import java.util.ArrayList;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PropertySearchAdapter extends ArrayAdapter<PropertySearchInfo> {
private Context  context;
private LayoutInflater inflater;
private ArrayList<PropertySearchInfo> data;
	public PropertySearchAdapter(Context context, ArrayList<PropertySearchInfo> data) {
		super(context,R.layout.property_search_list_items,data);
		this.context=context;
		this.data=data;
		// TODO Auto-generated constructor stub
	}
	@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView==null){
			inflater=LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.property_search_list_items, null);
			viewHolder=new ViewHolder();
			viewHolder.txtVehicle_reg_no=(TextView)convertView.findViewById(R.id.txt_vehicle_reg_no);
			viewHolder.txtVehicle_type=(TextView)convertView.findViewById(R.id.txt_vehicle_type);
			viewHolder.txtVehicle_Model=(TextView)convertView.findViewById(R.id.txt_vehicle_Model);

			convertView.setTag(viewHolder);
			
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
			viewHolder.txtVehicle_reg_no.setText(getItem(position).getREGISTRATION_NO());
			viewHolder.txtVehicle_type.setText(getItem(position).getMV_Type());
		viewHolder.txtVehicle_Model.setText(getItem(position).getMvModel());

		
			return convertView;
		}
	class ViewHolder{
		TextView txtVehicle_reg_no,txtVehicle_type,txtVehicle_Model;
	}
	

}
