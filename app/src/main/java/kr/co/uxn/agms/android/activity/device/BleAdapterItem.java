package kr.co.uxn.agms.android.activity.device;

public class BleAdapterItem {

    private String uuid;
    private String name;
    private int rssi;

    public BleAdapterItem() {
    }

    public BleAdapterItem(String uuid, String name, int rssi) {
        this.uuid = uuid;
        this.name = name;
        this.rssi = rssi;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
