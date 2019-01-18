package hund.otto.ppob.schwein.ottoag.bpjs.view;

import hund.otto.ppob.core.View.Ppob_landing.PpobBaseFragment;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;

public class BpjsFragment extends PpobBaseFragment {

    public BpjsFragment() {
        setListener(new childFragmentCallback() {
            @Override
            public void onFragmentReady() {
                PpobBaseProductFragment productFragment = new BpjsProductFragment();
                addProductFragment(productFragment);
            }
        });
    }
}

