package sc.music.upnp.localcontent;

/**
 * Created by Administrator on 2015/7/21.
 */

import android.util.Log;

import org.fourthline.cling.support.model.item.AudioItem;

        import org.fourthline.cling.support.model.Person;
        import org.fourthline.cling.support.model.PersonWithRole;
        import org.fourthline.cling.support.model.Res;
        import org.fourthline.cling.support.model.StorageMedium;
        import org.fourthline.cling.support.model.container.Container;

        import org.fourthline.cling.support.model.item.Item;

        import java.util.List;

/**
 * Created by Administrator on 2015/7/21.
 */



        import org.fourthline.cling.support.model.DIDLObject.Property.UPNP.ARTIST;


//public class MusicTrack  {
public class SCMusicTrack extends AudioItem {
    String TAG="SCMusicTrack";

    public static final Class CLASS = new Class("object.item.audioItem.musicTrack");

    public SCMusicTrack() {
        this.setClazz(CLASS);
    }

    public SCMusicTrack(Item other) {
        super(other);
    }

    public SCMusicTrack(String id, Container parent, String title, String creator, String album, String artist, Res... resource) {
        this(id, parent.getId(), title, creator, album, artist, resource);
    }

    public SCMusicTrack(String id, Container parent, String title, String creator, String album, PersonWithRole artist, Res... resource) {
        this(id, parent.getId(), title, creator, album, artist, resource);
    }

    public SCMusicTrack(String id, String parentID, String title, String creator, String album, String artist, Res... resource) {
        this(id, parentID, title, creator, album, artist == null ? null : new PersonWithRole(artist), resource);
    }


   private String localpath=null;
    public String getLocalPath(){
        return localpath;
    }
    //应该用的是这个吧
public SCMusicTrack(String id, String parentID, String title, String creator, String album, PersonWithRole artist, Res... resource) {
    super(id, parentID, title, creator, resource);
    this.setClazz(CLASS);
    if(album != null) {
        this.setAlbum(album);
    }

    if(artist != null) {
        this.addProperty(new ARTIST(artist));
    }

}   //add localpath 20150721
    public SCMusicTrack(String id, String parentID,String localpath, String title, String creator, String album, PersonWithRole artist, Res... resource) {
        super(id, parentID, title, creator, resource);
        this.setClazz(CLASS);
        this.localpath=localpath;
        Log.e(TAG,"localpath ["+localpath+"]");
        if(album != null) {
            this.setAlbum(album);
        }

        if(artist != null) {
            this.addProperty(new ARTIST(artist));
        }

    }

    public PersonWithRole getFirstArtist() {
        return (PersonWithRole)this.getFirstPropertyValue(Property.UPNP.ARTIST.class);
    }

    public PersonWithRole[] getArtists() {
        List list = this.getPropertyValues(Property.UPNP.ARTIST.class);
        return (PersonWithRole[])list.toArray(new PersonWithRole[list.size()]);
    }

    public SCMusicTrack setArtists(PersonWithRole[] artists) {
        this.removeProperties(Property.UPNP.ARTIST.class);
        PersonWithRole[] arr$ = artists;
        int len$ = artists.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            PersonWithRole artist = arr$[i$];
            this.addProperty(new Property.UPNP.ARTIST(artist));
        }

        return this;
    }

    public String getAlbum() {
        return (String)this.getFirstPropertyValue(Property.UPNP.ALBUM.class);
    }

    public SCMusicTrack setAlbum(String album) {
        this.replaceFirstProperty(new Property.UPNP.ALBUM(album));
        return this;
    }

    public Integer getOriginalTrackNumber() {
        return (Integer)this.getFirstPropertyValue(Property.UPNP.ORIGINAL_TRACK_NUMBER.class);
    }

    public SCMusicTrack setOriginalTrackNumber(Integer number) {
        this.replaceFirstProperty(new Property.UPNP.ORIGINAL_TRACK_NUMBER(number));
        return this;
    }

    public String getFirstPlaylist() {
        return (String)this.getFirstPropertyValue(Property.UPNP.PLAYLIST.class);
    }

    public String[] getPlaylists() {
        List list = this.getPropertyValues(Property.UPNP.PLAYLIST.class);
        return (String[])list.toArray(new String[list.size()]);
    }

    public SCMusicTrack setPlaylists(String[] playlists) {
        this.removeProperties(Property.UPNP.PLAYLIST.class);
        String[] arr$ = playlists;
        int len$ = playlists.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            this.addProperty(new Property.UPNP.PLAYLIST(s));
        }

        return this;
    }

    public StorageMedium getStorageMedium() {
        return (StorageMedium)this.getFirstPropertyValue(Property.UPNP.STORAGE_MEDIUM.class);
    }

    public SCMusicTrack setStorageMedium(StorageMedium storageMedium) {
        this.replaceFirstProperty(new Property.UPNP.STORAGE_MEDIUM(storageMedium));
        return this;
    }

    public Person getFirstContributor() {
        return (Person)this.getFirstPropertyValue(Property.DC.CONTRIBUTOR.class);
    }

    public Person[] getContributors() {
        List list = this.getPropertyValues(Property.DC.CONTRIBUTOR.class);
        return (Person[])list.toArray(new Person[list.size()]);
    }

    public SCMusicTrack setContributors(Person[] contributors) {
        this.removeProperties(Property.DC.CONTRIBUTOR.class);
        Person[] arr$ = contributors;
        int len$ = contributors.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Person p = arr$[i$];
            this.addProperty(new Property.DC.CONTRIBUTOR(p));
        }

        return this;
    }

    public String getDate() {
        return (String)this.getFirstPropertyValue(Property.DC.DATE.class);
    }

    public SCMusicTrack setDate(String date) {
        this.replaceFirstProperty(new Property.DC.DATE(date));
        return this;
    }
}
