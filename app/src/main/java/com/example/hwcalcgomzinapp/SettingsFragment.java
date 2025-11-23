package com.example.hwcalcgomzinapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.hwcalcgomzinapp.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private CalculatorViewModel viewModel;
    private SharedPreferences preferences;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentSettingsBinding.bind(view);
        viewModel = new ViewModelProvider(requireActivity()).get(CalculatorViewModel.class);

        preferences = requireActivity().getSharedPreferences("calculator_prefs", 0);
        setupSettings();
    }

    private void setupSettings() {
        // Восстанавливаем сохраненные настройки
        boolean formulaMode = preferences.getBoolean("formula_mode", false);
        binding.switchFormulaMode.setChecked(formulaMode);
        viewModel.setUseFormulaMode(formulaMode);

        binding.switchFormulaMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setUseFormulaMode(isChecked);
            preferences.edit().putBoolean("formula_mode", isChecked).apply();

            // Показываем сообщение о изменении режима
            if (getActivity() != null) {
                String message = isChecked ?
                        "Режим формулы включен - вводите полные выражения" :
                        "Последовательный режим включен - операции выполняются поочередно";

                android.widget.Toast.makeText(
                        getActivity(),
                        message,
                        android.widget.Toast.LENGTH_LONG
                ).show();
            }
        });

        binding.btnResetSettings.setOnClickListener(v -> {
            binding.switchFormulaMode.setChecked(false);
            viewModel.setUseFormulaMode(false);
            preferences.edit().clear().apply();

            if (getActivity() != null) {
                android.widget.Toast.makeText(
                        getActivity(),
                        "Настройки сброшены",
                        android.widget.Toast.LENGTH_SHORT
                ).show();
            }
        });

        // Добавляем описание режимов
        binding.textFormulaModeInfo.setText(
                "• Последовательный режим: операции выполняются поочередно (как в стандартном калькуляторе)\n\n" +
                        "• Режим формулы: можно вводить сложные выражения (например: 2+3*4) которые вычисляются с учетом приоритета операций"
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}