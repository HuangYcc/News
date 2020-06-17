package com.example.ts.news.Fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ts.news.Adapter.FragmentAdapter;
import com.example.ts.news.R;
import com.example.ts.news.Utils.HttpUtils;
import com.example.ts.news.Utils.MD5Encoder;
import com.example.ts.news.Utils.TimeCount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ts on 18-8-20.
 */

public class MainFragment extends Fragment {

    private View view;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<String> titleList;
    private List<Fragment> fragmentList;

    private FragmentAdapter fragmentAdapter;

    private TechFragment tech_fragment;
    private EnteFragment ente_fragment;
    private SportFragment sport_fragment;
    private MiliFragment mili_fragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        fragmentChange();
        TimeCount.getInstance().setTime(System.currentTimeMillis());
        return view;
    }

    private void initView() {
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);

    }

    private void fragmentChange() {
        fragmentList = new ArrayList<>();

        tech_fragment = new TechFragment();
        ente_fragment = new EnteFragment();
        sport_fragment = new SportFragment();
        mili_fragment = new MiliFragment();


        fragmentList.add(tech_fragment);
        fragmentList.add(ente_fragment);
        fragmentList.add(sport_fragment);
        fragmentList.add(mili_fragment);

        titleList = new ArrayList<>();
        titleList.add("科技");
        titleList.add("娱乐");
        titleList.add("体育");
        titleList.add("军事");

        fragmentAdapter = new FragmentAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(fragmentAdapter);

        //将tabLayout与viewPager连起来
        tabLayout.setupWithViewPager(viewPager);
    }


}
