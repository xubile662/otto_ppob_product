package hund.otto.ppob.schwein.ottoag.bpjs.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.otto.mart.viewmodule.dialog.FavoriteDialog;
import com.otto.mart.viewmodule.model.PickerUiModel.ProductIconModel;
import com.otto.mart.viewmodule.view.ProductPickerViewGroup;
import com.otto.mart.viewmodule.viewModels.FavoriteItemModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.beelabs.com.codebase.component.ProgressDialogComponent;
import glenn.base.viewmodule.adapterDecorator.GridDividerDecorator;
import glenn.base.viewmodule.dialog.ErrorDialog;
import glenn.base.viewmodule.editText.KudaEdittext;
import glenn.base.viewmodule.spinner.CategoryModel;
import glenn.base.viewmodule.spinner.HideableSpinnerView;
import glenn.base.viewmodule.spinner.SpinnerCategoryModelAdapter;
import glenn.base.viewmodule.textView.LazyTextview;
import hund.otto.ppob.core.CORE;
import hund.otto.ppob.core.Interactor.Model.Misc.OttoagDenomModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagDenomResponseModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagInquiryResponseModel;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;
import hund.otto.ppob.core.View.component.KeyValueListAdapter;
import hund.otto.ppob.schwein.ottoag.bpjs.R;
import hund.otto.ppob.schwein.ottoag.presenter.PpobOttoagProductViewPresenter;
import hund.otto.ppob.schwein.ottoag.view.PpobOttoagProductViewInterface;

public class BpjsProductFragment extends PpobBaseProductFragment implements PpobOttoagProductViewInterface {
    private final String PPOB_TYPE_TAG = "bpjs";
    private Context mContext;
    private View mView, tv_checkfav, process;
    private KudaEdittext ket_1;
    private ProductPickerViewGroup rv;
    List<ProductIconModel> saleList;
    private ExpandableLayout eLayout, elayout2, elayout3;
    private HideableSpinnerView hsv_month;
    private boolean isMonthReady, isKkReady;
    private int howManyMonth;

    private RecyclerView rv_let;
    private KeyValueListAdapter displayAdapter;

    private FavoriteDialog dialog;
    private List<FavoriteItemModel> models;

