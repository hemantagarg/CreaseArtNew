package com.app.creaseart.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.app.creaseart.R;
import com.app.creaseart.adapter.AdapterPackages;
import com.app.creaseart.aynctask.CommonAsyncTaskHashmap;
import com.app.creaseart.iclasses.HeaderViewManager;
import com.app.creaseart.interfaces.ApiResponse;
import com.app.creaseart.interfaces.ConnectionDetector;
import com.app.creaseart.interfaces.HeaderViewClickListener;
import com.app.creaseart.interfaces.JsonApiHelper;
import com.app.creaseart.interfaces.OnCustomItemClicListener;
import com.app.creaseart.models.ModelPackage;
import com.app.creaseart.utils.AppUtils;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneyConstants;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 06-01-2016.
 */
public class PackageList extends AppCompatActivity implements ApiResponse, OnCustomItemClicListener {


    private RecyclerView list_request;
    private Bundle b;
    private Activity context;
    private AdapterPackages adapterPackages;
    private ModelPackage modelPackage;
    private ArrayList<ModelPackage> arrayList;
    private static final int REQUEST_CODE_PAYUMONEY = 11;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ConnectionDetector cd;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private LinearLayoutManager layoutManager;
    private int skipCount = 0;
    private boolean loading = true;
    private String maxlistLength = "", finalPrice = "", promocode = "";
    boolean isPromoApplied = false;
    View view_about;
    private RelativeLayout rl_price;
    private TextView text_price, text_paynow, text_promocode;
    private double totalPrice = 0;
    private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    public static PackageList fragmentPackage;
    private final String TAG = PackageList.class.getSimpleName();
    private String mStrTransId = "";
    private String hashGenerated = "";

