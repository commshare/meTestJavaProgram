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

package sc.music.ui.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

import sc.droid.dmc.R;
import sc.music.Main;
import sc.music.upnp.cling.CallableContentDirectoryFilter;

import sc.music.upnp.controler.DIDLObjectDisplay;
import sc.music.upnp.didl.DIDLDevice;
import sc.music.upnp.didl.IDIDLContainer;
import sc.music.upnp.didl.IDIDLItem;
import sc.music.upnp.didl.IDIDLObject;
import sc.music.upnp.didl.IDIDLParentContainer;
import sc.music.upnp.model.IContentDirectoryCommand;
import sc.music.upnp.model.IDeviceDiscoveryObserver;
import sc.music.upnp.model.IRendererCommand;
import sc.music.upnp.model.IUpnpDevice;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;


//用来显示DMS内容的
public class ContentDirectoryFragment extends ListFragment implements Observer
{
	private static final String TAG = "ContentDirectoryFragment";

	private ArrayAdapter<DIDLObjectDisplay> contentList/*实际上是一个adatper啊*/;
	private LinkedList<String> tree = null;
	private String currentID = null;
	private IUpnpDevice device;
	private IUpnpDevice localdevice;
	private IContentDirectoryCommand contentDirectoryCommand;

	static final String STATE_CONTENTDIRECTORY = "contentDirectory";
	static final String STATE_TREE = "tree";
	static final String STATE_CURRENT = "current";

