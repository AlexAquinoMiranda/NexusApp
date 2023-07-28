package com.example.nexusapp.proyectofinal.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nexusapp.R;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseController;
import com.example.nexusapp.proyectofinal.MainActivity;

import com.example.nexusapp.proyectofinal.Registrar.Registrar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Login es un activity en donde el usuario se autentica para entrar a la app
 */
public class Login extends AppCompatActivity {

    private EditText user, password;
    private TextView linkRegistrar;
    private Button login;
    private FirebaseController firebase;

    public static Usuario usuarioAutenticado;

    public static ArrayList<Usuario> getUsers() {
        return users;
    }

    private static ArrayList<Usuario> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ocultarNavegacion();
        inicializar();
        listeners();
    }
    void ocultarNavegacion(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * Autenticar usuario es un método que recibe el correo y email y comprueba si existe usuarios con esos datos
     *
     * @param email     email del usuario
     * @param passwordd password del usuario
     */
    void autenticarUsuario(String email, String passwordd) {

        firebase.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Login.getUsers().clear();
                for (DataSnapshot element : snapshot.getChildren()) {
                    Usuario userAux = element.getValue(Usuario.class);
                    Login.users.add(userAux);
                }
                Optional<Usuario> usuario = users.stream()
                        .filter(user_aux -> (user_aux.getEmail().equals(email)) && user_aux.getPassword().equals(passwordd))
                        .findFirst();

                if (usuario.isPresent()) {
                    System.out.println("\t ----datos correctos-------");
                    //Publicationes.userglobal = usuario.get();
                    usuarioAutenticado = users.stream()
                            .filter(user_aux -> (user_aux.getNombre().equals(email) || user_aux.getEmail().equals(email)) && user_aux.getPassword().equals(passwordd))
                            .findFirst().get();

                    System.out.println(usuarioAutenticado.toString());
                    abrirVentana(MainActivity.class);
                } else {
                    Toast.makeText(getApplicationContext(), "Credenciales Incorrecta. Ingréselas nuevamente", Toast.LENGTH_SHORT).show();
                    user.setText("");
                    password.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * listeners es un método en donde realiza acciones de botones
     * cuando estas se presionen
     */
    void listeners() {
        //link para registrarse.
        this.linkRegistrar.setOnClickListener(v -> {
            abrirVentana(Registrar.class);
        });
        // autenticar usuario.
        this.login.setOnClickListener(v -> {
            String email, pass;
            email = String.valueOf(user.getText().toString());
            pass = String.valueOf(password.getText().toString());
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) return;
            autenticarUsuario(email, pass);
        });
    }

    /**
     * inicializar atributos
     */
    void inicializar() {
        this.login = (Button) findViewById(R.id.Login_btn);
        this.user = findViewById(R.id.crearNombreEvento);
        this.password = findViewById(R.id.password);
        this.linkRegistrar = findViewById(R.id.signup);
        firebase = new FirebaseController("Usuarios", getApplicationContext());
    }

    /**
     * abrirVentana un método que recibe una clase para abrirla posteriormente.
     *
     * @param view
     */
    void abrirVentana(Class view) {
        Intent i = new Intent(getApplicationContext(), view);
        startActivity(i);
        finish();
    }

    /**
     * Metodo que detecta si se pulsa botón hacia Atras
     */
    @Override
    public void onBackPressed() {
        abrirVentana(Login.class);
       // posicionActual = 0; // Inicializar la variable a 0
        System.out.println("Botón atrasssssss");
        super.onBackPressed();
    }

}