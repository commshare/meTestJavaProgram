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
		import android.app.Fragment;
		import android.content.Context;
		import android.graphics.Color;
		import android.media.MediaPlayer;
		import android.net.Uri;
		import android.os.Bundle;
		import android.os.Handler;
		import android.util.Log;
		import android.view.LayoutInflater;
		import android.view.View;
		import android.view.View.OnClickListener;
		import android.view.ViewGroup;
		import android.widget.ImageView;
		import android.widget.SeekBar;
		import android.widget.SeekBar.OnSeekBarChangeListener;
		import android.widget.TextView;
		import android.widget.Toast;



		import java.util.Observable;
		import java.util.Observer;
		import java.util.concurrent.Callable;

		import sc.droid.dmc.R;
		import sc.music.Main;
		import sc.music.Render.AudioWife;
		import sc.music.Render.LocalRender;
		import sc.music.upnp.cling.RendererState;
		import sc.music.upnp.model.ARendererState;
		import sc.music.upnp.model.IRendererCommand;
		import sc.music.upnp.model.IUpnpDevice;

public class RendererFragment extends Fragment implements Observer,LocalRender.IUIRenderControl
{
	private static final String TAG = "RendererFragment";

	private IUpnpDevice device;
	private ARendererState rendererState;
	private IRendererCommand rendererCommand;

	// NowPlaying Slide
	private ImageView stopButton;//停止
	private ImageView play_pauseButton;//播放暂停
	private ImageView volumeButton;//静音

	// Settings Slide
	SeekBar progressBar; //进度
	SeekBar volume; //音量

	TextView duration; //总时间
	boolean durationRemaining;//剩余时间

	private Context mContext;

	public RendererFragment()
	{
		super();
		durationRemaining = true;
	}

	public void hide()
	{
//		Activity a = getActivity();
//		if (a==null)
//			return;
//		a.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
//		a.findViewById(R.id.separator).setVisibility(View.INVISIBLE);
//		getFragmentManager().beginTransaction().hide(this).commit();
	}

	public void show()
	{
		Activity a = getActivity();
		if (a==null)
			return;
		a.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		a.findViewById(R.id.separator).setVisibility(View.VISIBLE);
		getFragmentManager().beginTransaction().show(this).commit();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		Log.e(TAG, "RenderFragment onActivityCreated");//看来当前用的还是这个
		super.onActivityCreated(savedInstanceState);

		//for audio wife
		mContext=getActivity().getApplicationContext();
		Main.myrender.setUIRenderControler(this);
		// Listen to renderer change
		if (Main.upnpServiceController != null)
			Main.upnpServiceController.addSelectedRendererObserver(this);
		else
			Log.w(TAG, "upnpServiceController was not ready !!!");

		// Initially hide renderer
		hide();
	}

	@Override
	public void onStart()
	{
		super.onStart();

		// Call Main Initialise Function
		this.init();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if(Main.checkActiveDmc()) {
			startControlPoint();

			if (rendererCommand != null)
				rendererCommand.resume();
		}
	}

	@Override
	public void onPause()
	{
		device = null;
		if(Main.checkActiveDmc()) {
			if (rendererCommand != null)
				rendererCommand.pause();
		}
		super.onPause();
	}

	@Override
	public void onDestroy()
	{
		if(Main.checkActiveDmc()) {
			Main.upnpServiceController.delSelectedRendererObserver(this);
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.renderer_fragment, container, false);
	}

