package hund.otto.ppob.schwein.ottoag.pulsa.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.otto.mart.viewmodule.dialog.FavoriteDialog;
import com.otto.mart.viewmodule.dialog.SalepriceDialog;
import com.otto.mart.viewmodule.model.PickerUiModel.ProductIconModel;
import com.otto.mart.viewmodule.view.ProductPickerViewGroup;
import com.otto.mart.viewmodule.viewModels.FavoriteItemModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.component.ProgressDialogComponent;
import glenn.base.viewmodule.adapterDecorator.GridDividerDecorator;
import glenn.base.viewmodule.dialog.ErrorDialog;
import glenn.base.viewmodule.editText.KudaEdittext;
import hund.otto.ppob.core.CORE;
import hund.otto.ppob.core.Interactor.Model.Misc.OttoagDenomModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagDenomResponseModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagInquiryResponseModel;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;
import hund.otto.ppob.core.View.component.KeyValueListAdapter;
import hund.otto.ppob.schwein.ottoag.presenter.PpobOttoagProductViewPresenter;
import hund.otto.ppob.schwein.ottoag.pulsa.R;
import hund.otto.ppob.schwein.ottoag.view.PpobOttoagProductViewInterface;

public class PulsaNominalProductFragment extends PpobBaseProductFragment implements PpobOttoagProductViewInterface {

    private final String[] PPOB_TYPE_TAG =
            {"phone-postpaid2", "phone-prepaid2"};

    Context gContext;
    private View mView, fl_actions, tv_checkfav, tv_action;

    private KudaEdittext ket_cust_ref;
    private ProductPickerViewGroup rv_pra;
    private RadioGroup rg;
    private CheckBox cb_savefav;
    private ExpandableLayout elayout_pra, elayout_pasca;
    private boolean isPascabayar;
    private RecyclerView rv_pasca;
    private FavoriteDialog favDialog;
    private KeyValueListAdapter pascaAdapter;
    private PpobOttoagProductViewPresenter presenter;

    private SalepriceDialog salepriceDialog;

    private Dialog loadingDialog;

