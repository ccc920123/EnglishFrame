package com.jysd.englishframe.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jysd.englishframe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_project, container, false);
        ButterKnife.bind(this, view);
        initing();
        return view;
    }

    /**
     * 开始处理事件
     */
    private void initing() {
//        SwipeRefreshLayout 的使用
//        void setColorSchemeColors(int color1, int color2, int color3, int color4)  设置四种颜色进度条样式
//        void setRefreshing(boolean refreshing)  隐藏或显示进度条
//        boolean isRefreshing()  判断进度条是否显示

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.holo_green_light,
                R.color.holo_green_light,
                R.color.holo_orange_light,
                R.color.holo_red_light);





    }

    /**
     * 重新刷新页面
     */
    @Override
    public void onRefresh() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
