package snowpool.googlemaptest;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMyLocationButtonClickListener,LocationSource,LocationListener{

    //マーカーをアイコン変更
//    private GoogleMap mMap=null;
//    private SupportMapFragment mapFragment;
//    private LatLng latLng;
//    private LatLng location;
//    private int width,height;
//    private double longitude,latitude;

    //gps で現在地表示
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private LocationSource.OnLocationChangedListener onLocationChangedListener = null;

    //GPS 制度設定
    private int priority[] ={
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
            LocationRequest.PRIORITY_LOW_POWER,
            LocationRequest.PRIORITY_NO_POWER
    };
    private int locationPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // LocationRequest を生成して精度、インターバルを設定
        locationRequest = LocationRequest.create();

        // 測位の精度、消費電力の優先度
        locationPriority = priority[1];

        if(locationPriority == priority[0]){
            // 位置情報の精度を優先する場合
            locationRequest.setPriority(locationPriority);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(16);
        }
        else if(locationPriority == priority[1]){
            // 消費電力を考慮する場合
            locationRequest.setPriority(locationPriority);
            locationRequest.setInterval(60000);
            locationRequest.setFastestInterval(16);
        }
        else if(locationPriority == priority[2]){
            // "city" level accuracy
            locationRequest.setPriority(locationPriority);
        }
        else{
            // 外部からのトリガーでの測位のみ
            locationRequest.setPriority(locationPriority);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //onResume() で接続
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //onPause で切断
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("debug", "permission granted");

            // FusedLocationApi
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, locationRequest, this);
        }
        else{
            Log.d("debug", "permission error");
            return;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "onMyLocationButtonClick", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        this.onLocationChangedListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (onLocationChangedListener != null) {
            onLocationChangedListener.onLocationChanged(location);

            double lat = location.getLatitude();
            double lng = location.getLongitude();

            Log.d("debug","location="+lat+","+lng);

            Toast.makeText(this, "location="+lat+","+lng, Toast.LENGTH_SHORT).show();

            // Add a marker and move the camera
            LatLng newLocation = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(newLocation).title("My Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation,18));

        }
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

        //permission check
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap = googleMap;
            // default の LocationSource から自前のsourceに変更する
            mMap.setLocationSource(this);
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
        }else {
            return;
        }



//        //緯度経度決め打ち
//        latitude =35.68;
//        longitude = 139.76;
//        latLng = new LatLng(latitude,longitude);
//
//        //マーカーの追加
//        mMap.addMarker(new MarkerOptions().position(latLng).title("Tokyo"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
//
//        //タップしたときの処理
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng taplatLng) {
//                //タップした場所の緯度経度取得
//                location = new LatLng(taplatLng.latitude,taplatLng.longitude);
//                //タップした場所へマーカー設定
//                mMap.addMarker(new MarkerOptions().position(location).title("tappiint is "+taplatLng.latitude+" : "+taplatLng.longitude));
//                //カメラ移動
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,14));
//            }
//        });
//
//        //長押しの処理
//        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(LatLng longtaplatLng) {
//                LatLng longtaplocation = new LatLng(longtaplatLng.latitude,longtaplatLng.longitude);
//                mMap.addMarker(new MarkerOptions().position(longtaplocation).title("longtap point is "+longtaplatLng.latitude+" : "+longtaplatLng.longitude));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(longtaplocation,14));
//            }
//        });

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
