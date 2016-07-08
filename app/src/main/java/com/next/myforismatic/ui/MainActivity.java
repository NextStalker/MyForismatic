package com.next.myforismatic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.next.myforismatic.R;
import com.next.myforismatic.ui.settings.SettingsActivity;
import com.next.myforismatic.ui.quoteslist.QuoteListFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = QuoteListFragment.class.getSimpleName();

    private View root;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            showQuoteListFragment();
        }

        root = findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.isChecked()) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }

            drawerLayout.closeDrawers();

            switch (item.getItemId()) {
                case R.id.search:
                    Snackbar.make(root, "search", Snackbar.LENGTH_LONG).show();
                    return true;
                case R.id.settings:
                    showSettings();
                    return true;
                default:
                    Snackbar.make(root, "Something wrong", Snackbar.LENGTH_LONG).show();
                    return false;
            }
        });

        drawerLayout = (DrawerLayout) root;
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    private void showQuoteListFragment() {
        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, new QuoteListFragment(), TAG)
                    .commit();
        }
    }
    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
