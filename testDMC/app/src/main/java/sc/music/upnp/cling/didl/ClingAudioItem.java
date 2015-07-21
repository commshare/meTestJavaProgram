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
import org.fourthline.cling.support.model.item.AudioItem;
import org.fourthline.cling.support.model.item.MusicTrack;

import java.util.List;

import sc.droid.dmc.R;
import sc.music.upnp.localcontent.SCMusicTrack;

public class ClingAudioItem extends ClingDIDLItem
{
	String TAG="ClingAudioItem";
	public ClingAudioItem(AudioItem/*cling的*/ item)
	{

		super(item);
		Log.e(TAG, "created ClingAudioItem");
	}

	@Override
	public String getDataType()
	{
		return "audio/*";
	}

	@Override
	public String getDescription()
	{
		Log.e(TAG, " ClingAudioItem getDescription");
		if(item instanceof MusicTrack)
		{
			Log.e(TAG,"item instanceof MusicTrack");
			MusicTrack track = (MusicTrack) item;
			return ( (track.getFirstArtist()!=null && track.getFirstArtist().getName()!=null) ? track.getFirstArtist().getName() : "") +
				((track.getAlbum()!=null) ?  (" - " + track.getAlbum()) : "");
		}
			if(item instanceof  SCMusicTrack){
				Log.e(TAG,"==$$$=item instanceof SCMusicTrack");

			}

		return ((AudioItem) item).getDescription();

//		if(item instanceof SCMusicTrack){
//			Log.e(TAG,"item instanceof SCMusicTrack");
//			SCMusicTrack track = (SCMusicTrack) item;
//
//			return ( (track.getFirstArtist()!=null && track.getFirstArtist().getName()!=null) ? track.getFirstArtist().getName() : "") +
//					((track.getAlbum()!=null) ?  (" - " + track.getAlbum()) : "");
//		}
//		return ((AudioItem) item).getDescription();
	}

	@Override
	public String getCount()
	{
		Log.e(TAG, " ClingAudioItem getCount");
		//居然是res的列表
		List<Res> res = item.getResources();
		if(res!=null && res.size()>0)
			return "" + ((res.get(0).getDuration()!=null) ? res.get(0).getDuration().split("\\.")[0] : "");

		return "";
	}

	@Override
	public int getIcon()
	{
		return R.drawable.ic_action_headphones;
	}

	@Override
	public String getLocalpath(){

		String localpath=null;
		if(item instanceof SCMusicTrack){
			SCMusicTrack track = (SCMusicTrack) item;
			localpath=track.getLocalPath();
			Log.e(TAG, "==!!!=localpath is [" + localpath + "]");
		}else {
			Log.e(TAG, "NOT GET local path");
//			SCMusicTrack track = (SCMusicTrack) item;
//			localpath=track.getLocalPath();
		}
		return localpath;
	}
}
