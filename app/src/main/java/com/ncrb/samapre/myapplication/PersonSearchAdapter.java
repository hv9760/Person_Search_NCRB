package com.ncrb.samapre.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonSearchAdapter extends ArrayAdapter<PersonSearchInfo> {
private Context  context;
private LayoutInflater inflater;
private ArrayList<PersonSearchInfo> data;
	public PersonSearchAdapter(Context context, ArrayList<PersonSearchInfo> data) {
		super(context,R.layout.person_search_list_items,data);
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
			convertView=inflater.inflate(R.layout.person_search_item, null);
			viewHolder=new ViewHolder();

			viewHolder.txt_person_name=(TextView)convertView.findViewById(R.id.txt_person_name);
			viewHolder.txt_person_alias=(TextView)convertView.findViewById(R.id.txt_person_alias);
			viewHolder.txt_person_relation_type=(TextView)convertView.findViewById(R.id.txt_person_relation_type);
			viewHolder.txt_person_relative_name=(TextView)convertView.findViewById(R.id.txt_person_relative_name);

			convertView.setTag(viewHolder);
			
		}
		else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
			viewHolder.txt_person_name.setText(getItem(position).getFULL_NAME());
			viewHolder.txt_person_alias.setText(getItem(position).getALIAS1());
		viewHolder.txt_person_relation_type.setText(getItem(position).getRELATION_TYPE());
		viewHolder.txt_person_relative_name.setText(getItem(position).getRELATIVE_NAME());

		
			return convertView;
		}
	class ViewHolder{
		TextView txt_person_name,txt_person_alias,txt_person_relation_type, txt_person_relative_name;
	}
	

}
