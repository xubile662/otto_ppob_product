package hund.otto.ppob.schwein.ottoag.listrik.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.beelabs.com.codebase.base.BaseFragment;
import glenn.base.viewmodule.layout.GridRadioGroup;
import hund.otto.ppob.schwein.ottoag.listrik.R;

public class ListrikAMainFragment extends BaseFragment {

    private Context mContext;
    private View mView;
    private FragmentManager fm;
    private Fragment child1, child2, child3;
    private GridRadioGroup rg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mView = inflater.inflate(R.layout.fragment_ppob_listrik_main, container, false);
        initComponent();
        initFragments();

        initContent();
        return mView;
    }

    private void initComponent() {
        rg = mView.findViewById(R.id.rb_rg);
    }


    private void initFragments() {
        child1 = new ListrikTokenFragment();
        child2 = new ListrikBillFragment();
        child3 = new ListrikNonBillFragment();
        fm = getFragmentManager();

        fm.beginTransaction().add(R.id.rb_fragment, child1).commit();
//        ((ListrikTokenOttoagFragment) child1).setListener(new IListrikListener() {
//            @Override
//            public void OnProductInputDone(BasePpobPaymentRequestModel model) {
//                if (listener != null) {
//                    listener.onProductInputDone(30, model);
//                }
//            }
//
//            @Override
//            public void OnProductInputUnDone() {
//                if (listener != null) {
//                    listener.onProductInputUnDone();
//                }
//            }
//
//            @Override
//            public void OnProductCallError() {
//
//            }
//
//            @Override
//            public void onRequestSaveFav(String a, String b, String c) {
//                saveToFavorite(a, b, c);
//            }
//        });
//
//        ((ListrikOttoagBillFragment) child2).setListener(new IListrikListener() {
//            @Override
//            public void OnProductInputDone(BasePpobPaymentRequestModel model) {
//                if (listener != null) {
//                    listener.onProductInputDone(40, model);
//                }
//            }
//
//            @Override
//            public void OnProductInputUnDone() {
//                if (listener != null) {
//                    listener.onProductInputUnDone();
//                }
//            }
//
//            @Override
//            public void OnProductCallError() {
//
//            }
//
//            @Override
//            public void onRequestSaveFav(String a, String b, String c) {
//                saveToFavorite(a, b, c);
//            }
//        });
//
//
//        ((ListrikNonOttoagBillFragment) child3).setListener(new IListrikListener() {
//            @Override
//            public void OnProductInputDone(BasePpobPaymentRequestModel model) {
//                if (listener != null) {
//                    listener.onProductInputDone(50, model);
//                }
//            }
//
//            @Override
//            public void OnProductInputUnDone() {
//                if (listener != null) {
//                    listener.onProductInputUnDone();
//                }
//            }
//
//            @Override
//            public void OnProductCallError() {
//
//            }
//
//            @Override
//            public void onRequestSaveFav(String a, String b, String c) {
//                saveToFavorite(a, b, c);
//            }
//        });
    }

    private void initContent() {
        rg.setOnCheckedChangeListener(new GridRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(int checkedId) {
                if (checkedId == R.id.rb_token) {
                    fm.beginTransaction().setCustomAnimations(R.anim.fast_slide_in_left, R.anim.fast_slide_out_right).replace(R.id.rb_fragment, child1).commit();
                } else if (checkedId == R.id.rb_prepaid) {
                    fm.beginTransaction().setCustomAnimations(R.anim.fast_slide_in_left, R.anim.fast_slide_out_right).replace(R.id.rb_fragment, child2).commit();
                } else if (checkedId == R.id.rb_nonpaid) {
                    fm.beginTransaction().setCustomAnimations(R.anim.fast_slide_in_left, R.anim.fast_slide_out_right).replace(R.id.rb_fragment, child3).commit();
                } else {
                    Log.e("", "onCheckedChanged: JENK");

                }
            }
        });
    }
}