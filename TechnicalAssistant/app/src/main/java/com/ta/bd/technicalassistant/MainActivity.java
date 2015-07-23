package com.ta.bd.technicalassistant;

import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.ta.bd.technicalassistant.Fragments.EquationFragment;
import com.ta.bd.technicalassistant.Fragments.GraphFragment;
import com.ta.bd.technicalassistant.Fragments.MainFragment;
import com.ta.bd.technicalassistant.Fragments.MinimizeFragment;
import com.ta.bd.technicalassistant.Fragments.NormalFormsFragment;

import java.util.Locale;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    //Current fragment.
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.action_bar_color));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if(this.position == position)
            return;
        Fragment switch_fragment = new MainFragment();
        String[] section_titles = getResources().getStringArray(R.array.section_titles);
        getSupportActionBar().setTitle(section_titles[position]);

        switch(position)
        {
            case 0:
                switch_fragment = new MainFragment();
                this.position = 0;
                break;
            case 1:
                switch_fragment = new MinimizeFragment();
                this.position = 1;
                break;
            case 2:
                switch_fragment = new NormalFormsFragment();
                this.position = 2;
                break;
            case 3:
                switch_fragment = new GraphFragment();
                this.position = 3;
                break;
            case 4:
                switch_fragment = new EquationFragment();
                this.position = 4;
                break;
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, switch_fragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId())
        {
            case R.id.action_english:
                setLocale("en");
                return true;
            case R.id.action_ukrainian:
                setLocale("ukr");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setLocale(String languageToLoad)
    {
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        mNavigationDrawerFragment.getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.recreate();
    }

}
