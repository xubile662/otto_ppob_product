package hund.otto.ppob.schwein.ottoag.pdam.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.otto.mart.viewmodule.dialog.FavoriteDialog;
import com.otto.mart.viewmodule.viewModels.FavoriteItemModel;

import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;
import app.beelabs.com.codebase.component.ProgressDialogComponent;
import glenn.base.viewmodule.dialog.ErrorDialog;
import hund.otto.ppob.core.CORE;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagDenomResponseModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagInquiryResponseModel;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;
import hund.otto.ppob.schwein.ottoag.pdam.R;
import hund.otto.ppob.schwein.ottoag.presenter.PpobOttoagProductViewPresenter;
import hund.otto.ppob.schwein.ottoag.view.PpobOttoagProductViewInterface;

public class PdamProductFragment extends PpobBaseProductFragment implements PpobOttoagProductViewInterface {

    private Context mContext;
    private View mView;
    private PpobOttoagProductViewPresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_service_iac, container, false);
        presenter = new PpobOttoagProductViewPresenter(this, CORE.getAppComponent(), this, CORE.getEndpoint());

        initComponent();
        initContent();
        return mView;
    }

    private void initComponent() {

    }

    private void initContent() {

    }

    @Override
    public void doOnProductlistObtainedAction(PpobOttoagDenomResponseModel model) {

    }

    @Override
    public void doOnInquiryObtainedAction(PpobOttoagInquiryResponseModel model) {

    }

    @Override
    public void doShowFavoriteView(List<FavoriteItemModel> odel) {
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        FavoriteDialog dialog = new FavoriteDialog(getContext(), getActivity(), false, false);
        dialog.addItem(odel);
        dialog.show();
    }

    @Override
    public void doShowMsgDialog(String message) {
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, "Pesan", message);
        dialog.show();
    }

    @Override
    public void doOnApiFailedAction(int code, String message) {
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, mContext.getString(R.string.error_apifailed), message);
        dialog.show();
    }

    @Override
    public void doOnConnectionFailedAction(Throwable t) {
        ProgressDialogComponent.dismissProgressDialog((BaseActivity) getActivity());
        ErrorDialog dialog = new ErrorDialog(getContext(), getActivity(), false, false, mContext.getString(R.string.fail_connection), t.getMessage());
        dialog.show();
    }

    @Override
    public void doReset() {
//        saleList.clear();
//        if (cb_savefav.isChecked())
//            cb_savefav.setChecked(false);
//        rv.notifyAdapterForDatasetChange();
        presenter.removePaymentRequestData();
    }
}
