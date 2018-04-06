package demo.sharesdk.cn.mywing_s;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class SFProvince extends Activity {
	private TextView tv_pro;
	private TextView tv_city;
	// private TextView tv_zone;

	private ListView lv_pro;
	private ListView lv_city;
	private ListView lv_zone;

	private MyAdapter<Province> proAdapter;
	private MyAdapter<City> cityAdapter;
	private MyAdapter<String> zoneAdapter;
	private Province p;
	private Province p1;
	private City city;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sheng_fen);
		initView();

		String result = getData();
		// List<Province> proList = decodeJson(result);
		List<Province> proList = decodeJson(result);
		proAdapter = new MyAdapter(this, proList);
		lv_pro.setAdapter(proAdapter);

		lv_pro.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				p1 = proAdapter.getItem(position);
				tv_pro.setText(p1.getProvinceName());

				tv_city.setText("市");
				// tv_zone.setText("区");
				List<City> cityList = p1.getCityList();
				if (cityAdapter == null) {
					cityAdapter = new MyAdapter<City>(SFProvince.this, cityList);
					lv_city.setAdapter(cityAdapter);
				} else {
					cityAdapter.refresh(cityList);
				}
				if (zoneAdapter != null) {
					zoneAdapter.refresh(null);

				}

			}
		});
		lv_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				city = cityAdapter.getItem(position);
				tv_city.setText(city.getCityName());
				List<String> zoneList = city.getZoneList();
				if (zoneAdapter == null) {
					zoneAdapter = new MyAdapter<String>(SFProvince.this, zoneList);
					lv_zone.setAdapter(zoneAdapter);
				} else {
					zoneAdapter.refresh(zoneList);
				}

				Intent i = new Intent(SFProvince.this, MainActivity.class);
				i.putExtra("date", p1.getProvinceName() + "");
				i.putExtra("sss", city.getCityName() + "");
				startActivity(i);
			}
		});
		lv_zone.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String zone = zoneAdapter.getItem(position);
				// tv_zone.setText(zone);
			}
		});

	}

	private void initView() {
		tv_pro = (TextView) findViewById(R.id.tv_pro);
		tv_city = (TextView) findViewById(R.id.tv_city);
		// tv_zone = (TextView) findViewById(R.id.tv_zone);
		lv_pro = (ListView) findViewById(R.id.lv_pro);
		lv_city = (ListView) findViewById(R.id.lv_city);
		lv_zone = (ListView) findViewById(R.id.lv_zone);
	}

	private List<Province> decodeJson(String result) {
		try {
			List<Province> proList = new ArrayList<Province>();
			JSONArray ja = new JSONArray(result);
			for (int i = 0; i < ja.length(); i++) {
				Province province = new Province();
				JSONObject joProvince = ja.optJSONObject(i);
				String provinceName = joProvince.optString("name");

				JSONArray jaCity = joProvince.optJSONArray("city");

				List<City> cityList = new ArrayList<City>();
				for (int j = 0; j < jaCity.length(); j++) {
					City city = new City();
					JSONObject joCity = jaCity.optJSONObject(j);
					String cityName = joCity.optString("name");
					JSONArray jaZone = joCity.optJSONArray("area");

					List<String> zoneList = new ArrayList<String>();
					for (int k = 0; k < jaZone.length(); k++) {
						String zone = jaZone.optString(k);
						zoneList.add(zone);
					}

					city.setCityName(cityName);
					city.setZoneList(zoneList);

					cityList.add(city);
				}
				province.setProvinceName(provinceName);
				province.setCityList(cityList);

				proList.add(province);
			}

			return proList;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String getData() {
		// 获取assest文件夹下的资源
		AssetManager am = getResources().getAssets();
		try {
			// 打开资源
			InputStream in = am.open("ssq.txt");
			// InputStreamReader 字节转字符的输入流
			InputStreamReader isr = new InputStreamReader(in);
			// 缓冲流
			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			isr.close();
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
