package com.app.creaseart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.creaseart.R;
import com.app.creaseart.activities.PickupBoyDashboard;
import com.app.creaseart.adapter.AdapterPickUpPackages;
import com.app.creaseart.adapter.AdapterPickupBundle;
import com.app.creaseart.adapter.AdapterPickupItem;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.Item;
import com.app.creaseart.models.ModelPackage;
import com.app.creaseart.models.Packages;
import com.app.creaseart.utils.AppUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class ChooseClothPickUpBoy extends BaseFragment implements ApiResponse, OnCustomItemClicListener {

    private RecyclerView recyclerPackage, recyclerItem, recyclerBundle;
    private Bundle b;
    private Activity context;
    private AdapterPickUpPackages adapterPackages;
    private AdapterPickupBundle adapterBundle;
    private AdapterPickupItem adapterItem;
    private ModelPackage modelPackage;
    private ArrayList<Packages> packagesArrayList;
    private com.app.creaseart.models.Bundle[] bundleArrayList;
    private Item[] itemArrayList;
    private int lastPackagePosition, latBundlePosition, lastItemPosition;
    private TextView text_name, text_orderId, text_date, text_package, text_bundle, text_item;
    private LinearLayoutManager layoutManager;
    View view_about;
    private Button btnSubmit;
    private EditText edt_sticker_code;
    String userId = "", orderId = "";
    public static ChooseClothPickUpBoy fragmentPackage;
    private final String TAG = ChooseClothPickUpBoy.class.getSimpleName();

    public static ChooseClothPickUpBoy getInstance() {
        if (fragmentPackage == null)
            fragmentPackage = new ChooseClothPickUpBoy();
        return fragmentPackage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.app.justclap.fragment

        view_about = inflater.inflate(R.layout.fragment_choosecloth, container, false);
        context = getActivity();
        packagesArrayList = new ArrayList<>();
        b = getArguments();

        return view_about;
    }

    private void setData() {
        if (b != null) {
            userId = b.getString("userId");
            text_name.setText(b.getString("username"));
            text_date.setText(b.getString("date"));
            text_orderId.setText(b.getString("orderId"));
            orderId = b.getString("orderId");
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerPackage = (RecyclerView) view.findViewById(R.id.recyclerPackage);
        recyclerPackage.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        recyclerItem = (RecyclerView) view.findViewById(R.id.recyclerItem);
        recyclerItem.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        recyclerBundle = (RecyclerView) view.findViewById(R.id.recyclerBundle);
        recyclerBundle.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        text_date = (TextView) view.findViewById(R.id.text_date);
        text_name = (TextView) view.findViewById(R.id.text_name);
        text_orderId = (TextView) view.findViewById(R.id.text_orderId);
        text_bundle = (TextView) view.findViewById(R.id.text_bundle);
        text_package = (TextView) view.findViewById(R.id.text_package);
        text_item = (TextView) view.findViewById(R.id.text_item);
        text_item.setVisibility(View.GONE);
        text_bundle.setVisibility(View.GONE);
        edt_sticker_code = (EditText) view.findViewById(R.id.edt_sticker_code);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        setData();
        packagesArrayList = new ArrayList<>();
        setlistener();
        manageHeaderView();

        getServicelistRefresh();
    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        PickupBoyDashboard.getInstance().manageHeaderVisibitlity(false);
        HeaderViewManager.getInstance().InitializeHeaderView(null, view_about, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Active Packages");
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
                context.onBackPressed();
            }

            @Override
            public void onClickOfHeaderRightView() {
                //   Toast.makeText(mActivity, "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void setlistener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edt_sticker_code.getText().toString().equalsIgnoreCase("")) {
                    submitData();
                } else {
                    Toast.makeText(context, "Please enter Sticker code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void submitData() {

        try {
            JSONObject data = new JSONObject();
            JSONArray packages = new JSONArray();

            for (int i = 0; i < packagesArrayList.size(); i++) {
                JSONObject packageObject = new JSONObject();
                packageObject.put("packageId", packagesArrayList.get(i).getPackageId());

                JSONArray bundleArray = new JSONArray();

                com.app.creaseart.models.Bundle[] bundle = packagesArrayList.get(i).getBundle();
                for (int j = 0; j < bundle.length; j++) {

                    JSONObject bundleObject = new JSONObject();
                    bundleObject.put("bundleId", bundle[j].getBundleId());

                    JSONArray items = new JSONArray();
                    Item[] itemArray = bundle[j].getItem();
                    for (int k = 0; k < itemArray.length; k++) {

                        JSONObject itemObject = new JSONObject();
                        itemObject.put("itemId", itemArray[k].getItemId());
                        itemObject.put("itemCount", itemArray[k].getItemAdded() + "");

                        items.put(itemObject);
                    }
                    bundleObject.put("item", items);
                    bundleArray.put(bundleObject);
                }

                packageObject.put("bundle", bundleArray);
                packages.put(packageObject);
            }
            data.put("packages", packages);
            Log.e("data", "**" + data);

            if (AppUtils.isNetworkAvailable(context)) {

                //packages_data(json data),pickupboy_id,service_id,sticker_code
                String url = JsonApiHelper.BASEURL + JsonApiHelper.BOOKCLOTHESSERVICE + "sticker_code=" + edt_sticker_code.getText().toString()
                        + "&pickupboy_id=" + AppUtils.getUserId(context) + "&packages_data=" + data + "&service_id=" + b.getString("orderId");
                new CommonAsyncTaskHashmap(2, context, this).getqueryNoProgress(url);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {
            lastPackagePosition = position;
            for (int i = 0; i < packagesArrayList.size(); i++) {
                packagesArrayList.get(i).setSeletedPosition(-1);
            }
            packagesArrayList.get(position).setSeletedPosition(position);
            adapterPackages.notifyDataSetChanged();
            setBundleData(position);
        } else if (flag == 2) {
            latBundlePosition = position;
            for (int i = 0; i < bundleArrayList.length; i++) {
                bundleArrayList[i].setSeletedPosition(-1);
            }
            bundleArrayList[position].setSeletedPosition(position);
            adapterBundle.notifyDataSetChanged();
            setItemData(position);
        } else if (flag == 11) {
            addItem(position);
        } else if (flag == 12) {
            subtractItem(position);
        }
    }

    private void subtractItem(int position) {
        int count = itemArrayList[position].getItemAdded();
        if (count > 0) {
            count--;
            itemArrayList[position].setItemAdded(count);
            int totalPrice = bundleArrayList[latBundlePosition].getTotalAddedItemprice();
            Log.e("totalPrice", "**" + totalPrice);
            totalPrice = totalPrice - Integer.parseInt(itemArrayList[position].getItemPrice());
            bundleArrayList[latBundlePosition].setTotalAddedItemprice(totalPrice);
            int priceLeft = Integer.parseInt(itemArrayList[position].getItemPrice()) + bundleArrayList[latBundlePosition].getIntPriceLeft();
            bundleArrayList[latBundlePosition].setIntPriceLeft(priceLeft);

            Log.e("bundlePriafterSubtract", "**" + bundleArrayList[latBundlePosition].getTotalAddedItemprice());
            adapterItem.notifyItemChanged(position);
        }
    }

    private void addItem(int position) {
        int bundlePrice = Integer.parseInt(bundleArrayList[latBundlePosition].getBundlePrice());
        Log.e("bundlePrice", "**" + bundlePrice);
        int totalBundleItemPrice = bundleArrayList[latBundlePosition].getTotalAddedItemprice();
        if (totalBundleItemPrice < bundlePrice && (Integer.parseInt(itemArrayList[position].getItemPrice()) <= bundleArrayList[latBundlePosition].getIntPriceLeft())) {
            int count = itemArrayList[position].getItemAdded();
            count++;
            itemArrayList[position].setItemAdded(count);
            int price = totalBundleItemPrice + Integer.parseInt(itemArrayList[position].getItemPrice());
            bundleArrayList[latBundlePosition].setTotalAddedItemprice(price);

            int priceLeft = bundlePrice - bundleArrayList[latBundlePosition].getTotalAddedItemprice();
            bundleArrayList[latBundlePosition].setIntPriceLeft(priceLeft);

            Log.e("bundlePriceafterAdding", "**" + bundleArrayList[latBundlePosition].getTotalAddedItemprice());
            adapterItem.notifyItemChanged(position);
        } else {
            Toast.makeText(context, "Please choose another bundle", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBundleData(int position) {
        try {
            if (packagesArrayList.get(position).getBundle() != null) {

                bundleArrayList = packagesArrayList.get(position).getBundle();
                for (int i = 0; i < bundleArrayList.length; i++) {
                    bundleArrayList[i].setSeletedPosition(-1);
                }
                adapterBundle = new AdapterPickupBundle(context, this, bundleArrayList);
                recyclerBundle.setAdapter(adapterBundle);
                text_bundle.setVisibility(View.VISIBLE);
                recyclerItem.setVisibility(View.GONE);
                text_item.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setItemData(int position) {
        try {
            if (bundleArrayList[position].getItem() != null) {
                itemArrayList = bundleArrayList[position].getItem();
                adapterItem = new AdapterPickupItem(context, this, itemArrayList);
                recyclerItem.setAdapter(adapterItem);
                recyclerItem.setVisibility(View.VISIBLE);
                text_item.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getServicelistRefresh() {
        PickupBoyDashboard.getInstance().setProgressLoader(true);
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                //    http://sfscoring.betasportzfever.com/getNotifications/155/efc0c68e-8bb5-11e7-8cf8-008cfa5afa52
             /*   HashMap<String, Object> hm = new HashMap<>();*/
                String url = JsonApiHelper.BASEURL + JsonApiHelper.ACTIVEPACKAGE + "user_id=" + userId;
                new CommonAsyncTaskHashmap(1, context, this).getqueryNoProgress(url);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPostSuccess(int position, JSONObject jObject) {
        try {
            if (position == 1) {
                PickupBoyDashboard.getInstance().setProgressLoader(false);
                JSONObject commandResult = jObject.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    packagesArrayList.clear();
                    JSONArray array = data.getJSONArray("ActivePackage");

                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {

                        Packages packages = null;
                        try {
                            packages = gson.fromJson(array.getJSONObject(i).toString(), Packages.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        packagesArrayList.add(packages);
                    }
                    adapterPackages = new AdapterPickUpPackages(getActivity(), this, packagesArrayList);
                    recyclerPackage.setAdapter(adapterPackages);

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } else if (position == 2) {
                JSONObject commandResult = jObject.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    //    JSONObject data = commandResult.getJSONObject("data");
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    context.onBackPressed();

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {
        if (context != null && isAdded())
            Toast.makeText(getActivity(), getResources().getString(R.string.problem_server), Toast.LENGTH_SHORT).show();
    }
}

