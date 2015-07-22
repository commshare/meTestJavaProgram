package sc.music.Render;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import org.fourthline.cling.controlpoint.ControlPoint;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.avtransport.callback.SetAVTransportURI;
import org.fourthline.cling.support.avtransport.callback.Stop;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.item.AudioItem;
import org.fourthline.cling.support.model.item.ImageItem;
import org.fourthline.cling.support.model.item.Item;
import org.fourthline.cling.support.model.item.PlaylistItem;
import org.fourthline.cling.support.model.item.TextItem;
import org.fourthline.cling.support.model.item.VideoItem;

import sc.music.Main;
import sc.music.server.ContentDirectoryService;
import sc.music.upnp.cling.CDevice;
import sc.music.upnp.cling.TrackMetadata;
import sc.music.upnp.cling.didl.ClingDIDLItem;
import sc.music.upnp.didl.IDIDLItem;
import sc.music.upnp.model.IUpnpDevice;

/**
 * Created by Administrator on 2015/7/10.
 */
public class LocalRender {
    Context ctx;
    private String TAG = "LocalRender";
    private ControlPoint controlPoint;
    String uri;

    public LocalRender() {

    }

    public LocalRender(Context ctx) {
        this.ctx = ctx;
    }

    public static AudioWife getAudioPlayer() {
        return AudioWife.getInstance();

    }

    public static Service getAVTransportService() {
        if (Main.upnpServiceController.getSelectedRenderer() == null)
            return null;

        return ((CDevice) Main.upnpServiceController.getSelectedRenderer()).getDevice().findService(
                new UDAServiceType("AVTransport"));
    }

    public static interface IUIRenderControl {
        void onLocalRenderStartPlay(Context ctx, String uri);
    }

    private static IUIRenderControl uiRender;

    public static void setUIRenderControler(IUIRenderControl c) {
        uiRender = c;
    }

    public void setURI(String uri, TrackMetadata trackMetadata) throws myException {
        Log.i(TAG, "Set uri to " + uri);
        this.uri = uri;
        Log.e(TAG, "path is [" + uri + "] and Uri.parse(path).getPath() [" + Uri.parse(uri).getPath() + "]");
        String realpath=getRealpath(Uri.parse(uri).getPath());

        uiRender.onLocalRenderStartPlay(ctx, realpath);
//        //这是发给dmr了，从此开始控制dmr
//        controlPoint.execute(new SetAVTransportURI(getAVTransportService(), uri, trackMetadata.getXML()) {
//
//            @Override
//            public void success(ActionInvocation invocation) {
//                super.success(invocation);
//                Log.i(TAG, "URI successfully set !");
//                commandPlay();
//            }
//
//            @Override
//            public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
//                Log.w(TAG, "Fail to set URI ! " + arg2);
//            }
//        });
    }

