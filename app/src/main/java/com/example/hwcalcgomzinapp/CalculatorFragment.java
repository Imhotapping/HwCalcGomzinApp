package com.example.hwcalcgomzinapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.hwcalcgomzinapp.databinding.FragmentCalculatorBinding;

public class CalculatorFragment extends Fragment {
    private FragmentCalculatorBinding binding;
    private CalculatorViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(CalculatorViewModel.class);

        setupNumberButtons();
        setupOperationButtons();
        setupMemoryButtons();
        setupOtherButtons();
        observeViewModel();
    }

    private void setupNumberButtons() {
        binding.btn0.setOnClickListener(v -> viewModel.appendNumber("0"));
        binding.btn1.setOnClickListener(v -> viewModel.appendNumber("1"));
        binding.btn2.setOnClickListener(v -> viewModel.appendNumber("2"));
        binding.btn3.setOnClickListener(v -> viewModel.appendNumber("3"));
        binding.btn4.setOnClickListener(v -> viewModel.appendNumber("4"));
        binding.btn5.setOnClickListener(v -> viewModel.appendNumber("5"));
        binding.btn6.setOnClickListener(v -> viewModel.appendNumber("6"));
        binding.btn7.setOnClickListener(v -> viewModel.appendNumber("7"));
        binding.btn8.setOnClickListener(v -> viewModel.appendNumber("8"));
        binding.btn9.setOnClickListener(v -> viewModel.appendNumber("9"));
    }

    private void setupOperationButtons() {
        binding.btnAdd.setOnClickListener(v -> viewModel.setOperation("+"));
        binding.btnSubtract.setOnClickListener(v -> viewModel.setOperation("-"));
        binding.btnMultiply.setOnClickListener(v -> viewModel.setOperation("*"));
        binding.btnDivide.setOnClickListener(v -> viewModel.setOperation("/"));
        binding.btnEquals.setOnClickListener(v -> viewModel.calculate());
        binding.btnClear.setOnClickListener(v -> showClearDialog());
    }

    private void setupMemoryButtons() {
        binding.btnMemoryAdd.setOnClickListener(v -> viewModel.memoryAdd());
        binding.btnMemoryRecall.setOnClickListener(v -> viewModel.memoryRecall());
        binding.btnMemoryClear.setOnClickListener(v -> viewModel.memoryClear());
    }

    private void setupOtherButtons() {
        binding.btnDecimal.setOnClickListener(v -> viewModel.addDecimalPoint());
    }

    private void observeViewModel() {
        viewModel.getDisplayValue().observe(getViewLifecycleOwner(),
                value -> binding.display.setText(value));
    }

    private void showClearDialog() {
        new ClearDialogFragment().show(getParentFragmentManager(), "ClearDialog");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}