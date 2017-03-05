package snowpool.googlemaptest;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //マーカーをアイコン変更
    private GoogleMap mMap=null;
    private SupportMapFragment mapFragment;
    private LatLng latLng;
    private LatLng location;
    private int width,height;
    private double longitude,latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //緯度経度決め打ち
        latitude =35.68;
        longitude = 139.76;
        latLng = new LatLng(latitude,longitude);

        //マーカーの追加
        mMap.addMarker(new MarkerOptions().position(latLng).title("Tokyo"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

        //タップしたときの処理
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng taplatLng) {
                //タップした場所の緯度経度取得
                location = new LatLng(taplatLng.latitude,taplatLng.longitude);
                //タップした場所へマーカー設定
                mMap.addMarker(new MarkerOptions().position(location).title("tappiint is "+taplatLng.latitude+" : "+taplatLng.longitude));
                //カメラ移動
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,14));
            }
        });

        //長押しの処理
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng longtaplatLng) {
                LatLng longtaplocation = new LatLng(longtaplatLng.latitude,longtaplatLng.longitude);
                mMap.addMarker(new MarkerOptions().position(longtaplocation).title("longtap point is "+longtaplatLng.latitude+" : "+longtaplatLng.longitude));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(longtaplocation,14));
            }
        });
        //アイコン画像をマーカーにする
//        setIcon(latitude,longitude);


//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//
//        //地図の移動
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(35.68, 139.76),12);
//        mMap.moveCamera(cameraUpdate);
    }
//
//    //マーカーの設定
//    private void setMarker(double latitude,double longitude){
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Marker");
//        //地図にマーカー設定
//        mMap.addMarker(markerOptions);
//        //ズーム設定
//        zoomMap(latitude, longitude);
//    }
//
//    //アイコン設定
//    private void setIcon(double latitude, double longitude){
//        //map に貼り付ける Bitmapdescripter 設定
//        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_speaker_dark);
//        //貼り付け設定
//        GroundOverlayOptions overlayOptions = new GroundOverlayOptions();
//        overlayOptions.image(descriptor);
//
//        //アンカーの座標指定
//        overlayOptions.anchor(0.5f,0.5f);
//
//        //地図に表示する画像の大きさ設定
//        overlayOptions.position(latLng,300f,300f);
//
//        //設定した画像をマップへ表示
//        GroundOverlay overlay = mMap.addGroundOverlay(overlayOptions);
//
//        //ズーム設定
//        zoomMap(latitude,longitude);
//
//        //透明度設定
//        overlay.setTransparency(0.0F);
//    }
//
//    private void zoomMap(double latitude, double longitude){
//        // 表示する東西南北の緯度経度を設定
//        double south = latitude * (1-0.00005);
//        double west = longitude * (1-0.00005);
//        double north = latitude * (1+0.00005);
//        double east = longitude * (1+0.00005);
//
//        // LatLngBounds (LatLng southwest, LatLng northeast)
//        LatLngBounds bounds = LatLngBounds.builder()
//                .include(new LatLng(south , west))
//                .include(new LatLng(north, east))
//                .build();
//
//        width = getResources().getDisplayMetrics().widthPixels;
//        height = getResources().getDisplayMetrics().heightPixels;
//
//        // static CameraUpdate.newLatLngBounds(LatLngBounds bounds, int width, int height, int padding)
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
//
//    }
}
