package hund.otto.ppob.schwein.ottoag.insurance.view;

import hund.otto.ppob.core.View.Ppob_landing.PpobBaseFragment;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;

public class InsuranceFragment extends PpobBaseFragment {

    public InsuranceFragment() {
        setListener(new childFragmentCallback() {
            @Override
            public void onFragmentReady() {
                PpobBaseProductFragment productFragment = new InsuranceProductFragment();
                addProductFragment(productFragment);
            }
        });
    }
}
