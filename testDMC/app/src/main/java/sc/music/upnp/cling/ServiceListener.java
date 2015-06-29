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

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;


import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.Device;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

import sc.music.common.Settings;
import sc.music.server.MediaServer;
import sc.music.upnp.cling.CDevice;
import sc.music.upnp.localcontent.CRegistryListener;
import sc.music.upnp.model.ICallableFilter;
import sc.music.upnp.model.IRegistryListener;
import sc.music.upnp.model.IServiceListener;
import sc.music.upnp.model.IUpnpDevice;
import sc.music.util.Network;

@SuppressWarnings("rawtypes")
public class ServiceListener implements IServiceListener
{
	private static final String TAG = "scdmc.ServiceListener";

	protected AndroidUpnpService/*cling库*/ upnpService;
	protected ArrayList<IRegistryListener> waitingListener;

	//服务器
	private MediaServer mediaServer = null;
	private Context ctx = null;

	public ServiceListener(Context ctx)
	{
		waitingListener = new ArrayList<IRegistryListener>();
		this.ctx = ctx;
	}

	@Override
	public void refresh()
	{
		//调用控制点搜寻
		upnpService.getControlPoint().search();
	}

	//获取设备列表
	@Override
	public Collection<IUpnpDevice> getDeviceList()
	{
		//看来设备列表，不是在这里获取到的，没影响啊。
		ArrayList<IUpnpDevice> deviceList = new ArrayList<IUpnpDevice>();
		if(upnpService != null && upnpService.getRegistry() != null) {
			for (Device device : upnpService.getRegistry().getDevices()) {
				deviceList.add(new CDevice(device));
			}
		}
//		deviceList.add(new CDevice(mediaServer.getDevice()));
		return deviceList;
	}

	@Override
	public Collection<IUpnpDevice> getFilteredDeviceList(ICallableFilter filter)
	{
		Log.e("scdmc.ServiceListener","getFilteredDeviceList");
		ArrayList<IUpnpDevice> deviceList = new ArrayList<IUpnpDevice>();
		try
		{
			if(upnpService != null && upnpService.getRegistry() != null) {
				for (Device device : upnpService.getRegistry().getDevices()) {
					IUpnpDevice upnpDevice = new CDevice(device);
					filter.setDevice(upnpDevice);

					if (filter.call())
						deviceList.add(upnpDevice);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return deviceList;
	}

	//android的
	protected ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service)
		{
			Log.i(TAG, "Service connexion");
			upnpService = (AndroidUpnpService) service;

			//是否支持本地dms
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
			if(sharedPref.getBoolean(Settings.CONTENTDIRECTORY_SERVICE, true))
			{
				try
				{
					// Local content directory  本地内容目录
					if(mediaServer == null)
					{
						//使用的是自己的服务器，传入ip地址
						mediaServer = new MediaServer(Network.getLocalIpAddress(ctx), ctx);
						mediaServer.start();
						Log.e(TAG,"after mediaServer start");
					}
					else
					{
						mediaServer.restart();//重启服务
					}
					//注册本地设备及服务，这是注册给cling库了
					upnpService.getRegistry().addDevice(mediaServer.getDevice());
				}
				catch (UnknownHostException e1)
				{
					Log.e(TAG, "Creating demo device failed");
					Log.e(TAG, "exception", e1);
				}
				catch (ValidationException e2)
				{
					Log.e(TAG, "Creating demo device failed");
					Log.e(TAG, "exception", e2);
				}
				catch (IOException e3)
				{
					Log.e(TAG, "Starting http server failed");
					Log.e(TAG, "exception", e3);
				}
			}
			else if(mediaServer != null)
			{
				mediaServer.stop();
				mediaServer = null;
			}

			for (IRegistryListener registryListener : waitingListener)
			{
				addListenerSafe(registryListener);
			}

			// Search asynchronously for all devices, they will respond soon
			//搜寻的设备
			upnpService.getControlPoint().search();
		}

		@Override
		public void onServiceDisconnected(ComponentName className)
		{
			Log.i(TAG, "Service disconnected");
			upnpService = null;
		}
	};

	@Override
	public ServiceConnection getServiceConnexion()
	{
		return serviceConnection;
	}

	public AndroidUpnpService getUpnpService()
	{
		return upnpService;
	}

	@Override
	public void addListener(IRegistryListener registryListener)
	{
		Log.d(TAG, "Add Listener !");
		if (upnpService != null)
			addListenerSafe(registryListener);
		else
			waitingListener.add(registryListener);
	}

	private void addListenerSafe(IRegistryListener registryListener)
	{
		assert upnpService != null;
		Log.d(TAG, "Add Listener Safe !");

		// Get ready for future device advertisements
		upnpService.getRegistry().addListener(new CRegistryListener(registryListener));

		// Now add all devices to the list we already know about 加入所有的设备
		for (Device device : upnpService.getRegistry().getDevices())
		{
			registryListener.deviceAdded(new CDevice(device));
		}
	}

	@Override
	public void removeListener(IRegistryListener registryListener)
	{
		Log.d(TAG, "remove listener");
		if (upnpService != null)
			removeListenerSafe(registryListener);
		else
			waitingListener.remove(registryListener);
	}

	private void removeListenerSafe(IRegistryListener registryListener)
	{
		assert upnpService != null;
		Log.d(TAG, "remove listener Safe");
		upnpService.getRegistry().removeListener(new CRegistryListener(registryListener));
	}

	@Override
	public void clearListener()
	{
		waitingListener.clear();
	}
}
