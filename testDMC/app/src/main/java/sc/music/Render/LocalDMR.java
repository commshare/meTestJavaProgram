package sc.music.Render;

import sc.droid.dmc.R;
import sc.music.SpiderApplication;
import sc.music.upnp.model.IUpnpDevice;

/**
 * Created by Administrator on 2015/7/10.
 */
public class LocalDMR implements IUpnpDevice {

    @Override
    public String getDisplayString() {
        return "localdmr";
    }

    @Override
    public String getFriendlyName() {
        return SpiderApplication.getApplication().getString(R.string.local_dmr);
    }

    @Override
    public String getExtendedInformation() {
        return "SCME";
    }

    @Override
    public String getManufacturer() {
        return "zhangbin";
    }

    @Override
    public String getManufacturerURL() {
        return null;
    }

    @Override
    public String getModelName() {
        return null;
    }

    @Override
    public String getModelDesc() {
        return null;
    }

    @Override
    public String getModelNumber() {
        return null;
    }

    @Override
    public String getModelURL() {
        return null;
    }

    @Override
    public String getXMLURL() {
        return null;
    }

    @Override
    public String getPresentationURL() {
        return null;
    }

    @Override
    public String getSerialNumber() {
        return "20150710";
    }

    @Override
    public String getUDN() {
        return "myudn";
    }

    @Override
    public boolean equals(IUpnpDevice otherDevice) {
        return false;
    }

    @Override
    public String getUID() {
        return "zhangbin_20150710_dmr";
    }

    @Override
    public boolean asService(String service) {
        if(service.equals( "RenderingControl"))
            return true;
        else
            return false;
    }

    @Override
    public boolean asDeviceType(String type) {
        if(type.equals("LocalDLNARender"))
            return  true;
        else
            return false;
    }

    @Override
    public void printService() {

    }

    @Override
    public boolean isFullyHydrated() {
        return false;
    }
}