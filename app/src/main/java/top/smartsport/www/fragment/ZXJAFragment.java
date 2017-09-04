package top.smartsport.www.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import app.base.JsonUtil;
import app.base.MapAdapter;
import app.base.MapContent;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;

import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.widget.MyGridView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Aaron on 2017/7/24.
 * 青训--在线案例
 */
@ContentView(R.layout.fragment_zxja)
public class ZXJAFragment extends BaseFragment {
    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;
    private PullToRefreshScrollView pullToRefreshScrollView;

    public static ZXJAFragment newInstance() {
        ZXJAFragment fragment = new ZXJAFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected void initView() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        pullToRefreshScrollView = (PullToRefreshScrollView) root.findViewById(R.id.pullrefreshlistview);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getData();

            }
        });
        getData();
    }


    private void getData() {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getNewOnlineCourses");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                String name = "begin";
                int gridid = R.id.rumenjigrid;
                setGrid(entity, name, gridid);
                setGrid(entity, "primary", R.id.chujigrid);
                setGrid(entity, "middle", R.id.zhongjigrid);
                setGrid(entity, "senior", R.id.gaojigrid);
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });

    }

    private void setGrid(NetEntity entity, String name, int gridid) {
        MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
        adaptinfo.addListviewItemLayoutId(R.layout.qingxun_zaixianjiaoan);
        adaptinfo.addObjectFields(new String[]{"cover", "hours", "title"});
        adaptinfo.addViewIds(new Integer[]{R.id.shipin, R.id.duration, R.id.kechengmingzi, R.id.xueqi_hours});

        MapAdapter mapadapter = new MapAdapter(getContext(), adaptinfo) {
            @Override
            protected boolean findAndBindView(View convertView, int pos, Object item, String name, Object value) {
                return super.findAndBindView(convertView, pos, item, name, value);
            }

            @Override
            protected void getViewInDetail(Object item, int position, View convertView) {
                super.getViewInDetail(item, position, convertView);
                if (!((TextView) convertView.findViewById(R.id.duration)).getText().toString().endsWith("小时")) {
                    ((TextView) convertView.findViewById(R.id.duration)).append("小时");
                }

                String time = "第" + map.get(((Map) item).get("term").toString()) + "学期 | 共" + ((Map) item).get("hours").toString() + "个课时";
                ((TextView) convertView.findViewById(R.id.xueqi_hours)).setText(time);
            }
        };

        List list = (List) JsonUtil.extractJsonRightValue(JsonUtil.findJsonLink(name, entity.getData().toString()).toString());

        mapadapter.setItemDataSrc(new MapContent(list));
        ((MyGridView) root.findViewById(gridid)).setAdapter(mapadapter);
    }

    Map map = MapBuilder.build().add("1", "一").add("2", "二").add("3", "三").add("4", "四").add("5", "五").add("6", "六").add("7", "七").add("8", "八").add("9", "九").get();
}
