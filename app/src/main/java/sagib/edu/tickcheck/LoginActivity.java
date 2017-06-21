package sagib.edu.tickcheck;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnSignInIf)
    BootstrapButton btnSignInIf;
    @BindView(R.id.tvIfRegistered)
    TextView tvIfRegistered;
    private FirebaseAuth mAuth;
    String email;
    String password;
    String firstName;
    String lastName;

    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.tilUserName)
    TextInputLayout tilUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.btnSignIn)
    BootstrapButton btnSignIn;
    @BindView(R.id.btnRegister)
    BootstrapButton btnRegister;
    @BindView(R.id.tvRegDetailsTitle)
    TextView tvRegDetailsTitle;
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.tilFirstName)
    TextInputLayout tilFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.tilLastName)
    TextInputLayout tilLastName;
    @BindView(R.id.btnFinalRegister)
    BootstrapButton btnFinalRegister;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        hideRegisterItems();
        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btnSignIn)
    public void onBtnSignInClicked() {
        email = etUserName.getText().toString();
        password = etPassword.getText().toString();
        dialog = new ProgressDialog(this);
        dialog.setMessage("מתחבר...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            dialog.dismiss();
                            goToMain();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.btnRegister)
    public void onBtnRegisterClicked() {
        btnSignIn.animate().alpha(0).rotation(360);
        btnSignIn.setVisibility(View.GONE);
        btnRegister.animate().alpha(0).rotation(360);
        btnRegister.setVisibility(View.GONE);
        tvRegDetailsTitle.setVisibility(View.VISIBLE);
        etFirstName.setVisibility(View.VISIBLE);
        tilFirstName.setVisibility(View.VISIBLE);
        etLastName.setVisibility(View.VISIBLE);
        tilLastName.setVisibility(View.VISIBLE);
        btnFinalRegister.setVisibility(View.VISIBLE);
        tvIfRegistered.setVisibility(View.VISIBLE);
        btnSignInIf.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.btnFinalRegister)
    public void onBtnFinalRegisterClicked() {
        email = etUserName.getText().toString();
        password = etPassword.getText().toString();
        firstName = etFirstName.getText().toString();
        lastName = etLastName.getText().toString();
        if (!email.isEmpty() && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty()) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("מבצע רישום...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    final FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest change = new UserProfileChangeRequest.Builder().setDisplayName(firstName + " " + lastName).build();
                    if (user != null) {
                        user.updateProfile(change).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog.dismiss();
                                        goToMain();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    public void goToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.btnSignInIf)
    public void onBtnSignInIfClicked() {
        btnSignIn.animate().alpha(100).rotation(360);
        btnRegister.setVisibility(View.VISIBLE);
        btnRegister.animate().alpha(100).rotation(360);
        btnSignIn.setVisibility(View.VISIBLE);
        hideRegisterItems();
    }

    void hideRegisterItems() {
        tvRegDetailsTitle.setVisibility(View.GONE);
        etFirstName.setVisibility(View.GONE);
        tilFirstName.setVisibility(View.GONE);
        etLastName.setVisibility(View.GONE);
        tilLastName.setVisibility(View.GONE);
        btnFinalRegister.setVisibility(View.GONE);
        tvIfRegistered.setVisibility(View.GONE);
        btnSignInIf.setVisibility(View.GONE);
    }
}
