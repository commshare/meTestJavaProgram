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

package sc.music.upnp.controler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import org.fourthline.cling.model.meta.LocalDevice;

import sc.music.upnp.cling.ServiceListener;
import sc.music.upnp.cling.UpnpService;
import sc.music.upnp.cling.UpnpServiceController;

public class ServiceController extends UpnpServiceController
{
	private static final String TAG = "Cling.ServiceController";

	private final ServiceListener upnpServiceListener;
	private Activity activity = null;

	public ServiceController(Context ctx)
	{
		super();
		//这好像就是upnp的service啊
		upnpServiceListener = new ServiceListener(ctx);
	}

	@Override
	protected void finalize()
	{
		pause();
	}

	@Override
	public ServiceListener getServiceListener()
	{
		return upnpServiceListener;
	}

	@Override
	public void pause()
	{
		super.pause();
		activity.unbindService(upnpServiceListener.getServiceConnexion());
		activity = null;
	}

	@Override
	public void resume(Activity activity)
	{
		super.resume(activity);
		this.activity = activity;

		// This will start the UPnP service if it wasn't already started
		Log.d(TAG, "Start upnp service");
		//绑定了这个服务
		activity.bindService(new Intent(activity,/*自己的*/ UpnpService.class), upnpServiceListener.getServiceConnexion(),
				Context.BIND_AUTO_CREATE);
	}

	//注册本地设备
	@Override
	public void addDevice(LocalDevice localDevice) {
		upnpServiceListener.getUpnpService().getRegistry().addDevice(localDevice);
	}

	@Override
	public void removeDevice(LocalDevice localDevice) {
		upnpServiceListener.getUpnpService().getRegistry().removeDevice(localDevice);
	}

}
