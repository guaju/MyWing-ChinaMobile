package demo.sharesdk.cn.mywing_s;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UdpConnect {



	private static UdpConnect instance = null;
	public int atPort;
	public String city;
	public boolean isGetPingParamOK = false;
	public boolean isGetServerParamOK = false;
	public boolean isInitOK = false;
	public boolean isSetPingParamOK = false;
	public boolean isSetServerParamOK = false;




	private int passTime;
	private String pingIP;
	private int pingTimes;
	private int ping_gap;
	private int pkgSize;
	private int port;
	private String province;
	private RecieveThread receiver;
	private String remoteIP = "192.168.4.1";
	private int serverDevID;
	private String serverIP;
	DatagramSocket wifiSocket = null;
	Mcallback callback;
	public static UdpConnect getInstance() {
		if (instance == null)
			instance = new UdpConnect();
		return instance;
	}

	public void init(int paramInt,Mcallback callback) {
		this.atPort = paramInt;
		this.callback=callback;
		Util.init();
		TransformCodeAndName.init();
		getConnectedHotPort();
		boolean init=initSocket();
		if (init) {
			callback.initSuccess();
		}
	}

	private void getConnectedHotPort() {
		try {
			BufferedReader localBufferedReader = new BufferedReader(
					new FileReader("/proc/net/arp"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private boolean initSocket() {
		try {
			if (this.wifiSocket != null)
				return false;
			this.wifiSocket = new DatagramSocket(atPort);
			this.wifiSocket.setReuseAddress(true);
			this.receiver = new RecieveThread(this.wifiSocket,callback);
			this.receiver.start();
			return true;
		} catch (IOException localIOException) {
			if (this.wifiSocket != null) {
				this.wifiSocket.close();
				this.wifiSocket = null;
			}
			localIOException.printStackTrace();
		}
		return false;
	}

	public void sendCmd(byte[] paramArrayOfByte) {
		if ((this.wifiSocket == null) || (paramArrayOfByte == null))
			initSocket();
		if ("".equals(this.remoteIP))
			return;
		DatagramPacket localDatagramPacket2;
		try {
			DatagramPacket localDatagramPacket1 = new DatagramPacket(paramArrayOfByte, paramArrayOfByte.length,InetAddress.getByName(this.remoteIP), this.atPort);
			localDatagramPacket2 = localDatagramPacket1;
			if (this.wifiSocket != null) 
				try {
					this.wifiSocket.send(localDatagramPacket2);
					return;
				} catch (IOException localIOException) {
					localIOException.printStackTrace();
					return;
				}
			
		} catch (UnknownHostException localUnknownHostException) {
			localUnknownHostException.printStackTrace();
			localDatagramPacket2 = null;
		}
	}
	public void cancel() throws IOException {
		Log.i("WWW", "UdpConnect cancel");
		if (receiver!=null) {
			receiver.cancle();
		}
		if (this.wifiSocket != null){
			this.wifiSocket.close();
			wifiSocket=null;
		}
	
	}
	
	
	public void sendPingSetCmd(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4, String paramString) {
		byte[] arrayOfByte1 = new byte[40];
		byte[] arrayOfByte2 = new byte[8];
		arrayOfByte2[0] = 70;
		arrayOfByte2[1] = 67;
		arrayOfByte2[2] = 72;
		arrayOfByte2[3] = 11;
		arrayOfByte2[7] = 32;
		System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, 8);
		int i = 0 + 8;

		System.arraycopy(getBytes(paramInt1), 0, arrayOfByte1, i, 4);
		int j = i + 4;
		System.arraycopy(getBytes(paramInt2), 0, arrayOfByte1, j, 4);
		int k = j + 4;
		System.arraycopy(getBytes(paramInt3), 0, arrayOfByte1, k, 4);
		int l = k + 4;
		System.arraycopy(getBytes(paramInt4), 0, arrayOfByte1, l, 4);
		int i1 = l + 4;
		byte[] arrayOfByte3 = null;
		if ((paramString != null) && (!paramString.isEmpty())) {
			arrayOfByte3 = paramString.getBytes();
			System.arraycopy(arrayOfByte3, 0, arrayOfByte1, i1,
					arrayOfByte3.length);
		}
		for (int i2 = 24 + arrayOfByte3.length;; ++i2) {
			if (i2 >= arrayOfByte1.length) {
				if (this.wifiSocket == null)
					initSocket();
				if (!"".equals(this.remoteIP))
					break;
				return;
			}
			arrayOfByte1[i2] = 0;
		}
		DatagramPacket localDatagramPacket2;
		try {
			DatagramPacket localDatagramPacket1 = new DatagramPacket(
					arrayOfByte1, arrayOfByte1.length,
					InetAddress.getByName(this.remoteIP), this.atPort);
			localDatagramPacket2 = localDatagramPacket1;
			try {
				this.wifiSocket.send(localDatagramPacket2);
				return;
			} catch (IOException localIOException) {
				localIOException.printStackTrace();
				return;
			}
		} catch (UnknownHostException localUnknownHostException) {
			localUnknownHostException.printStackTrace();
			localDatagramPacket2 = null;
		}
	}
	

	  public void sendServerSetCmd(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString)
	  {
	    byte[] arrayOfByte1 = new byte[40];
	    byte[] arrayOfByte2 = new byte[8];
	    arrayOfByte2[0] = 70;
	    arrayOfByte2[1] = 67;
	    arrayOfByte2[2] = 72;
	    arrayOfByte2[3] = 15;
	    arrayOfByte2[7] = 32;
	    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, 8);
	    int i = 0 + 8;
	    System.arraycopy(getBytes(paramInt1), 0, arrayOfByte1, i, 4);
	    int j = i + 4;
	    System.arraycopy(getBytes(paramInt2), 0, arrayOfByte1, j, 4);
	    int k = j + 4;
	    System.arraycopy(getBytes(paramInt3), 0, arrayOfByte1, k, 4);
	    int l = k + 4;
	    System.arraycopy(getBytes(paramInt4), 0, arrayOfByte1, l, 4);
	    int i1 = l + 4;
	    byte[] arrayOfByte3 = null;
	    if ((paramString != null) && (!paramString.isEmpty()))
	    {
	      arrayOfByte3 = paramString.getBytes();
	      System.arraycopy(arrayOfByte3, 0, arrayOfByte1, i1, arrayOfByte3.length);
	    }
	    for (int i2 = 24 + arrayOfByte3.length; ; ++i2)
	    {
	      if (i2 >= arrayOfByte1.length)
	      {
	        if (this.wifiSocket == null)
	          initSocket();
	        if (!"".equals(this.remoteIP))
	          break;
	        return;
	      }
	      arrayOfByte1[i2] = 0;
	    }
	    DatagramPacket localDatagramPacket2;
	    try
	    {
	      DatagramPacket localDatagramPacket1 = new DatagramPacket(arrayOfByte1, arrayOfByte1.length, InetAddress.getByName(this.remoteIP), this.atPort);
	      localDatagramPacket2 = localDatagramPacket1;
	      try
	      {
	        this.wifiSocket.send(localDatagramPacket2);

	        return;
	      }
	      catch (IOException localIOException)
	      {
	        localIOException.printStackTrace();
	        return;
	      }
	    }
	    catch (UnknownHostException localUnknownHostException)
	    {
	      localUnknownHostException.printStackTrace();
	      localDatagramPacket2 = null;
	    }
	  }
	

	private byte[] getBytes(int paramInt) {
		byte[] arrayOfByte = new byte[4];
		arrayOfByte[3] = (byte) (paramInt >> 24);
		arrayOfByte[2] = (byte) (paramInt >> 16);
		arrayOfByte[1] = (byte) (paramInt >> 8);
		arrayOfByte[0] = (byte) (paramInt >> 0);
		return arrayOfByte;
	}
}
