package com.app.creaseart.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.creaseart.R;
import com.app.creaseart.adapter.AdapterItem;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.interfaces.ConnectionDetector;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.ModelPackage;
import com.app.creaseart.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 06-01-2016.
 */
public class ActivityItem extends AppCompatActivity implements OnCustomItemClicListener {


    private RecyclerView list_request;
    private Bundle b;
    private Activity context;
    private AdapterItem adapterPackages;
    private ModelPackage modelPackage;
    private ArrayList<ModelPackage> arrayList;
    private ConnectionDetector cd;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager layoutManager;
    private int skipCount = 0;
    private boolean loading = true;
    private String maxlistLength = "";
    View view_about;

    public static ActivityItem fragmentPackage;
    private final String TAG = ActivityItem.class.getSimpleName();

    public static ActivityItem getInstance() {
        if (fragmentPackage == null)
            fragmentPackage = new ActivityItem();
        return fragmentPackage;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bundle);
        context = ActivityItem.this;
        arrayList = new ArrayList<>();
        list_request = (RecyclerView) findViewById(R.id.list_request);
        list_request.setLayoutManager(new LinearLayoutManager(context));
        arrayList = new ArrayList<>();
        setlistener();
        setData();
        manageHeaderView();

    }

    private void setData() {
        try {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            if (bundle != null) {
                if (bundle.containsKey("array")) {
                    String array = bundle.getString("array");

                    JSONArray itemArray = new JSONArray(array);

                    for (int i = 0; i < itemArray.length(); i++) {

                        JSONObject jsonObject = itemArray.getJSONObject(i);
                        ModelPackage serviceDetail = new ModelPackage();
                        serviceDetail.setItemId(jsonObject.getString("itemId"));
                        serviceDetail.setItemName(jsonObject.getString("itemName"));
                        serviceDetail.setItemPrice(jsonObject.getString("itemPrice"));
                        serviceDetail.setItemAttribute(jsonObject.getString("itemAttribute"));
                        serviceDetail.setRowType(1);

                        arrayList.add(serviceDetail);
                    }
                    adapterPackages = new AdapterItem(context, this, arrayList);
                    list_request.setAdapter(adapterPackages);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*******************************************************************
     * Function name - manageHeaderView
     * Description - manage the initialization, visibility and click
     * listener of view fields on Header view
     *******************************************************************/
    private void manageHeaderView() {

        Dashboard.getInstance().manageHeaderVisibitlity(false);
        HeaderViewManager.getInstance().InitializeHeaderView(context, null, manageHeaderClick());
        HeaderViewManager.getInstance().setHeading(true, "Items");
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

    }

    @Override
    public void onItemClickListener(int position, int flag) {

    }


}

