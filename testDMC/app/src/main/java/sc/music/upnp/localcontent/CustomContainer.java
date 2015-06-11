package sc.music.upnp.localcontent;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;

import sc.music.server.ContentDirectoryService;

/**
 * Created by Administrator on 2015/6/10. �������ڴ���һ���Զ����cling��DLNA��������
 */
public class CustomContainer extends Container/*cling��*/ {
    protected String baseURL = null;

    public CustomContainer(String id, String parentID, String title, String creator, String baseURL)
    {
        this.setClazz(new DIDLObject.Class("object.container"));

        if(parentID==null || parentID.compareTo(""+ContentDirectoryService.ROOT_ID)==0)
            setId(id);
        else if(id==null)
            setId(parentID);
        else
            setId(parentID + ContentDirectoryService.SEPARATOR + id);

        setParentID(parentID);
        setTitle(title);
        setCreator(creator);
        setRestricted(true);
        setSearchable(true);
        setWriteStatus(WriteStatus.NOT_WRITABLE);
        setChildCount(0);

        this.baseURL = baseURL;
    }
}
