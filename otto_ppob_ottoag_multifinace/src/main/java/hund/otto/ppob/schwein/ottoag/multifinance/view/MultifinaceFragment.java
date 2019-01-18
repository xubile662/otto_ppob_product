package hund.otto.ppob.schwein.ottoag.multifinance.view;

import hund.otto.ppob.core.View.Ppob_landing.PpobBaseFragment;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;

public class MultifinaceFragment extends PpobBaseFragment {

    public MultifinaceFragment() {
        setListener(new childFragmentCallback() {
            @Override
            public void onFragmentReady() {
                PpobBaseProductFragment productFragment = new MultifinanceProductFragment();
                addProductFragment(productFragment);
            }
        });
    }
}

