package demo.sharesdk.cn.mywing_s;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter<T> extends BaseAdapter {
	private List<T> list;
	private Context context;
	public MyAdapter(Context context ,List<T> list){
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list==null){
			return 0;
		}
		return list.size();
	}
	public void refresh(List<T> list){
		this.list=list;
		notifyDataSetChanged();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.item, null);
			holder=new ViewHolder();
			holder.tv=(TextView)convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		T o=list.get(position);
		if(o instanceof Province){
			Province p=(Province) o;
			holder.tv.setText(p.getProvinceName());
		}
		if(o instanceof City){
			City c=(City) o;
			holder.tv.setText(c.getCityName());
		}
		if(o instanceof String){
			String z=(String) o;
			holder.tv.setText(z);
		}
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tv;
	}
}
