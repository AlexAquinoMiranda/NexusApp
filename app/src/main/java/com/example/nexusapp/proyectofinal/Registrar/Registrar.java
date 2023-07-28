package com.example.nexusapp.proyectofinal.Registrar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nexusapp.proyectofinal.DTO.Usuario;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudUser;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.R;

import java.util.UUID;

/**
 * Registrar es una clase que recibe datos de un nuevo usuario para crearlos y guardar en firebase
 */
public class Registrar extends AppCompatActivity {
    private EditText username, nombre, apellido, password, email;
    private Button btnCrear;
    private TextView link;

    private FirebaseCrudUser firebaseCrudUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        ocultarNavegacion();
        inicializar();
        listeners();

    }
    /**
     * Método que oculta los botones que tiene Android por defecto para la navegacion
     */
    void ocultarNavegacion(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * Método para inicializar atributos
     */
    void inicializar() {
        this.username = findViewById(R.id.nombreUsuario);
        this.nombre = findViewById(R.id.nombre);
        this.apellido = findViewById(R.id.apellidos);
        this.password = findViewById(R.id.contraseñaRegistro);
        this.email = findViewById(R.id.emailRegistro);
        this.btnCrear = findViewById(R.id.btnRegistrar);
        this.link = findViewById(R.id.iniciarSesion);
        firebaseCrudUser = new FirebaseCrudUser(getApplicationContext());
    }

    /**
     * Método que espera escucha de click de botones para realizar acciones
     */
    void listeners() {
        link.setOnClickListener(v -> {
            abrirVentana(Login.class);
        });
        this.btnCrear.setOnClickListener(v -> {
            validateData();
        });
    }

    /**
     * método que vacía entrada de texto de usuario
     */
    void limpiar() {
        this.username.setText("");
        this.apellido.setText("");
        this.nombre.setText("");
        this.password.setText("");
        this.email.setText("");
    }

    /**
     * validarExistenciaUsuario es un metodo que comprueba que no exista previamente usuarios con mismos datos.
     * todo: HACER FUNCIONALIDAD
     */
    void validarExistenciaUsuario() {
        if (email.getText().toString().equals("") && username.getText().toString().equals("")) {
            System.out.println("ingrese nuevas credenciales o inicie sesión con su cuenta");
        }
    }

    /**
     * método que valida que todos los datos esten rellenos y crea usuario.
     */
    void validateData() {
        if (this.username.getText().toString().equals("") || apellido.getText().toString().equals("")
                || nombre.getText().toString().equals("") || password.getText().toString().equals("") &&
                email.getText().toString().equals("")) {
            //datas vacías
            limpiar();
            Toast.makeText(getApplicationContext(), "Registro incorrecto. rellena todos los campos", Toast.LENGTH_LONG).show();
            return;
        } else {
            if(validarCorreoElectronico(email.getText().toString()) &&
                    validarContrasena(password.getText().toString()) && validarNombreUsuario(username.getText().toString())){

                crearUser();
                Toast.makeText(getApplicationContext(), "Registro correcto. Logueate con tu usuario", Toast.LENGTH_LONG).show();
                //abrirLogin();
                abrirVentana(Login.class);

            }else{
                if (!validarCorreoElectronico(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Correo electrónico incorrecto.", Toast.LENGTH_LONG).show();
                }
                if (!validarContrasena(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta. \nIntroduce al menos:\n " +
                            "-8 carácteres.\n" +
                            "-1 Mayuscula y miniscula.\n" +
                            "-1 Número", Toast.LENGTH_LONG).show();
                }
                if (!validarNombreUsuario(username.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Nombre de usuario incorrecto. Debe tener almenos 5 carácteres.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * crearUser es un metodo que rellena los datos en un pojo de Usuario y lo agrega a firebase.
     */
    void crearUser() {
        Usuario usuario = new Usuario();
        usuario.setUid(UUID.randomUUID().toString());
        usuario.setNombreUsuario(this.username.getText().toString());
        usuario.setApellido(this.apellido.getText().toString());
        usuario.setNombre(this.nombre.getText().toString());
        usuario.setPassword(this.password.getText().toString());
        usuario.setEmail(this.email.getText().toString());
        usuario.setPerfil("por_defecto.png");
        usuario.setDescripcion("");
        for (Usuario u : Login.getUsers()) {
            usuario.addSeguidor(u.getUid(), false);
        }
        this.firebaseCrudUser.create(usuario);
    }

    /**
     * validarCorreoElectronico un método que verifica si se cumple las condiciones del email.
     * @param email
     */
    public boolean validarCorreoElectronico(String email) {

        // Verificar si el correo coincide con el patrón de una dirección de correo electrónico
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.endsWith("@gmail.com");
    }
    /**
     * validarCorreoElectronico un método que verifica si se cumple las condiciones de la contraseña.
     * @param contrasena
     */
    public boolean validarContrasena(String contrasena) {
        // Verificar si la contraseña cumple con los requisitos
        // Mínimo 8 caracteres
        // Al menos una letra mayúscula, una letra minúscula y un número
        String patron = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
        return contrasena.matches(patron);
    }
    /**
     * validarCorreoElectronico un método que verifica si se cumple las condiciones del nombre del usuario.
     * @param nombreUsuario
     */
    public boolean validarNombreUsuario(String nombreUsuario) {
        // Verificar si el nombre de usuario tiene al menos 5 caracteres
        return nombreUsuario.length() >= 5;
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
        abrirVentana(Registrar.class);
   //     posicionActual = 0; // Inicializar la variable a 0
        System.out.println("Botón atrasssssss");
        super.onBackPressed();
    }

}