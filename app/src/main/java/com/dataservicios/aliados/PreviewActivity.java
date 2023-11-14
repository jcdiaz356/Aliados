package com.dataservicios.aliados;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.github.barteksc.pdfviewer.PDFView;
import com.ornach.nobobutton.NoboButton;
import com.dataservicios.aliados.util.RecibirPDFStream;

public class PreviewActivity extends AppCompatActivity {

    private NoboButton btn_close;
    private PDFView pdfView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Bundle bundle = getIntent().getExtras();
        String url_image_preview   = bundle.getString("url_image_preview");

        btn_close = (NoboButton) findViewById(R.id.btn_close);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar);

      // pdfView.fromUri(Uri.parse(url_image_preview));
        new RecibirPDFStream(pdfView,progressBar).execute(url_image_preview);


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



}