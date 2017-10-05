package com.app.creaseart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.creaseart.R;
import com.app.creaseart.activities.Dashboard;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.utils.AppUtils;


public class UserBookingFragmentNew extends BaseFragment implements View.OnClickListener {


    public static UserBookingFragmentNew userBookingFragment;
    private Activity mActivity;
    private View view;
    private final String TAG = UserBookingFragmentNew.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static UserBookingFragmentNew getInstance() {
        if (userBookingFragment == null)
            userBookingFragment = new UserBookingFragmentNew();
        return userBookingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.vendor_booking_fragment, container, false);
        mActivity = getActivity();
        userBookingFragment = this;
        initViews();
        getBundle();
        manageHeaderView();
        setupTabIcons();
        setListener();
        setFragment(new FragementUserOngoingBooking());

        return view;
    }


    private void setFragment(Fragment fragment) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.commit();
    }

    private void setListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        setFragment(new FragementUserOngoingBooking());
                        break;
                    case 1:
                        setFragment(new FragementUserCompleteBooking());
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getBundle() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            //      vendorId = bundle.getString(AppConstant.VENDORID);

        }
    }


    private void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);

    }

    private void setupTabIcons() {

        tabLayout.addTab(tabLayout.newTab().setText("Ongoing"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));

        tabLayout.setTabTextColors(getResources().getColor(R.color.textcolordark), getResources().getColor(R.color.appcolor));

    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {
        Dashboard.getInstance().manageHeaderVisibitlity(false);
        HeaderViewManager.getInstance().InitializeHeaderView(null, view, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, mActivity.getResources().getString(R.string.bokings));
        HeaderViewManager.getInstance().setLeftSideHeaderView(true, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setRightSideHeaderView(false, R.drawable.left_arrow);
        HeaderViewManager.getInstance().setLogoView(false);
        HeaderViewManager.getInstance().setProgressLoader(false, false);

    }

    /*****************************************************************************
     * Function name - manageHeaderClick
     * Description - manage the click on the left and right image view of header
     *****************************************************************************/
    private HeaderViewClickListener manageHeaderClick() {
        return new HeaderViewClickListener() {
            @Override
            public void onClickOfHeaderLeftView() {
                AppUtils.showLog(TAG, "onClickOfHeaderLeftView");
                mActivity.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

        }

    }

}
