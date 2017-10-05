package com.app.creaseart.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.creaseart.R;
import com.app.creaseart.activities.PickupBoyDashboard;
import com.app.creaseart.adapter.AdapterPickUpConvertedBookings;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.GlobalConstants;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.ModelCategory;
import com.app.creaseart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragementPickUpConvertedBooking extends Fragment implements OnCustomItemClicListener, ApiResponse {

    private RecyclerView recycler_service;
    private ArrayList<ModelCategory> arrayList;
    private AdapterPickUpConvertedBookings adapterPickUpConvertedBookings;
    private Activity mActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String orderId = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.freelancer_ongoing_booking, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();
        arrayList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        recycler_service = (RecyclerView) view.findViewById(R.id.recycler_services);
        recycler_service.setLayoutManager(new LinearLayoutManager(mActivity));
        getData();
        setListener();
    }

    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();

            }
        });

    }

    private void refreshData() {
        swipeRefreshLayout.setRefreshing(true);
        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.PICKUPBOYCONVERTEDLEAD + "user_id=" + AppUtils.getUserId(mActivity);
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        //  http://dev.stackmindz.com/trendi/api/mybooking.php?user_id=200&user_role=3


        if (AppUtils.isNetworkAvailable(mActivity)) {

            String url = JsonApiHelper.BASEURL + JsonApiHelper.PICKUPBOYCONVERTEDLEAD + "user_id=" + AppUtils.getUserId(mActivity);
            new CommonAsyncTaskHashmap(1, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 11) {
            ChooseClothPickUpBoy clothPickUpBoy = new ChooseClothPickUpBoy();
            Bundle bundle = new Bundle();
            bundle.putString("userId", arrayList.get(position).getUserId());
            bundle.putString("username", arrayList.get(position).getUserName());
            bundle.putString("orderId", arrayList.get(position).getOrderId());
            bundle.putString("date", arrayList.get(position).getDate());
            clothPickUpBoy.setArguments(bundle);
            PickupBoyDashboard.getInstance().pushFragments(GlobalConstants.TAB_HOME_BAR, clothPickUpBoy, true);
        } else if (flag == 12) {

            showAcceptConfirmation(arrayList.get(position).getOrderId());
        }
    }

    private void acceptservice(String id) {

        if (AppUtils.isNetworkAvailable(mActivity)) {
            orderId = id;
            String url = JsonApiHelper.BASEURL + JsonApiHelper.PICKUPBOYCOMPLETEORDER + "&service_id=" + id;
            new CommonAsyncTaskHashmap(2, mActivity, this).getqueryNoProgress(url);

        } else {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
        }
    }

    private void showAcceptConfirmation(final String memberId) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                mActivity);

        alertDialog.setTitle("Accept !");

        alertDialog.setMessage("Are you sure you want to Accept service?");

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        acceptservice(memberId);
                    }

                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.show();


    }

    @Override
    public void onPostSuccess(int method, JSONObject response) {
        try {
            if (method == 1) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("Booking");
                    arrayList.clear();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);

                        ModelCategory serviceDetail = new ModelCategory();

                        serviceDetail.setOrderNo(jo.getString("OrderNo"));
                        serviceDetail.setQuantity(jo.getString("Quantity"));
                        serviceDetail.setAddress(jo.getString("Address"));
                        serviceDetail.setStatus(jo.getString("Status"));
                        serviceDetail.setDate(jo.getString("Date"));
                        serviceDetail.setOrderId(jo.getString("OrderId"));
                        serviceDetail.setUserName(jo.getString("userName"));
                        serviceDetail.setUserImage(jo.getString("userImage"));
                        serviceDetail.setZone(jo.getString("Zone"));
                        serviceDetail.setUserMobile(jo.getString("userMobile"));
                        serviceDetail.setUserId(jo.getString("userId"));
                        serviceDetail.setConfirmStatus(jo.getString("ConfirmStatus"));

                        arrayList.add(serviceDetail);
                    }
                    adapterPickUpConvertedBookings = new AdapterPickUpConvertedBookings(mActivity, FragementPickUpConvertedBooking.this, arrayList);
                    recycler_service.setAdapter(adapterPickUpConvertedBookings);
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } else {
                    arrayList.clear();
                    if (adapterPickUpConvertedBookings != null) {
                        adapterPickUpConvertedBookings.notifyDataSetChanged();
                    }
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } else if (method == 2) {
                JSONObject commandResult = response.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshData();
                    /*Intent intent = new Intent(mActivity, ActivityRating.class);
                    intent.putExtra("orderid",orderId);
                    startActivity(intent);*/

                } else {
                    Toast.makeText(mActivity, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {

    }
}