    private CheckBox cb_savefav;
    private PpobOttoagProductViewPresenter presenter;
    private Dialog loadingDialog;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_service_bpjs, container, false);

        presenter = new PpobOttoagProductViewPresenter(this, CORE.getAppComponent(), this, CORE.getEndpoint());

        initComponent();
        initContent();
        return mView;
    }

    private void initComponent() {
        rv = mView.findViewById(R.id.init);
        ket_1 = mView.findViewById(R.id.ket_custid);
        ket_1.addLogo(0);
        ((EditText) ket_1.getComponent()).setInputType(InputType.TYPE_CLASS_NUMBER);
        saleList = new ArrayList<>();
        eLayout = mView.findViewById(R.id.eLayout);
        elayout2 = mView.findViewById(R.id.elayout2);
        hsv_month = mView.findViewById(R.id.hsv_month);

        rv_let = mView.findViewById(R.id.rv_let);
        displayAdapter = new KeyValueListAdapter(R.layout.item_key_value_list_default);
        elayout3 = mView.findViewById(R.id.elayout3);

        dialog = new FavoriteDialog(getContext(), getParentFragment().getActivity(), false, false);
        tv_checkfav = mView.findViewById(R.id.tv_checkfav);
        process = mView.findViewById(R.id.process);
        cb_savefav = mView.findViewById(R.id.cb_savefav);
    }

    private void initContent() {

        GridDividerDecorator itemDecor = new GridDividerDecorator(16, 2);
        rv.initLayout(null, saleList, R.layout.cw_selector_product, R.layout.item_bpjs, new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false), itemDecor, false, true);
        rv.setListener(new ProductPickerViewGroup.onProductSelected() {
            @Override
            public void onProductSelected(ProductIconModel selectedItem) {
                eLayout.expand();
                presenter.callFavoriteListRequest(PPOB_TYPE_TAG);
                //                checkForFav();
            }

            @Override
            public void onListDeselected() {
                eLayout.collapse();
                elayout2.collapse();
            }
        });
        presenter.callDenomRequest(PPOB_TYPE_TAG, "");

        ((EditText) ket_1.getComponent()).addTextChangedListener(new TextWatcher() {
            private Timer timer = new Timer();
            private final long DELAY = 3000;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listener.onProductInputUnDone();
                ((EditText) ket_1.getComponent()).post(new Runnable() {
                    @Override
                    public void run() {
                        ((EditText) ket_1.getComponent()).requestFocus();
                    }
                });
                process.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() > 1) {

                    timer.cancel();
                    timer = new Timer();
                    hsv_month.resetSpinner();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    isKkReady = true;
                                    if (rv.getSelectedStoreObjectModel() != null) {
                                        if (isKkReady && isMonthReady) {
//                                            showLoadingDialog();
//                                            presenter.callInquiryRequest(PPOB_TYPE_TAG,
//                                                    ((OttoagDenomModel) rv.getSelectedStoreObjectModel()).getProduct_code(),
//                                                    ket_1.getTextContent());
                                        }
                                    }
                                }
                            },
                            DELAY
                    );
                    if (isMonthReady) process.setVisibility(View.VISIBLE);
                } else {
                    isKkReady = false;
                    if (timer != null) {
                        timer.cancel();
                    }
                    process.setVisibility(View.GONE);
                }
                if (elayout3.isExpanded()) {
                    elayout3.setExpanded(false);
                    listener.onProductInputUnDone();
                }
                if (elayout2.isExpanded()) {
                    elayout2.setExpanded(false);
                    listener.onProductInputUnDone();
                }
                hsv_month.resetSpinner();
                isMonthReady = false;
                ket_1.getComponent().post(new Runnable() {
                    @Override
                    public void run() {
                        ket_1.getComponent().requestFocus();
                    }
                });
            }
        });

        int monthcount;
        List<CategoryModel> months = new ArrayList<>();

        for (monthcount = 0; monthcount <= 12; monthcount++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, monthcount);
            CategoryModel month = new CategoryModel();
            month.setId(monthcount + 1);
            month.setName(new SimpleDateFormat("MMMM yyyy").format(cal.getTime()));
            months.add(month);
        }
        hsv_month.addData(months);
        hsv_month.setOnCallback(new HideableSpinnerView.HidableSpinnerviewCallback() {
            @Override
            public void onItemSelected(Spinner view, int position, SpinnerCategoryModelAdapter adapter) {
                elayout3.setExpanded(false);
                if (listener != null)
                    listener.onProductInputUnDone();
                isMonthReady = true;
                howManyMonth = ((CategoryModel) adapter.getItem(position)).getId();
                if (howManyMonth < 0) {
                    howManyMonth = 0;
                }
                if (isMonthReady && isKkReady) {
//                    showLoadingDialog();
//                    presenter.callInquiryRequest(PPOB_TYPE_TAG,
//                            ((OttoagDenomModel) rv.getSelectedStoreObjectModel()).getProduct_code(),
//                            ket_1.getTextContent());
                }
                if (ket_1.getTextContent().length() > 1) process.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDropdownOpen() {

            }

            @Override
            public void onHide() {

            }

            @Override
            public void onExpand() {

            }

            @Override
            public void onDataLoaded(Spinner view, SpinnerCategoryModelAdapter adapter) {
                hsv_month.hideLoading();
            }
        });

        process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMonthReady && !TextUtils.isEmpty(ket_1.getTextContent())) {
                    showLoadingDialog();
                    presenter.callInquiryRequest(PPOB_TYPE_TAG,
                            ((OttoagDenomModel) rv.getSelectedStoreObjectModel()).getProduct_code(),
                            ket_1.getTextContent());
                    ((EditText) ket_1.getComponent()).setError(null);
                } else if (TextUtils.isEmpty(ket_1.getTextContent())) {
                    ((EditText) ket_1.getComponent()).setError("Nomor kepesertaan harus di isi");
                } else if (!isMonthReady) {
                    Toast.makeText(getContext(), "Pilih bulan pembayaran", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rv_let.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_let.setAdapter(displayAdapter);

        dialog.setListener(new FavoriteDialog.FavoriteDialogInterface() {
            @Override
            public void OnItemSelected(FavoriteDialog dialog, FavoriteItemModel model) {
                ket_1.setContentText(model.getCustomer_reference());
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
                dialog.show();
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
    public void doOnProductlistObtainedAction(PpobOttoagDenomResponseModel model) {

        List<OttoagDenomModel> models = model.getData().getDenomination();
        saleList.clear();
        for (OttoagDenomModel odel :
                models) {
            saleList.add(
                    new ProductIconModel(odel.getProduct_name(),
                            null,
                            0,
                            "",
                            0,
                            "",
                            "",
                            null,
                            odel.getLogo(),
                            odel));
        }
        rv.notifyAdapterForDatasetChange();
    }

    @Override
    public void doOnInquiryObtainedAction(PpobOttoagInquiryResponseModel model) {
        model.setMonths(howManyMonth);
        presenter.addPaymentRequestData(PPOB_TYPE_TAG, model.getProductcode(), ket_1.getTextContent(), model);
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
