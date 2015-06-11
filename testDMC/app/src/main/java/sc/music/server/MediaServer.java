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
    private LocalDevice localDevice = null;//cling���
    private LocalService localService = null;//cling���cling.model.meta.LocalService;
    private Context ctx = null;

    private final static int port = 8192;
    private static InetAddress localAddress;//java�������
    public MediaServer(String host, int port, File wwwroot, boolean quiet) {
        super(host, port, wwwroot, quiet);
    }
    public MediaServer(InetAddress localAddress, Context ctx) throws ValidationException
    {
        super(null, port, null, true);//����Ĺ��캯��

        Log.i(TAG, "Creating media server !");

        localService = new AnnotationLocalServiceBinder()
                .read(ContentDirectoryService.class);

        localService.setManager(new DefaultServiceManager<ContentDirectoryService>(
                localService, ContentDirectoryService.class));

        udn = UDN.valueOf(new UUID(0, 10).toString());
        this.localAddress = localAddress;
        this.ctx = ctx;
//        //���������豸
//        createLocalDevice();
//
//        //����Ŀ¼����
//        ContentDirectoryService contentDirectoryService = (ContentDirectoryService)localService.getManager().getImplementation();
//        contentDirectoryService.setContext(ctx);
//        contentDirectoryService.setBaseURL(getAddress());
    }
}
