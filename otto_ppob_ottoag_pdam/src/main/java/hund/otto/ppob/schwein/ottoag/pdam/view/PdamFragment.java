package hund.otto.ppob.schwein.ottoag.pdam.view;

import hund.otto.ppob.core.View.Ppob_landing.PpobBaseFragment;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;

public class PdamFragment extends PpobBaseFragment {

    public PdamFragment() {
        setListener(new childFragmentCallback() {
            @Override
            public void onFragmentReady() {
                PpobBaseProductFragment productFragment = new PdamProductFragment();
                addProductFragment(productFragment);
            }
        });
    }
}
