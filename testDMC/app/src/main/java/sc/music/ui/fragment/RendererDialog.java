package sc.music.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;



import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import sc.droid.dmc.R;
import sc.music.Main;
import sc.music.Render.LocalDMR;
import sc.music.upnp.cling.CallableRendererFilter;
import sc.music.upnp.model.IUpnpDevice;

public class RendererDialog extends DialogFragment {

	private Callable<Void> callback = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		//是通过这么复杂的一个过程获取到DMR设备的
		final Collection<IUpnpDevice> upnpDevices = Main.upnpServiceController.getServiceListener()
				.getFilteredDeviceList(new CallableRendererFilter());

		ArrayList<DeviceDisplay> list = new ArrayList<DeviceDisplay>();
		//创建一个类，叫做LocalDMR，可用来构造DeviceDisplay
		LocalDMR mydmr=new LocalDMR();
		list.add(new DeviceDisplay(mydmr));
		for (IUpnpDevice upnpDevice : upnpDevices)
			list.add(new DeviceDisplay(upnpDevice));

		final DialogFragment dialog = this;

		if(list.size()==0)
		{
			builder.setTitle(R.string.select_a_dmr)
				.setMessage(R.string.noRenderer)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		}
		else
		{
			ArrayAdapter<DeviceDisplay> rendererList = new ArrayAdapter<DeviceDisplay>(getActivity(),
				android.R.layout.simple_list_item_1, list);
			builder.setTitle(R.string.selectRenderer).setAdapter(rendererList, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Main.upnpServiceController.setSelectedRenderer((IUpnpDevice) upnpDevices.toArray()[which]);
					try {
						if (callback != null)
							callback.call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		return builder.create();
	}

	public void setCallback(Callable<Void> callback)
	{
		this.callback = callback;
	}
}
