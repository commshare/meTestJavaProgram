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

import android.app.Activity;
import android.util.Log;



import java.util.Observer;

import sc.music.upnp.controler.IUpnpServiceController;
import sc.music.upnp.controler.RendererDiscovery;
import sc.music.upnp.model.CObservable;
import sc.music.upnp.model.IUpnpDevice;

//这是DMC后台服务的具体实现？
public abstract class UpnpServiceController implements IUpnpServiceController {

	private static final String TAG = "UpnpServiceController";

	protected IUpnpDevice renderer;
	protected IUpnpDevice contentDirectory;

	protected CObservable rendererObservable;
	protected CObservable contentDirectoryObservable;

	private final ContentDirectoryDiscovery contentDirectoryDiscovery;
	private final RendererDiscovery rendererDiscovery; //发现dmr

	@Override
	public ContentDirectoryDiscovery getContentDirectoryDiscovery()
	{
		return contentDirectoryDiscovery;
	}

	@Override
	public RendererDiscovery getRendererDiscovery()
	{
		return rendererDiscovery;
	}

	protected UpnpServiceController()
	{
		rendererObservable = new CObservable();
		contentDirectoryObservable = new CObservable();

		//搜寻dms的服务
		contentDirectoryDiscovery = new ContentDirectoryDiscovery(getServiceListener());
		//搜寻dmr的服务
		rendererDiscovery = new RendererDiscovery(getServiceListener());
	}

	//设置选中的render
	@Override
	public void setSelectedRenderer(IUpnpDevice renderer)
	{
		setSelectedRenderer(renderer, false);
	}

	//经常调用的是这个
	@Override
	public void setSelectedRenderer(IUpnpDevice renderer, boolean force)
	{
		// Skip if no change and no force
		if (!force && renderer != null && this.renderer != null && this.renderer.equals(renderer))
			return;

		this.renderer = renderer;

		//通知所有观察者
		rendererObservable.notifyAllObservers();
	}

	//选中一个dms
	@Override
	public void setSelectedContentDirectory(IUpnpDevice contentDirectory)
	{
		setSelectedContentDirectory(contentDirectory, false);
	}

	//dms在这里被拿到
	@Override
	public void setSelectedContentDirectory(IUpnpDevice contentDirectory, boolean force)
	{
		// Skip if no change and no force
		if (!force && contentDirectory != null && this.contentDirectory != null
				&& this.contentDirectory.equals(contentDirectory))
			return;

		//在这里获得dms啊
		this.contentDirectory = contentDirectory;
		contentDirectoryObservable.notifyAllObservers();
	}
	//add by me 20150722
	private boolean isLocalDmr=false;
	public void LockLocalRender(){
		isLocalDmr=true;
	}
	public void UnlockLocalRender(){
		isLocalDmr=false;
	}
	public boolean isLocalDmr(){
		return isLocalDmr;
	}
	private IUpnpDevice localdmr;
	public void addLocalDmr(IUpnpDevice localdmr){
		this.localdmr=localdmr;
	}
	public IUpnpDevice getLocadDmr(){
		return localdmr;
	}
	@Override
	public IUpnpDevice getSelectedRenderer()
	{
		return renderer;
	}

	@Override
	public IUpnpDevice getSelectedContentDirectory()
	{
		return contentDirectory;
	}

	@Override
	public void addSelectedRendererObserver(Observer o)
	{
		Log.i(TAG, "New SelectedRendererObserver");
		rendererObservable.addObserver(o);
	}

	@Override
	public void delSelectedRendererObserver(Observer o)
	{
		rendererObservable.deleteObserver(o);
	}

	@Override
	public void addSelectedContentDirectoryObserver(Observer o)
	{
		contentDirectoryObservable.addObserver(o);
	}

	@Override
	public void delSelectedContentDirectoryObserver(Observer o)
	{
		contentDirectoryObservable.deleteObserver(o);
	}

	// Pause the service
	@Override
	public void pause()
	{
		rendererDiscovery.pause(getServiceListener());
		contentDirectoryDiscovery.pause(getServiceListener());
	}

	// Resume the service
	@Override
	public void resume(Activity activity)
	{
		rendererDiscovery.resume(getServiceListener());
		contentDirectoryDiscovery.resume(getServiceListener());
	}

}