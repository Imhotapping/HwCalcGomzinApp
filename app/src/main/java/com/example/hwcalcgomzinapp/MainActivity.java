package com.example.hwcalcgomzinapp;

import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.hwcalcgomzinapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        // настройка кнопки меню - используем стандартную иконку
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size);
            getSupportActionBar().setHomeActionContentDescription(R.string.Меню);
        }

        // Обработчик клика по иконке меню в тулбаре
        binding.toolbar.setNavigationOnClickListener(v -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        // Новый способ обработки кнопки "Назад"
        setupBackPressedHandler();

        setupNavigation();
        showCalculatorFragment();
    }

    private void setupBackPressedHandler() {
        // Создаем callback для обработки нажатия кнопки "Назад"
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Если NavigationDrawer открыт - закрываем его
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    } else {
                        setEnabled(false);
                        MainActivity.this.finish();
                    }
                }
            }
        };

        // Регистрируем callback в OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setupNavigation() {
        binding.navView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_calculator) {
                showCalculatorFragment();
            } else if (itemId == R.id.nav_about) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AboutFragment())
                        .addToBackStack("about")
                        .commit();
            } else if (itemId == R.id.nav_settings) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .addToBackStack("settings")
                        .commit();
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void showCalculatorFragment() {
        getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new CalculatorFragment())
                .commit();
    }
}