    public void launchItem(final IDIDLItem item) {
        if (Main.checkActiveDmc()) {
//        if (getAVTransportService() == null)
//            return;
            //为了先stop dlna的
            controlPoint = Main.factory.getControlPoint();
        }
        DIDLObject obj = ((ClingDIDLItem) item).getObject();
        if (!(obj instanceof Item))
            return;

        Item upnpItem = (Item) obj;


        String type = "";
        if (upnpItem instanceof AudioItem)
            type = "audioItem";
        else if (upnpItem instanceof VideoItem)
            type = "videoItem";
        else if (upnpItem instanceof ImageItem)
            type = "imageItem";
        else if (upnpItem instanceof PlaylistItem)
            type = "playlistItem";
        else if (upnpItem instanceof TextItem)
            type = "textItem";

        if (type.equals("audioItem")) {
            Log.e(TAG, "audioItem ,check localpath");

        } else
            Log.e(TAG, "NOT audioItem");
        // TODO genre && artURI  专辑图片还不支持啊
        //通过upnpitem构造一个TrackMetadata出来，传递信息都来自item
        final TrackMetadata trackMetadata = new TrackMetadata(upnpItem.getId(), upnpItem.getTitle(),
                upnpItem.getCreator(), "", "", upnpItem.getFirstResource().getValue(),
                "object.item." + type);
        Log.e(TAG, "url : upnpItem.getFirstResource().getValue() : [" + upnpItem.getFirstResource().getValue() + "]");
        Log.i(TAG, "TrackMetadata : " + trackMetadata.toString());

        if (Main.checkActiveDmc()) {
            Log.e(TAG, "dmc actived");
            if (controlPoint != null) {
                Log.e(TAG, "control point NOT null");
                //重新播放之前，首先暂停
//                // Stop playback before setting URI
//                controlPoint.execute(new Stop(getAVTransportService()) {
//                    @Override
//                    public void success(ActionInvocation invocation) {
//                        Log.v(TAG, "Success stopping ! ");
//                        callback();
//                    }
//
//                    @Override
//                    public void failure(ActionInvocation arg0, UpnpResponse arg1, String arg2) {
//                        Log.w(TAG, "Fail to stop ! " + arg2);
//                        callback();
//                    }
//
//                    public void callback() {
//                        //原来在这里，使用这俩来给播放器播放
//				/* 果然是全路径的
//				07-10 17:20:26.782: E/RendererCommand(24220):
//				 item.getURI()http://172.16.34.206:8192/a-8070.mp3]
//				这个URL是http的啊
//				* */
//                        Log.e(TAG, "item.getURI()" + item.getURI() + "]");
//                        setURI(item.getURI(), trackMetadata);
//                    }
//                });

                Log.e(TAG, "##item.getURI()" + item.getURI() + "]");
                try {
                    setURI(item.getURI(), trackMetadata);
                }catch(Exception e) {
                    Log.e(TAG, "1 Error setURI(item.getURI(), trackMetadata)");
                    Log.e(TAG, "exception", e);
                }
            } else
                Log.e(TAG, "control point is null");

        } else {
            Log.e(TAG, "dmc not actived item.getURI()" + item.getURI() + "]");
            try {
                setURI(item.getURI(), trackMetadata);
            }catch(Exception e) {
                Log.e(TAG, "2  Error setURI(item.getURI(), trackMetadata)");
                Log.e(TAG, "exception", e);
            }

        }
    }

    //copy from MediaServer private ServerObject getFileServerObject(String id) throws InvalidIdentificatorException
    public String getRealpath(String id) throws myException{
        try {
            // Remove extension
            int dot = id.lastIndexOf('.');
            if (dot >= 0)
                id = id.substring(0, dot);

            /*
            * 07-22 16:39:08.929: E/MediaServer(2749): uri is a id [/a-8068.mp3]
07-22 16:39:08.929: V/MediaServer(2749): media of id is 8068
*/
            // Try to get media id
            int mediaId = Integer.parseInt(id.substring(3));
            Log.v(TAG, "media of id is " + mediaId);

            MediaStore.MediaColumns mediaColumns = null;
            Uri uri = null;

            if (id.startsWith("/" + ContentDirectoryService.AUDIO_PREFIX)) {
                Log.v(TAG, "Ask for audio");
                uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                mediaColumns = new MediaStore.Audio.Media();
            } else if (id.startsWith("/" + ContentDirectoryService.VIDEO_PREFIX)) {
                Log.v(TAG, "Ask for video");
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                mediaColumns = new MediaStore.Video.Media();
            } else if (id.startsWith("/" + ContentDirectoryService.IMAGE_PREFIX)) {
                Log.v(TAG, "Ask for image");
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                mediaColumns = new MediaStore.Images.Media();
            }

            if (uri != null && mediaColumns != null) {
                String[] columns = new String[]{mediaColumns.DATA, mediaColumns.MIME_TYPE};
                String where = mediaColumns._ID + "=?";
                String[] whereVal = {"" + mediaId};

                String path = null;
                String mime = null;
                Cursor cursor = ctx.getContentResolver().query(uri, columns, where, whereVal, null);

                if (cursor.moveToFirst()) {
                    path = cursor.getString(cursor.getColumnIndexOrThrow(mediaColumns.DATA));
                    mime = cursor.getString(cursor.getColumnIndexOrThrow(mediaColumns.MIME_TYPE));
                }
                cursor.close();
                Log.e(TAG, "get path " + path + "]");
                if (path != null) //有path的啊
                    return path; // new ServerObject(path, mime);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while parsing " + id);
            Log.e(TAG, "exception", e);
        }

         throw new myException(id + " was not found in media database");
    }
    public class myException extends java.lang.Exception
    {
        public myException(){super();}
        public myException(String message){super(message);}
    }
}