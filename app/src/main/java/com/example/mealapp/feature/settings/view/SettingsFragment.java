    package com.example.mealapp.feature.settings.view;

    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.content.res.Configuration;
    import android.os.Bundle;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AlertDialog;
    import androidx.fragment.app.Fragment;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.mealapp.R;
    import com.example.mealapp.feature.auth.sign_in.view.SignIn;
    import com.example.mealapp.feature.main.view.MainScreen;
    import com.example.mealapp.feature.settings.presenter.ISettingsPresenter;
    import com.example.mealapp.feature.settings.presenter.SettingsPresenter;
    import com.example.mealapp.utils.common_layer.models.UserSessionHolder;
    import com.example.mealapp.utils.connection_helper.NetworkUtil;
    import com.example.mealapp.utils.data_source_manager.MealRepositoryImpl;
    import com.example.mealapp.utils.dp.MealLocalDataSourceImpl;
    import com.example.mealapp.utils.network.MealRemoteDataSourceImpl;
    import com.example.mealapp.utils.shared_preferences.SharedPreferencesManager;

    import java.util.Locale;


    public class SettingsFragment extends Fragment implements ISettings {

        Button signInBtn, signOutBtn, backUpBtn;
        TextView email, emailText, language;

        private ProgressDialog progressDialog;
        LinearLayout languageContainer;
        ISettingsPresenter presenter;
        boolean isEnglish = true;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            presenter = new SettingsPresenter(this, MealRepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(requireActivity()), SharedPreferencesManager.getInstance(requireActivity())));
            presenter.initializeLanguageSetting();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_settings, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            intiUI(view);
            setUpListeners();

        }

        private void setUpListeners() {
            signInBtn.setOnClickListener(view -> {
                SignIn signInFragment = new SignIn();
                signInFragment.show(getParentFragmentManager(), "signInFragment");
            });

            signOutBtn.setOnClickListener(view -> showSignOutDialog());

            backUpBtn.setOnClickListener(view -> {
                if(NetworkUtil.isConnected()){
                    presenter.uploadDataToFirebase(this);
                } else {
                    showMessage(getString(R.string.no_internet_message));
                }
            });

            languageContainer.setOnClickListener(v -> {
                isEnglish = !isEnglish;
                presenter.handleLanguageSetting(isEnglish);
            });
        }
        @Override
        public void onSignOut(){
            Intent intent = new Intent(getActivity(), MainScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        }

        private void showSignOutDialog() {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.sign_out))
                    .setMessage(getString(R.string.are_you_sure_sign_out))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> presenter.signOut()) //dialog reference to the alertdialog, which is the clicked button
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.dismiss())
                    .show();
        }


        private void intiUI(View view) {
            signInBtn = view.findViewById(R.id.signInBtn);
            signOutBtn = view.findViewById(R.id.signOutBtn);
            backUpBtn = view.findViewById(R.id.backUpBtn);
            email = view.findViewById(R.id.email);
            emailText = view.findViewById(R.id.emailText);
            language = view.findViewById(R.id.language);
            languageContainer = view.findViewById(R.id.languageContainer);

            language.setText(isEnglish ? getString(R.string.lang) : getString(R.string.lang));


            if (UserSessionHolder.getInstance().getUser() != null) {
                signInBtn.setVisibility(View.GONE);
                signOutBtn.setVisibility(View.VISIBLE);
                emailText.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                email.setText(UserSessionHolder.getInstance().getUser().getEmail());
                backUpBtn.setVisibility(View.VISIBLE);
            }
        }

        public void setLocale(String languageCode) {
            Locale locale = new Locale(languageCode);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            requireActivity().recreate();
        }

        @Override
        public void setLanguage(boolean isEnglish) {
            this.isEnglish = isEnglish;
        }

        @Override
        public void showMessage(String msg) {
            Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void showBackUpWarning() {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle(getString(R.string.warning))
                    .setMessage(getString(R.string.backup_interrupted_message))
                    .setPositiveButton(getStringFromRes(R.string.ok), (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override

        public void showLoading() {
            if (progressDialog == null) {
                Log.d("TAG", "showLoading: ");
                progressDialog = new ProgressDialog(requireActivity());
                progressDialog.setMessage(getString(R.string.backing_up_message));
                progressDialog.setCancelable(false);
            }
            progressDialog.show();
        }

        @Override
        public void hideLoading() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        @Override
        public String getStringFromRes(int resId) {
            return getString(resId);
        }

    }