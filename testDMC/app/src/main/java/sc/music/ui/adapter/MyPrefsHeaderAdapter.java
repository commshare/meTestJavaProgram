package sc.music.ui.adapter;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import sc.droid.dmc.R;
import sc.music.ui.view.ContentDirectoryEnabler;

/**
 * Created by Administrator on 2015/6/16.
 */
public class MyPrefsHeaderAdapter  extends ArrayAdapter<PreferenceActivity.Header> {
    static final int HEADER_TYPE_CATEGORY = 0;
    static final int HEADER_TYPE_NORMAL = 1;
    static final int HEADER_TYPE_SWITCH = 2;

    private LayoutInflater mInflater;
    private ContentDirectoryEnabler mContentDirectoryEnabler;

    public MyPrefsHeaderAdapter(Context context, List<PreferenceActivity.Header> objects)
    {
        super(context, 0, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentDirectoryEnabler = new ContentDirectoryEnabler(context, new Switch(context));
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        //adapter根据位置得到一个item
        PreferenceActivity.Header header = getItem(position);
        //读取header的类型
        int headerType = getHeaderType(header);
        View view = null;

        switch (headerType)
        {
            case HEADER_TYPE_CATEGORY:
                view = mInflater.inflate(android.R.layout.preference_category, parent, false);
                ((TextView) view.findViewById(android.R.id.title)).setText(header.getTitle(getContext().getResources()));
                break;

            case HEADER_TYPE_SWITCH:
                //从这么一个布局。得到这么一个view
                view = mInflater.inflate(R.layout.preference_header_switch_item, parent, false);
                //view中拿到资源，设置header的属性
                ((ImageView) view.findViewById(android.R.id.icon)).setImageResource(header.iconRes);
                ((TextView) view.findViewById(android.R.id.title)).setText(header.getTitle(getContext().getResources()));
                ((TextView) view.findViewById(android.R.id.summary)).setText(header.getSummary(getContext().getResources()));

                if(header.id == R.id.contentdirectory_settings)
                    mContentDirectoryEnabler = new ContentDirectoryEnabler(getContext(),
                            (Switch) view.findViewById(R.id.switchWidget));
                break;

            case HEADER_TYPE_NORMAL:
                view = mInflater.inflate(R.layout.preference_header_item, parent, false);
                ((ImageView) view.findViewById(android.R.id.icon)).setImageResource(header.iconRes);
                ((TextView) view.findViewById(android.R.id.title)).setText(header.getTitle(getContext().getResources()));
                ((TextView) view.findViewById(android.R.id.summary)).setText(header.getSummary(getContext().getResources()));
                break;
        }

        return view;
    }

    //怎么heaer的类型的呢，根据id
    public static int getHeaderType(PreferenceActivity.Header header)
    {
        if ((header.fragment == null) && (header.intent == null)) {
            return HEADER_TYPE_CATEGORY;
        } else if (header.id == R.id.contentdirectory_settings) {
            return HEADER_TYPE_SWITCH;
        } else {
            return HEADER_TYPE_NORMAL;
        }
    }

    public void resume() {
        mContentDirectoryEnabler.resume();
    }

    public void pause() {
        mContentDirectoryEnabler.pause();
    }
}
