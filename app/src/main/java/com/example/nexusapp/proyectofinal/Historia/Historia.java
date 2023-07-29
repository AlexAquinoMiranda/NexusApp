package com.example.nexusapp.proyectofinal.Historia;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudHistoriaDisponible;
import com.example.nexusapp.proyectofinal.Firebase.CargarImagenGlide;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCargaImg;
import com.example.nexusapp.proyectofinal.Firebase.FirebaseCrudHistoriaDisponible;
import com.example.nexusapp.proyectofinal.Login.Login;
import com.example.nexusapp.proyectofinal.MainActivity;
import com.example.nexusapp.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Historia es una clase en donde se visualiza todas las historias del usuario
 * <p>
 * todo: hacer que se elimine la historia pasada 24 horas.
 * <p>
 * todo: calcular guardando fecha mes/día/hora/seg. y  comparar si es mayor que la fecha actual del sistema.
 */
public class Historia extends AppCompatActivity {
    private View siguienteHistoria, retrocederHistoria;
    LinearLayout progressBarContainer;
    private ImageView fotoHistoria, like;
    private TextView nombreUsuario;
    private CircleImageView perfil;
    private static String imagenActual;
    public static int posicionActual = 0;
    public static com.example.nexusapp.proyectofinal.DTO.Historia historiaVisualizar;
    public static ArrayList<String> historias;
    private Set<String> claves;

