package hund.otto.ppob.schwein.ottoag.listrik.view;

import com.otto.mart.viewmodule.viewModels.FavoriteItemModel;

import java.util.List;

import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagDenomResponseModel;
import hund.otto.ppob.core.Interactor.Model.ResponseModel.PpobOttoagInquiryResponseModel;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;
import hund.otto.ppob.schwein.ottoag.presenter.PpobOttoagProductViewPresenter;
import hund.otto.ppob.schwein.ottoag.view.PpobOttoagProductViewInterface;

public class ListrikNonBillFragment extends PpobBaseProductFragment implements PpobOttoagProductViewInterface {

    PpobOttoagProductViewPresenter presenter;

    @Override
    public void doOnProductlistObtainedAction(PpobOttoagDenomResponseModel model) {

    }

    @Override
    public void doOnInquiryObtainedAction(PpobOttoagInquiryResponseModel model) {

    }

    @Override
    public void doShowFavoriteView(List<FavoriteItemModel> odel) {

    }

    @Override
    public void doShowMsgDialog(String message) {

    }

    @Override
    public void doOnApiFailedAction(int code, String message) {

    }

    @Override
    public void doOnConnectionFailedAction(Throwable t) {

    }

    @Override
    public void doReset() {

    }
}
