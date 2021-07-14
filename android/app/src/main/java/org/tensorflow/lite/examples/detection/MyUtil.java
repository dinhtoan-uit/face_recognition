package org.tensorflow.lite.examples.detection;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;


import org.tensorflow.lite.examples.detection.MTCNN.Box;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MyUtil {

    public static Bitmap readFromAssets(Context context, String filename){
        Bitmap bitmap;
        AssetManager asm = context.getAssets();
        try {
            InputStream is = asm.open(filename);
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public static float[][][] normalizeImage(Bitmap bitmap) {
        int h = bitmap.getHeight();
        int w = bitmap.getWidth();
        float[][][] floatValues = new float[h][w][3];

        float imageMean = 127.5f;
        float imageStd = 128;

        int[] pixels = new int[h * w];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, w, h);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                final int val = pixels[i * w + j];
                float r = (((val >> 16) & 0xFF) - imageMean) / imageStd;
                float g = (((val >> 8) & 0xFF) - imageMean) / imageStd;
                float b = ((val & 0xFF) - imageMean) / imageStd;
                float[] arr = {r, g, b};
                floatValues[i][j] = arr;
            }
        }
        return floatValues;
    }

    public static Bitmap bitmapResize(Bitmap bitmap, float scale) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, true);
    }

    public static float[][][] transposeImage(float[][][] in) {
        int h = in.length;
        int w = in[0].length;
        int channel = in[0][0].length;
        float[][][] out = new float[w][h][channel];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                out[j][i] = in[i][j] ;
            }
        }
        return out;
    }

    public static float[][][][] transposeBatch(float[][][][] in) {
        int batch = in.length;
        int h = in[0].length;
        int w = in[0][0].length;
        int channel = in[0][0][0].length;
        float[][][][] out = new float[batch][w][h][channel];
        for (int i = 0; i < batch; i++) {
            for (int j = 0; j < h; j++) {
                for (int k = 0; k < w; k++) {
                    out[i][k][j] = in[i][j][k] ;
                }
            }
        }
        return out;
    }

    public static float[][][] cropAndResize(Bitmap bitmap, Box box, int size) {
        // crop and resize
        Matrix matrix = new Matrix();
        float scaleW = 1.0f * size / box.width();
        float scaleH = 1.0f * size / box.height();
        matrix.postScale(scaleW, scaleH);
        Rect rect = box.transform2Rect();
        Bitmap croped = Bitmap.createBitmap(
                bitmap, rect.left, rect.top, box.width(), box.height(), matrix, true);

        return normalizeImage(croped);
    }

    public static Bitmap crop(Bitmap bitmap, Rect rect) {
        Bitmap cropped = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
        return cropped;
    }

    public static void l2Normalize(float[][] embeddings, double epsilon) {
        for (int i = 0; i < embeddings.length; i++) {
            float squareSum = 0;
            for (int j = 0; j < embeddings[i].length; j++) {
                squareSum += Math.pow(embeddings[i][j], 2);
            }
            float xInvNorm = (float) Math.sqrt(Math.max(squareSum, epsilon));
            for (int j = 0; j < embeddings[i].length; j++) {
                embeddings[i][j] = embeddings[i][j] / xInvNorm;
            }
        }
    }
}