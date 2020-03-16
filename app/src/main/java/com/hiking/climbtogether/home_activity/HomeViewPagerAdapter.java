package com.hiking.climbtogether.home_activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hiking.climbtogether.personal_fragment.PersonalChatFragment;
import com.hiking.climbtogether.disscuss_fragment.DiscussFragment;
import com.hiking.climbtogether.equipment_fragment.EquipmentFragment;
import com.hiking.climbtogether.home_fragment.HomeFragment;
import com.hiking.climbtogether.mountain_fragment.MountainFragment;

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return HomeFragment.newInstance();
        }else if (position == 1){
            return MountainFragment.newInstance();
        }else if (position == 2){
            return EquipmentFragment.newInstance();
        }else if (position == 3){
            return DiscussFragment.newInstance();
        }else {
            return PersonalChatFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
