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

package sc.music.upnp.cling.didl;


import android.util.Log;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.item.AudioItem;

import sc.music.upnp.didl.IDIDLObject;

public class ClingDIDLObject implements IDIDLObject {

	private static final String TAG = "ClingDIDLObject";

	protected DIDLObject/*cling的*/ item;

	public ClingDIDLObject(DIDLObject item)
	{
		this.item = item;
	}

	//从这里获取到一个cling库的DIDLObject
	public DIDLObject getObject()
	{
		return item;
	}

	@Override
	public String getDataType()
	{
		return "";
	}

	@Override
	public String getTitle()
	{
		return item.getTitle();
	}

	@Override
	public String getDescription()
	{
		return "";
	}

	@Override
	public String getCount()
	{
		return "";
	}

	@Override
	public int getIcon()
	{
		return android.R.color.transparent;
	}

	@Override
	public String getParentID()
	{
		return item.getParentID();
	}

	@Override
	public String getId()
	{
		return item.getId();
	}

	//新加入的,ClingAudioItem中有实现
	@Override
	public String getLocalpath() {

		Log.e(TAG, "ClingDIDLObject getLocalpath");

		return null;
	}
}
