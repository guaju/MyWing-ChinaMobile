package demo.sharesdk.cn.mywing_s;

import java.util.List;

public class Province {
	private String provinceName;
	private List<City> cityList;
	
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public List<City> getCityList() {
		return cityList;
	}
	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}
}
