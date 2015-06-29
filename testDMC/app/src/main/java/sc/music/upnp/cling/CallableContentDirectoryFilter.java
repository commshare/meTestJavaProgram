package sc.music.upnp.cling;

import android.util.Log;

import sc.music.upnp.model.ICallableFilter;
import sc.music.upnp.model.IUpnpDevice;

public class CallableContentDirectoryFilter implements ICallableFilter {
	private String TAG="CallableContentDirectoryFilter";
	private IUpnpDevice device;

	public void setDevice(IUpnpDevice device)
	{
		this.device = device;
	}

	@Override
	public Boolean call() throws Exception
	{
		Log.e(TAG, "filter dms");
		//return device.asService("ContentDirectory");
		return device.asDeviceType("SCMediaServer");
	}
}
