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

import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.item.Item;


import sc.droid.dmc.R;
import sc.music.upnp.didl.IDIDLItem;
//import sc.music.upnp.cling.didl.ClingDIDLObject;


public class ClingDIDLItem extends ClingDIDLObject implements IDIDLItem {

	private static final String TAG = "ClingDIDLItem";

	public ClingDIDLItem(Item/*cling的*/ item)
	{
		/*父类要求传入的是DIDLObject 对象，这也是cling库的*/
		super(item);
	}

	@Override
	public int getIcon()
	{
		return R.drawable.ic_file;
	}

//	@Override //新加入的
//	public String getLocalpath() {
//
//			Log.e(TAG,"ClingDIDLItem getLocalpath");
//		return null;
//	}

	@Override
	public String getURI()
	{
		if (item != null)
		{
			//居然是从res列表取出第一个res，然后得到value值
			Log.d(TAG, "getURI from Item : " + item.getFirstResource().getValue());
			if (item.getFirstResource() != null && item.getFirstResource().getValue() != null)
				return item.getFirstResource().getValue();
		}
		return null;
	}
}
