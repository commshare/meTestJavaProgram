package sc.music.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Observable;

import sc.droid.dmc.R;
import sc.music.Main;
import sc.music.upnp.model.IUpnpDevice;
import java.util.Observer;
/**
 * Created by Administrator on 2015/6/5.
 */
public class CDSDeviceFragment extends UpnpDeviceListFragment implements Observer
//extends Fragment
//
{

//    private static View view=null;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null)
//                parent.removeView(view);
//        }
//        try{
//            view=inflater.inflate(R.layout.sub_device_fragment_layout,container,false);
//        }catch(InflateException e){
//                /* parent is already there, just return view as it is */
//        }
//        return view;
//    }
    protected static final String TAG = "CDSFragment";

    public CDSDeviceFragment()
    {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Main.upnpServiceController.getContentDirectoryDiscovery().addObserver(this);
        Main.upnpServiceController.addSelectedContentDirectoryObserver(this);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Main.upnpServiceController.getContentDirectoryDiscovery().removeObserver(this);
        Main.upnpServiceController.delSelectedContentDirectoryObserver(this);
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected boolean isSelected(IUpnpDevice device)
    {
        if (Main.upnpServiceController != null && Main.upnpServiceController.getSelectedContentDirectory() != null)
            return device.equals(Main.upnpServiceController.getSelectedContentDirectory());

        return false;
    }

    //点击了设备之后，就会到这里
    @Override
    protected void select(IUpnpDevice device)
    {
        select(device, false);
    }

    @Override
    protected void select(IUpnpDevice device, boolean force)
    {
        //传入一个设备，丢给dmc知道
        Main.upnpServiceController.setSelectedContentDirectory(device, force);
    }

    //选中一个设备
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        select(list.getItem(position).getDevice());
        Log.d(TAG, "Set contentDirectory to " + list.getItem(position));
    }

    @Override
    public void update(Observable observable, Object o)
    {
        IUpnpDevice device = Main.upnpServiceController.getSelectedContentDirectory();
        if(device==null)
        {
            if (getActivity() != null) // Visible
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Uncheck device
                        getListView().clearChoices();
                        list.notifyDataSetChanged();
                    }
                });
        }
        else
        {
            addedDevice(device);
        }
    }
}
