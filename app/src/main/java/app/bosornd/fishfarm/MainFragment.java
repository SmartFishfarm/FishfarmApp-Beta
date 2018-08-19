package app.bosornd.fishfarm;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by jun on 17. 9. 29.
 */

public class MainFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    ArrayList<String> tabName;
    int number;


    public  MainFragment() {
        // Required empty public constructor
    }


    public static  MainFragment newInstance(String navigation) {
        MainFragment fragment = new  MainFragment();
        Bundle args = new Bundle();
        args.putString(Constants.FRAG_A, navigation);
        args.putString(Constants.FRAG_a, navigation);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mViewPager = (ViewPager)view.findViewById(R.id.container);
        tabName=new ArrayList<String>();

        if(getArguments().getString(Constants.FRAG_A)=="http://okhwa.sogari.co.kr/app/main/") {
            tabLayout.addTab(tabLayout.newTab().setText("메인"));
            tabLayout.addTab(tabLayout.newTab().setText("상세정보"));
            tabName.add(getArguments().getString(Constants.FRAG_A)+"index.php");
            tabName.add(getArguments().getString(Constants.FRAG_A)+"info/index.php");
        } else if(getArguments().getString(Constants.FRAG_A)=="http://okhwa.sogari.co.kr/app/chart/"){
            tabLayout.addTab(tabLayout.newTab().setText("산소차트"));
            tabLayout.addTab(tabLayout.newTab().setText("온도차트"));
            tabName.add(getArguments().getString(Constants.FRAG_A)+"chartdo.php?period=1d");
            tabName.add(getArguments().getString(Constants.FRAG_A)+"charttemp.php?period=1d");
        } else if(getArguments().getString(Constants.FRAG_A)=="http://okhwa.sogari.co.kr/app/record/bbs/default/"){
            tabLayout.addTab(tabLayout.newTab().setText("기록일지"));
            tabLayout.addTab(tabLayout.newTab().setText("리스트"));
            tabName.add(getArguments().getString(Constants.FRAG_A) + "write_default.php");
            tabName.add(getArguments().getString(Constants.FRAG_A) + "../list_default.php");
        } else if(getArguments().getString(Constants.FRAG_A)=="http://okhwa.sogari.co.kr/app/record/bbs/") {
            tabLayout.addTab(tabLayout.newTab().setText("기록일지"));
            tabLayout.addTab(tabLayout.newTab().setText("리스트"));

            if(getArguments().getString(Constants.FRAG_a)=="1"){number=1;}
            if(getArguments().getString(Constants.FRAG_a)=="2"){number=2;}

            //getArguments().getStringArrayList() 쓸예정

            tabName.add(getArguments().getString(Constants.FRAG_A) + "write.php?tank="+number);
            tabName.add(getArguments().getString(Constants.FRAG_A) + "list.php?tank="+number);

            getArguments().clear();
            getArguments().remove(Constants.FRAG_a);
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                getChildFragmentManager().beginTransaction().addToBackStack(null).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Note that we are passing childFragmentManager, not FragmentManager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}