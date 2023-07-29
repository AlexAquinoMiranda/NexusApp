package com.example.nexusapp.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                int destinationId = navController.getCurrentDestination().getId();
                switch (destinationId) {
                    case R.id.navegacionInicio:
                        toolbarText.setText("Nexus-App");
                        break;
                    case R.id.navegacionPaneles:
                        toolbarText.setText("Nueva Publicación");
                        break;
                    case R.id.navegacionPerfil:
                        toolbarText.setText(Login.usuarioAutenticado.getNombreUsuario());
                        break;
                    case R.id.navegacionBusqueda:
                        toolbarText.setText("Búsqueda de usuarios");
                        break;
                    default:
                        toolbarText.setText("Nexus-App");
                        break;
                }
                  /**  int currentFragmentId = navController.getCurrentDestination().getId();

                    // Comparar con el ID de la ventana que deseas verificar
                    if (currentFragmentId == R.id.navegacionBusqueda) {

                    } else {
                        navController.navigate(R.id.navegacionBusqueda);
                        // El NavController no está navegando a la ventana de búsqueda.
                        // Puedes realizar aquí otras acciones o comprobar otras condiciones si es necesario.
                    }*/
                    //   navController.navigate(R.id.navegacionBusqueda);

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

    }

    void ocultarNavegacion() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }



}