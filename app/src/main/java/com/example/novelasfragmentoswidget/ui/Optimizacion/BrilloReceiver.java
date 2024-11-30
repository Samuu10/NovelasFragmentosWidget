package com.example.novelasfragmentoswidget.ui.Optimizacion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.provider.Settings;
import android.widget.Toast;

//Clase que se encarga de ajustar el brillo de la pantalla en función del porcentaje de batería
public class BrilloReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = (int) ((level / (float) scale) * 100);

        //Ajustamos el brillo en función del porcentaje de batería
        int brightness = (int) (batteryPct * 2.55);
        ajustarBrillo(context, brightness);
    }

    //Metodo que ajusta el brillo de la pantalla
    private void ajustarBrillo(Context context, int brightness) {
        try {
            if (Settings.System.canWrite(context)) {
                Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
                Toast.makeText(context, "Brillo ajustado a " + brightness, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No se tiene permiso para modificar los ajustes del sistema", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}