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

package sc.music.upnp.localcontent;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import org.fourthline.cling.support.model.container.Container;

import java.util.List;

//为啥叫做动态的啊
public abstract class DynamicContainer extends CustomContainer
{
	protected Context ctx;
	protected BaseColumns mediaColumns;
	protected Uri uri;

	protected String where = null;
	protected String[] whereVal = null;
	protected String orderBy = null;

	public DynamicContainer(String id, String parentID, String title, String creator, String baseURL,
							Context ctx, MediaStore.MediaColumns mediaColumns, Uri uri)
	{
		super(id, parentID, title, creator, baseURL);
		this.uri = uri;
		this.mediaColumns = mediaColumns;
		this.ctx = ctx;
	}

	// Dynamic container should re-implement those

	@Override
	public abstract Integer getChildCount();

	@Override
	public abstract List<Container> getContainers();

}
