package com.qingchenglei.toolbardemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.qingchenglei.toolbardemo.util.Tools;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tools.setStatusBar(this, R.color.colorPrimary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);

        toolbar.setNavigationIcon(R.drawable.ic_action_menu);

        toolbar.setLogo(R.mipmap.ic_launcher);

        toolbar.setTitle(R.string.app_name);

        toolbar.setSubtitle(R.string.app_des);

        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you confirm?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id){
            case R.id.action_settings:
                startActivity(new Intent().setClass(MainActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_tabs:
                startActivity(new Intent().setClass(MainActivity.this, TabActivity.class));
                return true;
            case R.id.action_tabs_host:
                startActivity(new Intent().setClass(MainActivity.this, TabLibraryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
