package sc.music.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sc.droid.dmc.R;
import sc.music.ui.Models.DrawerItem;

/**
 * Created by Administrator on 2015/6/12.
 */
public class NavDrawerAdapter  extends RecyclerView.Adapter<NavDrawerAdapter.ViewHolder> {

    //Declare variable to identify which view that is being inflated
    //The options are either the Navigation Drawer HeaderView or the list items in the Navigation drawer
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    // String Array to store the passed titles Value from MainActivity.java
    private String mNavTitles[];

    // Int Array to store the passed icons resource value from MainActivity.java
    private int mIcons[];

    //String Resource for header View Name
    private String name;

    //int Resource for header view profile picture
    private int profile;

    //String for the email displayed in the Navigation header
    private String email;

    private Context mContext;
    @Override
    public NavDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(NavDrawerAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public NavDrawerAdapter(List<DrawerItem> dataList, Context context, String Name, String Email, int Profile){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we


        mNavTitles = new String[dataList.size()];
        mIcons = new int[dataList.size()];

        for (int i = 0; i < dataList.size(); i++){
            mNavTitles[i] = dataList.get(i).getItemName();
            mIcons[i] = dataList.get(i).getImgResID();
        }
        mContext = context;
        name = Name;
        email = Email;
        profile = Profile;                     //here we assign those passed values to the values we declared here
        //in adapter
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;

        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.drawer_rowText); // Creating TextView object with the id of textView from nav_bar_rowrow.xml
                imageView = (ImageView) itemView.findViewById(R.id.drawer_rowicon);// Creating ImageView object with the id of ImageView from nav_bar_row.xmlxml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{


//                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
//                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
//                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }

    }
}
