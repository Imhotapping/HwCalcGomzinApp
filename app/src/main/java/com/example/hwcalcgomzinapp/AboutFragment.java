package com.example.hwcalcgomzinapp;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.hwcalcgomzinapp.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {
    private FragmentAboutBinding binding;

    public AboutFragment() {
        super(R.layout.fragment_about);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация ViewBinding
        binding = FragmentAboutBinding.bind(view);

        setupAboutContent();
    }

    private void setupAboutContent() {
        binding.textVersion.setText("Версия 1.0");

        binding.textDescription.setText(
                "Калькулятор с расширенными функциями:\n\n" +
                        "• Базовые операции (+, -, *, /)\n" +
                        "• Память калькулятора (M+, MR, MC)\n" +
                        "• Поддержка портретной и ландшафтной ориентации\n" +
                        "• Сохранение состояния при повороте\n" +
                        "• Два режима расчета\n" +
                        "• История операций\n\n" +
                        "Разработано с использованием современных технологий:\n" +
                        "• ViewBinding для безопасной работы с View\n" +
                        "• ViewModel для хранения состояния\n" +
                        "• LiveData для реактивного обновления UI\n" +
                        "• Fragment для модульной архитектуры"
        );

        binding.btnBack.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}