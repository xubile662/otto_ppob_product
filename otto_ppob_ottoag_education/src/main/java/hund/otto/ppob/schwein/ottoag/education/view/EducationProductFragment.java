package hund.otto.ppob.schwein.ottoag.education.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;

import com.otto.mart.viewmodule.dialog.FavoriteDialog;
import com.otto.mart.viewmodule.viewModels.FavoriteItemModel;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import glenn.base.viewmodule.dialog.ErrorDialog;
import glenn.base.viewmodule.editText.InstantAutoCompleteEditText;
import glenn.base.viewmodule.editText.KudaEdittext;
import glenn.base.viewmodule.spinner.CategoryModel;
import hund.otto.ppob.core.CORE;
import hund.otto.ppob.core.Interactor.Model.Misc.OttoagDenomModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagDenomResponseModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagInquiryResponseModel;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;
import hund.otto.ppob.core.View.component.InstantAutocompleteOttoPpobAdapter;
import hund.otto.ppob.core.View.component.KeyValueListAdapter;
import hund.otto.ppob.schwein.ottoag.education.R;
import hund.otto.ppob.schwein.ottoag.presenter.PpobOttoagProductViewPresenter;
import hund.otto.ppob.schwein.ottoag.view.PpobOttoagProductViewInterface;

public class EducationProductFragment extends PpobBaseProductFragment implements PpobOttoagProductViewInterface {

    private final String PPOB_TYPE_TAG = "education";

    private Context mContext;
    private View mView, tv_checkfav, tv_action;
    private KudaEdittext ket_1;
    List<CategoryModel> saleList;
    private ExpandableLayout eLayout, elayout3;

    private boolean isKkReady;
    private RecyclerView rv_let;
    private KeyValueListAdapter displayAdapter;

    private FavoriteDialog dialog;

    private InstantAutoCompleteEditText iac_product;
    private InstantAutocompleteOttoPpobAdapter autoCompleteAdapter;
    private CheckBox cb_savefav;

    private PpobOttoagProductViewPresenter presenter;

    private Dialog loadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_service_iac, container, false);
        presenter = new PpobOttoagProductViewPresenter(this, CORE.getAppComponent(), this, CORE.getEndpoint());
        initComponent();
        initContent();
        return mView;
    }

    @Override
    public void onPause() {
        iac_product.dismissDropDown();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initComponent() {
        ket_1 = mView.findViewById(R.id.ket_custid);
        ket_1.addLogo(0);
        ((EditText) ket_1.getComponent()).setInputType(InputType.TYPE_CLASS_NUMBER);
        saleList = new ArrayList<>();
        eLayout = mView.findViewById(R.id.eLayoutx);
        eLayout.setExpanded(false);
        rv_let = mView.findViewById(R.id.rv_let);
        displayAdapter = new KeyValueListAdapter(R.layout.item_key_value_list_default);
        elayout3 = mView.findViewById(R.id.elayout3);
        elayout3.setExpanded(false);

        dialog = new FavoriteDialog(getContext(), getParentFragment().getActivity(), false, false);
        tv_checkfav = mView.findViewById(R.id.tv_checkfav);
        tv_action = mView.findViewById(R.id.process);

        iac_product = mView.findViewById(R.id.iac_product);
        cb_savefav = mView.findViewById(R.id.cb_savefav);

        saleList = new ArrayList<>();

        autoCompleteAdapter = new InstantAutocompleteOttoPpobAdapter(mContext, saleList);


    }

    private void initContent() {
        showLoadingDialog();
        rv_let.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_let.setAdapter(displayAdapter);
        presenter.callDenomRequest(PPOB_TYPE_TAG, "");
        presenter.callFavoriteListRequest(PPOB_TYPE_TAG);

        ((EditText) ket_1.getComponent()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() > 8) {
                    tv_action.setVisibility(View.VISIBLE);
                    isKkReady = true;

                } else {
                    tv_action.setVisibility(View.GONE);
                    isKkReady = false;
                }
                if (listener != null)
                    listener.onProductInputUnDone();

                ket_1.getComponent().post(new Runnable() {
                    @Override
                    public void run() {
                        ket_1.getComponent().requestFocus();
                    }
                });
                displayAdapter.clearModel();
            }
        });

        tv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isKkReady) {
                    showLoadingDialog();
                    presenter.callInquiryRequest(PPOB_TYPE_TAG, (String) ket_1.getTag(44), ket_1.getTextContent());
                }
            }
        });

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
                showLoadingDialog();
                dialog.show();
            }
        });

        iac_product.setAdapter(autoCompleteAdapter);

        iac_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iac_product.showDropDown();
            }
        });

        iac_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OttoagDenomModel selectedModel = (OttoagDenomModel) ((CategoryModel) autoCompleteAdapter.getModel(position)).getSavedModel();
                tv_checkfav.setVisibility(View.GONE);
                ket_1.setContentText("");
                ket_1.addLogo(selectedModel.getLogo());
                ket_1.setTag(44, selectedModel.getProduct_code());
                eLayout.expand();
                presenter.callFavoriteListRequest(PPOB_TYPE_TAG);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iac_product.clearFocus();
                        ket_1.getComponent().requestFocus();
                    }
                }, 100);

                if (listener != null)
                    listener.onProductInputUnDone();
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
        int i = 0;
        for (OttoagDenomModel model :
                models.getData().getDenomination()) {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setTitle(model.getProduct_name());
            categoryModel.setName(model.getBiller_code());
            categoryModel.setId(i);
            categoryModel.setSavedModel(model);
            saleList.add(categoryModel);
            i++;
        }
    }

    @Override
    public void doOnInquiryObtainedAction(PpobOttoagInquiryResponseModel model) {
        closeLoadingDialog();
        presenter.addPaymentRequestData(
                PPOB_TYPE_TAG,
                model.getProductcode(),
                ket_1.getTextContent(), model);
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
//        rv.notifyAdapterForDatasetChange();
        presenter.removePaymentRequestData();
    }
}