    private Map<String, Long> datoHistorias;
    private ProgressBar progressBar;
    private ProgressBar progresoCargaImagen;
    private CountDownTimer countDownTimer;
    private FirebaseCrudHistoriaDisponible firebaseCrudHistoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);
        ocultarNavegacion();
        inicializar();

        // cargarProgresos();

        listeners();
        //   progresoHistoria();
    }

    void ocultarNavegacion() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }


    /**
     * inicializar atributos
     */
    void inicializar() {
        siguienteHistoria = findViewById(R.id.historiaDelante);
        retrocederHistoria = findViewById(R.id.historiaAtras);
        fotoHistoria = findViewById(R.id.imagenHistoria);
        perfil = findViewById(R.id.perfilUsuarioHistoria);
        nombreUsuario = findViewById(R.id.Historia_NombreUsuario);
        like = findViewById(R.id.deleteHistory);
        historias = new ArrayList<>();
        progressBar = findViewById(R.id.progressBarHistoria);
        progresoCargaImagen = findViewById(R.id.progresoCargaImagen);

        progressBarContainer = findViewById(R.id.linearProgress);

        firebaseCrudHistoria = new FirebaseCrudHistoriaDisponible(getApplicationContext());

        if (historiaVisualizar.getIdUsuario().equals(Login.usuarioAutenticado.getUid())) {
            like.setVisibility(View.VISIBLE);
        }
        progresoHistoria();

    }

    /**
     * listeners es un método que espera eventos de views para realizar acciones
     */
    void listeners() {
        //iniciar en la primera historia
        siguienteHistoria.setOnClickListener(v -> {
            historiaDelante();

            System.out.println("hacia adelanteeeee");
        });
        retrocederHistoria.setOnClickListener(v -> {
            historiaAtras();
            System.out.println("hacia Atrasssss");
        });
        like.setOnClickListener(v -> {
            eliminarHistoria();
            Historia.posicionActual=0;
        });

        visualizarHistoria();
        cargarImagenes();
    }

    /**
     * Metodo que elimina una historia
     */
    private void eliminarHistoria() {
        System.out.println("SE HA ELIMINADOOOOOOOOOO");
        historiaVisualizar.getFotos().remove(historias.get(posicionActual));
        this.datoHistorias.remove(historias.get(posicionActual));
        if (historiaVisualizar != null && historiaVisualizar.getFotos().isEmpty()) {
            this.firebaseCrudHistoria.delete(historiaVisualizar.getUidHistoria());
            posicionActual = 0;
        } else {
            firebaseCrudHistoria.modify(historiaVisualizar, historiaVisualizar.getUidHistoria());
            posicionActual = 0;
            historiaDelante();
        }
        if( historiaVisualizar.getFotos().isEmpty()){
            posicionActual = 0;
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }


    }

    /**
     * progresoHistoria es un metodo encargado del progressBar en donde cuenta
     * si la historia duró 15 segundos y pasar al siguente
     */
    void progresoHistoria() {
        int totalTime = 2000; // Tiempo total en milisegundos
        //actualización en milisegundos
        progressBar.setProgress(0);
        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished / 1000);
                progressBar.setProgress(progress);

            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                historiaDelante();
                System.out.println("se mueve por is soloooo");
                // Tarea completada
            }
        };

        countDownTimer.start();
        if (progressBar.getProgress() == totalTime) {
            historiaDelante();
            System.out.println("se mueve por is soloooo");
        }
    }

    /**
     * Metodo para pasar a la siguiente historia
     */
    void historiaDelante() {
        if (posicionActual < historias.size() - 1) {
            posicionActual++;
            cargarImagenes();
            progressBar.setProgress(0);
            progresoHistoria();
        } else {
            posicionActual = 0;
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }
    /**
     * Método para retroceder la historia
     */
    void historiaAtras() {
        if (posicionActual > 0) {
            posicionActual--;
            cargarImagenes();
            progressBar.setProgress(0);
            progresoHistoria();
        }
    }

    /**
     * Comprobar si ha pasado un día para eliminar historia
     * @param fechaAlmacenada fecha almacenada de la historia
     * @return true si es mayor de 1 día
     */
    private boolean esPasadoUnDia(long fechaAlmacenada) {
        long fechaActual = System.currentTimeMillis(); // Obtén la fecha actual en milisegundos
        long milisegundosEnUnDia = 24 * 60 * 60 * 1000; // Cantidad de milisegundos en un día

        long diferenciaEnMilisegundos = fechaActual - fechaAlmacenada; // Calcula la diferencia en milisegundos

        return diferenciaEnMilisegundos >= milisegundosEnUnDia;
    }

    /**
     * Método que visualiza los datos actualizados de la historia
     */
    void visualizarHistoria() {

        LocalDateTime fechaHoraActual = LocalDateTime.now();
        // Crear el patrón para obtener día, hora y minuto
        String patron = "ddHHmm";
        // Crear el objeto DateTimeFormatter
        DateTimeFormatter formato = DateTimeFormatter.ofPattern(patron);
        // Formatear la fecha y hora actual usando el patrón y el formato
        String fechaHoraFormateada = fechaHoraActual.format(formato);

        if (historiaVisualizar != null && historiaVisualizar.getFotos() != null && !historiaVisualizar.getFotos().isEmpty()) {
            datoHistorias = historiaVisualizar.getFotos();//obtengo mi mapa

            long HoraDeInicioHistoria = historiaVisualizar.getTiempoInicio();
            long horaActual = Long.parseLong(fechaHoraFormateada);
            //////
            boolean haPasadoUnDia = esPasadoUnDia(HoraDeInicioHistoria);

            if (haPasadoUnDia) {
                // Ha pasado un día completo
                System.out.println("Ha pasado un día completo desde la fecha almacenada");
               // this.eliminarHistoria();
            } else {
                // No ha pasado un día completo
                System.out.println("No ha pasado un día completo desde la fecha almacenada");
            }
            progresoCargaImagen.setVisibility(View.VISIBLE);

            new FirebaseCargaImg(historiaVisualizar.getPerfilUsuario(), perfil, getApplicationContext());//muestro la historia
            nombreUsuario.setText(historiaVisualizar.getUsuarioNombre());
            claves = historiaVisualizar.getFotos().keySet();
            historias.addAll(claves);
            for (String f : claves) {
                System.out.println(f);
            }


        } else {
            System.out.println("error Eliminar ------");

            if (historiaVisualizar != null && historiaVisualizar.getFotos().isEmpty()) {
                this.firebaseCrudHistoria.delete(historiaVisualizar.getUidHistoria());
                posicionActual = 0;
            }
        }
    }

    /**
     * cargarImagenes es un método encargado de mostrar toda la lista de historias que tiene un usuario
     */
    void cargarImagenes() {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        // Crear el patrón para obtener día, hora y minuto
        String patron = "ddHHmm";
        // Crear el objeto DateTimeFormatter
        DateTimeFormatter formato = DateTimeFormatter.ofPattern(patron);
        // Formatear la fecha y hora actual usando el patrón y el formato
        String fechaHoraFormateada = fechaHoraActual.format(formato);

        if (historias != null && !historias.isEmpty()) {
            if (posicionActual > historias.size()) {
                return;
            }
            String foto = "";
            try {
                 foto = historias.get(posicionActual);//obtengo la foto.
            }catch (IndexOutOfBoundsException e){
                //todo
                posicionActual = 0;
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }

            Long finaliza = this.datoHistorias.get(historias.get(posicionActual));//obtengo de fecha de la imagen.
            System.out.println("\n foto a mostrar \n\n " + foto);
            long horaActual = Long.parseLong(fechaHoraFormateada);
            if (finaliza != null) {
                if (horaActual >= finaliza) {
                    System.out.println("finaliza la historia............");
                } else {
                    System.out.println(horaActual + "  es la hora actiual, la historia. termina a " + finaliza + "...........");
                }
            }

            new CargarImagenGlide(foto, fotoHistoria, progresoCargaImagen, getApplicationContext());
            System.out.print("posicion actual de la i,magen es " + posicionActual + " la lista es de longitud" + historias.size());

        } else {
            System.out.println("VACIOOOO---------");
        }
    }


    /**
     * Metodo que detecta si se pulsa botón hacia Atras
     */
    @Override
    public void onBackPressed() {
        posicionActual = 0; // Inicializar la variable a 0
        System.out.println("Botón atrasssssss");
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detener el CountDownTimer si la actividad se destruye
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}