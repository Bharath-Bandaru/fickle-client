package info.fickle.fickleclient;

import android.net.wifi.ScanResult;

import java.util.List;

public interface ScanResultsListener {
    void onScanResultsAvailable(List<ScanResult> scanResults);
}