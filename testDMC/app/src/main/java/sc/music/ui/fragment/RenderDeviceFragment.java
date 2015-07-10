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
        //����dmc�����豸��ѡ����
        Main.upnpServiceController.setSelectedRenderer(device, force);
    }

    //����ѡ����һ��dmr
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        //position �ǶԵģ�����0��Ӧ�Ĳ���list��0
        //Log.e(TAG,"select dmr position["+position+"]");
        ListView listView=getListView();
        // TODO ������Header��ListView��position��BUG
        int headerViewsCount = listView.getHeaderViewsCount();//�õ�header��������
        Log.e(TAG, "select dmr position[" + position + "]  headerViewsCount[" + headerViewsCount + "]");
        //super.onListItemClick(l, v, position, id);
        if(position == 0 )//local dmr
        {
            Log.e(TAG, "local dmr");
        }
        else{
            int newpos=position-1;
            //���������ѡ�е��豸
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
                //�õ���ǰѡ�е�dmr
                IUpnpDevice device = Main.upnpServiceController.getSelectedRenderer();
                if (device == null)
                {
                    // Uncheck device
                    getListView().clearChoices();
                    list.notifyDataSetChanged();
                }
                else
                {
                    //��dmr����
                    addedDevice(device);
                }
            }
        });
    }
}
