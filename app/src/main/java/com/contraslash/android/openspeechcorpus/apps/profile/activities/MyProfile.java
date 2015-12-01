package com.contraslash.android.openspeechcorpus.apps.profile.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.contraslash.android.network.HttpConnection;
import com.contraslash.android.network.HttpConnectionMultipart;
import com.contraslash.android.network.HttpParameter;
import com.contraslash.android.network.MultipartParameter;
import com.contraslash.android.network.OnServerResponse;
import com.contraslash.android.network.Util;
import com.contraslash.android.openspeechcorpus.R;
import com.contraslash.android.openspeechcorpus.base.BaseActivity;
import com.contraslash.android.openspeechcorpus.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyProfile extends BaseActivity {

    Toolbar toolbar;
    ImageView profileImage;
    EditText profileName;
    ImageButton save;


    int REQUEST_IMAGE_CAPTURE_FRAGMENT_PROFILE = 1;
    String picturePath;

    boolean pictureTaked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_profile;
    }

    @Override
    protected void mapearGUI() {
        toolbar=(Toolbar)findViewById(R.id.my_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        profileImage=(ImageView)findViewById(R.id.my_profile_profile_image);
        profileName=(EditText)findViewById(R.id.my_profile_profile_name);
        save=(ImageButton)findViewById(R.id.my_profile_save);

    }

    @Override
    protected void cargarEventos() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto();
            }
        });

        profileName.setText(getPreferencias().getString(Config.ANONYMOUS_USER_NAME,""));
        File imgFile = new  File(getPreferencias().getString(Config.ANONYMOUS_USER_PICTURE,""));
        Log.i(TAG,"Picture PATH: "+ getPreferencias().getString(Config.ANONYMOUS_USER_PICTURE,""));

        if(imgFile.exists()){
            Log.i(TAG,"Path Exists");
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            profileImage.setImageBitmap(myBitmap);

        }
        else
        {
            Log.i(TAG, "Path Does NOT Exists");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    /**
     * Método para llamar al servicio de fotos del dispositivo
     */
    private void tomarFoto()
    {
        Log.i(TAG, "Trying to Take a Picture");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));

            }

        }

        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        String pickTitle = "Seleccione o tome una foto "; // Or get from strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[]{takePictureIntent}
                );

        startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE_FRAGMENT_PROFILE);


    }

    /**
     * Manejador de los resultados que llegan a la aplicacion
     * @param requestCode codigo de identificación del llamado
     * @param resultCode resultado de la ejecución
     * @param data información recibida
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "On Activity Result Edit Profile");
        if (requestCode == REQUEST_IMAGE_CAPTURE_FRAGMENT_PROFILE && resultCode == Activity.RESULT_OK)
        {
            Log.i(TAG,"Resolving from IMAGE CAPTURE to fragmentProfile");
            //updatePicture(data.getExtras());
            //galleryAddPic();
            if(data!=null)
            {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    FileOutputStream out = new FileOutputStream(new File(picturePath));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);

                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }

            pictureTaked = true;

            updatePicture();

        }
    }

    /**
     * Creador de archivo persistente en la memoria del dispositivo con la foto de perfil
     * @return archivo con la información de la foto de perfil
     * @throws java.io.IOException la imagen de perfil no pudo ser creada
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/openspeechcorpus/profile-images/";
        File dir = new File(dirPath);
        Log.i(TAG, dir.mkdirs() + "");

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                dir
        );
        Log.i(TAG, "Y la ruta de la foto es: " + image.getAbsolutePath());
        picturePath =  image.getAbsolutePath();
        return image;
    }


    /**
     * Actualiza la foto de perfil con la ultima foto tomada
     */
    public void updatePicture()
    {
        Log.i(TAG,"Updating Profile");
        // Get the dimensions of the View
        int targetW = profileImage.getWidth();
        int targetH = profileImage.getHeight();
        Log.i(TAG,targetH+" "+targetW);
        if(targetW==0)
            targetW=300;
        if(targetH==0)
            targetH=300;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, bmOptions);
        profileImage.setImageBitmap(bitmap);

        //Bitmap imageBitmap = (Bitmap) bundle.get("data");
        //picture.setImageBitmap(imageBitmap);
        Log.i(TAG,"Update Done");

    }

    private void updateProfile()
    {
        ArrayList<HttpParameter> encabezados = new ArrayList<>();
        ArrayList<HttpParameter> parametros=new ArrayList<>();
        parametros.add(new HttpParameter(Config.ANONYMOUS_USER, getPreferencias().getInt(Config.USER_ID, -1)+""));
        parametros.add(new HttpParameter(Config.ANONYMOUS_USER_NAME, profileName.getText()+""));
        ArrayList<MultipartParameter> parametroImagens = new ArrayList<>();
        if(pictureTaked)
        {
            parametroImagens.add(new MultipartParameter(Config.ANONYMOUS_USER_PICTURE, picturePath, "image/jpeg"));
        }


        HttpConnectionMultipart cargarImagenes = new HttpConnectionMultipart(
                this,
                Config.BASE_URL + Config.API_BASE_URL + "/anonymous-user/update/",
                encabezados,
                parametros,
                parametroImagens,
                HttpConnection.POST,
                new OnServerResponse() {


                    @Override
                    public void ConexionExitosa(int codigoRespuesta, String respuesta) {
                        getPreferencias().edit().putString(Config.ANONYMOUS_USER_NAME,profileName.getText()+"").apply();
                        getPreferencias().edit().putString(Config.ANONYMOUS_USER_PICTURE,picturePath).apply();

                        Toast.makeText(MyProfile.this,getString(R.string.profile_saved),Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void ConexionFallida(int codigoRespuesta) {
                        new Util(MyProfile.this).mostrarErrores(codigoRespuesta);
                    }

                    @Override
                    public void MalaParametrizacion() {
                        Log.i(TAG, "Mala Parametrización");
                    }
                }
        );

        cargarImagenes.setShowDialog(true);

        cargarImagenes.initDialog("Cargando las imagenes", getString(R.string.may_take_few_seconds));

        cargarImagenes.setDialogCancelable(false);


        cargarImagenes.execute();

    }



}
