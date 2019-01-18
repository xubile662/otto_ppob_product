package hund.otto.ppob.schwein.ottoag.telkom;

import android.app.Dialog;
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

import app.beelabs.com.codebase.component.ProgressDialogComponent;
import glenn.base.viewmodule.dialog.ErrorDialog;
import hund.otto.ppob.core.CORE;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagDenomResponseModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagInquiryResponseModel;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;
import hund.otto.ppob.schwein.ottoag.presenter.PpobOttoagProductViewPresenter;
import hund.otto.ppob.schwein.ottoag.view.PpobOttoagProductViewInterface;

public class TelkomProductFragment extends PpobBaseProductFragment implements PpobOttoagProductViewInterface {
    private Context mContext;
    private View mView, action1, tv_checkfav, tv_action;
    private PpobOttoagProductViewPresenter presenter;
    private Dialog loadingDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_service_generic, container, false);

        presenter = new PpobOttoagProductViewPresenter(this, CORE.getAppComponent(), this, CORE.getEndpoint());

        initComponent();
        initContent();
        return mView;
    }

    private void initComponent() {

    }

    private void initContent() {

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

    }

    @Override
    public void doOnInquiryObtainedAction(PpobOttoagInquiryResponseModel model) {

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

    }
}

