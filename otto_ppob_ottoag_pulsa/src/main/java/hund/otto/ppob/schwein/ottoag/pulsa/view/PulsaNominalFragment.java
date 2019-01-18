package hund.otto.ppob.schwein.ottoag.pulsa.view;

import hund.otto.ppob.core.View.Ppob_landing.PpobBaseFragment;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;

public class PulsaNominalFragment extends PpobBaseFragment {

    public PulsaNominalFragment() {
        setListener(new childFragmentCallback() {
            @Override
            public void onFragmentReady() {
                PpobBaseProductFragment productFragment = new PulsaNominalProductFragment();
                addProductFragment(productFragment);
            }
        });
    }
}
