package com.jysd.englishframe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.jysd.englishframe.adapter.FragmentAdapter;
import com.pan.materialdrawer.AccountHeader;
import com.pan.materialdrawer.Drawer;
import com.pan.materialdrawer.DrawerBuilder;
import com.pan.materialdrawer.holder.StringHolder;
import com.pan.materialdrawer.model.PrimaryDrawerItem;
import com.pan.materialdrawer.model.interfaces.IDrawerItem;
import com.pan.materialdrawer.util.RecyclerViewCacheUtil;

import java.util.Arrays;

import butterknife.Bind;


public class MainActivity extends BaseActivity {


    //使用侧滑框架
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private int dId = 1;
    @Bind(R.id.viewpager)
    public ViewPager mViewPager;
    @Bind(R.id.tabs)
    public TabLayout mTabLayout;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

    /**
     *
     * @return toolbar
     */
    @Override
    public int getToolBarId() {
        return R.id.toolbar;
    }

    /**
     * 代替activity中的 onCreate
     * @param savedInstanceState
     */
    @Override
    public void bindView(Bundle savedInstanceState) {

        setupActivityViewPager();
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(getToolbar())
                .withHasStableIds(true)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header /GoogleMaterial.Icon.gmd_videocam/FontAwesome.Icon.faw_picture_o/GoogleMaterial.Icon.gmd_text_format
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.pagetwo).withIcon(R.mipmap.ic_launcher).withIdentifier(1).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.pageone).withIcon(R.mipmap.ic_launcher).withIdentifier(2).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.pagethree).withIcon(R.mipmap.ic_launcher).withIdentifier(3).withSelectable(true),
                        new PrimaryDrawerItem().withName(R.string.pageseting).withIcon(R.mipmap.ic_launcher).withIdentifier(5).withSelectable(false)

                        //                    new SwitchDrawerItem().withName("日间模式").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)

                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1 && dId != 1) {
                                dId = 1;
                                mTabLayout.removeAllTabs();
                                mViewPager.removeAllViews();
                                setupActivityViewPager();
                            } else if (drawerItem.getIdentifier() == 2 && dId != 2) {
                                dId = 2;
                                mTabLayout.removeAllTabs();
                                mViewPager.removeAllViews();
                                intent = new Intent(MainActivity.this, ORCIdentifyActivity.class);
                                finish();
                            } else if (drawerItem.getIdentifier() == 3 && dId != 3) {
                                dId = 3;
                                mTabLayout.removeAllTabs();
                                mViewPager.removeAllViews();
                                setupViewPager();//文章欣赏
                            } /*else if (drawerItem.getIdentifier() == 4) {
                                //                intent = new Intent(MainActivity.this, SmallGameActivity.class);
                            }*/ else if (drawerItem.getIdentifier() == 5) {
                                Toast.makeText(MainActivity.this, "这里是设置界面", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            result.setSelection(1, false);

            //set the active profile
//            headerResult.setActiveProfile(profile);
        }

        result.updateBadge(4, new StringHolder(10 + ""));
    }

    private  void setupViewPager()
    {
        Toast.makeText(MainActivity.this, "这里是界面", Toast.LENGTH_SHORT).show();


    }

    /**
     * 加载界面
     */
 private void setupActivityViewPager()
    {
        //得到标题数据
        String[] titles = getResources().getStringArray(R.array.activity_tab);
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(), Arrays.asList(titles));
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);




    }
    @Override
    public int getContentLayout() {
        return R.layout.activity_sample_dark_toolbar;
    }
}
