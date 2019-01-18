package hund.otto.ppob.schwein.ottoag.telkom;

import hund.otto.ppob.core.View.Ppob_landing.PpobBaseFragment;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;

public class TelkomFragment extends PpobBaseFragment {

    public TelkomFragment() {
        setListener(new childFragmentCallback() {
            @Override
            public void onFragmentReady() {
                PpobBaseProductFragment productFragment = new TelkomProductFragment();
                addProductFragment(productFragment);
            }
        });
    }
}
