package com.example.nexusapp.proyectofinal.Inicio;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.R;

public class Inicio extends AppCompatActivity {

    private ImageView logoNexus;
    private ObjectAnimator objetoAnimado, objetoX, objetoY;
    private AnimatorSet animador, animadorRegreso, animadorFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_principal);
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

    void inicializar() {

        logoNexus = findViewById(R.id.logoNexus);

        // Crear el objeto ObjectAnimator para la animación
//        objetoAnimado = ObjectAnimator.ofFloat(logoNexus, "rotation", 0f, 360f);
//        objetoAnimado.setDuration(2000); // Duración de la animación en milisegundos
//        objetoAnimado.setInterpolator(new LinearInterpolator()); // Interpolador lineal para movimiento uniforme
        objetoAnimado = ObjectAnimator.ofFloat(logoNexus, "rotation", 0f, 360f);
        objetoAnimado.setDuration(1000); // Duración de la animación en milisegundos
        objetoAnimado.setInterpolator(new LinearInterpolator());


        // Crear un objeto AnimatorSet para ejecutar la animación
        animador = new AnimatorSet();
        animador.playTogether(objetoAnimado);
        animador.addListener(new Animator.AnimatorListener() {
                                 @Override
                                 public void onAnimationStart(@NonNull Animator animator) {

                                 }

                                 @Override
                                 public void onAnimationEnd(@NonNull Animator animator) {
                                     animarLogo(logoNexus);


                                 }

                                 @Override
                                 public void onAnimationCancel(@NonNull Animator animator) {

                                 }

                                 @Override
                                 public void onAnimationRepeat(@NonNull Animator animator) {

                                 }
        });

        // Iniciar la animación
        animador.start();

        }
    void listeners(){

        logoNexus.setOnClickListener(v -> {
            abrirVentana(Login.class);
        });
    }

    /**
     * Realiza una animación en el logo mediante la reducción de tamaño seguida de una animación de regreso al tamaño original.
     * Después de la animación de regreso, inicia una nueva actividad y, opcionalmente, finaliza la actividad actual.
     * @param view La vista del logo que se animará.
     */
    public void animarLogo(View view) {
        // Crear un objeto ObjectAnimator para escalar en el eje X
        objetoX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.5f);
        objetoX.setDuration(200); // Duración de la animación en milisegundos

        // Crear un objeto ObjectAnimator para escalar en el eje Y
        objetoY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.5f);
        objetoY.setDuration(200); // Duración de la animación en milisegundos

        // Crear un objeto AnimatorSet para ejecutar las animaciones en secuencia
        animadorFinal = new AnimatorSet();
        animadorFinal.playSequentially(objetoX, objetoY);

        // Iniciar la animación de reducción de tamaño
        animadorFinal.start();

        // Crear otro AnimatorSet para la animación de regreso al tamaño original
        animadorRegreso = new AnimatorSet();
        animadorRegreso.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f)
        );
        animadorRegreso.setDuration(200); // Duración de la animación en milisegundos
        animadorRegreso.setStartDelay(200); // Retraso antes de iniciar la animación de regreso
        animadorRegreso.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
            }
            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                Intent intent = new Intent(Inicio.this, Login.class);
                startActivity(intent);
                finish(); // Opcional: finalizar la actividad actual después de iniciar la nueva actividad
            }
            @Override
            public void onAnimationCancel(@NonNull Animator animator) {
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {
            }
        });
        // Iniciar la animación de regreso al tamaño original después de la animación de reducción de tamaño
        animadorRegreso.start();
    }
    void abrirVentana(Class view) {
        Intent i = new Intent(getApplicationContext(), view);
        startActivity(i);
        finish();
    }
}
