package sc.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import com.xwj.toolbardemo.BaseCardFragment;

import sc.droid.dmc.R;
import sc.music.Render.LocalDMR;
import sc.music.Render.LocalRender;
import sc.music.ui.activity.SettingsActivity;
import sc.music.ui.fragment.ContentDirectoryFragment;
import sc.music.ui.fragment.NavigationDrawerCallbacks;
import sc.music.ui.fragment.NavigationDrawerFragment;
import sc.music.ui.widget.PagerSlidingTabStrip;

import java.lang.reflect.Field;
import java.util.List;


import sc.music.ui.adapter.PagerFragmentAdapter;
import sc.music.upnp.controler.IUpnpServiceController;
import sc.music.upnp.model.IFactory;

/*
* http://www.codeproject.com/Articles/996561/Create-and-Publish-Your-First-Android-App-Part
* */
public class Main extends /*ActionBarActivity废弃*/AppCompatActivity  implements NavigationDrawerCallbacks {
    private String TAG="scdmc.Main";
    // Controller
    public static IUpnpServiceController/*这是一个接口*/ upnpServiceController = null;
    private static boolean isActiveDmc=false;
    public static IFactory factory = null;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;//抽屉
    private ActionBarDrawerToggle mDrawerToggle; //触发抽屉
    private ShareActionProvider mShareActionProvider; //共享action
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private Toolbar mToolbar; //工具条
    LocalDMR mydmr;
    public static LocalRender myrender;

    //直接分配一次内存，是不是重复了啊
    private List<Fragment> mFragmentList ;
    private PagerFragmentAdapter mFragmentAdapter;
    //////////////////////////////////////////////////
    private static ContentDirectoryFragment mContentDirectoryFragment;

    public String HEADER_NAME ="test"; //“Your Name”; (that shows your name in the navigation header)
    public String HEADER_EMAIL ="test";// “Your Email”;
    public int HEADER_IMAGE = 1;// (we will change this later to point to a resource file)
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DrawerLayout Drawer;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
  //  private ActionBarDrawerToggle mDrawerToggle;
 //   private List<DrawerItem> dataList;

    //////////////////////////////////////////////////
    Context mContext;
    public static boolean checkActiveDmc(){
        return isActiveDmc;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;    // since Activity extends Context
        Log.d(TAG, "onCreated : " + savedInstanceState + factory + upnpServiceController);

         mydmr=new LocalDMR();
        myrender=new LocalRender(mContext);
        //当启用dlna，打开factory构造层控制点
        isActiveDmc=true;

        // Use cling factory
        if (factory == null)//实现dmc的
            factory = new sc.music.upnp.controler.Factory();

        // Upnp service
        if (upnpServiceController == null) {
            upnpServiceController = factory.createUpnpServiceController(this);
            //控制点开启后，传递dmr进去
            if (upnpServiceController != null){
                upnpServiceController.addLocalDmr(mydmr);
            }
        }
        /**
         * DrawerLayout不覆盖Toolbar
         */
        setContentView(R.layout.activity_main);
        /**
         * DrawerLayout覆盖Toolbar
         */
        //setContentView(R.layout.activity_main_toggleover);
        initViews();
        setOverflowShowingAlways();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.ic_launcher);
        // 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitle(R.string.MainActivity_title);
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
       // mToolbar.setNavigationIcon(R.drawable.ic_action_menu);

        //这个可以显示系统状态栏啊
        //Tools.setStatusBar(this, R.color.colorPrimary);

        /* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 */
        // getSupportActionBar().setTitle("标题");
        // getSupportActionBar().setSubtitle("副标题");
        // getSupportActionBar().setLogo(R.drawable.ic_launcher);

