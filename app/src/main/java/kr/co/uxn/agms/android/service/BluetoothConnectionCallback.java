package kr.co.uxn.agms.android.service;


public interface BluetoothConnectionCallback {

    void onUartChanged(byte[] uartData, String address);
    void doOnConnectionStateChange(String address, int status, int newState, int mConnectionState, boolean showReconnectNoti);
}