	public void startControlPoint()
	{
		//没有选中dmr的情况下
		if (Main.upnpServiceController.getSelectedRenderer() == null)
		{
			//也没有之前的dmr，会隐藏掉播控UI
			if (device != null)
			{
				Log.i(TAG, "Current renderer have been removed");
				device = null;

				final Activity a = getActivity();
				if (a == null)
					return;

				//这个线程会一直跑么？
				a.runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						try {
							hide();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			return;
		}

		//当有了设备或者已经在播放了
		if (device == null || rendererState == null || rendererCommand == null
				|| !device.equals(Main.upnpServiceController.getSelectedRenderer()))
		{
			device = Main.upnpServiceController.getSelectedRenderer();

			Log.i(TAG, "Renderer changed !!! " + Main.upnpServiceController.getSelectedRenderer().getDisplayString());

			//播控来了
			rendererState = Main.factory.createRendererState();
			rendererCommand = Main.factory.createRendererCommand(rendererState);

			if (rendererState == null || rendererCommand == null)
			{
				Log.e(TAG, "Fail to create renderer command and/or state");
				return;
			}

			rendererCommand.resume();

			rendererState.addObserver(this);
			rendererCommand.updateFull();
		}
		//在这里搞？更新UI 啥的
		updateRenderer();
	}

	public void updateRenderer()
	{
		Log.v(TAG, "updateRenderer");

		if (rendererState != null)
		{
			final Activity a = getActivity();
			if (a == null)
				return;

			a.runOnUiThread(new Runnable() {
				@Override
				public void run()
				{
					try {
						show();

						TextView title = (TextView) a.findViewById(R.id.title);
						TextView artist = (TextView) a.findViewById(R.id.subtitle);
						SeekBar seek = (SeekBar) a.findViewById(R.id.progressBar);
						SeekBar volume = (SeekBar) a.findViewById(R.id.volume);
						TextView durationElapse = (TextView) a.findViewById(R.id.trackDurationElapse);

						if (title == null || artist == null || seek == null || duration == null || durationElapse == null)
							return;

						if (durationRemaining)
							duration.setText(rendererState.getRemainingDuration());
						else
							duration.setText(rendererState.getDuration());

						durationElapse.setText(rendererState.getPosition());

						seek.setProgress(rendererState.getElapsedPercent());

						title.setText(rendererState.getTitle());
						artist.setText(rendererState.getArtist());

						if (rendererState.getState() == RendererState.State.PLAY) {
							//原来是在这里切换为pause图标的
							play_pauseButton.setImageResource(R.drawable.pause);
							play_pauseButton.setContentDescription(getResources().getString(R.string.pause));
						} else {
							play_pauseButton.setImageResource(R.drawable.play);
							play_pauseButton.setContentDescription(getResources().getString(R.string.play));
						}

						if (rendererState.isMute())
							volumeButton.setImageResource(R.drawable.volume_mute);
						else
							volumeButton.setImageResource(R.drawable.volume);

						volume.setProgress(rendererState.getVolume());

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			Log.v(TAG, rendererState.toString());
		}
	}

	@Override
	public void update(Observable observable, Object data)
	{

		if(Main.checkActiveDmc())
			startControlPoint();
	}

	private void init()
	{
		SetupButtons();//设置ui
		SetupButtonListeners();
	}

	private void SetupButtons()
	{
		// Now_Playing Footer Buttons
		play_pauseButton = (ImageView) getActivity().findViewById(R.id.play_pauseButton);
		volumeButton = (ImageView) getActivity().findViewById(R.id.volumeIcon);
		stopButton = (ImageView) getActivity().findViewById(R.id.stopButton);
		progressBar = (SeekBar) getActivity().findViewById(R.id.progressBar);
		volume = (SeekBar) getActivity().findViewById(R.id.volume);
		volume.setVisibility(View.INVISIBLE);
		volumeButton.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onLocalRenderStartPlay(final Context ctx, String path) {
		Log.e(TAG,"onLocalRenderStartPlay");
		final Activity a = getActivity();
		if (a == null)
			return;
		TextView durationElapse = (TextView) a.findViewById(R.id.trackDurationElapse);
		TextView durationRemain = (TextView) a.findViewById(R.id.trackDurationRemaining);
		Log.e(TAG,"we got realpath ["+path+"]");
		AudioWife.getInstance().init(ctx, Uri.parse(path))
				.setPlayView(play_pauseButton)
				.setPauseView(play_pauseButton)
				.setSeekBar(progressBar)
				.setRuntimeView(durationElapse)
				.setTotalTimeView(durationRemain);

		AudioWife.getInstance().addOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				Toast.makeText(ctx, "Completed", Toast.LENGTH_SHORT).show();
				// do you stuff.
			}
		});

		AudioWife.getInstance().addOnPlayClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(ctx, "Play", Toast.LENGTH_SHORT).show();
				// get-set-go. Lets dance.
			}
		});

		AudioWife.getInstance().addOnPauseClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(ctx, "Pause", Toast.LENGTH_SHORT).show();
				// Your on audio pause stuff.
			}
		});

	}

	public abstract class ButtonCallback implements Callable<Void>
	{
		protected View view = null;

		// Button action
		protected abstract void action();

		public ButtonCallback(View v)
		{
			this.view = v;
		}

		public Void call() throws Exception
		{
			view.setBackgroundColor(getResources().getColor(R.color.blue_trans));
			action();
			new Handler().postDelayed(new Runnable() {
				public void run()
				{
					view.setBackgroundColor(Color.TRANSPARENT);
				}
			}, 100L);
			return null;
		}
	}

	public class PlayPauseCallback extends ButtonCallback
	{
		public PlayPauseCallback(View v) {
			super(v);
		}

		@Override
		protected void action()
		{
			if (rendererCommand != null)
				rendererCommand.commandToggle();
		}
	}

	public class StopCallback extends ButtonCallback
	{
		public StopCallback(View v) {
			super(v);
		}

		@Override
		protected void action()
		{
			if (rendererCommand != null)
				rendererCommand.commandStop();
		}
	}

	public class MuteCallback extends ButtonCallback
	{
		public MuteCallback(View v) {
			super(v);
		}

		@Override
		protected void action()
		{
			if (rendererCommand != null)
				rendererCommand.toggleMute();
		}
	}

	public class TimeSwitchCallback extends ButtonCallback
	{
		public TimeSwitchCallback(View v) {
			super(v);
		}

		@Override
		protected void action()
		{
			if (rendererState == null)
				return;

			durationRemaining = !durationRemaining;
			if (durationRemaining)
				duration.setText(rendererState.getRemainingDuration());
			else
				duration.setText(rendererState.getDuration());
		}
	}

	private void SetupButtonListeners()
	{
		if (play_pauseButton != null)
		{
			//播放还是暂停？
			play_pauseButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					try {
					(new PlayPauseCallback(v)).call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		if (stopButton != null)
		{
			stopButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					try {
						(new StopCallback(v)).call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		if (volumeButton != null)
		{
			volumeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					try {
						(new MuteCallback(v)).call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		if (progressBar != null)
		{
			progressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
					if (rendererState == null)
						return;

					int position = seekBar.getProgress();

					long t = (long) ((1.0 - ((double) seekBar.getMax() - position) / (seekBar.getMax())) * rendererState
							.getDurationSeconds());
					long h = t / 3600;
					long m = (t - h * 3600) / 60;
					long s = t - h * 3600 - m * 60;
					String seek = formatTime(h, m, s);

					Toast.makeText(getActivity().getApplicationContext(), "Seek to " + seek, Toast.LENGTH_SHORT).show();
					Log.d(TAG, "Seek to " + seek);
					if (rendererCommand != null)
						rendererCommand.commandSeek(seek);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
				{
				}
			});
		}

		if (volume != null)
		{
			volume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
					Toast.makeText(getActivity().getApplicationContext(), "Set volume to " + seekBar.getProgress(),
							Toast.LENGTH_SHORT).show();

					if (rendererCommand != null)
						rendererCommand.setVolume(seekBar.getProgress());
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
				{
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
				}
			});
		}

		//剩余时间
		duration = (TextView) getActivity().findViewById(R.id.trackDurationRemaining);
		if (duration != null)
		{
			duration.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					try {
						(new TimeSwitchCallback(v)).call();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private String formatTime(long h, long m, long s)
	{
		return ((h >= 10) ? "" + h : "0" + h) + ":" + ((m >= 10) ? "" + m : "0" + m) + ":"
				+ ((s >= 10) ? "" + s : "0" + s);
	}
}
