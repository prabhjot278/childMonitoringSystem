package com.prabhjot.pschildmonitoringsystem;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgressHUD
            extends Dialog
    {
        public ProgressHUD(Context paramContext)
        {
            super(paramContext);
        }

        public ProgressHUD(Context paramContext, int paramInt)
        {
            super(paramContext, paramInt);
        }

        public static ProgressHUD show(Context paramContext, CharSequence paramCharSequence, boolean paramBoolean1, boolean paramBoolean2, OnCancelListener paramOnCancelListener)
        {
            ProgressHUD localProgressHUD = new ProgressHUD(paramContext, R.style.ProgressHUD);
            localProgressHUD.setTitle("");
            localProgressHUD.setContentView(R.layout.progress_hud);
            if ((paramCharSequence == null) || (paramCharSequence.length() == 0)) {
                localProgressHUD.findViewById(R.id.message).setVisibility(8);
            }
            for (;;)
            {
                localProgressHUD.setCancelable(paramBoolean2);
                localProgressHUD.setOnCancelListener(paramOnCancelListener);
                localProgressHUD.getWindow().getAttributes().gravity = 17;
                WindowManager.LayoutParams localLayoutParams = localProgressHUD.getWindow().getAttributes();
                localLayoutParams.dimAmount = 0.2F;
                localProgressHUD.getWindow().setAttributes(localLayoutParams);
                localProgressHUD.show();
                ((TextView)localProgressHUD.findViewById(R.id.message)).setText(paramCharSequence);
                return localProgressHUD;

            }
        }

        public void onWindowFocusChanged(boolean paramBoolean)
        {ImageView imageView = (ImageView)findViewById(R.id.spinnerImageView);
            ((AnimationDrawable)(imageView).getBackground()).start();
        }

        public void setMessage(CharSequence paramCharSequence)
        {
            if ((paramCharSequence != null) && (paramCharSequence.length() > 0))
            {
                findViewById(R.id.message).setVisibility(0);
                TextView localTextView = (TextView)findViewById(R.id.message);
                localTextView.setText(paramCharSequence);
                localTextView.invalidate();
            }
        }
    }
