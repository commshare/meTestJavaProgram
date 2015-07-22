package sc.music.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;



import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import sc.droid.dmc.R;
import sc.music.Main;
import sc.music.Render.LocalDMR;
import sc.music.upnp.cling.CallableRendererFilter;
import sc.music.upnp.model.IUpnpDevice;

public class RendererDialog extends DialogFragment {

	private static String TAG="RenderDialog";
	private Callable<Void> callback = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		//��ͨ����ô���ӵ�һ�����̻�ȡ��DMR�豸��
		final Collection<IUpnpDevice> upnpDevices = Main.upnpServiceController.getServiceListener()
				.getFilteredDeviceList(new CallableRendererFilter());

		ArrayList<DeviceDisplay> list = new ArrayList<DeviceDisplay>();
		//����һ���࣬����LocalDMR������������DeviceDisplay
	//	LocalDMR mydmr=new LocalDMR();
		//�����Ͳ���ÿ�ζ�����һ����
		LocalDMR mydmr=(LocalDMR)Main.upnpServiceController.getLocadDmr();
		if(mydmr!=null)
			//���ȼ������Լ��ı����豸
			list.add(new DeviceDisplay(mydmr));
		else
			Log.e(TAG,"NOT FOUND localdmr");
		for (IUpnpDevice upnpDevice : upnpDevices)
			list.add(new DeviceDisplay(upnpDevice));

		final DialogFragment dialog = this;
		Log.e(TAG,"DMR list size ["+list.size()+"]");
		if(list.size()==0)
		{
			builder.setTitle(R.string.select_a_dmr)
				.setMessage(R.string.noRenderer)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		}
		else
		{
			ArrayAdapter<DeviceDisplay> rendererList = new ArrayAdapter<DeviceDisplay>(getActivity(),
				android.R.layout.simple_list_item_1, list);
			builder.setTitle(R.string.select_a_dmr).setAdapter(rendererList, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(which ==0 ){
						Log.e(TAG,"LocalDMR");
						//���Ǹ���dmc����ǰ��localdmr��������û������Ϊlocadmr�������һ�����
						Main.upnpServiceController.LockLocalRender();
					}
					else {
						//�Ƚ���
						Main.upnpServiceController.UnlockLocalRender();
						int newWhich=which-1;
						//����Ҫ���ݸ���dmc�������������Լ��õ���upnpDevices��
						Main.upnpServiceController.setSelectedRenderer((IUpnpDevice) upnpDevices.toArray()[newWhich]);
					}
					try {
						//ִ�е����ߵ�callback����
						if (callback != null)
							callback.call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		return builder.create();
	}

	public void setCallback(Callable<Void> callback)
	{
		this.callback = callback;
	}
}