    //viewdata
    List<ProductIconModel> saleList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gContext = getContext();
        mView = inflater.inflate(R.layout.fragment_ppob_pulsa_nominal, container, false);
        //TODO set endpoint and stuff for presenter
        presenter = new PpobOttoagProductViewPresenter(this, CORE.getAppComponent(), this, CORE.getEndpoint());
        initComponent();
        initContent();
        return mView;
    }

    private void initComponent() {
        rg = mView.findViewById(R.id.rg);
        ket_cust_ref = mView.findViewById(R.id.ket_phone);
        elayout_pra = mView.findViewById(R.id.elayout_pra);
        elayout_pasca = mView.findViewById(R.id.elayout_pasca);
        rv_pasca = mView.findViewById(R.id.rv_pasca);
        rv_pra = mView.findViewById(R.id.rv_pra);
        fl_actions = mView.findViewById(R.id.fl_actions);
        tv_checkfav = mView.findViewById(R.id.tv_checkfav);
        tv_action = mView.findViewById(R.id.tv_action);
        favDialog = new FavoriteDialog(getContext(), getActivity(), false, false);
        cb_savefav = mView.findViewById(R.id.cb_savefav);

        pascaAdapter = new KeyValueListAdapter(R.layout.item_key_value_list_default);
        salepriceDialog = new SalepriceDialog(getContext(), getActivity(), false);
    }

    private void initContent() {
        showLoadingDialog();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_pra) {
                    isPascabayar = false;
                    elayout_pra.setExpanded(true);
                    elayout_pasca.setExpanded(false);
                } else if (checkedId == R.id.rb_pasca) {
                    isPascabayar = true;
                    elayout_pra.setExpanded(false);
                    elayout_pasca.setExpanded(true);
                }
            }
        });

        GridDividerDecorator itemDecor = new GridDividerDecorator(32, 2);

        rv_pra.initLayout("Pilih Nominal",
                saleList, new GridLayoutManager(getContext(), 2,
                        GridLayoutManager.VERTICAL, false),
                itemDecor);

        tv_checkfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                presenter.callFavoriteListRequest(isPascabayar ? PPOB_TYPE_TAG[0] : PPOB_TYPE_TAG[1]);
                hideKeyboard();
            }
        });

        tv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                presenter.callDenomRequest(isPascabayar ? PPOB_TYPE_TAG[0] : PPOB_TYPE_TAG[1],
                        ket_cust_ref.getTextContent());
                hideKeyboard();

            }
        });

        rv_pra.setListener(new ProductPickerViewGroup.onProductSelected() {
            @Override
            public void onProductSelected(ProductIconModel selectedItem) {
                OttoagDenomModel selectedModel = (OttoagDenomModel) selectedItem.getStoredModel();
                presenter.callInquiryRequest(
                        isPascabayar ? PPOB_TYPE_TAG[0] : PPOB_TYPE_TAG[1],
                        selectedModel.getProduct_code(),
                        ket_cust_ref.getTextContent());

                presenter.addPaymentConfirmationData(selectedModel.getProduct_name(), null, ket_cust_ref.getTextContent());
            }

            @Override
            public void onListDeselected() {
                presenter.removePaymentRequestData();
            }
        });

        ((EditText) ket_cust_ref.getComponent()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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

    @Override
    public void doOnProductlistObtainedAction(PpobOttoagDenomResponseModel models) {
        closeLoadingDialog();
        if (isPascabayar) {
            presenter.callInquiryRequest(isPascabayar ? PPOB_TYPE_TAG[0] : PPOB_TYPE_TAG[1],
                    models.getData().getDenomination().get(0).getProduct_code(),
                    ket_cust_ref.getTextContent());
        } else {
            saleList.clear();
            for (OttoagDenomModel model : models.getData().getDenomination()) {
                saleList.add(new ProductIconModel(
                        "hund",
                        null,
                        0,
                        model.getProvider_code() + "," + model.getBiller_code() + "," + model.getProduct_code(),
                        model.getPrice(),
                        model.getProduct_name(),
                        String.valueOf(model.getPrice()),
                        null,
                        model));
            }
            rv_pra.notifyAdapterForDatasetChange();
        }
        favDialog.setListener(new FavoriteDialog.FavoriteDialogInterface() {
            @Override
            public void OnItemSelected(FavoriteDialog dialog, FavoriteItemModel model) {
                ket_cust_ref.setContentText(model.getCustomer_reference());
            }

            @Override
            public void OnDeleteItemSelected(FavoriteDialog dialog, FavoriteItemModel model) {

            }
        });
    }

    @Override
    public void doOnInquiryObtainedAction(final PpobOttoagInquiryResponseModel model) {
        showLoadingDialog();
        if (isPascabayar) {
            pascaAdapter.replaceModel(model.getKey_value_list());
            presenter.addPaymentRequestData(
                    isPascabayar ? PPOB_TYPE_TAG[0] : PPOB_TYPE_TAG[1],
                    model.getProductcode(),
                    ket_cust_ref.getTextContent(), model);
        } else {
            salepriceDialog.setInitVal(model.getAmount());
            salepriceDialog.setListener(new SalepriceDialog.salepriceDialogListener() {
                @Override
                public void onSalepriceDecided(int initPrice, int salePrice) {
                    presenter.addpaymentPriceData(initPrice, (salePrice - initPrice), salePrice, null);
                    presenter.addPaymentRequestData(
                            isPascabayar ? PPOB_TYPE_TAG[0] : PPOB_TYPE_TAG[1],
                            model.getProductcode(),
                            ket_cust_ref.getTextContent(), model, salePrice);
                    if (cb_savefav.isChecked())
                        presenter.callAddFavListRequest(model.getProductcode(), ket_cust_ref.getTextContent(), isPascabayar ? PPOB_TYPE_TAG[0] : PPOB_TYPE_TAG[1]);
                    salepriceDialog.dismiss();
                }

                @Override
                public void onDialogDismiss() {

                }
            });
            salepriceDialog.show();
        }
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
        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, gContext.getString(R.string.error_apifailed), message);
        dialog.show();
    }

    @Override
    public void doOnConnectionFailedAction(Throwable t) {
        closeLoadingDialog();
        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, gContext.getString(R.string.fail_connection), t.getMessage());
        dialog.show();
    }

    @Override
    public void doReset() {
        saleList.clear();
        if (cb_savefav.isChecked())
            cb_savefav.setChecked(false);
        rv_pra.notifyAdapterForDatasetChange();
        presenter.removePaymentRequestData();
    }
}
