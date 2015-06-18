package sc.music.upnp.cling;

import sc.music.upnp.model.ICallableFilter;
import sc.music.upnp.model.IUpnpDevice;

public class CallableRendererFilter implements ICallableFilter {

	private IUpnpDevice device;

	public void setDevice(IUpnpDevice device)
	{
		this.device = device;
	}

	@Override
	public Boolean call() throws Exception
	{
		return device.asService("RenderingControl");
	}
}
