package com.example.nexusapp.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.nexusapp.R;
import com.example.nexusapp.databinding.ActivityMainBinding;
import com.example.nexusapp.proyectofinal.Contacto.Contacto;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.Notificacion.Notificaciones;

import com.example.nexusapp.proyectofinal.ui.PerfilUsuarios.PerfilDeUsuario;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity es la vista principal de la app el cual contiene la navegación de distintos
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static NavController navController;
    private ImageView notificaciones, chat/*, buscar*/;
    private TextView toolbarText;

    /**
     * Inicializar atributos
     */
    void inicializar() {
        chat = findViewById(R.id.chat);
        //buscar = findViewById(R.id.btnBuscar);
        toolbarText = findViewById(R.id.nombreUsuarioLocal);
        notificaciones = findViewById(R.id.notificaciones);
    }

    void prueba() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Suponiendo que tengas un fragment llamado MyFragment
        PerfilDeUsuario fragment = PerfilDeUsuario.newInstance();

        fragmentTransaction.replace(R.id.perfilDeUsuariosFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * listeners es un método en donde realiza acciones de botones
     * cuando estas se presionen
     */
    void listeners() {
        chat.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Contacto.class);
            startActivity(i);
            finish();
        });
        notificaciones.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), Notificaciones.class);
            startActivity(i);
            finish();
        });
        /*buscar.setOnClickListener(v -> {
            //prueba();
            Intent i = new Intent(getApplicationContext(), Busqueda.class);
           startActivity(i);
            finish();
        });*/
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navegacionInicio) {
                    toolbarText.setText("Nexus-App");
                } else if (destination.getId() == R.id.navegacionPaneles) {
                    toolbarText.setText("Nueva Publicación");
                } else if (destination.getId() == R.id.navegacionPerfil) {
                    toolbarText.setText(Login.usuarioAutenticado.getNombreUsuario());
                } else if (destination.getId() == R.id.navegacionBusqueda) {
                    toolbarText.setText("Busqueda de usuarios");
                    //   navController.navigate(R.id.navegacionBusqueda);
                } else {
                    toolbarText.setText("Nexus-App");
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ocultarNavegacion();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        inicializar();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navegacionInicio, R.id.navegacionPaneles, R.id.navegacionPerfil)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupWithNavController(binding.navView, navController);
        listeners();
/**
 View decorView = getWindow().getDecorView();
 int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
 | View.SYSTEM_UI_FLAG_FULLSCREEN;
 decorView.setSystemUiVisibility(uiOptions);**/
    }
    void ocultarNavegacion(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

}