package com.rohitneel.photopixelpro.photoframe.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Nick Bapu on 03-11-2017.
 */

public class AlertDialogBox {

    public static void AlertMessage(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLACK);
        pbutton.setBackgroundColor(Color.WHITE);

    }
}
