package com.sdoward.rxgooglemaps;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.sdoward.rxgooglemap.MapObservableProvider;

import rx.functions.Action1;
import rx.subscriptions.*;

public class MapsActivity extends FragmentActivity {

    private SupportMapFragment mapFragment;
    private MapObservableProvider mapObservableProvider;
    private CompositeSubscription subscriptions = Subscriptions.from();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapObservableProvider = new MapObservableProvider(mapFragment);
        subscriptions.add(mapObservableProvider.getMapReadyObservable()
                .subscribe(new Action1<GoogleMap>() {
                    @Override
                    public void call(GoogleMap googleMap) {
                        Log.d(MapsActivity.class.getName(), "map ready");
                    }
                }));
        subscriptions.add(mapObservableProvider.getMapLongClickObservable()
                .subscribe(new Action1<LatLng>() {
                    @Override
                    public void call(LatLng latLng) {
                        Log.d(MapsActivity.class.getName(), "map long click");
                    }
                }));
        subscriptions.add(mapObservableProvider.getMapClickObservable()
                .subscribe(new Action1<LatLng>() {
                    @Override
                    public void call(LatLng latLng) {
                        Log.d(MapsActivity.class.getName(), "map click");
                    }
                }));
        subscriptions.add(mapObservableProvider.getCameraChangeObservable().subscribe(new Action1<CameraPosition>() {
            @Override
            public void call(CameraPosition cameraPosition) {
                Log.d(MapsActivity.class.getName(), "camera position changed");
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}
