package com.jysd.englishframe;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jysd.englishframe.impl.IBase;
import com.jysd.englishframe.utils.AppManager;
import com.jysd.englishframe.utils.ContextUtils;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements IBase {

    /**
     * 是否设置沉浸式
     *
     * @return
     */
//    protected boolean isSetStatusBar() {
//        return false;
//    }
    protected View mRootView;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        getIntentValue();
        mRootView = createView(null, null, savedInstanceState);
        setContentView(mRootView);
        mToolbar = (Toolbar) findViewById(getToolBarId());
        setSupportActionBar(mToolbar);
        setActionBar();
        bindView(savedInstanceState);

    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = ContextUtils.inflate(this, getContentLayout());
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public View getView() {
        return mRootView;
    }
    public Toolbar getToolbar() {
        return mToolbar;
    }
    public abstract int getToolBarId();
    public void getIntentValue() {
    }
    public void setActionBar() {
    }
//    @TargetApi(19)
//    private void initWindow() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isSetStatusBar()) {
//            getWindow().addFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.material_drawer_primary);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }
}
