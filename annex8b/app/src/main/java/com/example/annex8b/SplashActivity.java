package com.example.annex8b;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button startButton = findViewById(R.id.startButton);
        final View circleView = findViewById(R.id.circleView);
        final TextView splashText = findViewById(R.id.splashText);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Interpolateur de rebond pour l'animation de chute
                BounceInterpolator bounceInterpolator = new BounceInterpolator();

                // Animation pour faire tomber le cercle vers le centre avec un effet de rebond
                ObjectAnimator translateY = ObjectAnimator.ofFloat(circleView, "translationY", 0f, 400f);
                translateY.setInterpolator(bounceInterpolator);

                // Animation pour faire apparaître progressivement le texte "splash"
                ObjectAnimator fadeInText = ObjectAnimator.ofFloat(splashText, "alpha", 0f, 1f);

                // Ensemble d'animateurs pour combiner les animations
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(translateY, fadeInText);
                animatorSet.setDuration(2000); // Durée totale de l'animation (augmentée pour l'effet de rebond)

                // Écouteur pour suivre la progression de l'animation
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        // Début de l'animation
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        // Fin de l'animation
                        splashText.setTextColor(getResources().getColor(android.R.color.white));
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        // Annulation de l'animation
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                        // Répétition de l'animation
                    }
                });

                // Ajouter un écouteur pour mettre à jour la couleur du texte lorsque le cercle touche le texte
                translateY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float translateYValue = (float) valueAnimator.getAnimatedValue();
                        float textTop = splashText.getY();
                        float textBottom = textTop + splashText.getHeight();

                        // Vérifier si la position Y du cercle est dans la plage du texte
                        if (translateYValue >= textTop && translateYValue <= textBottom) {
                            // Changer progressivement la couleur du texte de gris à noir
                            float fraction = (translateYValue - textTop) / splashText.getHeight();
                            int grayToBlackColor = blendColors(getResources().getColor(android.R.color.darker_gray),
                                    getResources().getColor(android.R.color.black), fraction);
                            splashText.setTextColor(grayToBlackColor);
                        }
                    }
                });

//                // Rétrécir le cercle avant de commencer l'animation de grossissement
//                ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(circleView, "scaleX", 1f, 0.8f);
//                ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(circleView, "scaleY", 1f, 0.8f);
//                AnimatorSet scaleDownSet = new AnimatorSet();
//
//                scaleDownSet.setDuration(300); // Durée de l'animation de rétrécissement

                // Grossir le cercle
                ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(circleView, "scaleX", 1f, 50f);
                ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(circleView, "scaleY", 1f, 50f);

                AnimatorSet scaleUpSet = new AnimatorSet();
                scaleUpSet.playTogether(scaleUpX, scaleUpY);
                scaleUpSet.setDuration(1000); // Durée de l'animation de grossissement

                // Séquence des animations
                AnimatorSet sequenceSet = new AnimatorSet();
                ///rajout scaleDownSet,
                sequenceSet.playSequentially( animatorSet, scaleUpSet);

                // Démarrer la séquence d'animation
                sequenceSet.start();
            }
        });
    }

    // Méthode auxiliaire pour mélanger les couleurs en fonction d'une fraction
    private int blendColors(int color1, int color2, float fraction) {
        float inverseFraction = 1f - fraction;
        float red1 = Color.red(color1) * fraction + Color.red(color2) * inverseFraction;
        float green1 = Color.green(color1) * fraction + Color.green(color2) * inverseFraction;
        float blue1 = Color.blue(color1) * fraction + Color.blue(color2) * inverseFraction;
        return Color.rgb((int) red1, (int) green1, (int) blue1);
    }
}