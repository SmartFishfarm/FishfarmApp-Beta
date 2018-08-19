package app.bosornd.fishfarm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by jun on 17. 9. 29.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    ArrayList<String> tabName;

    public SectionsPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> tabName) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabName = tabName;
    }

    @Override
    public Fragment getItem(int position) {
        MainWebFragment comn = new MainWebFragment();
        return comn.newInstance(tabName.get(position));
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}