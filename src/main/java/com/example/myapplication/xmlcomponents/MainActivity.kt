package com.example.myapplication.xmlcomponents

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapplication.xmlcomponents.databinding.ActivityMainBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var biding: ActivityMainBinding

    @OptIn(ExperimentalBadgeUtils::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(biding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(biding.bottonAppBar)


// con estas acciones el FloatingActionButton cambia de posicion y se restablece posteriormente
        biding.fab.setOnClickListener {
            if (biding.bottonAppBar.fabAlignmentMode == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
                biding.bottonAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            } else {
                biding.bottonAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            }
        }

        //trabajando con el boton menu: verificando que este conectado con el MainActivity
        biding.bottonAppBar.setNavigationOnClickListener {
            Log.i("Curso Kotlin", "onCreate: NavIcon")
            Snackbar.make(biding.root, "Evento Exitoso", Snackbar.LENGTH_SHORT)
                .setAnchorView((biding.fab))
                .show()
        }
        biding.content.comprar.setOnClickListener {
            Snackbar.make(biding.root, "Comprado", Snackbar.LENGTH_INDEFINITE)
                .setAction("ir") {
                    Toast.makeText(this, "Historial", Toast.LENGTH_SHORT).show()
                }
                .setAnchorView(biding.fab)
                .show()
        }

        // biding permite gestional todos los archivos que tenga un identificador tipo id
        // los centraliza para mas comodidad
        //este snackbar ya estaba hecho arriba
//        biding.content.comprar.setOnClickListener {
//            Toast.makeText(this, "Historial", Toast.LENGTH_SHORT).show()
//        }

        biding.content.btnSkip.setOnClickListener {
            findViewById<MaterialCardView>(R.id.cvAd).visibility = View.GONE
        }

        // Gestion para traer una imagen desde internet
        val url: String =
            "https://blog.sosafeapp.com/content/images/size/w2000/2020/04/blog---mi-primera-app-en-Kotlin.png"
        Glide.with(this)//Esta librería (Glide) permite gestionar la carga de imagenes desde internnet
            .load(url) //
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // Gestion de la cache en el dispositivo,
            // para no volver a cargar la img o que este disponible cuando no haya inet
            .placeholder(R.drawable.ic_sharp) // Muestra un icono mientras carga la image de la url
            .error(R.drawable.ic_off) // Icono en el caso de un error, falta interner por ejemplo
            .centerCrop() // Adapta la imagen al contenedor
            .into(biding.content.imgUrl)// Organiza la img con biding desde inet


        //esta funcionalidad permitiría cambiar el color de fondo del textField
        // al hacer foco, pero no resulto y el codigo es el mismo del ejemplo
        biding.content.tilEmail.onFocusChangeListener =
            View.OnFocusChangeListener { view, focused ->
                if (focused) {
                    biding.content.tilEmail.setBackgroundColor(Color.CYAN)
                } else {
                    biding.content.tilEmail.setBackgroundColor(Color.WHITE)
                }
            }
        biding.content.tfUrl.addTextChangedListener {
            var errorStr: String? = null
            val url = biding.content.tfUrl.text.toString()
            when {
                url.isEmpty() -> {
                    errorStr = "Required"
                }

                URLUtil.isValidUrl(url) -> {
                    loadImage(url)
                }

                else -> {
                    errorStr = "Invalid URL"
                }
            }
            biding.content.tilUrl.error = errorStr
        }
        // Despues de habilitar el boton en xml, aca se realiza la funcionalidad de habilitar o desabilitar
        // el boton, que permite escribir la paa en el campo correspondiente

        biding.content.cbEnablePassword.setOnClickListener {
            biding.content.tilpass.isEnabled = !biding.content.tilpass.isEnabled
        }

        //esta funcionalidad permite a traves del switch mostrar u ocultar el boton flotante (floating action button)
        biding.content.swFab.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                button.text = "Hide Fab"
                biding.fab.show()
            } else {
                button.text = "Show Fab"
                biding.fab.hide()
            }
        }
        biding.content.cpEmail.setOnCheckedChangeListener { chip, isChecked ->
            Toast.makeText(this, chip.text.toString(), Toast.LENGTH_SHORT).show()
        }
        biding.content.cpEmail.setOnCloseIconClickListener {
            biding.content.cpEmail.visibility = View.GONE
        }

        biding.content.slVol.addOnChangeListener { slider, value, fromUser ->
            biding.content.cpEmail.text = "vol : $value"
        }
        biding.content.btgColors.addOnButtonCheckedListener { group, checkedId, isCheked ->
            biding.root.setBackgroundColor(
                when (checkedId) {
                    R.id.btnRed -> Color.RED
                    R.id.btnBlue -> Color.BLUE
                    else -> Color.GREEN
                }
            )
        }
        val badge = BadgeDrawable.create(this)// estas lineas y codigos, crean la "insignia" sobre
        // las notificaciones, que se acutalizaran cuando los listener les indiquen que todo esta listo
        badge.number = 21
        biding.content.containerNotifications.foreground = badge
        biding.content.containerNotifications.addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            BadgeUtils.attachBadgeDrawable(
                badge,
                biding.content.idNotifications,
                biding.content.containerNotifications
            )
        }


        //ventanas de dialogo de las notificaciones, perimite aceptar, cancelar o saltar la ventana
        biding.content.idNotifications.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Notifications")
                .setMessage("Today Activity: ")
                .setNeutralButton("Skip") { dialog, which ->
                    Log.i("CursoKotlin", "Saltar")
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    Log.i("CursoKotlin", "Cancelar")
                }
                .setPositiveButton("OK") { dialog, which ->
                    Log.i("CursoKotlin", "Aceptar")
                }
                .show()
        }

    }

    private fun loadImage(
        url: String = "https://blog.sosafeapp.com/content/images/size/w2000/2020/04/blog---mi-primera-app-en-Kotlin.png"
    ) {
        Glide.with(this)//esta libreria (Glide) permite gestionar la carga de imagenes desde internnet
            .load(url) //
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE) // gestion de la cache en el dispositivo,
            // para no volver a cargar la img o que este disponible cuando no haya inet
            .placeholder(R.drawable.ic_sharp) // muestra un icono mientras carga la image de la url
            .error(R.drawable.ic_off) // icono en el caso de un error, falta interner por ejemplo
            .centerCrop() // adapta la imagen al contenedor
            .into(biding.content.imgUrl)// organiza la img con biding desde inet
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_exit -> {
                Toast.makeText(this, "Hasta Pronto", Toast.LENGTH_SHORT).show()
                finish()
                true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

}