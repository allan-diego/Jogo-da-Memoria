package com.example.god.jogodamemoria;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Game extends AppCompatActivity implements View.OnClickListener {

    private GridLayout grid;
    private ImageButton oldCardButton;
    public Estado estado;
    public int resources[] = new int[]{
            R.mipmap.calunga,
            R.mipmap.capela_bjesus,
            R.mipmap.capela_rosario,
            R.mipmap.comes_damiao,
            R.mipmap.coroa_aviao,
            R.mipmap.refugio,
            R.mipmap.sitio_marcos,
            R.mipmap.velho_faceta
    };
    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        estado = Estado.NAO_VIRADA;
        grid = (GridLayout) findViewById(R.id.grid);
        grid.setColumnCount(4);

        final int NUMBER_OF_CARDS = this.resources.length * 2;

        ArrayList<ImageButton> cards;
        cards = new ArrayList<>(NUMBER_OF_CARDS);

        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            ImageButton btn = new ImageButton(this);
            btn.setImageResource(R.mipmap.ic_costas);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, 150);
            btn.setLayoutParams(layoutParams);
            btn.setOnClickListener(this);
            btn.setTag(new CardInfo(resources[i % 8]));
            cards.add(btn);
        }

        Collections.shuffle(cards);

        for(ImageButton button : cards){
            grid.addView(button);
        }
    }


    @Override
    public void onClick(View v) {
        if (v instanceof ImageButton) {
            Context context = getApplicationContext();
            final ImageButton newCardButton = (ImageButton) v;
            final CardInfo newCardInfo = (CardInfo) newCardButton.getTag();

            if (estado == Estado.NAO_VIRADA) {
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(newCardButton, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(newCardButton, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        newCardButton.setImageResource(newCardInfo.getResource());
                        oa2.start();
                    }
                });
                oa1.start();
                newCardButton.setEnabled(false);
                oldCardButton = newCardButton;


                estado = Estado.VIRADA;

            } else if (estado == Estado.VIRADA) {
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(newCardButton, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(newCardButton, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        newCardButton.setImageResource(newCardInfo.getResource());
                        oa2.start();
                    }
                });
                oa1.start();
                if (oldCardButton != newCardButton) {
                    if (((CardInfo) oldCardButton.getTag()).getResource() == newCardInfo.getResource()) {
                        newCardButton.setImageResource(newCardInfo.getResource());
                        newCardButton.setEnabled(false);
                        CharSequence  text = "Você Acertou!!";
                        int duration  = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context,text,duration);
                        toast.show();
                    } else {
                        oldCardButton.setImageResource(R.mipmap.ic_costas);
                        oldCardButton.setEnabled(true);

                        newCardButton.setImageResource(newCardInfo.getResource());

                        newCardButton.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                newCardButton.setImageResource(R.mipmap.ic_costas);
                            }
                        }, 1000);
                    }
                }
                estado = Estado.NAO_VIRADA;
                oldCardButton = null;

                CharSequence  text = "Você Errou!!";
                int duration  = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context,text,duration);
                toast.show();
            }
        }
    }
}
