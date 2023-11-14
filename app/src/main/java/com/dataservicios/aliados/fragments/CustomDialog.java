package com.dataservicios.aliados.fragments;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.ornach.magicicon.IconButton;

import com.ornach.nobobutton.ViewButton;
import com.dataservicios.aliados.PanelAdminActivity;
import com.dataservicios.aliados.PreviewActivity;
import com.dataservicios.aliados.R;
import com.dataservicios.aliados.model.Promotion;
import com.dataservicios.aliados.model.PromotionDetail;
import com.dataservicios.aliados.model.User;
import com.dataservicios.aliados.servicesApi.RestApiAdapter;
import com.dataservicios.aliados.servicesApi.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomDialog extends Dialog implements android.view.View.OnClickListener {
   private static final String LOG_TAG = CustomDialog.class.getSimpleName();
   private Activity activity;
   private int promotion_id;
   private Dialog d;

   private IconButton action_close;
   private User user;


   private LinearLayout ly_contendor_btn_promotions,ly_loading,ly_message;

   private Promotion promotion = null ;

//   private String filepath = "https://drive.google.com/uc?id=1_1lI4OGXezUpTFCpea6rL1IBxGsijWRD&export=download&authuser=0";




   public CustomDialog(Activity activity,int promotion_id,User user) {
      super(activity);
      this.activity = activity;
      this.promotion_id = promotion_id;
      this.user = user;
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.dialog_fragment_custom);

      action_close = (IconButton) findViewById(R.id.action_close);
      ly_loading = (LinearLayout) findViewById(R.id.ly_loading);
      ly_message = (LinearLayout) findViewById(R.id.ly_message);


      ly_loading.setVisibility(View.VISIBLE);


      Map<String, String> map = new HashMap<String, String>();

      map.put("promotion_id", String.valueOf(this.promotion_id));
      map.put("zone_id", String.valueOf(user.getZone_id()));



      RestApiAdapter restApiAdapter = new RestApiAdapter();
      Service service =  restApiAdapter.getClientService(getContext());
      //Call<Promotion> call =  service.getDataPromotion(this.promotion_id);

      Call<Promotion> call =  service.promotionsForZone(map);

      call.enqueue(new Callback<Promotion>() {
         @Override
         public void onResponse(Call<Promotion> call, Response<Promotion> response) {


            try {


               if(response.isSuccessful()){
                  Log.d(LOG_TAG,response.body().toString());
                  promotion = response.body();

                  ly_contendor_btn_promotions = (LinearLayout) findViewById(R.id.ly_contendor_btn_promotions);
                  ly_contendor_btn_promotions.removeAllViews();

                  if (promotion.getPromotion_details().size() == 0){
                     ly_loading.setVisibility(View.GONE);
                     ly_message.setVisibility(View.VISIBLE);
                     return;
                  }



                  for (PromotionDetail object: promotion.getPromotion_details()) {


                     View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_buttons_dynamic, null, false);
                     TextView txt_buttons_promotion =  layout.findViewById(R.id.txt_buttons_promotion);
                     IconButton ib_dowload = layout.findViewById(R.id.ib_dowload);

                     txt_buttons_promotion.setText(object.getName().toString());

                     //NoboButton icon_buttons_promotion = layout.findViewById(R.id.icon_buttons_promotion);
                     ViewButton icon_buttons_promotion = layout.findViewById(R.id.icon_buttons_promotion);
                     icon_buttons_promotion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

//                           Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1_1lI4OGXezUpTFCpea6rL1IBxGsijWRD/view"));
//                           Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(object.getUrl()));
//                           activity.startActivity(browserIntent);
//                           Toast.makeText(getContext(),  object.getName().toString(),Toast.LENGTH_SHORT).show();

                           Intent intent = new Intent(activity, PreviewActivity.class);
                           intent.putExtra("url_image_preview"              , object.getUrl().toString());
                           activity.startActivity(intent);

                        }
                     });

                     ib_dowload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(object.getUrl()));
                           activity.startActivity(browserIntent);
                           Toast.makeText(getContext(),  object.getName().toString(),Toast.LENGTH_SHORT).show();



                        }
                     });

                     ly_contendor_btn_promotions.addView(layout);
                  }

                  ly_loading.setVisibility(View.GONE);




               }else {
                  try {
                     Log.d(LOG_TAG,"Error credenciales: " + response.errorBody().string());
                     // processInteractor.showErrorServer("Error: intetelo nuevamente");
                     Toast.makeText(getContext(), "No se pudo obtener información", Toast.LENGTH_SHORT).show();
                     ly_loading.setVisibility(View.GONE);

                  } catch (IOException e) {
                     e.printStackTrace();
                     Toast.makeText(getContext(), "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();
                     ly_loading.setVisibility(View.GONE);
                  }
               }

            } catch (Exception e) {
               e.printStackTrace();

               Log.d(LOG_TAG, "Error SQL: " + e.getMessage());
               Toast.makeText(getContext(), "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();
               ly_loading.setVisibility(View.GONE);
            }

         }

         @Override
         public void onFailure(Call<Promotion> call, Throwable t) {
            ly_loading.setVisibility(View.GONE);
            Log.d(LOG_TAG, "Error En red: " + t.getMessage());
            Log.d(LOG_TAG, t.getMessage());
            Toast.makeText(getContext(), "Server error, no se pudo obtener información", Toast.LENGTH_SHORT).show();

         }

      });







      action_close.setOnClickListener(this);



   }

   @Override
   public void onClick(View view) {
      this.dismiss();
   }




}
