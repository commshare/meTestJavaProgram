package sc.music.upnp.cling;

import sc.music.upnp.model.IUpnpDevice;

public interface IDeviceDiscoveryObserver {

	public void addedDevice(IUpnpDevice device);

	public void removedDevice(IUpnpDevice device);
}
