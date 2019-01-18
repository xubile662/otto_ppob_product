package hund.otto.ppob.schwein.ottoag.paket.view;

import hund.otto.ppob.core.View.Ppob_landing.PpobBaseFragment;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;

public class PulsaPaketFragment extends PpobBaseFragment {

    public PulsaPaketFragment() {
        setListener(new childFragmentCallback() {
            @Override
            public void onFragmentReady() {
                PpobBaseProductFragment productFragment = new PulsaPaketProductFragment();
                addProductFragment(productFragment);
            }
        });
    }
}