	public static ContentDirectoryFragment newInstance(){
		ContentDirectoryFragment f=new ContentDirectoryFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
	   Main.setContentDirectoryFragment(this);
	   super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.browsing_list_fragment, container, false);
	}

	/** This update the search visibility depending on current content directory capabilities */
	public void updateSearchVisibility()
	{
		final Activity a = getActivity();
		if(a!=null) {
			a.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						Main.setSearchVisibility(contentDirectoryCommand != null && contentDirectoryCommand.isSearchAvailable());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	//这个类究竟是干啥的？
	private DeviceObserver deviceObserver;

	//本地设备会从这里传递进来，当前的操作是更新UI，我可以变成触发其他功能
	public class DeviceObserver implements IDeviceDiscoveryObserver
	{
		ContentDirectoryFragment cdf; //UI啊

		//构造函数要传入一个ContentDirectoryFragment
		public DeviceObserver(ContentDirectoryFragment cdf){
			this.cdf = cdf;
		}

		@Override
		public void addedDevice(IUpnpDevice device) {
			//有设备传入啊
			Log.e(TAG,"CDF DeviceObserver,ADD A device :"+device.getFriendlyName());
			//如果有选中的设备，就让ContentDirectoryFragment更新
			if(Main.upnpServiceController.getSelectedContentDirectory() == null)
				//cdf.update();//更新UI
			cdf.update(device);
		}

		@Override
		public void removedDevice(IUpnpDevice device) {
			if(Main.upnpServiceController.getSelectedContentDirectory() == null)
				cdf.update();
		}
	}

	//这个adapter是显示dms的cds的
	public class CustomAdapter extends ArrayAdapter<DIDLObjectDisplay>
	{
		private final int layout; //每个item的布局。
		private LayoutInflater inflater;

		public CustomAdapter(Context context) {
			super(context, 0);
			this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //每个item的布局
			this.layout = R.layout.browsing_list_item;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
				convertView = inflater.inflate(layout, null);//从item的布局变成item的view

			// Item
			final DIDLObjectDisplay entry = getItem(position);
            //根据item的didl信息来填充这些UI
            //icon
			ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
			imageView.setImageResource(entry.getIcon());

            //title
			TextView text1 = (TextView) convertView.findViewById(R.id.text1);
			text1.setText(entry.getTitle());

            //描述
			TextView text2 = (TextView) convertView.findViewById(R.id.text2);
			text2.setText((entry.getDescription()!=null) ? entry.getDescription() : "");

            //count是什么？
			TextView text3 = (TextView) convertView.findViewById(R.id.text3);
			text3.setText(entry.getCount());

			return convertView;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

        //content是getView返回的？？？？
		contentList = new CustomAdapter(this.getView().getContext());

        //这是ListFragment的方法啊，设置contentList实际上是一个ArrayAdapter
		setListAdapter(contentList);

        //==========开始搜寻DMS
		//创建了这个设备观察者
		deviceObserver = new DeviceObserver(this);
		//dms的设备观察者加入到“内容目录服务设备发现”观察者列表中了。
		//一旦设备被发现，就会通知设备观察者
		Main.upnpServiceController.getContentDirectoryDiscovery().addObserver(deviceObserver);

		// Listen to content directory change监听内容目录的改变
		if (Main.upnpServiceController != null)
			//监听啥？
			Main.upnpServiceController.addSelectedContentDirectoryObserver(this);
		else
			Log.e(TAG, "upnpServiceController was not ready !!!");

		if (savedInstanceState != null
			&& savedInstanceState.getStringArray(STATE_TREE) != null
			&& Main.upnpServiceController.getSelectedContentDirectory() != null
			&& 0 == Main.upnpServiceController.getSelectedContentDirectory().getUID()/*获取所选中设备的UID*/
			.compareTo(savedInstanceState.getString(STATE_CONTENTDIRECTORY)))
		{
			Log.i(TAG, "Restore previews state");

			// Content directory is still the same => reload context
			tree = new LinkedList<>(Arrays.asList(savedInstanceState.getStringArray(STATE_TREE)));
			currentID = savedInstanceState.getString(STATE_CURRENT);

			//从这里获得选中的DMS
			device = Main.upnpServiceController.getSelectedContentDirectory();
			contentDirectoryCommand = Main.factory.createContentDirectoryCommand();
		}
		//长按设备或者某个文件
		getListView().setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View v, int position, long id) {
				Log.v(TAG, "On long-click event");

				IDIDLObject didl = contentList.getItem(position).getDIDLObject();

				if (didl instanceof IDIDLItem)
				{
					IDIDLItem ididlItem = (IDIDLItem) didl;
					final Activity a = getActivity();
					final Intent intent = new Intent(Intent.ACTION_VIEW);

                    /*07-10 15:10:28.822: D/ClingDIDLItem(6862):
                    Item : http://172.16.34.206:8192/a-8068.mp3
                    * */
					Uri uri = Uri.parse(ididlItem.getURI());
					intent.setDataAndType(uri, didl.getDataType());
                    /*
                    07-10 15:13:21.592: E/ContentDirectoryFragment(7348): Uri path[/a-8068.mp3] type [audio/*]
                    * */
                    Log.e(TAG,"Uri path["+uri.getPath()+"] type ["+didl.getDataType()+"]");

                    //就暂时不启动其他应用来处理了吧
//					try {
//						a.startActivity(intent);
//					} catch (ActivityNotFoundException ex) {
//						Toast.makeText(getActivity(), R.string.failed_action, Toast.LENGTH_SHORT).show();
//					}
				}
				else
				{
					Toast.makeText(getActivity(), R.string.no_action_available, Toast.LENGTH_SHORT).show();
				}

				return true;
			}
		});

		Log.d(TAG, "Force refresh");
		refresh();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Main.upnpServiceController.delSelectedContentDirectoryObserver(this);
		Main.upnpServiceController.getContentDirectoryDiscovery().removeObserver(deviceObserver);
	}

	private PullToRefreshLayout mPullToRefreshLayout;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

		// This is the View which is created by ListFragment
		ViewGroup viewGroup = (ViewGroup) view;

		view.setBackgroundColor(getResources().getColor(R.color.grey));

		// We need to create a PullToRefreshLayout manually
		mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

		// We can now setup the PullToRefreshLayout
		ActionBarPullToRefresh.from(getActivity())
			.insertLayoutInto(viewGroup)
			.theseChildrenArePullable(getListView(), getListView().getEmptyView())
			.listener(new uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener() {
				@Override
				public void onRefreshStarted(View view) {
					refresh();
				}
			})
			.setup(mPullToRefreshLayout);
	}

	@Override
	public void onDestroyView()
	{
		mPullToRefreshLayout.setRefreshComplete();
		super.onDestroyView();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		Log.i(TAG, "Save instance state");

		if (Main.upnpServiceController.getSelectedContentDirectory() == null)
			return;

		savedInstanceState.putString(STATE_CONTENTDIRECTORY, Main.upnpServiceController.getSelectedContentDirectory()
			.getUID());

		if (tree != null)
		{
			String[] arrayTree = new String[tree.size()];
			int i = 0;
			for (String s : tree)
				arrayTree[i++] = s;

			savedInstanceState.putStringArray(STATE_TREE, arrayTree);
			savedInstanceState.putString(STATE_CURRENT, currentID);
		}

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		contentList.clear();
		refresh();
	}

	public Boolean goBack()
	{
		Log.e(TAG,"goBack()");
		if(tree == null || tree.isEmpty())
		{
			if(Main.upnpServiceController.getSelectedContentDirectory() != null)
			{
				// Back on device root, unselect device
				Main.upnpServiceController.setSelectedContentDirectory(null);
				return false;
			}
			else
			{
				// Already at the upper level
				return true;
			}
		}
		else
		{
			Log.d(TAG, "Go back in browsing");
			currentID = tree.pop();
			update();
			return false;
		}
	}

	public void printCurrentContentDirectoryInfo()
	{
		Log.i(TAG, "current Device : " + Main.upnpServiceController.getSelectedContentDirectory().getDisplayString());
		Main.upnpServiceController.getSelectedContentDirectory().printService();
	}

	public class RefreshCallback implements Callable<Void> {
		public Void call() throws Exception {
			final Activity a = getActivity();
			if(a!=null) {
				a.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							mPullToRefreshLayout.setRefreshComplete();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			return null;
		}
	}

	public class ContentCallback extends RefreshCallback
	{
		private ArrayAdapter<DIDLObjectDisplay> contentList;
		private ArrayList<DIDLObjectDisplay> content;

		public ContentCallback(ArrayAdapter<DIDLObjectDisplay> contentList)
		{
			this.contentList = contentList;
			this.content = new ArrayList<>();//这是个list
		}

		public void setContent(ArrayList<DIDLObjectDisplay> content)
		{
			this.content = content;
		}

		public Void call() throws Exception
		{
			Log.e(TAG,"ContentCallback call");
			final Activity a = getActivity();
			if(a!=null) {
				a.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							// Empty the list
							contentList.clear();
							// Fill the list
							contentList.addAll(content);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			// Refresh
			return super.call();
		}
	}

	public void setEmptyText(CharSequence text) {
		((TextView)getListView().getEmptyView()).setText(text);
	}

	public synchronized void refresh()
	{

		setEmptyText("refresh..."+getString(R.string.loading));

		final Activity a = getActivity();
		if(a!=null) {
			a.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						mPullToRefreshLayout.setRefreshing(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		Log.e(TAG, "refresh()-----2--Update search visibility----");
		// Update search visibility
		updateSearchVisibility();

		if (Main.upnpServiceController.getSelectedContentDirectory() == null)
		{
			// List here the content directory devices
			setEmptyText(getString(R.string.device_list_empty));

			if (device != null)
			{
				Log.i(TAG, "Current content directory have been removed");
				device = null;
				tree = null;
			}
			//Log.e(TAG, "refresh()-----3------");
			// Fill with the content directory list 用当前设备的列表来填充
			Log.e(TAG,"refresh. 3 .TO GET DMS LIST");
			//主动获取dms列表upnpDevices
			final Collection<IUpnpDevice> upnpDevices = Main.upnpServiceController.getServiceListener()
				.getFilteredDeviceList(new CallableContentDirectoryFilter());

			ArrayList<DIDLObjectDisplay> list = new ArrayList<DIDLObjectDisplay>();
			for (IUpnpDevice upnpDevice : upnpDevices)
				list.add(new DIDLObjectDisplay(new DIDLDevice(upnpDevice)));
			Log.e(TAG, "refresh()-----4--SHOW DMS LIST----");
			try {
				ContentCallback cc = new ContentCallback(contentList);
				cc.setContent(list);
				//Log.e(TAG, "refresh...UI add  dms list");
				cc.call();
			} catch (Exception e){e.printStackTrace();}
			Log.e(TAG, "refresh..4 AFTER SHOW UI  dms list ,RETURN ");

			return;//退出
		}
		Log.e(TAG, "refresh()-----5---Main.upnpServiceController.getSelectedContentDirectory() NOT NULL---");
		Log.i(TAG, "device " + device + " device " + ((device != null) ? device.getDisplayString() : ""));
		Log.i(TAG, "contentDirectoryCommand : " + contentDirectoryCommand);

		contentDirectoryCommand = Main.factory.createContentDirectoryCommand();
		if (contentDirectoryCommand == null) {
			Log.e(TAG,"Can't do anything if upnp not ready");
			return; // Can't do anything if upnp not ready
		}
		Log.e(TAG, "refresh()-----6---UPNP READY--");
		//设备为空
		if (device == null || !device.equals(Main.upnpServiceController.getSelectedContentDirectory()))
		{Log.e(TAG,"device IS NULL ");
			device = Main.upnpServiceController.getSelectedContentDirectory();

			Log.e(TAG, "Content directory changed !!! "
					+ Main.upnpServiceController.getSelectedContentDirectory().getDisplayString());

			tree = new LinkedList<String>();

			Log.e(TAG, "Browse root of a new device");
			String rootid="0";
			//用dm的contentDirectoryCommand功能执行的遍历啊
			//contentDirectoryCommand.browse(rootid, null, new ContentCallback(contentList));
			//ADD 直接all music了
			String mCurrentID="2$0";String mParentID="0";
			contentDirectoryCommand.browse(mCurrentID, mParentID, new ContentCallback(contentList));
		}
		else
		{ Log.e(TAG,"device NOT NULL ");

			if (tree != null && tree.size() > 0)
			{
				String parentID = (tree.size() > 0) ? tree.getLast() : null;
				//这里很关键，根据这俩来搜寻的
				Log.e(TAG, "Browse, currentID : " + currentID + ", parentID : " + parentID);
				//直接all music了
				currentID="2$0";parentID="0";
				contentDirectoryCommand.browse(currentID, parentID, new ContentCallback(contentList));
			}
			else
			{
				Log.i(TAG, "Browse root");
//				String rootid="0";
//				contentDirectoryCommand.browse(rootid, null, new ContentCallback(contentList));
				//ADD 直接all music了
				String mCurrentID="2$0";String mParentID="0";
				contentDirectoryCommand.browse(mCurrentID, mParentID, new ContentCallback(contentList));
			}
		}
	}

	//点击选中设备或者是文件
	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		Log.e(TAG,"click position["+position+"]");
		super.onListItemClick(l, v, position, id);

		IDIDLObject didl = contentList.getItem(position).getDIDLObject();

		try {
			if(didl instanceof DIDLDevice) //是设备
			{
				Log.e(TAG,"DIDLDevice");
				Main.upnpServiceController.setSelectedContentDirectory( ((DIDLDevice)didl).getDevice(), false );

				// Refresh display
				//Log.e(TAG,"refresh display");
				refresh();
			}
			else if (didl instanceof IDIDLContainer) //是容器
			{Log.e(TAG,"IDIDLContainer");
				// Update position
				if (didl instanceof IDIDLParentContainer)
				{
					currentID = tree.pop();
				}
				else
				{
					currentID = didl.getId();
					String parentID = didl.getParentID();
					tree.push(parentID);
				}

				// Refresh display
				refresh();
			}
			else if (didl instanceof IDIDLItem) //是文件了吧
			{
				// Launch item
				launchURI((IDIDLItem) didl);
			}
		} catch (Exception e) {
			Log.e(TAG, "Unable to finish action after item click");
			e.printStackTrace();
		}
	}

	private void launchURI(final IDIDLItem uri)
	{
		if (Main.upnpServiceController.getSelectedRenderer() == null)
		{
			// No renderer selected yet, open a popup to select one
			final Activity a = getActivity();
			//创建一个线程，处理播放url的问
			if(a!=null) {
				a.runOnUiThread(new Runnable(){
					@Override
					public void run() {
						try {
							RendererDialog rendererDialog = new RendererDialog();
							rendererDialog.setCallback(new Callable<Void>() {
								@Override
								public Void call() throws Exception {
									//把uri传递给dmr
									launchURIRenderer(uri);
									return null;
								}
							});
							rendererDialog.show(getActivity().getFragmentManager(), "RendererDialog");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
		else
		{
			// Renderer available, go for it
			launchURIRenderer(uri);
		}

	}

	private void launchURIRenderer(IDIDLItem uri)
	{
		IRendererCommand rendererCommand = Main.factory.createRendererCommand(Main.factory.createRendererState());
		rendererCommand.launchItem(uri);
	}

	@Override
	public void update(Observable observable, Object data)
	{
		Log.i(TAG, "ContentDirectory have changed");
		update();
	}

	public void update()
	{
		final Activity a = getActivity();
		if(a!=null) {
			a.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					refresh();
				}
			});
		}
	}

//这是我自己新加入的
	public void update(IUpnpDevice device)
	{
		this.localdevice=device;

		final Activity a = getActivity();
		if(a!=null) {
			a.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					screfresh();
				}
			});
		}
	}
    //这是我自己新加入的
	public synchronized void screfresh()
	{
		device=this.localdevice;
		setEmptyText("refresh..."+getString(R.string.loading));

		final Activity a = getActivity();
		if(a!=null) {
			a.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						mPullToRefreshLayout.setRefreshing(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		Log.e(TAG, "refresh()-----2--Update search visibility----");
		// Update search visibility
		updateSearchVisibility();

//		if (Main.upnpServiceController.getSelectedContentDirectory() == null)
//		{
//			// List here the content directory devices
//			setEmptyText(getString(R.string.device_list_empty));
//
//			if (device != null)
//			{
//				Log.i(TAG, "Current content directory have been removed");
//				device = null;
//				tree = null;
//			}
//			//Log.e(TAG, "refresh()-----3------");
//			// Fill with the content directory list 用当前设备的列表来填充
//			Log.e(TAG,"refresh. 3 .TO GET DMS LIST");
//			//主动获取dms列表upnpDevices
//			final Collection<IUpnpDevice> upnpDevices = Main.upnpServiceController.getServiceListener()
//					.getFilteredDeviceList(new CallableContentDirectoryFilter());
//
//			ArrayList<DIDLObjectDisplay> list = new ArrayList<DIDLObjectDisplay>();
//			for (IUpnpDevice upnpDevice : upnpDevices)
//				list.add(new DIDLObjectDisplay(new DIDLDevice(upnpDevice)));
//			Log.e(TAG, "refresh()-----4--SHOW DMS LIST----");
//			try {
//				ContentCallback cc = new ContentCallback(contentList);
//				cc.setContent(list);
//				//Log.e(TAG, "refresh...UI add  dms list");
//				cc.call();
//			} catch (Exception e){e.printStackTrace();}
//			Log.e(TAG, "refresh..4 AFTER SHOW UI  dms list ,RETURN ");
//
//			return;//退出
//		}
		DIDLObjectDisplay didldevice=new DIDLObjectDisplay(new DIDLDevice(device));
		IDIDLObject didl = didldevice.getDIDLObject();

		try {
			if(didl instanceof DIDLDevice) //是设备
			{
				Log.e(TAG,"DIDLDevice MYLOCALDECIVE");
				Main.upnpServiceController.setSelectedContentDirectory( ((DIDLDevice)didl).getDevice(), false );

				// Refresh display
				//Log.e(TAG,"refresh display");
				//refresh();
			}
//			else if (didl instanceof IDIDLContainer) //是容器
//			{Log.e(TAG,"IDIDLContainer");
//				// Update position
//				if (didl instanceof IDIDLParentContainer)
//				{
//					currentID = tree.pop();
//				}
//				else
//				{
//					currentID = didl.getId();
//					String parentID = didl.getParentID();
//					tree.push(parentID);
//				}
//
//				// Refresh display
//				refresh();
//			}
//			else if (didl instanceof IDIDLItem)
//			{
//				// Launch item
//				launchURI((IDIDLItem) didl);
//			}
		} catch (Exception e) {
			Log.e(TAG, "Unable to finish action after item click");
			e.printStackTrace();
		}

		Log.e(TAG, "refresh()-----5---Main.upnpServiceController.getSelectedContentDirectory() NOT NULL---");
		Log.i(TAG, "device " + device + " device " + ((device != null) ? device.getDisplayString() : ""));
		Log.i(TAG, "contentDirectoryCommand : " + contentDirectoryCommand);

		contentDirectoryCommand = Main.factory.createContentDirectoryCommand();
		if (contentDirectoryCommand == null) {
			Log.e(TAG,"Can't do anything if upnp not ready");
			return; // Can't do anything if upnp not ready
		}
		Log.e(TAG, "refresh()-----6---UPNP READY--");
		//设备为空
		if (device == null || !device.equals(Main.upnpServiceController.getSelectedContentDirectory()))
		{Log.e(TAG,"device IS NULL ");
			device = Main.upnpServiceController.getSelectedContentDirectory();

			Log.e(TAG, "Content directory changed !!! "
					+ Main.upnpServiceController.getSelectedContentDirectory().getDisplayString());

			tree = new LinkedList<String>();

			Log.e(TAG, "Browse root of a new device");
			String rootid="0";
			//用dm的contentDirectoryCommand功能执行的遍历啊
			//contentDirectoryCommand.browse(rootid, null, new ContentCallback(contentList));
			//ADD 直接all music了
			String mCurrentID="2$0";String mParentID="0";
			contentDirectoryCommand.browse(mCurrentID, mParentID, new ContentCallback(contentList));
		}
		else
		{ Log.e(TAG,"device NOT NULL ");

			if (tree != null && tree.size() > 0)
			{
				String parentID = (tree.size() > 0) ? tree.getLast() : null;
				//这里很关键，根据这俩来搜寻的
				Log.e(TAG, "Browse, currentID : " + currentID + ", parentID : " + parentID);
				//直接all music了
				currentID="2$0";parentID="0";
				contentDirectoryCommand.browse(currentID, parentID, new ContentCallback(contentList));
			}
			else
			{
				Log.i(TAG, "Browse root");
//				String rootid="0";
//				contentDirectoryCommand.browse(rootid, null, new ContentCallback(contentList));
				//ADD 直接all music了
				String mCurrentID="2$0";String mParentID="0";
				contentDirectoryCommand.browse(mCurrentID, mParentID, new ContentCallback(contentList));
			}
		}
	}
}
