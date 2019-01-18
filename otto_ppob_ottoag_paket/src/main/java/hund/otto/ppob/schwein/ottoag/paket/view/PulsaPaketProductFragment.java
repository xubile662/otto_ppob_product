package hund.otto.ppob.schwein.ottoag.paket.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;


import com.otto.mart.viewmodule.dialog.FavoriteDialog;
import com.otto.mart.viewmodule.dialog.FilterDialog;
import com.otto.mart.viewmodule.dialog.SalepriceDialog;
import com.otto.mart.viewmodule.model.PickerUiModel.ProductIconModel;
import com.otto.mart.viewmodule.view.ProductPickerViewGroup;
import com.otto.mart.viewmodule.viewModels.FavoriteItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import glenn.base.viewmodule.dialog.ErrorDialog;
import glenn.base.viewmodule.editText.KudaEdittext;
import hund.otto.ppob.core.CORE;
import hund.otto.ppob.core.Interactor.Model.Misc.OttoagDenomModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagDenomResponseModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagInquiryResponseModel;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;
import hund.otto.ppob.schwein.ottoag.paket.R;
import hund.otto.ppob.schwein.ottoag.presenter.PpobOttoagProductViewPresenter;
import hund.otto.ppob.schwein.ottoag.view.PpobOttoagProductViewInterface;

public class PulsaPaketProductFragment extends PpobBaseProductFragment implements PpobOttoagProductViewInterface {

    private final String PPOB_TYPE_TAG = "phone-data2";

    private Context mContext;
    private View mView, action1, tv_checkfav, tv_action;
    private KudaEdittext tv;
    private ProductPickerViewGroup rv;
    List<ProductIconModel> saleList;
    private FilterDialog filterDialog;

    private SalepriceDialog salepriceDialog;
    private FavoriteDialog favoriteDialog;

    private PpobOttoagProductViewPresenter presenter;

    private CheckBox cb_savefav;

    private Dialog loadingDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(R.layout.fagment_services_pulsa_pakt, container, false);

        presenter = new PpobOttoagProductViewPresenter(this, CORE.getAppComponent(), this, CORE.getEndpoint());

        initComponent();
        initContent();
        return mView;
    }

    private void initComponent() {
        rv = mView.findViewById(R.id.init);
        tv = mView.findViewById(R.id.phone);
        action1 = mView.findViewById(R.id.action1);

        ((EditText) tv.getComponent()).setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_PHONE);
        filterDialog = new FilterDialog(getContext(), getActivity(), false, false);

        favoriteDialog = new FavoriteDialog(getContext(), getParentFragment().getActivity(), false, false);
        salepriceDialog = new SalepriceDialog(getContext(), getActivity(), false);
        tv_checkfav = mView.findViewById(R.id.tv_checkfav);
        tv_action = mView.findViewById(R.id.tv_action);
        tv_action.setVisibility(View.GONE);

        cb_savefav = mView.findViewById(R.id.cb_savefav);
        salepriceDialog = new SalepriceDialog(getContext(), getActivity(), false);

    }

    private void initContent() {
        showLoadingDialog();
        ((EditText) tv.getComponent()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                tv.addLogo(0);
                if (s.length() > 3) {

                    tv_action.setVisibility(View.VISIBLE);
                } else {
                    tv_action.setVisibility(View.GONE);

                    if (saleList.size() > 0 && listener != null)
                        listener.onProductInputUnDone();

                    saleList.clear();
                    rv.notifyAdapterForDatasetChange();
                    tv.addLogo(0);
                }
            }
        });


        tv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                presenter.callDenomRequest(PPOB_TYPE_TAG, tv.getTextContent());
                hideKeyboard();
            }
        });

        saleList = new ArrayList<>();
        rv.initLayout("Pilih Nominal", saleList, com.otto.mart.R.layout.cw_selector_product, R.layout.item_paketdata, new LinearLayoutManager(mContext), null, true);

        rv.setListener(new ProductPickerViewGroup.onProductSelected() {
            @Override
            public void onProductSelected(ProductIconModel selectedItem) {
                OttoagDenomModel selectedModel = (OttoagDenomModel) selectedItem.getStoredModel();
                presenter.callInquiryRequest(PPOB_TYPE_TAG, selectedModel.getProduct_code(), tv.getTextContent());
            }

            @Override
            public void onListDeselected() {

            }
        });

        action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.show();
            }
        });

        favoriteDialog.setListener(new FavoriteDialog.FavoriteDialogInterface() {
            @Override
            public void OnItemSelected(FavoriteDialog dialog, FavoriteItemModel model) {
                tv.setContentText(model.getCustomer_reference());
                dialog.dismiss();
            }

            @Override
            public void OnDeleteItemSelected(FavoriteDialog dialog, FavoriteItemModel model) {
                presenter.callDelFavListRequest(model.getId());
            }
        });

        tv_checkfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteDialog.show();
            }
        });
    }


    private void showLoadingDialog() {
        if (loadingDialog == null)
            loadingDialog = ProgressDialogComponent.showProgressDialog(getActivity(), "Mohon Menunggu", false);
        else
            loadingDialog.show();
    }


    private void closeLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

