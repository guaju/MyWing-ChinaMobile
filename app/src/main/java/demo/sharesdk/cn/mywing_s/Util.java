package demo.sharesdk.cn.mywing_s;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.util.Log;

public class Util {

    final static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static native String QcTimeNew(int BiasHour, long ts, long ts1);

    public static native String QcTime(int BiasHour, long ts);

    public static native String QcOnlyTime(int BiasHour, long ts);

    public static native long TimetoMSec(int BiasHour, long ts);

    public static native byte[] TimeToQcTime(int BiasHour, int year, int month,
                                             int day, int hour, int min, int sec, int msec);

    public static native int GetSec(long ts);

    public static int secOffsetTzone = -1
            * TimeZone.getDefault().getRawOffset() / 60000;

    static {
        // System.loadLibrary("parser-common");
    }

    public static String ToHexString(byte[] bytes, int Length) {
        if (bytes == null)
            return "";

        String strMask = "";
        for (int i = 0; i < Length; i++) {
            String b = ToHexString(bytes[i]);
            strMask += b;
        }
        return strMask;
    }

    public static String ToHexString(byte bytes) {
        char[] chars = new char[2];

        chars[0] = Util.hexDigits[(bytes >> 4) & 0x0F];
        chars[1] = Util.hexDigits[bytes & 0xF];
        return new String(chars);
    }

    public static void ushortToByteArray(int val, int sindex, byte[] data) {
        int valtest = val & 0xff00;
        valtest = valtest >> 8;
        valtest = valtest & 0xff;

        data[sindex + 1] = (byte) (((val & 0xff00) >> 8) & 0xff);
        data[sindex] = (byte) (val & 0xff);
    }

    public static byte[] ushortToByteArray(int val) {
        byte[] data = new byte[2];

        int valtest = val & 0xff00;
        valtest = valtest >> 8;
        valtest = valtest & 0xff;

        data[1] = (byte) (((val & 0xff00) >> 8) & 0xff);
        data[0] = (byte) (val & 0xff);
        return data;
    }

    public static int byteArrayToUshort(int sindex, byte[] data) {
        int val;
        val = ((data[sindex + 1] & 0xff) << 8) & 0xff00;
        val += data[sindex] & 0xff;
        return (val & 0xffff);
    }

    public static int byteArrayToUshortBG(int sindex, byte[] data) {
        int val;
        val = ((data[sindex] & 0xff) << 8) & 0xff00;
        val += data[sindex + 1] & 0xff;
        return (val & 0xffff);
    }

    public static short byteArrayToShort(int sindex, byte[] data) {
        // [lolo:120217] .

        // ByteBuffer buf = ByteBuffer.wrap(data);
        // buf.order(ByteOrder.LITTLE_ENDIAN);
        // return buf.getShort(sindex);

        int iLow = data[sindex + 0];
        int iHigh = data[sindex + 1];

        int iRetVal = ((0x000000FF & iHigh) << 8) | (0x000000FF & iLow);

        return (short) iRetVal;
    }

    public static void UInt32ToByteArray(long val, int sindex, byte[] data) {
        data[sindex + 3] = (byte) ((val & 0xff000000) >> 24);
        data[sindex + 2] = (byte) ((val & 0x00ff0000) >> 16);
        data[sindex + 1] = (byte) ((val & 0x0000ff00) >> 8);
        data[sindex] = (byte) (val & 0x000000ff);
    }

    public static long byteArrayToUInt32(int sindex, byte[] data) {
        long val = 0;

        val = (((long) (data[sindex + 3] & 0xff)) << 24) & 0x00ff000000;
        val += ((data[sindex + 2] & 0xff) << 16) & 0x00ff0000;
        val += ((data[sindex + 1] & 0xff) << 8) & 0x0000ff00;
        val += (data[sindex] & 0xff) & 0x000000ff;

        return val;
    }