		/* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_settings:
//                        Toast.makeText(Main.this, "action_settings", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_share:
//                        Toast.makeText(Main.this, "action_share", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
        //       mToolbar.setOnCreateContextMenuListener(this);
        //会有空指针异常么？
        getSupportActionBar().setHomeButtonEnabled(true);  // 设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* findView *///抽屉总布局
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        //左边那个抽屉的fragment的布局fragment_drawer
        //refer to http://stackoverflow.com/questions/25321222/v4-getfragmentmanager-with-activity-incompatible-types
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        //drawer是抽屉DrawerLayout总布局
        //把mDrawerToggle弄到mNavigationDrawerFragment里头去了
        mNavigationDrawerFragment.setup(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
//        //系统的抽屉类啊
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
//                R.string.drawer_close) {
//            //抽屉打开的时候，执行这个
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                //当drawer页面打开的时候，京东的那个RunningMan动画就是在此时关闭和打开的
//                //Toast.makeText(Main.this, "打开", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                //当drawer页面关闭的时候
//               // Toast.makeText(Main.this, "关闭", Toast.LENGTH_SHORT).show();
//            }
//
//        };
//        mDrawerToggle.syncState();
//        //监听抽屉？
//        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //就是ui上的一个pager组件
        mViewPager = (ViewPager) findViewById(R.id.pager);
        /*
        * new MyPagerAdapter(getSupportFragmentManager(),mContext
        * */
//        mFragmentList = new ArrayList<Fragment>();
//        mFragmentList.add(new LocalMusicFragment());
//        mFragmentList.add(BaseCardFragment.newInstance(1));
//        mFragmentList.add(BaseCardFragment.newInstance(2));
//        mFragmentList.add(new DeviceFragment());//(BaseCardFragment.newInstance(3));//(new DeviceFragment());
//        mFragmentAdapter = new PagerFragmentAdapter(getSupportFragmentManager(), mContext,mFragmentList);
        mFragmentAdapter = new PagerFragmentAdapter(getSupportFragmentManager(), mContext);
      //  mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),mContext));//mFragmentAdapter);
        mViewPager.setAdapter(mFragmentAdapter);
        if(mViewPager==null)
            Log.e("zb","viewpager is null ");
        if(mPagerSlidingTabStrip==null)
            Log.e("zb","mPagerSlidingTabStrip is null");
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //选中每个pager
            @Override
            public void onPageSelected(int arg0) {
               // colorChange(arg0);
              //  Toast.makeText(Main.this, "页面"+arg0, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        initTabsValue();
    }

    /**
     * mPagerSlidingTabStrip默认值配置
     */
    private void initTabsValue() {
        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.BLUE);//Color.BLUE //用来指示pageer的
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#4876FF"));
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getResources().getDisplayMetrics()));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics()));
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(Color.BLACK);
    }

    public static void setContentDirectoryFragment(ContentDirectoryFragment f) {
        mContentDirectoryFragment = f;
    }
    //是搜索啊
    private static Menu actionBarMenu = null;
    //actionbar的搜索是否可见
    public static void setSearchVisibility(boolean visibility)
    {
        if(actionBarMenu == null)
            return;

        //是搜索啊
        MenuItem item = actionBarMenu.findItem(R.id.action_search);

        if(item != null)
            item.setVisible(visibility);
    }

    /**
     * 界面颜色的更改
     */
    @SuppressLint("NewApi")
    private void colorChange(int position) {
        // 用来提取颜色的Bitmap
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                BaseCardFragment.getBackgroundBitmapPosition(position));
        // Palette的部分
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            /**
             * 提取完之后的回调方法
             */
            @Override
            public void onGenerated(Palette palette) {
                if (palette != null) {
                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                /* 界面颜色UI统一性处理,看起来更Material一些 */
                    mPagerSlidingTabStrip.setBackgroundColor(vibrant.getRgb());
                    mPagerSlidingTabStrip.setTextColor(vibrant.getTitleTextColor());
                    // 其中状态栏、游标、底部导航栏的颜色需要加深一下，也可以不加，具体情况在代码之后说明
                    mPagerSlidingTabStrip.setIndicatorColor(colorBurn(vibrant.getRgb()));

                    mToolbar.setBackgroundColor(vibrant.getRgb());
                    if (android.os.Build.VERSION.SDK_INT >= 21) {
                        Window window = getWindow();
                        // API>=21以上才能实现此效果，当然也可以用开源的状态栏SystemBarTintManager实现（支持4.4以上）
                        window.setStatusBarColor(colorBurn(vibrant.getRgb()));
                        window.setNavigationBarColor(colorBurn(vibrant.getRgb()));
                    }
                    //  释放掉，避免卡顿
                    bitmap.recycle();
                }

            }
        });

    }

    /**
     * 颜色加深处理
     *
     * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    private int colorBurn(int RGBValues) {
        Log.e("-----",RGBValues+"");
        int alpha = RGBValues >> 24;

        int red = RGBValues >> 16 & 0xFF;
        Log.e("--red---",red+"");

        int green = RGBValues >> 8 & 0xFF;
        Log.e("--green---",green+"");

        int blue = RGBValues & 0xFF;
        Log.e("--blue---",blue+"");
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }


    //可选菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu
                .findItem(R.id.action_share));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        mShareActionProvider.setShareIntent(intent);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(Main.this, "打开", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    //点击了可选的item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(Main.this, "action_settings", Toast.LENGTH_SHORT).show();
               //弹出一个设置Activity
                startActivity(new Intent(this, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));

                break;
            case R.id.action_share:
                Toast.makeText(Main.this, "action_share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_search:
                Toast.makeText(Main.this, "ab_search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_quit:
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 处理溢出菜单问题
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }

    //这个是给viewpager用的
    public class MyPagerAdapter extends FragmentPagerAdapter {
        Context mContext;
        private  String[] TITLES ;//=
        ;// {"推荐", "分类", "本月热榜", "热门推荐", "专栏", "热门收藏", "随缘"};
        private int title_length;
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public MyPagerAdapter(FragmentManager fm,Context mContext) {
            super(fm);
            this.mContext=mContext;
            TITLES = mContext.getResources().getStringArray(R.array.pager_title);//因为定义在arrays.xml中
            title_length=TITLES.length;
            Log.e("zb","title_length"+title_length);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        //获取每个viewpager上的fragement
        @Override
        public Fragment getItem(int position) {
            return BaseCardFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return title_length;  // TITLES.length;
        }
    }
    @Override
    public void onResume()
    {
        Log.v(TAG, "Resume activity");
        upnpServiceController.resume(this);
        super.onResume();
    }

    @Override
    public void onPause()
    {
        Log.v(TAG, "Pause activity");
        upnpServiceController.pause();
        upnpServiceController.getServiceListener().getServiceConnexion().onServiceDisconnected(null);
        super.onPause();
    }
    public static ContentDirectoryFragment getContentDirectoryFragment() {
        return mContentDirectoryFragment;
    }
    public void refresh()
    {
        //刷新设备
        upnpServiceController.getServiceListener().refresh();
        //刷新设备对应的文件列表
        ContentDirectoryFragment cd = getContentDirectoryFragment();
        if(cd!=null)
            cd.refresh();
    }
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }
}