    public static PackageList getInstance() {
        if (fragmentPackage == null)
            fragmentPackage = new PackageList();
        return fragmentPackage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_package);
        context = PackageList.this;
        arrayList = new ArrayList<>();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout1);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        list_request = (RecyclerView) findViewById(R.id.list_request);
        rl_price = (RelativeLayout) findViewById(R.id.rl_price);
        text_paynow = (TextView) findViewById(R.id.text_paynow);
        text_price = (TextView) findViewById(R.id.text_price);
        text_promocode = (TextView) findViewById(R.id.text_promocode);

        layoutManager = new GridLayoutManager(context, 2);
        list_request.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        setlistener();
        mStrTransId = getTxnId();
        manageHeaderView();
        getServicelistRefresh();

    }

    private void applyCode(String text) {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                // http://dev.stackmindz.com/creaseart/api/applycoupon.php?user_id=1&coupon_code=12345&total_value=100
                String url = JsonApiHelper.BASEURL + JsonApiHelper.APPLYCOUPON + "user_id=" + AppUtils.getUserId(context) + "&coupon_code=" + text.trim() + "&total_value=" + totalPrice;
                new CommonAsyncTaskHashmap(2, context, this).getqueryJsonbject(url, null, Request.Method.GET);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Open dialog for the add member
     */
    private void openAddDialog() {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            // inflate the layout dialog_layout.xml and set it as contentView
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.promocode_dialog, null, false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(view);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            RelativeLayout cross_img_rel = (RelativeLayout) view.findViewById(R.id.cross_img_rel);
            final EditText edt_comment = (EditText) view.findViewById(R.id.edt_comment);
            Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!edt_comment.getText().toString().equalsIgnoreCase("")) {
                        promocode = edt_comment.getText().toString();
                        applyCode(edt_comment.getText().toString());

                        dialog.dismiss();
                    } else {
                        edt_comment.setError("Please enter PromoCode");
                        edt_comment.requestFocus();
                    }

                }
            });

            cross_img_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            Log.e(TAG, " Exception error : " + e);
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
        HeaderViewManager.getInstance().setHeading(true, "Packages");
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
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getServicelistRefresh();
            }
        });

        text_promocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddDialog();
            }
        });
        text_paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalPrice > 0) {
                    launchPayUMoneyFlow();
                } else {
                    Toast.makeText(context, "Please select package", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getTxnId() {
        return ("" + System.currentTimeMillis());
    }

    /**
     * This function prepares the data for payment and launches payumoney plug n play sdk
     */
    private void launchPayUMoneyFlow() {

        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();

        //Use this to set your custom text on result screen button
        payUmoneyConfig.setDoneButtonText("Done");

        //Use this to set your custom title for the activity
        payUmoneyConfig.setPayUmoneyActivityTitle("Crease Art");

        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();

        double amount = 0;
        try {
            amount = Double.parseDouble(finalPrice);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String txnId = mStrTransId;
        String phone = AppUtils.getUserMobile(context);
        String productName = "CreaseArt";
        String firstName = AppUtils.getUserName(context);
        String email = AppUtils.getUseremail(context);
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        String udf6 = "";
        String udf7 = "";
        String udf8 = "";
        String udf9 = "";
        String udf10 = "";

        AppEnvironment appEnvironment = AppEnvironment.PRODUCTION;
        builder.setAmount(amount)
                .setTxnId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(appEnvironment.surl())
                .setfUrl(appEnvironment.furl())
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6(udf6)
                .setUdf7(udf7)
                .setUdf8(udf8)
                .setUdf9(udf9)
                .setUdf10(udf10)
                .setIsDebug(appEnvironment.debug())
                .setKey(appEnvironment.merchant_Key())
                .setMerchantId(appEnvironment.merchant_ID());

        try {
            mPaymentParams = builder.build();
            //   generateHashFromServer(mPaymentParams);
            getHash(mPaymentParams);
            Log.e("mPaymentParams", "&*" + mPaymentParams.getParams());

        } catch (Exception e) {
            // some exception occurred
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClickListener(int position, int flag) {
        if (flag == 1) {

            Bundle bundle = new Bundle();
            bundle.putString("array", arrayList.get(position).getJsonArray());
            Intent intent = new Intent(context, ActivityBundle.class);
            intent.putExtra("bundle", bundle);
            startActivity(intent);

        } else if (flag == 2) {
            if (arrayList.get(position).isSelected()) {
                arrayList.get(position).setSelected(false);
                if (arrayList.get(position).getDiscountPrice() != null && !arrayList.get(position).getDiscountPrice().equalsIgnoreCase("")) {
                    addPackagePrice(arrayList.get(position).getDiscountPrice(), false);
                } else {
                    addPackagePrice(arrayList.get(position).getPackagePrice(), false);
                }
            } else {
                if (arrayList.get(position).getDiscountPrice() != null && !arrayList.get(position).getDiscountPrice().equalsIgnoreCase("")) {
                    addPackagePrice(arrayList.get(position).getDiscountPrice(), true);
                } else {
                    addPackagePrice(arrayList.get(position).getPackagePrice(), true);
                }
                arrayList.get(position).setSelected(true);
            }
            adapterPackages.notifyDataSetChanged();

        }
    }


    private void addPackagePrice(String price, boolean add) {
        if (isPromoApplied) {
            text_promocode.setText("Have a Promocode ?");
            isPromoApplied = false;
        }
        if (add) {
            totalPrice = totalPrice + Double.parseDouble(price);
            DecimalFormat round = new DecimalFormat("###.##");
            String output = round.format(totalPrice);
            text_price.setText(output);
            finalPrice = text_price.getText().toString();
            if (rl_price.getVisibility() == View.GONE) {
                rl_price.setVisibility(View.VISIBLE);
            }
        } else {
            totalPrice = totalPrice - Double.parseDouble(price);
            DecimalFormat round = new DecimalFormat("###.##");
            String output = round.format(totalPrice);
            text_price.setText(output);
            finalPrice = text_price.getText().toString().trim();
            if (finalPrice.equalsIgnoreCase("0") && finalPrice.equalsIgnoreCase("-0")) {
                rl_price.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);
            try {
                if (transactionResponse != null) {
                    String payuResponse = transactionResponse.getPayuResponse();
                    Log.e("payumoneyresponse", "**" + payuResponse);
                    if (payuResponse != null && payuResponse.length() > 0) {
                        JSONObject payu = new JSONObject(payuResponse);
                        JSONObject result = payu.getJSONObject("result");
                        String paymentId = result.getString("paymentId");
                        String status = result.getString("status");
                        makePayment(status, paymentId);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void makePayment(String payment_status, String transaction_id) {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                String packages = "";
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).isSelected()) {
                        if (packages.equalsIgnoreCase("")) {
                            packages = arrayList.get(i).getPackageId();
                        } else {
                            packages = packages + "," + arrayList.get(i).getPackageId();
                        }
                    }
                }

                //http://dev.stackmindz.com/creaseart/api/payment.php?user_id=1&transaction_id=AG565JH078
                // &total_value=100&promo_code=TEST&package_id=2&payment_status=
                String url = JsonApiHelper.BASEURL + JsonApiHelper.PAYMENT + "user_id=" + AppUtils.getUserId(context) + "&transaction_id="
                        + mStrTransId + "&total_value=" + finalPrice + "&promo_code=" + promocode + "&package_id=" + packages + "&payment_status=" + payment_status;
                new CommonAsyncTaskHashmap(11, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getHash(PayUmoneySdkInitializer.PaymentParam paymentParam) {
        try {
            if (AppUtils.isNetworkAvailable(context)) {
                //   http://dev.stackmindz.com/creaseart/api/getHash.php?txnid=1&amount=10&productinfo=Test
                // &firstname=test&email=test@gmail.com&udf1=&udf2=&udf3=&udf4=&udf5=
                HashMap<String, String> params = paymentParam.getParams();
                params.put("txnid", params.get(PayUmoneyConstants.TXNID));
                params.put("amount", params.get(PayUmoneyConstants.AMOUNT));
                params.put("productinfo", params.get(PayUmoneyConstants.PRODUCT_INFO));
                params.put("firstname", params.get(PayUmoneyConstants.FIRSTNAME));
                params.put("email", params.get(PayUmoneyConstants.EMAIL));
                params.put("udf1", params.get(PayUmoneyConstants.UDF1));
                params.put("udf2", params.get(PayUmoneyConstants.UDF2));
                params.put("udf3", params.get(PayUmoneyConstants.UDF3));
                params.put("udf4", params.get(PayUmoneyConstants.UDF4));
                params.put("udf5", params.get(PayUmoneyConstants.UDF5));
                String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_HASH;
             /*   String url = JsonApiHelper.BASEURL + JsonApiHelper.GET_HASH + "txnid=" + params.get(PayUmoneyConstants.TXNID) + "&amount=" + params.get(PayUmoneyConstants.AMOUNT) +
                        "&productinfo=" + params.get(PayUmoneyConstants.PRODUCT_INFO) + "&firstname=" + params.get(PayUmoneyConstants.FIRSTNAME) + "&email=" + params.get(PayUmoneyConstants.EMAIL) +
                        "&udf1=" + params.get(PayUmoneyConstants.UDF1) + "&udf2=" + params.get(PayUmoneyConstants.UDF2) + "&udf3" + params.get(PayUmoneyConstants.UDF3) +
                        "=&udf4=" + params.get(PayUmoneyConstants.UDF4) + "&udf5=" + params.get(PayUmoneyConstants.UDF5);
*/
                //   url = url.replace(" ", "%20");
                new CommonAsyncTaskHashmap(21, context, this).getqueryHashMap(url, params);

            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getServicelistRefresh() {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            if (AppUtils.isNetworkAvailable(context)) {
                String url = JsonApiHelper.BASEURL + JsonApiHelper.PACKAGES;
                new CommonAsyncTaskHashmap(1, context, this).getqueryJsonbject(url, null, Request.Method.GET);
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.message_network_problem), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPaymentPopup() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setMessage("Payment is Successful");

        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        alertDialog.show();

    }


    @Override
    public void onPostSuccess(int position, JSONObject jObject) {
        try {
            if (position == 1) {

                JSONObject commandResult = jObject.getJSONObject("commandResult");

                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("packages");
                    arrayList.clear();

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        ModelPackage serviceDetail = new ModelPackage();

                        serviceDetail.setJsonArray(jo.toString());
                        serviceDetail.setPackageId(jo.getString("packageId"));
                        serviceDetail.setPackageName(jo.getString("packageName"));
                        serviceDetail.setSelected(false);
                        serviceDetail.setPackagePrice(jo.getString("packagePrice"));
                        serviceDetail.setDiscountPrice(jo.getString("discountPrice"));
                        serviceDetail.setIsDiscount(jo.getString("isDiscount"));
                        serviceDetail.setDiscount(jo.getString("discount"));
                        serviceDetail.setRowType(1);

                        arrayList.add(serviceDetail);
                    }
                    adapterPackages = new AdapterPackages(context, this, arrayList);
                    list_request.setAdapter(adapterPackages);

                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }

            } else if (position == 11) {

                JSONObject commandResult = jObject
                        .getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    showPaymentPopup();
                } else {
                    Toast.makeText(context,
                            commandResult.getString("message"),
                            Toast.LENGTH_LONG).show();

                }

            } else if (position == 2) {

                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    finalPrice = data.getString("TotalValue");
                    isPromoApplied = true;
                    text_promocode.setText("Promo code Applied Sucessfully");
                    String total = totalPrice + "";
                    SpannableString spannable = new SpannableString(total + finalPrice);
                    spannable.setSpan(new StrikethroughSpan(), 0, total.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    text_price.setText(spannable);

                } else {
                    Toast.makeText(context, commandResult.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } else if (position == 4) {
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {

                    JSONObject data = commandResult.getJSONObject("data");
                    JSONArray array = data.getJSONArray("packages");

                    arrayList.remove(arrayList.size() - 1);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jo = array.getJSONObject(i);
                        ModelPackage serviceDetail = new ModelPackage();

                        serviceDetail.setPackageId(jo.getString("packageId"));
                        serviceDetail.setPackageName(jo.getString("packageName"));
                        serviceDetail.setPackagePrice(jo.getString("packagePrice"));

                        modelPackage.setRowType(1);

                        arrayList.add(modelPackage);
                    }
                    adapterPackages.notifyDataSetChanged();
                    loading = true;
                    if (data.length() == 0) {
                        skipCount = skipCount - 10;
                        //  return;
                    }
                } else {
                    adapterPackages.notifyDataSetChanged();
                    skipCount = skipCount - 10;
                    loading = true;
                }
            } else if (position == 21) {
                JSONObject commandResult = jObject.getJSONObject("commandResult");
                if (commandResult.getString("success").equalsIgnoreCase("1")) {
                    hashGenerated = commandResult.getString("data");
                    mPaymentParams.setMerchantHash(hashGenerated);
                    PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, context, R.style.AppTheme_default, false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPostFail(int method, String response) {
        if (context != null)
            Toast.makeText(context, getResources().getString(R.string.problem_server), Toast.LENGTH_SHORT).show();
    }
}

