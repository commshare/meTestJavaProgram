package sc.music.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Observable;
import java.util.Observer;

import sc.droid.dmc.R;
import sc.music.Main;
import sc.music.upnp.model.IUpnpDevice;

/**
 * Created by Administrator on 2015/6/5.
 */
public class RenderDeviceFragment  extends  UpnpDeviceListFragment implements Observer {
//extends Fragment{

/*    public RenderDeviceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sub_device_fragment_layout,container,false);
    }*/
    protected static final String TAG = "RendererDeviceFragment";

    public RenderDeviceFragment()
    {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Main.upnpServiceController.getRendererDiscovery().addObserver(this);
        Main.upnpServiceController.addSelectedRendererObserver(this);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Main.upnpServiceController.getRendererDiscovery().removeObserver(this);
        Main.upnpServiceController.delSelectedRendererObserver(this);
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected boolean isSelected(IUpnpDevice device)
    {
        if (Main.upnpServiceController != null && Main.upnpServiceController.getSelectedRenderer() != null)
            return device.equals(Main.upnpServiceController.getSelectedRenderer());

        return false;
    }

    @Override
    protected void select(IUpnpDevice device)
    {
        select(device, false);
    }

    @Override
    protected void select(IUpnpDevice device, boolean force)
    {
        //告诉dmc，有设备被选中了
        Main.upnpServiceController.setSelectedRenderer(device, force);
    }

    //这是选中了一个dmr
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        //position 是对的，但是0对应的不是list的0
        //Log.e(TAG,"select dmr position["+position+"]");
        ListView listView=getListView();
        // TODO 修正有Header的ListView的position的BUG
        int headerViewsCount = listView.getHeaderViewsCount();//得到header的总数量
        Log.e(TAG, "select dmr position[" + position + "]  headerViewsCount[" + headerViewsCount + "]");
        //super.onListItemClick(l, v, position, id);
        if(position == 0 )//local dmr
        {
            Log.e(TAG, "local dmr");
        }
        else{
            int newpos=position-1;
            //发送这个被选中的设备
            select(list.getItem(newpos).getDevice());
            Log.d(TAG, "Set renderer to " + list.getItem(newpos));
        }

    }

    @Override
    public void update(Observable observable, Object o)
    {
        Activity a = getActivity();
        if (a == null)
            return;

        a.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                //得到当前选中的dmr
                IUpnpDevice device = Main.upnpServiceController.getSelectedRenderer();
                if (device == null)
                {
                    // Uncheck device
                    getListView().clearChoices();
                    list.notifyDataSetChanged();
                }
                else
                {
                    //把dmr加入
                    addedDevice(device);
                }
            }
        });
    }
}