    public static long byteArrayToUInt32BG(int sindex, byte[] data) {
        long val = 0;

        val = (((long) (data[sindex] & 0xff)) << 24) & 0x00ff000000;
        val += ((data[sindex + 1] & 0xff) << 16) & 0x00ff0000;
        val += ((data[sindex + 2] & 0xff) << 8) & 0x0000ff00;
        val += (data[sindex + 3] & 0xff) & 0x000000ff;

        return val;
    }

    public static void UInt64ToByteArray(double val, int sindex, byte[] data) {
        UInt32ToByteArray((long) val, sindex, data);
    }

    public static long byteArrayToUInt64(int sindex, byte[] data) {

        long val = 0;

        val = (((long) (data[sindex + 7] & 0xff)) << 56) & 0xff00000000000000l;
        val += ((((long) data[sindex + 6] & 0xff)) << 48) & 0x00ff000000000000l;
        val += ((((long) data[sindex + 5] & 0xff)) << 40) & 0x0000ff0000000000l;
        val += ((((long) data[sindex + 4] & 0xff)) << 32) & 0x000000ff00000000l;
        val = (((long) (data[sindex + 3] & 0xff)) << 24) & 0x00000000ff000000;
        val += ((((long) data[sindex + 2] & 0xff)) << 16) & 0x0000000000ff0000;
        val += ((((long) data[sindex + 1] & 0xff)) << 8) & 0x000000000000ff00;
        val += (((long) data[sindex] & 0xff)) & 0x00000000000000ff;

        return val;

        // return byteArrayToUInt32(sindex, data);
    }

    public static void Int32ToByteArray(int val, int sindex, byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data, sindex, 4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(val);
    }

    public static int byteArrayToInt32(int sindex, byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt(sindex);
    }

    public static void DoubleToByteArray(double val, int sindex, byte[] data) {
        //
        ByteBuffer buf = ByteBuffer.wrap(data, sindex, 8);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putDouble(val);

    }

    public static double byteArrayToDouble(int sindex, byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getDouble(sindex);

        // return (double)byteArrayToUInt32(sindex, data);
    }

    public static void LongToByteArray(long val, int sindex, byte[] data) {
        //
        ByteBuffer buf = ByteBuffer.wrap(data, sindex, 8);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putLong(val);

        // UInt32ToByteArray((int)val, sindex, data);
    }

    public static long byteArrayToLong(int sindex, byte[] data) {
        ByteBuffer buf = ByteBuffer.wrap(data, sindex, 8);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getLong();

        // return (double)byteArrayToUInt32(sindex, data);
    }

    public static void floatToByteArray(float val, int sindex, byte[] data) {

        ByteBuffer buf = ByteBuffer.wrap(data, sindex, 4);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putFloat(val);
    }

    public static float byteArrayToFloat(int sindex, byte[] data) {

        ByteBuffer buf = ByteBuffer.wrap(data);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        return buf.getFloat(sindex);
    }

    public static byte GetDigits(char c) {
        switch (c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'A':
                return 10;
            case 'B':
                return 11;
            case 'C':
                return 12;
            case 'D':
                return 13;
            case 'E':
                return 14;
            case 'F':
                return 15;
        }
        return 0;
    }

    public static byte[] StringToHex(String strHex) {
        if (strHex == null)
            return null;

        byte[] msg = new byte[strHex.length() / 2];

        for (int i = 0; i < strHex.length() / 2; i++) {
            int b = 0;

            byte b0 = GetDigits(strHex.charAt(i * 2 + 1));
            byte b1 = GetDigits(strHex.charAt(i * 2));

            b = (b1 * 16 + b0);

            msg[i] = (byte) (b & 0xFF);
        }

        return msg;
    }

    public static byte[] SysTimeToQcTime(long dt) {
        byte[] data = null;
        return data;
    }

    public static String create_datetime_fstring() {
        // [lolo:120706]
        Calendar c = Calendar.getInstance();
        int mYear;
        int mMonth;
        int mDay;

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Date dt = new Date();

        String dstring = String.format("%s%02d%02d%02d%02d%02d",
                (String.format("%04d", mYear)).substring(2, 4), mMonth, mDay,
                dt.getHours(), dt.getMinutes(), dt.getSeconds());

        return dstring;
    }

