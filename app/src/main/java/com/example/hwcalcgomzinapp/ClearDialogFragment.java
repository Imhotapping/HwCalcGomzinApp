package com.example.hwcalcgomzinapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.hwcalcgomzinapp.CalculatorViewModel;

public class ClearDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Очистка калькулятора")
                .setMessage("Вы уверены, что хотите очистить все данные?")
                .setPositiveButton("Очистить", (dialog, id) -> {
                    CalculatorViewModel viewModel = new ViewModelProvider(requireActivity())
                            .get(CalculatorViewModel.class);
                    viewModel.clear();
                })
                .setNegativeButton("Отмена", (dialog, id) -> {
                    dialog.cancel();
                });
        return builder.create();
    }
}