//    private void checkForFav() {
//        if (models != null) {
//            if (models.size() > 0) {
//                tv_checkfav.setVisibility(View.VISIBLE);
//            } else {
//                tv_checkfav.setVisibility(View.GONE);
//            }
//            favoriteDialog.addItem(models);
//        }
//    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void doOnProductlistObtainedAction(PpobOttoagDenomResponseModel model) {
        closeLoadingDialog();
        List<OttoagDenomModel> models = model.getData().getDenomination();
        saleList.clear();
        for (OttoagDenomModel odel : models) {
            saleList.add(new ProductIconModel("Item1",
                    null,
                    0,
                    odel.getProvider_code() + "," + odel.getBiller_code() + "," + odel.getProduct_code(),
                    odel.getPrice(),
                    odel.getProduct_name(),
                    String.valueOf(odel.getPrice()),
                    null,
                    odel));
        }
        rv.notifyAdapterForDatasetChange();
    }

    @Override
    public void doOnInquiryObtainedAction(final PpobOttoagInquiryResponseModel model) {
        closeLoadingDialog();

        salepriceDialog.setInitVal(model.getAmount());
        salepriceDialog.setListener(new SalepriceDialog.salepriceDialogListener() {
            @Override
            public void onSalepriceDecided(int initPrice, int salePrice) {
                presenter.addpaymentPriceData(initPrice, (salePrice - initPrice), salePrice, null);
                presenter.addPaymentRequestData(
                        PPOB_TYPE_TAG,
                        model.getProductcode(),
                        tv.getTextContent(),
                        model,
                        salePrice);

                if (cb_savefav.isChecked())
                    presenter.callAddFavListRequest(model.getProductcode(), tv.getTextContent(), PPOB_TYPE_TAG);

                salepriceDialog.dismiss();
            }

            @Override
            public void onDialogDismiss() {

            }
        });
        salepriceDialog.show();


    }

    @Override
    public void doShowFavoriteView(List<FavoriteItemModel> odel) {
        closeLoadingDialog();
        FavoriteDialog dialog = new FavoriteDialog(getContext(), getActivity(), false, false);
        dialog.addItem(odel);
        dialog.show();
    }

    @Override
    public void doShowMsgDialog(String message) {
        closeLoadingDialog();
        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, "Pesan", message);
        dialog.show();
    }

    @Override
    public void doOnApiFailedAction(int code, String message) {
        closeLoadingDialog();
        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, mContext.getString(R.string.error_apifailed), message);
        dialog.show();
    }

    @Override
    public void doOnConnectionFailedAction(Throwable t) {
        closeLoadingDialog();
        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, mContext.getString(R.string.fail_connection), t.getMessage());
        dialog.show();
    }

    @Override
    public void doReset() {
        saleList.clear();
        if (cb_savefav.isChecked())
            cb_savefav.setChecked(false);
        rv.notifyAdapterForDatasetChange();
        presenter.removePaymentRequestData();
    }
}
