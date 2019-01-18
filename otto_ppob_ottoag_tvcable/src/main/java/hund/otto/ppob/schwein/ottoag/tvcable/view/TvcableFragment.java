package hund.otto.ppob.schwein.ottoag.tvcable.view;

import hund.otto.ppob.core.View.Ppob_landing.PpobBaseFragment;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;

public class TvcableFragment extends PpobBaseFragment {

    public TvcableFragment() {
        setListener(new childFragmentCallback() {
            @Override
            public void onFragmentReady() {
                PpobBaseProductFragment productFragment = new TvcableProductFragment();
                addProductFragment(productFragment);
            }
        });
    }
}

