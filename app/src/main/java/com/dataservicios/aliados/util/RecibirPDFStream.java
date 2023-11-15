package com.dataservicios.aliados.util;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecibirPDFStream extends AsyncTask<String,Void, InputStream> {


   private  int i=0;
   private PDFView pdfView;
   private ProgressBar progressBar;

   public RecibirPDFStream(PDFView pdfView, ProgressBar progressBar) {
      this.pdfView = pdfView;
      this.progressBar = progressBar;
   }

   @Override
   protected InputStream doInBackground(String... strings) {
      InputStream inputStream = null;
      try {
         URL url = new URL(strings[0]);
         HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
         if (urlConnection.getResponseCode()==200){
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
         }
      }catch (IOException e){
         return null;
      }
      return  inputStream;
   }

   @Override
   protected void onPostExecute(InputStream inputStream) {


      /************************* CONTABILIZADOR DE TIEMPO /*****/
//      CountDownTimer mCountDownTimer;
//      mCountDownTimer=new CountDownTimer(75000,1000) {
//         @Override
//         public void onTick(long millisUntilFinished) {
//            Log.v("Log_tag", "Tick of Progress"+ i + millisUntilFinished);
//            i++;
//            // pbLoading.setProgress((int)i*100/(5000/1000));
//           // tvLoad.setText("Cargando información...");
//
//         }
//
//         @Override
//         public void onFinish() {
//
//
//
//         }
//      };
//      mCountDownTimer.start();

      pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
         @Override
         public void loadComplete(int nbPages) {

            progressBar.setVisibility(View.GONE);
         }
      }).load(); ;



   }
}
