package demo.sharesdk.cn.mywing_s;

/**
 * Created by Administrator on 2018/4/1.
 */

public interface Mcallback {
    void initSuccess();
    void connSuccess();

    void getPingParamOK(int pingTimes, int ping_gap, int passTime,
                        int pkgSize, String pingIP);

    void getServerParamOK(int province, int city, int serverDevID,
                          int port, String serverIP);

    void isSetPingParamOK();

    void isSetServerParamOK();
}
