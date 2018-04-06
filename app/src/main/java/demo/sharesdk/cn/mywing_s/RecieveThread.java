package demo.sharesdk.cn.mywing_s;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.Charset;

import android.util.Log;

public class RecieveThread extends Thread {
	private DatagramSocket rsSocket;
	Mcallback callback;
	private boolean isrun=true;

	public RecieveThread(DatagramSocket s, Mcallback callback) {
		this.rsSocket = s;
		this.callback = callback;
		isrun=true;
		Log.i("WWW", "rsSocket"+rsSocket);
	}

	public void cancle(){
		isrun=false;
		Log.i("WWW", "停止接收");
	}
	
	@Override
	public void run() {
		super.run();
		DatagramPacket localDatagramPacket;
		Log.i("WWW", "-------------");
		while (isrun) {
			byte[] arrayOfByte = new byte[1024];
			localDatagramPacket = new DatagramPacket(arrayOfByte,
					arrayOfByte.length);
			if (this.rsSocket != null) {
				try {
					
					this.rsSocket.receive(localDatagramPacket);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (localDatagramPacket.getData().length > 3) {
					Log.i("BBB", "getData");
					if ((localDatagramPacket.getData()[0] == 70)
							&& (localDatagramPacket.getData()[1] == 67)
							&& (localDatagramPacket.getData()[2] == 72)
							&& (localDatagramPacket.getData()[3] == 8)) {
						UdpConnect.getInstance().isInitOK = true;
						callback.connSuccess();
					} else if ((localDatagramPacket.getData()[0] == 70)
							&& (localDatagramPacket.getData()[1] == 67)
							&& (localDatagramPacket.getData()[2] == 72)
							&& (localDatagramPacket.getData()[3] == 10)
							&& (Util.bytesToShort(localDatagramPacket.getData(), 6) == 32)) {

						int i2 = 6 + 2;
						int pingTimes = Util.bytesToInt(
								localDatagramPacket.getData(), i2);
						int i3 = i2 + 4;
						int ping_gap = Util.bytesToInt(
								localDatagramPacket.getData(), i3);
						int i4 = i3 + 4;
						int passTime = Util.bytesToInt(
								localDatagramPacket.getData(), i4);
						int i5 = i4 + 4;
						int pkgSize = Util.bytesToInt(
								localDatagramPacket.getData(), i5);
						int i6 = i5 + 4;
						String pingIP = new String(localDatagramPacket.getData(),
								i6, 16, Charset.forName("UTF-8")).trim();

						UdpConnect.getInstance().isGetPingParamOK = true;
						
						callback.getPingParamOK(pingTimes, ping_gap, passTime, pkgSize,
								pingIP);
					} else if ((localDatagramPacket.getData()[0] == 70)
							&& (localDatagramPacket.getData()[1] == 67)
							&& (localDatagramPacket.getData()[2] == 72)
							&& (localDatagramPacket.getData()[3] == 14)
							&& (Util.bytesToShort(localDatagramPacket.getData(), 6) == 32)) {

						int i = 6 + 2;
						int province = Util.bytesToInt(
								localDatagramPacket.getData(), i);// Method.getProvinceNameByCode(Util.bytesToInt(localDatagramPacket.getData(),
																	// i));
						int j = i + 4;
						int city = Util
								.bytesToInt(localDatagramPacket.getData(), j);// Method.getCityNameByCode(Util.bytesToInt(localDatagramPacket.getData(),
																				// j));
						int k = j + 4;
						int serverDevID = Util.bytesToInt(
								localDatagramPacket.getData(), k);
						int l = k + 4;
						int port = Util
								.bytesToInt(localDatagramPacket.getData(), l);
						int i1 = l + 4;
						String serverIP = new String(localDatagramPacket.getData(),
								i1, 16, Charset.forName("UTF-8")).trim();
						UdpConnect.getInstance().isGetServerParamOK = true;
						callback.getServerParamOK(province, city, serverDevID, port,
								serverIP);
					} else if ((localDatagramPacket.getData()[0] == 70)
							&& (localDatagramPacket.getData()[1] == 67)
							&& (localDatagramPacket.getData()[2] == 72)
							&& (localDatagramPacket.getData()[3] == 12)) {
						UdpConnect.getInstance().isSetPingParamOK = true;
						callback.isSetPingParamOK();
					} else if ((localDatagramPacket.getData()[0] == 70)
							&& (localDatagramPacket.getData()[1] == 67)
							&& (localDatagramPacket.getData()[2] == 72)
							&& (localDatagramPacket.getData()[3] == 16)) {
						 UdpConnect.getInstance().isSetServerParamOK = true;
						 callback.isSetServerParamOK();
					}
				}
			}else{
				
			}
		}
	}
}
