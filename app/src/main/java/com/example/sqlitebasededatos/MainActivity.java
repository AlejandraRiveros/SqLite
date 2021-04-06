package com.example.sqlitebasededatos;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    private EditText etDocumento;
    private EditText etUsuario;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etContra;
    private ListView listaUsuarios;
    public static final String AES ="AES";
    int documento;
    String usuario;
    String nombres;
    String apellidos;
    String contra;

    private GestionBD gestionBD;
    SQLiteDatabase baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.casteo();


    }

    public void  registrarUsuario(View view) {
        UsuarioDAO usuarioDAO = new UsuarioDAO(this, view);
        Usuario usuario = new Usuario();
        Encriptar encriptar = new  Encriptar();

        Usuario usuarioBD = this.buscarUsuario(view);

        if(usuarioBD.nombres != null){
            Toast.makeText(this, "El usuario ya existe " + usuarioBD.nombres, Toast.LENGTH_LONG).show();
            return;
        }

            if (validarCampos()) {

                usuario.nombres = this.nombres;
                usuario.apellidos = this.apellidos;
                usuario.contra = encriptar.encriptar(this.contra);
                usuario.documento = this.documento;
                usuario.usuario = this.usuario;
                usuarioDAO.insert(usuario);
                this.ListarUsuario();
                borrarCampos();
            }

        Toast.makeText(this, "Contra"+ usuario.contra, Toast.LENGTH_LONG).show();

    }


    public void ListarUsuario() {
        //Poo
        UsuarioDAO usuarioDAO = new UsuarioDAO(this, findViewById(R.id.lvLista));
        ArrayList<Usuario> usuarioList = usuarioDAO.getUsuarioList();
        ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>
                (this, android.R.layout.simple_list_item_1, usuarioList);
        listaUsuarios.setAdapter(adapter);

    }
    public void actualizarUsuario(View view) {
       UsuarioDAO usuarioDAO = new UsuarioDAO(this, view);
       Usuario usuario = new Usuario();
       Encriptar encriptar = new Encriptar();

        if (validarCampos()) {

            usuario.nombres = this.nombres;
            usuario.apellidos = this.apellidos;
            usuario.contra = encriptar.encriptar(this.contra);
            usuario.documento = this.documento;
            usuario.usuario = this.usuario;
            usuarioDAO.actualizarUsuario(usuario, this);
            this.ListarUsuario();
            borrarCampos();
        }
    }
    public void consultaUsuario(View view) {
        //No orientado a objetos
        ArrayList<String> lista = new ArrayList<>();
        gestionBD = new GestionBD(this);
        SQLiteDatabase db = gestionBD.getWritableDatabase();
        Cursor fila = db.rawQuery("select * from usuarios", null);
        if (fila.moveToFirst()) {
            do {
                lista.add(fila.getString(0)+ "| "+ fila.getString(1));
            } while (fila.moveToNext());
            db.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
            listaUsuarios.setAdapter(adapter);
        }
        borrarCampos();
    }

    public Usuario buscarUsuario(View view) {
        ArrayList<String> listaBuscar = new ArrayList<>();

        gestionBD = new GestionBD(this);
        SQLiteDatabase db = gestionBD.getWritableDatabase();
        Cursor fila = db.rawQuery("select * from usuarios WHERE USU_DOCUMENTO="+ Integer.parseInt(etDocumento.getText().toString()), null);
        Usuario usuario = new Usuario();
        if (fila.moveToFirst()) {
            do {
                listaBuscar.add(fila.getString(0)+ "| "+ fila.getString(1));
                usuario.documento = fila.getInt(0);
                usuario.nombres = fila.getString(1);
                usuario.apellidos = fila.getString(2);
                usuario.usuario = fila.getString(3);
                usuario.contra = fila.getString(4);
            } while (fila.moveToNext());
            db.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaBuscar);

            listaUsuarios.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No existe ese documento, intente nuevamente", Toast.LENGTH_LONG).show();
        }

        etDocumento.setText(""+usuario.documento);
        etNombres.setText( usuario.nombres);
        etApellidos.setText(usuario.apellidos);
        etUsuario.setText(usuario.usuario);
        etContra.setText(usuario.contra);
        borrarCampos();

        return usuario;
    }

    // Borrar campos
    public void borrarCampos() {
        etNombres.setText("");
        etContra.setText("");
        etUsuario.setText("");
        etUsuario.setText("");
        etDocumento.setText("");
        etApellidos.setText("");
    }


    //2. Validar campos
    private  boolean validarCampos() {
        documento = Integer.parseInt(etDocumento.getText().toString());
        nombres = etNombres.getText().toString();
        apellidos = etApellidos.getText().toString();
        usuario = etUsuario.getText().toString();
        contra = etContra.getText().toString();
        boolean respuesta;
        if ( nombres.isEmpty() || apellidos.isEmpty() || usuario.isEmpty()
                || contra.isEmpty() ) {
            Snackbar.make(findViewById(R.id.parent), "Campos vacios", Snackbar.LENGTH_SHORT);
            respuesta = false;
        } else respuesta = true;


        return respuesta;
    }

    //1. Casteo
    private void casteo() {
        etDocumento = (EditText)findViewById(R.id.etDocumento);
        etApellidos = (EditText)findViewById(R.id.etApellidos);
        etUsuario = (EditText)findViewById(R.id.etUsuario);
        etNombres = (EditText)findViewById(R.id.etNombres);
        etContra = (EditText)findViewById(R.id.etContra);
        listaUsuarios = (ListView)findViewById(R.id.lvLista);
        //Listado
    }
}