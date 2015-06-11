package sc.music.server;

import android.content.Context;
import android.util.Log;

import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.types.UDN;

import java.io.File;
import java.net.InetAddress;
import java.util.UUID;

/**
 * Created by Administrator on 2015/6/10.
 */
public class MediaServer extends fi.iki.elonen.SimpleWebServer{

    private final static String TAG = "MediaServer";

    private UDN udn = null;
    private LocalDevice localDevice = null;//cling库的
    private LocalService localService = null;//cling库的cling.model.meta.LocalService;
    private Context ctx = null;

    private final static int port = 8192;
    private static InetAddress localAddress;//java的网络库
    public MediaServer(String host, int port, File wwwroot, boolean quiet) {
        super(host, port, wwwroot, quiet);
    }
    public MediaServer(InetAddress localAddress, Context ctx) throws ValidationException
    {
        super(null, port, null, true);//父类的构造函数

        Log.i(TAG, "Creating media server !");

        localService = new AnnotationLocalServiceBinder()
                .read(ContentDirectoryService.class);

        localService.setManager(new DefaultServiceManager<ContentDirectoryService>(
                localService, ContentDirectoryService.class));

        udn = UDN.valueOf(new UUID(0, 10).toString());
        this.localAddress = localAddress;
        this.ctx = ctx;
//        //创建本地设备
//        createLocalDevice();
//
//        //内容目录服务
//        ContentDirectoryService contentDirectoryService = (ContentDirectoryService)localService.getManager().getImplementation();
//        contentDirectoryService.setContext(ctx);
//        contentDirectoryService.setBaseURL(getAddress());
    }
}
