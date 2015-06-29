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

import android.content.Context;


import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.controlpoint.ControlPoint;

import sc.music.Main;
import sc.music.upnp.cling.RendererCommand;
import sc.music.upnp.cling.RendererState;
import sc.music.upnp.cling.ServiceListener;
import sc.music.upnp.model.ARendererState;
import sc.music.upnp.model.IContentDirectoryCommand;
import sc.music.upnp.model.IFactory;
import sc.music.upnp.model.IRendererCommand;
import sc.music.upnp.model.IRendererState;

public class Factory implements IFactory {

	@Override
	public IContentDirectoryCommand createContentDirectoryCommand()
	{
		//cling的服务
		AndroidUpnpService aus = ((ServiceListener) Main.upnpServiceController.getServiceListener()).getUpnpService();
		//cling的dmc
		ControlPoint cp = null;
		if (aus != null)
			cp = aus.getControlPoint();
		if (cp != null)
			return new ContentDirectoryCommand(cp);

		return null;
	}

	@Override
	public IRendererCommand createRendererCommand(IRendererState rs)
	{
		AndroidUpnpService aus = ((ServiceListener) Main.upnpServiceController.getServiceListener()).getUpnpService();
		ControlPoint cp = null;
		if (aus != null)
			cp = aus.getControlPoint();
		if (cp != null)
			return new RendererCommand(cp, (RendererState) rs);

		return null;
	}

	@Override
	public IUpnpServiceController createUpnpServiceController(Context ctx)
	{
		//这个类才是dmc啊
		return new ServiceController(ctx);
	}

	@Override
	public ARendererState createRendererState()
	{
		return new RendererState();
	}
}
