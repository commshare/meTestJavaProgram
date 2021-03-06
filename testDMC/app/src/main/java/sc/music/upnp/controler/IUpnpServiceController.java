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


import org.fourthline.cling.model.meta.LocalDevice;

import java.util.Observer;

import sc.music.upnp.cling.ContentDirectoryDiscovery;
import sc.music.upnp.model.IServiceListener;
import sc.music.upnp.model.IUpnpDevice;

public interface IUpnpServiceController {
	public void setSelectedRenderer(IUpnpDevice renderer);

	public void setSelectedRenderer(IUpnpDevice renderer, boolean force);

	public void setSelectedContentDirectory(IUpnpDevice contentDirectory);

	public void setSelectedContentDirectory(IUpnpDevice contentDirectory, boolean force);

	public IUpnpDevice getSelectedRenderer();

	public IUpnpDevice getSelectedContentDirectory();
	//20150722
	public void addLocalDmr(IUpnpDevice dmr);
	public IUpnpDevice getLocadDmr();

	public void LockLocalRender();
	public void UnlockLocalRender();
	public boolean isLocalDmr();

		public void addSelectedRendererObserver(Observer o);

	public void delSelectedRendererObserver(Observer o);

	public void addSelectedContentDirectoryObserver(Observer o);

	public void delSelectedContentDirectoryObserver(Observer o);

	public IServiceListener getServiceListener();

	public ContentDirectoryDiscovery getContentDirectoryDiscovery();

	public RendererDiscovery getRendererDiscovery();

	// Pause the service
	public void pause();

	// Resume the service
	public void resume(Activity activity);

	public void addDevice(LocalDevice localDevice);
	public void removeDevice(LocalDevice localDevice);

}
