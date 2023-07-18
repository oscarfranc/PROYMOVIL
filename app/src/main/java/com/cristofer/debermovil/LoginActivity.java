package com.cristofer.debermovil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText mEditTextEmail;
    EditText mEditTextPass;
    Button mButtonInicio;
    TextView mTextViewRespuesta;

    FirebaseAuth mAuth;

    private String email;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextEmail=findViewById(R.id.editTextEmail);
        mEditTextPass=findViewById(R.id.editTextPassword);
        mButtonInicio=findViewById(R.id.btnEntrar);
        mTextViewRespuesta=findViewById(R.id.textViewRespuesta);

        mAuth = FirebaseAuth.getInstance();

        mButtonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTextEmail.getText().toString().trim();
                pass = mEditTextPass.getText().toString().trim();

                if (email.isEmpty() || pass.isEmpty()){
                    mTextViewRespuesta.setText("Ingrese el correo y la contraaseña");
                    mTextViewRespuesta.setTextColor(Color.RED);
                }
                else{
                    if (emailValido(email)){
                        mAuth.signInWithEmailAndPassword(email, pass).addOnCanceledListener(new OnCompleteListener<AuthResult>{
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task){
                                if (task.isSuccessful()){
                                    mTextViewRespuesta.setText("INICIO CORRECTO");
                                    mTextViewRespuesta.setTextColor(Color.BLUE);
                                }
                                else {
                                    mTextViewRespuesta.setText("CONTRASEÑA INCORRECTA");
                                    mTextViewRespuesta.setTextColor(Color.RED);
                                }
                            }
                        })
                    }
                    else{
                        mTextViewRespuesta.setText("Email no valido");
                        mTextViewRespuesta.setTextColor(Color.RED);
                    }
                }
            }
        });
    }

    protected void onStart(){
        super.onStart();
        FirebaseUser usuario = mAuth.getCurrentUser();
        if (usuario != null){
            irHome();
        }
    }

    private void irHome(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean emailValido(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}