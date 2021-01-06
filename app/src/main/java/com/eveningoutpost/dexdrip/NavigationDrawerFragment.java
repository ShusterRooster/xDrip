package com.eveningoutpost.dexdrip;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerFragment extends Fragment {
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private NavigationDrawerCallbacks mCallbacks;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private String menu_name;
    public NavDrawerBuilder navDrawerBuilder;
    private int menu_position;
    //private List<String> menu_option_list;
    //private List<Intent> intent_list;
    private List<NavDrawerItem> menu_items;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = 0;
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        navDrawerBuilder = new NavDrawerBuilder(getActivity());
        //menu_option_list = navDrawerBuilder.nav_drawer_options;
        //intent_list = navDrawerBuilder.nav_drawer_intents;

        menu_items = navDrawerBuilder.nav_drawer_options;
        return mDrawerListView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, String current_activity, Context context) {
        navDrawerBuilder = new NavDrawerBuilder(context);
        menu_name = current_activity;
        Object[] menu_items_list = menu_items.toArray(new Object[menu_items.size()]);
        menu_position = menu_items.indexOf(menu_name);

        //menu_option_list = navDrawerBuilder.nav_drawer_options;
        //String[] menu_options = menu_option_list.toArray(new String[menu_option_list.size()]);
        //menu_position = menu_option_list.indexOf(menu_name);
        //intent_list = navDrawerBuilder.nav_drawer_intents;

        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mCurrentSelectedPosition = menu_position;
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        } else {
            try {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                getActivity().getActionBar().setHomeButtonEnabled(true);
            } catch (Exception e) {
                Log.d("NavigationDrawerFrag", "Exception with getActionBar: " + e.toString());
            }
        }

//        try {
//            mDrawerListView.setAdapter(new ArrayAdapter<String>(
//                    actionBar.getThemedContext(),
//                    android.R.layout.simple_list_item_activated_1,
//                    android.R.id.text1,
//                    menu_options
//            ));
//
//        } catch (NullPointerException e) {
//            try {
//                mDrawerListView.setAdapter(new ArrayAdapter<String>(
//                        getActivity().getActionBar().getThemedContext(),
//                        android.R.layout.simple_list_item_activated_1,
//                        android.R.id.text1,
//                        menu_options
//                ));
//            } catch (NullPointerException ex) {
//                Log.d("NavigationDrawerFrag", "Got second null pointer: " + ex.toString());
//            }
//        }

        try {
            mDrawerListView.setAdapter(new ArrayAdapter<Object>(
                    actionBar.getThemedContext(),
                    android.R.layout.simple_list_item_activated_1,
                    android.R.id.text1,
                    menu_items_list
            ));

        } catch (NullPointerException e) {
            try {
                mDrawerListView.setAdapter(new ArrayAdapter<Object>(
                        getActivity().getActionBar().getThemedContext(),
                        android.R.layout.simple_list_item_activated_1,
                        android.R.id.text1,
                        menu_items_list
                ));
            } catch (NullPointerException ex) {
                Log.d("NavigationDrawerFrag", "Got second null pointer: " + ex.toString());
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                // R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            Log.e("jamorham Runtime", "Call to on-create options menu in Navigation Drawer Fragment");
            //showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ActionBar getActionBar() {
        try {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }

    public void swapContext(int position) {
        if (position != menu_position) {
            //Intent[] intent_array = intent_list.toArray(new Intent[intent_list.size()]);
            List<NavDrawerItem> test = new ArrayList<NavDrawerItem>();
            ArrayList<Intent> intent_list = new ArrayList<>();

            for(NavDrawerItem item : test){
                intent_list.add(item.intent);
            }

            startActivity(intent_list.get(position));
            if (menu_position != 0) {
                getActivity().finish();
            }
        }
    }

    public static class NavDrawerItem {
        public String name;
        public Intent intent;
        public Integer drawable;

        NavDrawerItem(String name, Intent intent) {
            this.name = name;
            this.intent = intent;
        }

        NavDrawerItem(String name, Intent intent, Integer drawable) {
            this.name = name;
            this.intent = intent;
            this.drawable = drawable;
        }

        public String getName(){
            return this.name;
        }

        public Intent getIntent(){
            return this.intent;
        }

        public Integer getDrawable(){
            return this.drawable;
        }
        public List<Intent> getAllIntents(){
            List<NavDrawerItem> test = new ArrayList<NavDrawerItem>();
            List<Intent> intent_list = new ArrayList<>();

            for(NavDrawerItem item : test){
                intent_list.add(item.intent);
            }
            return intent_list;
        }

        public List<String> getAllStrings(){
            List<NavDrawerItem> test = new ArrayList<NavDrawerItem>();
            List<String> string_list = new ArrayList<>();

            for(NavDrawerItem item : test){
                string_list.add(item.name);
            }
            return string_list;
        }
    }
}
