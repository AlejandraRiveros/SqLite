package com.example.sqlitebasededatos;


import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;



public class Encriptar {

    private String llaveSecreta = "ALEJASTOP";

    Encriptar(){}

    public String encriptar(String string){
        try {

            SecretKeySpec secretKey = generateKey(llaveSecreta);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] datoEncriptadosEnByte = cipher.doFinal(string.getBytes());
            String datosEncriptadoString = Base64.encodeToString(datoEncriptadosEnByte, Base64.DEFAULT);
            return  datosEncriptadoString;

        }catch ( Exception ex){
            return null;
        }

    }

    public  String desEncriptar(String string){
        try{

            SecretKeySpec secretKey = generateKey(llaveSecreta);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] datosDecodificados = Base64.decode(string, Base64.DEFAULT);
            byte[] datosEncriptadoByte = cipher.doFinal(datosDecodificados);
            String datosDesencriptadoString = new String(datosEncriptadoByte);
            return datosDesencriptadoString;

        }catch ( Exception ex){
            return null;
        }


    }

    public  SecretKeySpec  generateKey(String password)throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }


}


    /* private void encriptar(String contra) throws  Exception{
          SecretKeySpec secretKeySpec = generateKey(contra);
          Cipher cipher = Cipher.getInstance("AES"); //Algoritmo de encriptación
          cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
      }
      //Lanza excepcion
      private SecretKeySpec generateKey(String contra) throws  Exception{
          MessageDigest sha = MessageDigest.getInstance("SHA-256"); //Proteger y codificar de forma segura
          byte[] key = contra.getBytes("UTF-8"); //Igual a cuando se crea una constante en la parte de arriba, pasamos la contraseña a bytes
          key = sha.digest(key); //Pasar el sha
          SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
          return  secretKeySpec;

      }
      */
  /*   public  String encriptar(String contra) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] bytesSecretKey = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(bytesSecretKey, AES); //Construir una clave secreta, convierte el secret key en el algoritmo aes
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec); //La clave que se ha generado
        byte[] contraEncriptada = cipher.doFinal(contra.getBytes()); //Encriptar la contraseña, se necesita en bytes

        Log.d("TAG CONTRAAAAAAAA", new String(contraEncriptada));

        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        byte[] contraDesencriptado = cipher.doFinal(contraEncriptada);
        Log.d("TAG", new String(contraDesencriptado));
        return new String(contraDesencriptado);


    }
        */
