package com.wyf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

public class Utils {
	   public static void scaleButton_dada(View v) {
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.05f, 1.0f,
				1.05f, AnimationSet.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		animationSet.addAnimation(scaleAnimation);
		// animationSet.setStartOffset(1000);
		animationSet.setFillAfter(true);
		animationSet.setFillBefore(false);
		animationSet.setDuration(100);
		v.startAnimation(animationSet);
	}
	   public static void writeSys(String file_patch,int value){
			File file = new File(file_patch);

			FileOutputStream out;
			try {
				out = new FileOutputStream(file);
				out.write((value+"").getBytes());
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   
		}
}
