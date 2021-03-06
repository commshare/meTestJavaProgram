/**
 * Copyright (C) 2013 Aurélien Chabot <aurelien@chabot.fr>
 * 
 * This file is part of DroidUPNP.
 * 
 * DroidUPNP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DroidUPNP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DroidUPNP.  If not, see <http://www.gnu.org/licenses/>.
 */

package sc.music.upnp.cling;


import sc.music.Main;
import sc.music.upnp.model.ICallableFilter;
import sc.music.upnp.model.IServiceListener;
import sc.music.upnp.model.IUpnpDevice;

//dms的搜寻服务在这里啊
public class ContentDirectoryDiscovery extends DeviceDiscovery {

	protected static final String TAG = "ContentDirectoryDiscovery";

	public ContentDirectoryDiscovery(IServiceListener serviceListener)
	{
		super(serviceListener);
	}

	@Override
	protected ICallableFilter getCallableFilter()
	{
		return new CallableContentDirectoryFilter();
	}

	@Override
	protected boolean isSelected(IUpnpDevice device)
	{
		if (Main.upnpServiceController != null && Main.upnpServiceController.getSelectedContentDirectory() != null)
			return device.equals(Main.upnpServiceController.getSelectedContentDirectory());

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
		//让dmc选择某个dms
		Main.upnpServiceController.setSelectedContentDirectory(device, force);
	}

	@Override
	protected void removed(IUpnpDevice d)//让DMC放弃选择某个设备
	{
		if (Main.upnpServiceController != null && Main.upnpServiceController.getSelectedContentDirectory() != null
				&& d.equals(Main.upnpServiceController.getSelectedContentDirectory()))
			Main.upnpServiceController.setSelectedContentDirectory(null);
	}
}
