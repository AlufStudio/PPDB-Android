package app.rumus.ppdb.lib;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.rumus.ppdb.R;
import app.rumus.ppdb.model.CodeModel;

public class ListCodeAdapter extends BaseAdapter{
	private Activity activity;
	private LayoutInflater inflater;
	private List<CodeModel> listCode;
	
	public ListCodeAdapter(Activity activity,List<CodeModel> listCode){
		this.activity = activity;
		this.listCode = listCode;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listCode.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listCode.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(inflater == null){
			inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.layout_listcode, null);
		}
		
		TextView textCode = (TextView)convertView.findViewById(R.id.textCode);
		TextView textTanggal = (TextView)convertView.findViewById(R.id.textTanggal);
		
		CodeModel cm = listCode.get(position);
		
		textCode.setText(cm.getCode());
		textTanggal.setText(cm.getTanggal());
		
		return convertView;
	}

}
