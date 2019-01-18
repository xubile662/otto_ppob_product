package hund.otto.ppob.schwein.ottoag.education.view;

import hund.otto.ppob.core.View.Ppob_landing.PpobBaseFragment;
import hund.otto.ppob.core.View.Ppob_product.PpobBaseProductFragment;

public class EducationFragment extends PpobBaseFragment {

    public EducationFragment() {
        setListener(new childFragmentCallback() {
            @Override
            public void onFragmentReady() {
                PpobBaseProductFragment productFragment = new EducationProductFragment();
                addProductFragment(productFragment);
            }
        });
    }
}

