package top.smartsport.www.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import app.base.JsonUtil;
import intf.MapBuilder;
import intf.QXFunCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.adapter.QXZXAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;

import top.smartsport.www.fragment.BSSCFragment;

import top.smartsport.www.fragment.QXFragment;
import top.smartsport.www.widget.PagerSlidingTabStrip;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aaron on 2017/7/25.
 * 课程表
 */
@ContentView(R.layout.fragment_qx)
public class KCBActivity extends BaseActivity {
    public static final String TAG = "QXFragment";
    @ViewInject(R.id.qx_tab)
    private PagerSlidingTabStrip qx_tab;
    @ViewInject(R.id.qx_viewpager)
    private ViewPager qx_viewpager;
    private QXZXAdapter qxzxAdapter;//比赛,直播adapter
    private FragmentManager fragmentManager;

    private List<Fragment> listFM;

    public static QXFragment newInstance() {
        QXFragment fragment = new QXFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    int count;

    private void addFragment() {

    }

    //[{"round":"1","list":[{"id":"1","match_id":"1","round":"1","home_team":"4","away_team":"21","home_score":"3","away_score":"2","start_time":"2017-07-14","end_time":"2017-07-14","status":"0","home_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","away_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","home_name":"我们是最棒的球队","away_name":"上海中国中学"},{"id":"9","match_id":"1","round":"1","home_team":"1","away_team":"14","home_score":null,"away_score":null,"start_time":"2017-07-06","end_time":"2017-08-31","status":"进行中","home_logo":"http://soccer.baibaobike.com/data/upload/default/logo.png","away_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","home_name":"人大附小队","away_name":"上海田林第三小学"},{"id":"12","match_id":"1","round":"1","home_team":"4","away_team":"14","home_score":null,"away_score":null,"start_time":"2017-08-16","end_time":"2017-08-18","status":"0","home_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","away_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","home_name":"我们是最棒的球队","away_name":"上海田林第三小学"}]},{"round":"3","list":[{"id":"8","match_id":"1","round":"3","home_team":"4","away_team":"1","home_score":null,"away_score":null,"start_time":"2017-07-12","end_time":"2017-08-27","status":"2","home_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","away_logo":"http://soccer.baibaobike.com/data/upload/default/logo.png","home_name":"我们是最棒的球队","away_name":"人大附小队"},{"id":"11","match_id":"1","round":"3","home_team":"1","away_team":"2","home_score":null,"away_score":null,"start_time":"2017-08-04","end_time":"2017-08-31","status":"进行中","home_logo":"http://soccer.baibaobike.com/data/upload/default/logo.png","away_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","home_name":"人大附小队","away_name":"中国少年球队"}]},{"round":"4","list":[{"id":"6","match_id":"1","round":"4","home_team":"14","away_team":"1","home_score":"2","away_score":"1","start_time":"2017-07-18","end_time":"2017-07-28","status":"0","home_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","away_logo":"http://soccer.baibaobike.com/data/upload/default/logo.png","home_name":"上海田林第三小学","away_name":"人大附小队"},{"id":"10","match_id":"1","round":"4","home_team":"3","away_team":"21","home_score":null,"away_score":null,"start_time":"2017-07-21","end_time":"2017-08-31","status":"进行中","home_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","away_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","home_name":"嘻哈快乐球队2","away_name":"上海中国中学"}]}]
    @Override
    protected void initView() {
        fragmentManager = getSupportFragmentManager();
        addFragment();
        callHttp(MapBuilder.build().add("match_id", "1").add("action", "getSchedule").get(), new QXFunCallback() {
            @Override
            public void onSuccess(NetEntity result, List<Object> object) {

            }

            @Override
            public void onFailure(String result, List<Object> object) {

            }

            @Override
            public void onCallback(NetEntity result, List<Object> object) {
                List list = (List) JsonUtil.extractJsonRightValue(result.getData().toString());
                count = list.size();
                List<String> titles = new ArrayList<String>();
                Map m = MapBuilder.build().add("1", "一").add("2", "二").add("3", "三").add("4", "四").add("5", "五").add("6", "六").add("7", "七").add("8", "九").add("9", "十").get();
                listFM = new ArrayList<>();
                for (int i = 1; i <= count; i++) {
                    listFM.add(BSSCFragment.newInstance());
                    titles.add("第" + m.get(i + "").toString() + "场");

                }


                String[] ts = new String[titles.size()];
                titles.toArray(ts);

                qxzxAdapter = new QXZXAdapter(getApplication(), fragmentManager, ts, listFM);
                qx_viewpager.setAdapter(qxzxAdapter);


                qx_tab.setViewPager(qx_viewpager);
                findViewById(R.id.bs_ll_choice).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        });
    }

    /**
     * 获取接口
     */
    private void getData() {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", getClient_id());
            json.put("state", getState());
            json.put("access_token", getAccess_token());
            json.put("action", "getSchedule");
            json.put("match_id", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(getUrl(), json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {

            }

            @Override
            public void onSuccess(NetEntity entity) {

            }
        });
    }


}
