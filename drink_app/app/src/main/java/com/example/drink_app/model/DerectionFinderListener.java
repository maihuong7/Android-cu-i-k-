package com.example.drink_app.model;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public interface DerectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
