package com.example.nexusapp.proyectofinal.Configuracion;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudUser;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.R;

public class Ajustes extends AppCompatActivity {

    private ImageView btnAtras;
    private EditText contraseña, repetirContraseña, cambiarGmail;
    private TextView cerrarSesion, usuario, gmail;
    private Button btnGuardar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracion);
        ocultarNavegacion();

        inicializar();
        rellenarDatos();
        listeners();

    }
    void ocultarNavegacion(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    void rellenarDatos() {
        usuario.setText(Login.usuarioAutenticado.getNombreUsuario());
        gmail.setText(Login.usuarioAutenticado.getEmail());
    }

    /**
     * Guarda los cambios realizados en el perfil del usuario autenticado.
     * Crea un nuevo objeto de usuario con los datos actualizados, incluyendo la URL de la imagen de perfil.
     * Utiliza FirebaseCrudUser para modificar los datos del usuario en la base de datos.
     * @param urlImagen La URL de la imagen de perfil actualizada.
     */
    void guardarCambios(String urlImagen) {

        Usuario usuario = new Usuario();
        usuario.setUid(Login.usuarioAutenticado.getUid());
        usuario.setNombreUsuario(Login.usuarioAutenticado.getNombreUsuario());
        usuario.setApellido(Login.usuarioAutenticado.getApellido());
        usuario.setNombre(Login.usuarioAutenticado.getNombre());
        usuario.setPassword(contraseña.getText().toString());
        usuario.setEmail(cambiarGmail.getText().toString());
        usuario.setPerfil(urlImagen);
        usuario.setDescripcion(Login.usuarioAutenticado.getDescripcion());

        new FirebaseCrudUser(getApplicationContext()).modify(usuario, usuario.getUid());
    }
    void inicializar() {

        btnAtras = findViewById(R.id.atrasConfig);
        usuario = findViewById(R.id.usuarioConfiguracion);
        gmail = findViewById(R.id.gmailConfiguracion);
        cerrarSesion = findViewById(R.id.cerrarSesion);
        contraseña = findViewById(R.id.nuevaContraseña);
        repetirContraseña = findViewById(R.id.descripcionPerfil);
        cambiarGmail = findViewById(R.id.nuevoGmail);
        btnGuardar = findViewById(R.id.btnGuardarConfig);

    }
    void listeners() {
        btnAtras.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });

        cerrarSesion.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Login.class);
            startActivity(i);
        });

        btnGuardar.setOnClickListener(v -> {
            validarCampos();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });
    }
    void abrirVentana(Class view) {
        Intent i = new Intent(getApplicationContext(), view);
        startActivity(i);
        finish();
    }
    /**
     * Valida los campos de contraseña, repetir contraseña y cambiar Gmail.
     * Si alguno de los campos está vacío, no se realiza ninguna acción.
     * Si todos los campos están completos, se guarda los cambios en el perfil del usuario autenticado y se abre la ventana principal.
     */
    void validarCampos() {

        if (TextUtils.isEmpty(contraseña.getText()) || TextUtils.isEmpty(repetirContraseña.getText())||TextUtils.isEmpty(cambiarGmail.getText())) {
            return;
        }else {
            guardarCambios(Login.usuarioAutenticado.getPerfil());
            abrirVentana(MainActivity.class);
        }
    }

}
