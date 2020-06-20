package com.procter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.procter.R;
import com.procter.databinding.FragmentPrivacyPolicyBinding;
import com.procter.databinding.ToolbarBindingBinding;
import com.procter.viewmodel.CommonViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link PrivacyPolicyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrivacyPolicyFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String type = "2";

    FragmentPrivacyPolicyBinding binding;
    private CommonViewModel viewModel;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrivacyPolicyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrivacyPolicyFragment newInstance(String param1, String param2) {
        PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_privacy_policy;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        viewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        ToolbarBindingBinding toolbarBindingBinding = DataBindingUtil.findBinding(binding.toolBar.getRoot());
        assert toolbarBindingBinding != null;
        toolbarBindingBinding.txtHeader.setText("Privacy Policy");

        binding.setViewModel(viewModel);
        getPrivacyPolicyData(type);
        return binding.getRoot();
    }

    private void getPrivacyPolicyData(String type) {
        customDialog.show();
        viewModel.getPrivacyPolicyData(type).observe(this, s -> {
            binding.wvPrivacyPolicy.loadData(s, "text/html", "UTF-8");
            customDialog.dismiss();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (customDialog != null)
            customDialog.dismiss();
    }
}
