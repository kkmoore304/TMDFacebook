package com.tellmedoctor.tmdfacebook.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.tellmedoctor.tmdfacebook.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kmoore on 5/2/15.
 */
public class DialogUtils {

    /**
     * Create the About dialog
     *
     * @return
     */
    public static Dialog createAboutDialog(final Context _context, LayoutInflater inflater) throws PackageManager.NameNotFoundException {

        PackageInfo pInfo = _context.getPackageManager().getPackageInfo(_context.getPackageName(), 0);
        String version = pInfo.versionName;
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        //LayoutInflater inflater = _context.getLayoutInflater();
        View v = inflater.inflate(R.layout.about, null);
        builder.setView(v);

        TextView et = (TextView) v.findViewById(R.id.about_logo_label2);

        if (et != null) {
            if (version != null)
                et.setText(R.string.version + version);
            else
                et.setText(R.string.version_unknown);
        }

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        // TODO remove reject eula
        builder.setNegativeButton(R.string.reject_eula, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrefsUtils.setEulaAccepted(_context, false);
            }
        });


        return builder.create();
    }


}
