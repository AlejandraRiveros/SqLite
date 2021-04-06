    package com.example.sqlitebasededatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class UsuarioDAO {
    private GestionBD gestionBD;
    Context context;
    View view;

    public UsuarioDAO(Context context, View view) {
        this.context = context;
        this.view = view;
        gestionBD = new GestionBD(this.context);
    }
    // Metodo para insertar en la BD en la tabla usuarios

    public void insert(Usuario usuario) {
        try {
            SQLiteDatabase db = gestionBD.getWritableDatabase();
            if (db != null) {
                ContentValues values = new ContentValues();
                values.put("USU_DOCUMENTO", usuario.documento);
                values.put("USU_NOMBRES", usuario.nombres);
                values.put("USU_APELLIDOS", usuario.apellidos);
                values.put("USU_USUARIO", usuario.usuario);
                values.put("USU_CONTRA", usuario.contra);
                long respuesta = db.insert("usuarios", null, values);
                Snackbar.make(this.view, "Se ha registrado un usuario" + respuesta, Snackbar.LENGTH_LONG);
                db.close();
            } else {
                Snackbar.make(this.view, "No se puede registrar datos", Snackbar.LENGTH_LONG);
            }
        } catch (SQLException sqlException) {
            Log.i( "Error ", "" + sqlException);
        }
    }

    public ArrayList<Usuario> getUsuarioList() {
        SQLiteDatabase db = gestionBD.getReadableDatabase();
        String query = "select * from usuarios";
        ArrayList<Usuario> usuarioArrayList = new ArrayList<Usuario>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.documento = cursor.getInt(0);
                usuario.nombres = cursor.getString(1);
                usuario.apellidos = cursor.getString(2);
                usuario.usuario = cursor.getString(3);
                usuario.contra = cursor.getString(4);
                usuarioArrayList.add(usuario);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return usuarioArrayList;
    }
    public void actualizarUsuario(Usuario usuario, Context context) {
        try {


            SQLiteDatabase db = gestionBD.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("USU_NOMBRES", usuario.nombres);
            values.put("USU_APELLIDOS", usuario.apellidos);
            values.put("USU_USUARIO", usuario.usuario);
            values.put("USU_CONTRA", usuario.contra);
            db.update("usuarios", values, "USU_DOCUMENTO=?", new String[]{"" + usuario.documento});
            Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {

        }
    }
}