    public static String create_datetime_nstring() {

        Calendar c = Calendar.getInstance();
        int mYear;
        int mMonth;
        int mDay;

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        mDay = c.get(Calendar.DAY_OF_MONTH);

        String dstring = String.format("%s-%02d-%02d",
                String.format("%04d", mYear), mMonth, mDay);

        return dstring;
    }

    public static boolean compare_checking_date(String limitDate) {
        String[] dateStr = limitDate.split("-");
        if (dateStr.length == 3) // YYYY-MM-DD
        {
            int limitYear = Integer.parseInt(dateStr[0]);
            int limitMonth = Integer.parseInt(dateStr[1]);
            int limitDay = Integer.parseInt(dateStr[2]);

            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH) + 1;
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            if (mYear > limitYear)
                return true;

            if (mYear == limitYear && mMonth > limitMonth)
                return true;

            return mYear == limitYear && mMonth == limitMonth && mDay >= limitDay;

        }

        return true;
    }

    public static boolean compare_checking_date(String issuDate, boolean lg) {
        String[] dateStr = issuDate.split("-");
        if (dateStr.length == 3) // YYYY-MM-DD
        {
            int pday = 5;
            Calendar cal = Calendar.getInstance();

            int mYear = cal.get(Calendar.YEAR);
            int mMonth = cal.get(Calendar.MONTH) + 1;
            int mDay = cal.get(Calendar.DAY_OF_MONTH);

            int issueYear = Integer.parseInt(dateStr[0]);
            int issueMonth = Integer.parseInt(dateStr[1]);
            int issueDay = Integer.parseInt(dateStr[2]);

            Calendar calIssue = new GregorianCalendar(issueYear, issueMonth,
                    issueDay);

            if (mYear == 2012 && mMonth <= 8) {
                // KDDI
                // if (SysDefine.this_model == SysDefine.MODEL_LG_KT1101) pday =
                // 7;
            }

            calIssue.add(Calendar.DATE, pday);

            int limitYear = calIssue.get(Calendar.YEAR);
            int limitMonth = calIssue.get(Calendar.MONTH);
            int limitDay = calIssue.get(Calendar.DAY_OF_MONTH);

            if (mYear > limitYear)
                return true;

            if (mYear == limitYear && mMonth > limitMonth)
                return true;

            return mYear == limitYear && mMonth == limitMonth && mDay >= limitDay;

        }

        return true;
    }

    public static String getCdmaMinNum(long min1, int min2) {
        int i, min1_1, min1_2, min1_3;
        int[] minBuf = new int[11];

        minBuf[0] = (min2 / 100) + 1;
        minBuf[1] = ((min2 - (minBuf[0] - 1) * 100) / 10) + 1;
        minBuf[2] = (min2 % 10) + 1;
        min1_3 = (int) (min1 & 0x000003ff);
        min1_2 = (int) (min1 >> 10) & 0x0000000f;
        min1_1 = (int) (min1 >> 14) & 0x000003ff;
        minBuf[3] = (min1_1 / 100) + 1;
        minBuf[4] = ((min1_1 - (minBuf[3] - 1) * 100) / 10) + 1;
        minBuf[5] = (min1_1 % 10) + 1;
        minBuf[6] = min1_2;
        minBuf[7] = (min1_3 / 100) + 1;
        minBuf[8] = ((min1_3 - (minBuf[7] - 1) * 100) / 10) + 1;
        minBuf[9] = (min1_3 % 10) + 1;

        String strMin = "";
        for (i = 0; i < 10; i++) {
            if (minBuf[i] == 10) {
                minBuf[i] = 0;
            }
            String buf = String.format("%d", minBuf[i]);
            strMin += buf;
        }
        minBuf[10] = 0;
        return strMin;
    }

    static Calendar lastCalendar = null;
    static byte[] lastTS = null;

    public static byte[] GetCurrentTimeStamp() {
        Calendar cal = Calendar.getInstance();

        if (lastCalendar != null && lastCalendar.compareTo(cal) == 0) {
            Log.i("LogEvent", "Same Time");
            return lastTS;
        }

        int msec = cal.get(Calendar.MILLISECOND);
        int sec = cal.get(Calendar.SECOND);
        int min = cal.get(Calendar.MINUTE);
        // 24 hour format
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        lastTS = Util.TimeToQcTime(Util.secOffsetTzone, year, month, day, hour,
                min, sec, msec);
        /*
         * String str = ""; for( int i=0; i<ts.length; i++) { str +=
		 * String.format("%X", ts[i]); } Log.i("LightDM", "TimeStamp : " + str);
		 */

        return lastTS;
    }

    static public long GetCurrentSec() {
        return System.currentTimeMillis() / 1000;
    }

    public static String byteArrayToString(int sindex, int length, byte[] data) {
        byte[] bytes = new byte[length];
        System.arraycopy(data, sindex, bytes, 0, length);
        return new String(bytes);
    }

    // ============================时间判断

    static long preDataTimeTms = 0;
    static byte[] preDateTimeBuffer = new byte[8];

    public static boolean CheckTmsOutOfOrder(byte[] storeBuff) {
        ByteBuffer ptsbuffer = ByteBuffer.wrap(storeBuff, 4, 8);
        ptsbuffer.order(ByteOrder.LITTLE_ENDIAN);
        long pts = ptsbuffer.getLong();

        if (pts < preDataTimeTms) {
            return true;
        } else {
            preDataTimeTms = pts;
            System.arraycopy(storeBuff, 4, preDateTimeBuffer, 0, 8);
        }
        return false;
    }

    public static void SetTmsOutOfOrder(byte[] storeBuff) {
        System.arraycopy(preDateTimeBuffer, 0, storeBuff, 4, 8);
        // Log.i("shiyan",String.format(" SysDefine.globalSec  -----------------------------%s============%d",
        // time,pts));
    }

    public static void ClearTmsOutOfOrder() {
        preDataTimeTms = 0;
        for (int i = 0; i < 8; i++) {
            preDateTimeBuffer[i] = 0;
        }
    }

    // ============================时间判断

    // gy 2017-9-9
    public static long binarray2Long(byte[] data) {
        long a = 0x0001;
        long value = 0;
        for (int i = 0; i < data.length; i++) {
            long b = a << (data.length - i - 1);
            value += data[i] * b;
        }
        return value;
    }

    // static byte[] temporary = new byte[100];
    // public static void putString2Array(String str,byte[] data,int size,int
    // sindex){
    // byte[] strarray=(str==null?"":str).getBytes();
    // System.arraycopy(strarray, 0, temporary, 0, strarray.length);
    // System.arraycopy(temporary, 0, data, sindex, size);
    // for (int i = 0; i < temporary.length; i++) {
    // temporary[i]=0;
    // }
    // }
    public static void putString2Array(String str, byte[] data, int size,
                                       int sindex) {
        byte[] temporary = new byte[size];
        byte[] strarray = null;
        try {
            strarray = (str == null ? "" : str).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int minsize = strarray.length > size ? size : strarray.length;
        System.arraycopy(strarray, 0, temporary, 0, minsize);
        System.arraycopy(temporary, 0, data, sindex, size);
        for (int i = 0; i < temporary.length; i++) {
            temporary[i] = 0;
        }
    }

    public static int bytesToInt(byte[] paramArrayOfByte, int paramInt) {
        return 0xFF & paramArrayOfByte[paramInt]
                | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8
                | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16
                | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24;
    }

    public static short bytesToShort(byte[] paramArrayOfByte, int paramInt) {
        return (short) (0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8);
    }

    public static ArrayList<String> provList;

    public static int getReportProvCode(String paramString) {
        for (int i = 0; ; ++i) {
            if (i >= provList.size())
                return -1;
            if (!paramString.equals(provList.get(i)))
                continue;
            if (i < 9)
                return 1000 * (i + 1);
            if ((i >= 9) && (i < 18))
                return 100 * (i + 2);
            if ((i >= 18) && (i < 27))
                return 100 * (i + 3);
            if ((i >= 27) && (i < 31))
                return 100 * (i + 4);
        }
    }

    public static void init() {
        provList = new ArrayList();
        provList.add("北京");
        provList.add("上海");
        provList.add("天津");
        provList.add("重庆");
        provList.add("河北");
        provList.add("山西");
        provList.add("内蒙古");
        provList.add("辽宁");
        provList.add("吉林");
        provList.add("黑龙江");
        provList.add("江苏");
        provList.add("浙江");
        provList.add("安徽");
        provList.add("福建");
        provList.add("江西");
        provList.add("山东");
        provList.add("河南");
        provList.add("湖北");
        provList.add("湖南");
        provList.add("广东");
        provList.add("广西");
        provList.add("海南");
        provList.add("四川");
        provList.add("贵州");
        provList.add("云南");
        provList.add("西藏");
        provList.add("陕西");
        provList.add("甘肃");
        provList.add("宁夏");
        provList.add("青海");
        provList.add("新疆");
    }

    public static ArrayList<String> getCitys(int paramInt) {
        ArrayList localArrayList = new ArrayList();
        switch (paramInt) {
            case 24:
            default:
                return localArrayList;
            case 10:
                localArrayList.add("北京");
                return localArrayList;
            case 20:
                localArrayList.add("上海");
                return localArrayList;
            case 30:
                localArrayList.add("天津");
                return localArrayList;
            case 40:
                localArrayList.add("重庆");
                return localArrayList;
            case 50:
                localArrayList.add("邯郸");
                localArrayList.add("石家庄");
                localArrayList.add("保定");
                localArrayList.add("张家口");
                localArrayList.add("承德");
                localArrayList.add("唐山");
                localArrayList.add("廊坊");
                localArrayList.add("沧州");
                localArrayList.add("衡水");
                localArrayList.add("邢台");
                localArrayList.add("秦皇岛");
                return localArrayList;
            case 60:
                localArrayList.add("朔州");
                localArrayList.add("忻州");
                localArrayList.add("太原");
                localArrayList.add("大同");
                localArrayList.add("阳泉");
                localArrayList.add("晋中");
                localArrayList.add("长治");
                localArrayList.add("晋城");
                localArrayList.add("临汾");
                localArrayList.add("吕梁");
                localArrayList.add("运城");
                return localArrayList;
            case 70:
                localArrayList.add("海拉尔");
                localArrayList.add("呼和浩特");
                localArrayList.add("包头");
                localArrayList.add("乌海");
                localArrayList.add("集宁");
                localArrayList.add("通辽");
                localArrayList.add("赤峰");
                localArrayList.add("东胜");
                localArrayList.add("临河");
                localArrayList.add("锡林浩特");
                localArrayList.add("乌兰浩特");
                localArrayList.add("阿拉善左旗");
                return localArrayList;
            case 80:
                localArrayList.add("沈阳");
                localArrayList.add("铁岭");
                localArrayList.add("大连");
                localArrayList.add("鞍山");
                localArrayList.add("抚顺");
                localArrayList.add("本溪");
                localArrayList.add("丹东");
                localArrayList.add("锦州");
                localArrayList.add("营口");
                localArrayList.add("阜新");
                localArrayList.add("辽阳");
                localArrayList.add("朝阳");
                localArrayList.add("盘锦");
                localArrayList.add("葫芦岛");
                return localArrayList;
            case 90:
                localArrayList.add("长春");
                localArrayList.add("吉林");
                localArrayList.add("延吉州");
                localArrayList.add("四平");
                localArrayList.add("通化");
                localArrayList.add("白城");
                localArrayList.add("辽源");
                localArrayList.add("松原");
                localArrayList.add("白山");
                localArrayList.add("珲春");
                localArrayList.add("梅河口");
                return localArrayList;
            case 11:
                localArrayList.add("哈尔滨");
                localArrayList.add("齐齐哈尔");
                localArrayList.add("牡丹江");
                localArrayList.add("佳木斯");
                localArrayList.add("绥化");
                localArrayList.add("黑河");
                localArrayList.add("大兴安岭");
                localArrayList.add("伊春");
                localArrayList.add("大庆");
                localArrayList.add("七台河");
                localArrayList.add("鸡西");
                localArrayList.add("鹤岗");
                localArrayList.add("双鸭山");
                return localArrayList;
            case 12:
                localArrayList.add("南京");
                localArrayList.add("无锡");
                localArrayList.add("镇江");
                localArrayList.add("苏州");
                localArrayList.add("南通");
                localArrayList.add("扬州");
                localArrayList.add("盐城");
                localArrayList.add("徐州");
                localArrayList.add("淮安");
                localArrayList.add("连云港");
                localArrayList.add("常州");
                localArrayList.add("泰州");
                localArrayList.add("宿迁");
                return localArrayList;
            case 13:
                localArrayList.add("衢州");
                localArrayList.add("杭州");
                localArrayList.add("湖州");
                localArrayList.add("嘉兴");
                localArrayList.add("宁波");
                localArrayList.add("绍兴");
                localArrayList.add("台州");
                localArrayList.add("温州");
                localArrayList.add("丽水");
                localArrayList.add("金华");
                localArrayList.add("舟山");
                return localArrayList;
            case 14:
                localArrayList.add("滁州");
                localArrayList.add("合肥");
                localArrayList.add("蚌埠");
                localArrayList.add("芜湖");
                localArrayList.add("淮南");
                localArrayList.add("马鞍山");
                localArrayList.add("安庆");
                localArrayList.add("宿州");
                localArrayList.add("阜阳");
                localArrayList.add("黄山");
                localArrayList.add("淮北");
                localArrayList.add("铜陵");
                localArrayList.add("宣城");
                localArrayList.add("六安");
                localArrayList.add("巢湖");
                localArrayList.add("池州");
                return localArrayList;
            case 15:
                localArrayList.add("福州");
                localArrayList.add("厦门");
                localArrayList.add("宁德");
                localArrayList.add("莆田");
                localArrayList.add("泉州");
                localArrayList.add("漳州");
                localArrayList.add("龙岩");
                localArrayList.add("三明");
                localArrayList.add("南平");
                return localArrayList;
            case 16:
                localArrayList.add("新余");
                localArrayList.add("南昌");
                localArrayList.add("九江");
                localArrayList.add("上饶");
                localArrayList.add("抚州");
                localArrayList.add("宜春");
                localArrayList.add("吉安");
                localArrayList.add("赣州");
                localArrayList.add("景德镇");
                localArrayList.add("萍乡");
                localArrayList.add("鹰潭");
                return localArrayList;
            case 17:
                localArrayList.add("菏泽");
                localArrayList.add("济南");
                localArrayList.add("青岛");
                localArrayList.add("淄博");
                localArrayList.add("德州");
                localArrayList.add("烟台");
                localArrayList.add("潍坊");
                localArrayList.add("济宁");
                localArrayList.add("泰安");
                localArrayList.add("临沂");
                localArrayList.add("滨州");
                localArrayList.add("东营");
                localArrayList.add("威海");
                localArrayList.add("枣庄");
                localArrayList.add("日照");
                localArrayList.add("莱芜");
                localArrayList.add("聊城");
                return localArrayList;
            case 18:
                localArrayList.add("商丘");
                localArrayList.add("郑州");
                localArrayList.add("安阳");
                localArrayList.add("新乡");
                localArrayList.add("许昌");
                localArrayList.add("平顶山");
                localArrayList.add("信阳");
                localArrayList.add("南阳");
                localArrayList.add("开封");
                localArrayList.add("洛阳");
                localArrayList.add("焦作");
                localArrayList.add("鹤壁");
                localArrayList.add("濮阳");
                localArrayList.add("周口");
                localArrayList.add("漯河");
                localArrayList.add("驻马店");
                localArrayList.add("三门峡");
                return localArrayList;
            case 19:
                localArrayList.add("武汉");
                localArrayList.add("襄樊");
                localArrayList.add("鄂州");
                localArrayList.add("孝感");
                localArrayList.add("黄冈");
                localArrayList.add("黄石");
                localArrayList.add("咸宁");
                localArrayList.add("荆州");
                localArrayList.add("宜昌");
                localArrayList.add("恩施");
                localArrayList.add("十堰");
                localArrayList.add("随州");
                localArrayList.add("荆门");
                localArrayList.add("仙桃");
                return localArrayList;
            case 21:
                localArrayList.add("岳阳");
                localArrayList.add("长沙");
                localArrayList.add("湘潭");
                localArrayList.add("株洲");
                localArrayList.add("衡阳");
                localArrayList.add("郴州");
                localArrayList.add("常德");
                localArrayList.add("益阳");
                localArrayList.add("娄底");
                localArrayList.add("邵阳");
                localArrayList.add("吉首");
                localArrayList.add("张家界");
                localArrayList.add("怀化");
                localArrayList.add("永州");
                return localArrayList;
            case 22:
                localArrayList.add("广州");
                localArrayList.add("汕尾");
                localArrayList.add("阳江");
                localArrayList.add("揭阳");
                localArrayList.add("茂名");
                localArrayList.add("江门");
                localArrayList.add("韶关");
                localArrayList.add("惠州");
                localArrayList.add("梅州");
                localArrayList.add("汕头");
                localArrayList.add("深圳");
                localArrayList.add("珠海");
                localArrayList.add("佛山");
                localArrayList.add("肇庆");
                localArrayList.add("湛江");
                localArrayList.add("中山");
                localArrayList.add("河源");
                localArrayList.add("清远");
                localArrayList.add("云浮");
                localArrayList.add("潮州");
                localArrayList.add("东莞");
                return localArrayList;
            case 23:
                localArrayList.add("防城港");
                localArrayList.add("南宁");
                localArrayList.add("柳州");
                localArrayList.add("桂林");
                localArrayList.add("梧州");
                localArrayList.add("玉林");
                localArrayList.add("百色");
                localArrayList.add("钦州");
                localArrayList.add("河池");
                localArrayList.add("北海");
                return localArrayList;
            case 25:
                localArrayList.add("成都");
                localArrayList.add("攀枝花");
                localArrayList.add("自贡");
                localArrayList.add("绵阳");
                localArrayList.add("南充");
                localArrayList.add("达县");
                localArrayList.add("遂宁");
                localArrayList.add("广安");
                localArrayList.add("巴中");
                localArrayList.add("泸州");
                localArrayList.add("宜宾");
                localArrayList.add("内江资阳");
                localArrayList.add("乐山眉山");
                localArrayList.add("西昌");
                localArrayList.add("雅安");
                localArrayList.add("康定");
                localArrayList.add("阿坝");
                localArrayList.add("德阳");
                localArrayList.add("广元");
                return localArrayList;
            case 26:
                localArrayList.add("贵阳");
                localArrayList.add("遵义");
                localArrayList.add("安顺");
                localArrayList.add("都匀");
                localArrayList.add("凯里");
                localArrayList.add("铜仁");
                localArrayList.add("毕节");
                localArrayList.add("六盘水");
                localArrayList.add("兴义");
                return localArrayList;
            case 27:
                localArrayList.add("版纳");
                localArrayList.add("德宏");
                localArrayList.add("昭通");
                localArrayList.add("昆明");
                localArrayList.add("大理");
                localArrayList.add("红河");
                localArrayList.add("曲靖");
                localArrayList.add("保山");
                localArrayList.add("文山");
                localArrayList.add("玉溪");
                localArrayList.add("楚雄");
                localArrayList.add("思茅");
                localArrayList.add("临沧");
                localArrayList.add("怒江");
                localArrayList.add("迪庆");
                localArrayList.add("丽江");
                return localArrayList;
            case 28:
                localArrayList.add("拉萨");
                localArrayList.add("日喀则");
                localArrayList.add("山南");
                localArrayList.add("林芝");
                localArrayList.add("昌都");
                localArrayList.add("那曲");
                localArrayList.add("阿里");
                return localArrayList;
            case 29:
                localArrayList.add("西安");
                localArrayList.add("咸阳");
                localArrayList.add("延安");
                localArrayList.add("榆林");
                localArrayList.add("渭南");
                localArrayList.add("商州");
                localArrayList.add("安康");
                localArrayList.add("汉中");
                localArrayList.add("宝鸡");
                localArrayList.add("铜川");
                return localArrayList;
            case 31:
                localArrayList.add("临夏");
                localArrayList.add("兰州");
                localArrayList.add("定西");
                localArrayList.add("平凉");
                localArrayList.add("西峰");
                localArrayList.add("金武");
                localArrayList.add("张掖");
                localArrayList.add("嘉酒");
                localArrayList.add("天水");
                localArrayList.add("武都");
                localArrayList.add("甘南");
                localArrayList.add("白银");
                return localArrayList;
            case 32:
                localArrayList.add("银川");
                localArrayList.add("石嘴山");
                localArrayList.add("吴忠");
                localArrayList.add("固原");
                localArrayList.add("中卫");
                return localArrayList;
            case 33:
                localArrayList.add("海北");
                localArrayList.add("西宁");
                localArrayList.add("海东");
                localArrayList.add("黄南");
                localArrayList.add("青海海南");
                localArrayList.add("果洛");
                localArrayList.add("玉树");
                localArrayList.add("海西");
                localArrayList.add("格尔木");
                return localArrayList;
            case 34:
        }
        localArrayList.add("塔城");
        localArrayList.add("哈密");
        localArrayList.add("和田");
        localArrayList.add("阿勒泰");
        localArrayList.add("克州");
        localArrayList.add("博州");
        localArrayList.add("克拉玛依");
        localArrayList.add("乌鲁木齐");
        localArrayList.add("奎屯");
        localArrayList.add("石河子");
        localArrayList.add("昌吉");
        localArrayList.add("吐鲁番");
        localArrayList.add("巴州");
        localArrayList.add("阿克苏");
        localArrayList.add("喀什");
        localArrayList.add("伊犁州");
        return localArrayList;
    }

    public static int getCityCodeByName(String paramString1, String paramString2) {
        int i = -1;
        int j = getProvincesCodeByName(paramString1);
        ArrayList localArrayList = getCitys(j);
        if (1 == localArrayList.size()) {
            return j * 100;
        }
        for (int k = 0; k < localArrayList.size(); ++k) {
            if (paramString2.equals(localArrayList.get(k))) {
                i = j * 100 + (k + 1);
                return i;
            }
        }
        return i;
    }

    public static int getProvincesCodeByName(String paramString) {
        for (int i = 0; ; ++i) {
            if (i >= provList.size())
                ;
            if (!paramString.equals(provList.get(i)))
                if (i < 9)
                    return 10 * (i + 1);
            if ((i >= 9) && (i < 18))
                return i + 2;
            if ((i >= 18) && (i < 27))
                return i + 3;
        }
    }

    public static String getProvinceNameByCode(int paramInt) {
        String str = "";
        if (paramInt % 1000 == 0) {
            int i = paramInt / 1000;
            if ((i >= 1) && (i <= 9))
                str = provList.get(i - 1);
        }
        do {
            if ((paramInt >= 1100) && (paramInt <= 1900))
                return provList.get(-2 + paramInt / 100);
            if ((paramInt >= 2100) && (paramInt <= 2900))
                return provList.get(-3 + paramInt / 100);
        }
        while ((paramInt < 3100) || (paramInt > 3400));
        return provList.get(-4 + paramInt / 100);
    }

    public static String getCityNameByCode(int paramInt) {
        if (paramInt % 1000 == 0) {
            int j = paramInt / 1000;
            if ((j >= 1) && (j <= 4)) {
                ArrayList localArrayList2 = getCitys(paramInt / 100);
                if (localArrayList2.size() > 0)
                    return (String) localArrayList2.get(0);
            }
        } else {
            int i = paramInt % 100;
            ArrayList localArrayList1 = getCitys(paramInt / 100);
            if ((localArrayList1.size() > 0) && (i > 0))
                return (String) localArrayList1.get(i - 1);
        }
        return "";
    }
}
