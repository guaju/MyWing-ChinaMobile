package demo.sharesdk.cn.mywing_s;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {
    private EditText tv_cishus;// 次数
    private EditText tv_jianges;// 间隔时间
    private EditText tv_chaoshis;// 超时时间
    private EditText tv_baodaxiaos;// 包的大小
    private EditText tv_ips;// IP地址
    private EditText tv_shebeis;// 设备
    private EditText tv_duankous;// 端口
    private EditText tv_serviceips;// 服务ip
    private RelativeLayout rl_shengfen;
    private RelativeLayout rl_city;
    private TextView tv_sf;
    private TextView tv_cs;
    private Button button;// 保存

    String _tv_sf;
    String _tv_cs;
    String _tv_cishus;
    String _tv_jianges;
    String _tv_chaoshis;
    String _tv_baodaxiaos;
    String _tv_ips;
    String _tv_shebeis;
    String _tv_duankous;
    String _tv_serviceips;

    public int getpingTimes;
    public int getping_gap;
    public int getpassTime;
    public int getpkgSize;
    public String getpingIP;

    public int getprovince;
    public int getcity;
    public int getserverDevID;
    public int getport;
    public String getserverIP;

    boolean isFirstPingParam=true;
    boolean isFirstServerParam=true;

    private Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                tv_cishus.setText(getpingTimes + "");
                //设置包大小
                tv_baodaxiaos.setText(getpkgSize + "");
                //设置超时时间
                tv_chaoshis.setText(getpassTime + "");
                //设置间隔时间
                tv_jianges.setText(getping_gap + "");
                tv_ips.setText(getpingIP);
            } else if (msg.what == 1) {
                tv_serviceips.setText(getserverIP);
                tv_shebeis.setText(getserverDevID + "");
                tv_duankous.setText(getport + "");
                tv_sf.setText(TransformCodeAndName.getProvinceNameByCode(getprovince));
                tv_cs.setText(TransformCodeAndName.getCityNameByCode(getcity));
            }
        }
    };
    private MyDialog myDialog;
    private MyBroadCast myBroadCast;
    private IntentFilter filter;

    @Override
    protected void onResume() {
        super.onResume();

        /*tv_sf.setText(in2);
        tv_cs.setText(in1);*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉头部标题的方法(Methods to remove the head title)
        setContentView(R.layout.activity_main);

        initView();
        UdpConnect.getInstance().init(8888, new Mcallback() {
            @Override
            public void initSuccess() {
                Log.i("WWW", "初始化成功");
                sendCommand2Device(0);
            }

            @Override
            public void connSuccess() {
                Log.i("WWW", "链接成功");
                sendCommand2Device(1);

            }

            @Override
            public void getPingParamOK(int _pingTimes, int _ping_gap,
                                       int _passTime, int _pkgSize, String _pingIP) {
                getpingTimes = _pingTimes;
                getping_gap = _ping_gap;
                getpassTime = _passTime;
                getpkgSize = _pkgSize;
                getpingIP = _pingIP;
                Log.i("WWW", "获取参数成功");
                hand.sendEmptyMessage(0);

                sendCommand2Device(2);
            }

            @Override
            public void getServerParamOK(int _province, int _city,
                                         int _serverDevID, int _port, String _serverIP) {
                getprovince = _province;
                getcity = _city;
                getserverDevID = _serverDevID;
                getport = _port;
                getserverIP = _serverIP;
                String provinceNameByCode = TransformCodeAndName.getProvinceNameByCode(getprovince);
                Log.i("WWW", "获取城市成功" + getprovince + "   " + provinceNameByCode);
                hand.sendEmptyMessage(1);
            }

            @Override
            public void isSetPingParamOK() {
                Log.i("WWWW", "设置参数成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFirstPingParam){
                        Toast.makeText(MainActivity.this, "设置ping参数成功!", Toast.LENGTH_SHORT).show();
                        isFirstPingParam=false;
                        }

                    }
                });
                sendCommand2Device(4);
            }

            @Override
            public void isSetServerParamOK() {
                Log.i("WWWW", "设置城市成功");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFirstServerParam){
                        Toast.makeText(MainActivity.this, "设置服务器参数成功!", Toast.LENGTH_SHORT).show();
                        isFirstServerParam=false;
                        }
                    }
                });
            }
        });

    }

    private void initView() {
        myDialog = new MyDialog(this);
        filter = new IntentFilter();
        filter.addAction("wahaha");
        myBroadCast = new MyBroadCast();
        registerReceiver(myBroadCast, filter);
        tv_cishus = (EditText) findViewById(R.id.ed_cishus);// 次数
        tv_jianges = (EditText) findViewById(R.id.tv_jianges);// 间隔时间
        tv_chaoshis = (EditText) findViewById(R.id.tv_chaoshis);// 超时时间
        tv_baodaxiaos = (EditText) findViewById(R.id.tv_baodaxiaos);// 包的大小

        tv_ips = (EditText) findViewById(R.id.tv_ips);// IP地址
        tv_shebeis = (EditText) findViewById(R.id.tv_shebeis);// 设备
        tv_duankous = (EditText) findViewById(R.id.tv_duankous);// 端口
        tv_serviceips = (EditText) findViewById(R.id.tv_serviceips);// 服务ip

        rl_shengfen = (RelativeLayout) findViewById(R.id.rl_sheng);
        rl_city = (RelativeLayout) findViewById(R.id.rl_city);
        button = (Button) findViewById(R.id.button);
        //省份
        tv_sf = (TextView) findViewById(R.id.tv_shengfens);
        //城市
        tv_cs = (TextView) findViewById(R.id.tv_citys);
        rl_shengfen.setOnClickListener(this);
        rl_city.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    public class MyBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String in2 = intent.getStringExtra("date");
            String in1 = intent.getStringExtra("sss");
            tv_sf.setText(in2);
            tv_cs.setText(in1);
            Log.e("wahaha", "onItemClick: " + in1 + "    " + in2);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_sheng:
//                Util.getCityCodeByName();
                Intent in = new Intent(MainActivity.this, ShengFen.class);
                startActivity(in);
                break;
            case R.id.button:
                _tv_cishus = tv_cishus.getText().toString().trim();
                _tv_jianges = tv_jianges.getText().toString().trim();
                _tv_chaoshis = tv_chaoshis.getText().toString().trim();
                _tv_baodaxiaos = tv_baodaxiaos.getText().toString().trim();
                _tv_ips = tv_ips.getText().toString().trim();
                _tv_shebeis = tv_shebeis.getText().toString().trim();
                _tv_duankous = tv_duankous.getText().toString().trim();
                _tv_serviceips = tv_serviceips.getText().toString().trim();
                if ("".equals(_tv_baodaxiaos)) {
                    myDialog.showDialog("包的大小不能为空");
                } else if ("".equals(_tv_jianges)) {
                    myDialog.showDialog("间隔不能为空");
                } else if ("".equals(_tv_cishus)) {
                    myDialog.showDialog("次数不能为空");
                } else if ("".equals(_tv_chaoshis)) {
                    myDialog.showDialog("超时时间不能为空");
                } else if ("".equals(_tv_ips)) {
                    myDialog.showDialog("地址不能为空");
                } else if ("".equals(_tv_shebeis)) {
                    myDialog.showDialog("设备不能为空");
                } else if ("".equals(_tv_duankous)) {
                    myDialog.showDialog("端口不能为空");
                } else if ("".equals(_tv_serviceips)) {
                    myDialog.showDialog("服务不能为空");
                } else {
                    sendCommand2Device(3);
                    isFirstPingParam=true;
                    isFirstServerParam=true;

                }
                break;
        }
    }

    private void sendCommand2Device(final int paramInt) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (paramInt == 0) {
                    byte[] arrayOfByte3 = new byte[8];
                    arrayOfByte3[0] = 70;
                    arrayOfByte3[1] = 67;
                    arrayOfByte3[2] = 72;
                    arrayOfByte3[3] = 7;
                    UdpConnect.getInstance().sendCmd(arrayOfByte3);
                    Log.i("WWW", "send 0");
                } else if (paramInt == 1) {
                    if (UdpConnect.getInstance().isInitOK) {
                        byte[] arrayOfByte1 = new byte[8];
                        arrayOfByte1[0] = 70;
                        arrayOfByte1[1] = 67;
                        arrayOfByte1[2] = 72;
                        arrayOfByte1[3] = 9;
                        UdpConnect.getInstance().isGetPingParamOK = false;
                        UdpConnect.getInstance().sendCmd(arrayOfByte1);
                    }
                } else if (paramInt == 2) {
                    if (UdpConnect.getInstance().isInitOK) {
                        byte[] arrayOfByte1 = new byte[8];
                        arrayOfByte1[0] = 70;
                        arrayOfByte1[1] = 67;
                        arrayOfByte1[2] = 72;
                        arrayOfByte1[3] = 13;
                        UdpConnect.getInstance().isGetServerParamOK = false;
                        UdpConnect.getInstance().sendCmd(arrayOfByte1);
                    }
                } else if (paramInt == 3) {
                    int i = Integer.valueOf(_tv_cishus);// Integer.valueOf(MainActivity.this.etPing_Times.getText().toString().trim()).intValue();
                    int j = Integer.valueOf(_tv_jianges);// Integer.valueOf(MainActivity.this.etPing_Time_gap.getText().toString().trim()).intValue();
                    int k = Integer.valueOf(_tv_chaoshis);// ;Integer.valueOf(MainActivity.this.etPing_Pass_Time.getText().toString().trim()).intValue();
                    int l = Integer.valueOf(_tv_baodaxiaos);// Integer.valueOf(MainActivity.this.etPing_Pkg_Size.getText().toString().trim()).intValue();
                    String str1 = _tv_ips;// MainActivity.this.etPing_IP.getText().toString().trim();
                    UdpConnect.getInstance().isSetPingParamOK = false;
                    UdpConnect.getInstance().sendPingSetCmd(i, j, k, l, str1);


                } else if (paramInt == 4) {
                    if (UdpConnect.getInstance().isSetPingParamOK) {
//                        int i1 = TransformCodeAndName.getProvincesCodeByName(tv_sf.getText()
//                                .toString());
                        int i1 = TransformCodeAndName.getReportProvCode(tv_sf.getText()
                                .toString());
//                        getReportProvCode
                        int i2 = TransformCodeAndName.getCityCodeByName(tv_sf.getText()
                                .toString(), tv_cs.getText().toString());
                        int i3 = Integer.valueOf(_tv_shebeis);// Integer.valueOf(MainActivity.this.etServer_DevID.getText().toString().trim()).intValue();
                        int i4 = Integer.valueOf(_tv_duankous);// Integer.valueOf(MainActivity.this.etServer_Port.getText().toString().trim()).intValue();
                        String str2 = _tv_serviceips;// MainActivity.this.etServer_IP.getText().toString().trim();
                        UdpConnect.getInstance().isSetServerParamOK = false;
                        UdpConnect.getInstance().sendServerSetCmd(i1, i2, i3,
                                i4, str2);
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadCast);
        try {
            UdpConnect.getInstance().cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public interface Mcallback{
//
//    }
}

