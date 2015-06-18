package sc.music.upnp.model;

import java.util.concurrent.Callable;

public interface ICallableFilter extends Callable<Boolean> {
	public void setDevice(IUpnpDevice device);
}
