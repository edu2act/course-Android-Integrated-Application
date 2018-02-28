package com.ruanjian.demo03overlay;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 百度地图视图
    private MapView mMapView = null;
    // 百度地图控制器
    private BaiduMap mBaiduMap = null;
    // 百度地图UI控制器
    private UiSettings mUiSettings = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        initBaiduMap();
    }

    // =============接口实现==========================
    /**
     * 菜单创建
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 菜单点击响应
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_map_mark:      // 标注覆盖物
                addMarkerOverlay();
                break;

            case R.id.id_map_polygon:   // 多边形覆盖物
                addPolygonOverlay();
                break;

            case R.id.id_map_polyline:  // 添加折线覆盖物
                addPolylineOverlay();
                break;

            case R.id.id_map_text:      // 文本覆盖物
                addTextOverLay();
                break;
            
            case R.id.id_map_ground:    // 地形图图层覆盖物
                addGroundOverLay();
                break;

            case R.id.id_map_infoWindow:// 弹出窗覆盖物
                addInfoWindow();
                break;

            case R.id.id_map_clear:     // 清空
                mBaiduMap.clear();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // =================自定义函数====================
    /**
     * 始化Baidu地图相关设置
     */
    private void initBaiduMap() {
        // 获取地图视图
        mMapView = (MapView) findViewById(R.id.bmapView);
        // 获取地图控制器
        mBaiduMap = mMapView.getMap();
        // 设置比例尺为500M
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        // 获取地图UI控制器
        mUiSettings = mBaiduMap.getUiSettings();
        // 隐藏指南针
        mUiSettings.setCompassEnabled(false);

        // 设置标注覆盖物的监听
        setMarkerListener();
    }

    /**
     * 设置标注覆盖物监听
     */
    private void setMarkerListener() {
        // 调用BaiduMap对象的setOnMarkerClickListener方法
        // 设置marker点击事件的监听
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                // 点击处理
                Toast.makeText(MainActivity.this,
                        marker.getTitle(),
                        Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        // 调用BaiduMap对象的setOnMarkerDragListener方法
        // 设置marker拖拽事件的监听
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            private boolean bFirst = true;
            public void onMarkerDrag(Marker marker) {
                // 拖拽中
                if(bFirst) {
                    Toast.makeText(MainActivity.this,
                            "拖拽中",
                            Toast.LENGTH_SHORT)
                            .show();
                    bFirst = false;
                }
            }
            public void onMarkerDragEnd(Marker marker) {
                // 拖拽结束
                Toast.makeText(MainActivity.this,
                        "拖拽结束",
                        Toast.LENGTH_SHORT)
                        .show();
                bFirst = true;
            }
            public void onMarkerDragStart(Marker marker) {
                // 开始拖拽
                Toast.makeText(MainActivity.this,
                        "开始拖拽",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /**
     * 添加标注覆盖物
     */
    private void addMarkerOverlay() {
        // 定义Maker坐标点
        LatLng point = new LatLng(39.913285, 116.403923);
        // 构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.marker);
        // 构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)    // 设置marker的位置
                .draggable(true)    // 设置是否允许拖拽
                .title("国旗")       // 设置marker的title
                .icon(bitmap);      // 必须设置marker图标
        //在地图上添加Marker，并显示
        Marker marker = (Marker) mBaiduMap.addOverlay(option);
    }

    /**
     * 添加多边形覆盖物
     */
    private void addPolygonOverlay() {
        // 定义多边形的五个顶点
        LatLng pt1 = new LatLng(39.919857,  116.399054);
        LatLng pt2 = new LatLng(39.92023,  116.408118);
        LatLng pt3 = new LatLng(39.929084,  116.407651);
        LatLng pt4 = new LatLng(39.928731,  116.398578);

        List<LatLng> pts = new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);

        // 构建用户绘制多边形的Option对象
        OverlayOptions polygonOption = new PolygonOptions()
                .points(pts)    // 点信息
                .stroke(new Stroke(5, 0xAAFF0000))  // 边框
                .fillColor(0xAAFFFF00); // 填充颜色

        // 在地图上添加多边形Option，用于显示
        mBaiduMap.addOverlay(polygonOption);
    }

    /**
     * 添加折线覆盖物
     */
    private void addPolylineOverlay() {
        // 构造折线点坐标
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(new LatLng(39.913776,116.398039));
        points.add(new LatLng(39.913887,116.402063));
        points.add(new LatLng(39.909325,116.402409));
        points.add(new LatLng(39.909463,116.404107));
        // 构建分段颜色索引数组
        List<Integer> colors = new ArrayList<>();
        colors.add(Integer.valueOf(Color.RED));
        colors.add(Integer.valueOf(Color.YELLOW));
        colors.add(Integer.valueOf(Color.GREEN));
        OverlayOptions polyline = new PolylineOptions()
                .width(15)              // 宽度
                .colorsValues(colors)   // 颜色信息
                .points(points);        // 点信息
        // 添加在地图中
        mBaiduMap.addOverlay(polyline);
    }

    /**
     * 添加文本覆盖物
     */
    private void addTextOverLay() {
        // 定义文字所显示的坐标点
        LatLng llText = new LatLng(39.912739, 116.404017);
        // 构建文字Option对象，用于在地图上添加文字
        OverlayOptions textOption = new TextOptions()
                .bgColor(0xAAFFFF00)    // 背景颜色
                .fontSize(32)           // 字号
                .fontColor(0xFFFF00FF)  // 字体颜色
                .text("天安门广场")      // 文本
                .position(llText);      // 位置
        // 在地图上添加该文字对象并显示
        mBaiduMap.addOverlay(textOption);

    }

    /**
     * 添加地形图图层覆盖物
     */
    private void addGroundOverLay() {
        // 定义Ground显示的图片
        BitmapDescriptor bdGround = BitmapDescriptorFactory
                .fromResource(R.drawable.gugong);

        LatLng southwest = new LatLng(39.919404, 116.398035);//西南
        LatLng northeast = new LatLng(39.92964, 116.408527);//东北
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(southwest)
                .include(northeast)
                .build();//得到一个地理范围对象

        // 定义Ground覆盖物选项
        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(bdGround)
                .transparency(0.8f);

        // 在地图中添加Ground覆盖物
        mBaiduMap.addOverlay(ooGround);

    }

    /**
     * 添加弹出窗覆盖物
     */
    private void addInfoWindow() {
        // 创建infowindow展示的view
        TextView location = new TextView(getApplicationContext());
        location.setBackgroundResource(R.drawable.popup);
        location.setPadding(15, 5, 15, 20);
        location.setText("天安门东");
        location.setTextColor(Color.WHITE);
        location.setGravity(Gravity.CENTER);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromView(location);

        // Infowindow点击事件
        InfoWindow.OnInfoWindowClickListener infoWindowClickListener
                = new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                // 隐藏InfoWindow
                mBaiduMap.hideInfoWindow();
            }
        };

        // 创建Infowindow
        LatLng point = new LatLng(39.914088, 116.407844);
        InfoWindow infoWindow
                = new InfoWindow(bitmapDescriptor,
                point,
                0,
                infoWindowClickListener);
        // 显示InfoWindow
        mBaiduMap.showInfoWindow(infoWindow);

    }
}
