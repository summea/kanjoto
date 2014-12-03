/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.andrewsummers.otashu.view;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.andrewsummers.otashu.model.Note;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.util.SparseArray;

public class PlaybackGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "PlaybackGLRenderer";
    private Square mSquare;
    private List<Square> mSquares = new ArrayList<Square>();
    private List<Note> noteSequence = new ArrayList<Note>();

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    
    //private HashMap<float[], List<Integer>> noteColorTable = new HashMap<float[], List<Integer>>();
    private SparseArray<float[]> noteColorTable = new SparseArray<float[]>();    
    
    
    /*
    int color = Color.parseColor("#ff0000");
    //private float[] noteAColor = { 1.0f, 0.0f, 0.0f, 1.0f };
    private float[] noteAColor = { Color.red(color) / 255.0f, Color.green(color) / 255.0f, Color.blue(color) / 255.0f };
    private float[] noteAsColor = { 1.0f, 0.2f, 0.0f, 1.0f };
    private float[] noteBColor = { 1.0f, 0.4f, 0.0f, 1.0f };
    private float[] noteCColor = { 1.0f, 0.6f, 0.0f, 1.0f };
    private float[] noteCsColor = { 1.0f, 0.8f, 0.0f, 1.0f };
    private float[] noteDColor = { 1.0f, 1.0f, 0.0f, 1.0f };
    private float[] noteDsColor = { 1.0f, 0.0f, 0.2f, 1.0f };
    private float[] noteEColor = { 1.0f, 0.0f, 0.4f, 1.0f };
    private float[] noteFColor = { 1.0f, 0.0f, 0.6f, 1.0f };
    private float[] noteFsColor = { 1.0f, 0.0f, 0.8f, 1.0f };
    private float[] noteGColor = { 1.0f, 0.0f, 1.0f, 1.0f };
    private float[] noteGsColor = { 1.0f, 0.2f, 1.0f, 1.0f };
     */
    
    public PlaybackGLRenderer() {        
    }
    
    public PlaybackGLRenderer(List<Note> notes, SparseArray<float[]> incomingNoteColorTable) {
        noteSequence = notes;
    
        noteColorTable = incomingNoteColorTable;
        
        /*
        noteColorTable.put(21, noteAColor);
        noteColorTable.put(33, noteAColor);
        noteColorTable.put(45, noteAColor);
        noteColorTable.put(57, noteAColor);
        noteColorTable.put(69, noteAColor);
        noteColorTable.put(81, noteAColor);
        noteColorTable.put(93, noteAColor);
        noteColorTable.put(105, noteAColor);

        noteColorTable.put(22, noteAsColor);
        noteColorTable.put(34, noteAsColor);
        noteColorTable.put(46, noteAsColor);
        noteColorTable.put(58, noteAsColor);
        noteColorTable.put(70, noteAsColor);
        noteColorTable.put(82, noteAsColor);
        noteColorTable.put(94, noteAsColor);
        noteColorTable.put(106, noteAsColor);
        
        noteColorTable.put(23, noteBColor);
        noteColorTable.put(35, noteBColor);
        noteColorTable.put(47, noteBColor);
        noteColorTable.put(59, noteBColor);
        noteColorTable.put(71, noteBColor);
        noteColorTable.put(83, noteBColor);
        noteColorTable.put(95, noteBColor);
        noteColorTable.put(107, noteBColor);
        
        noteColorTable.put(24, noteCColor);
        noteColorTable.put(36, noteCColor);
        noteColorTable.put(48, noteCColor);
        noteColorTable.put(60, noteCColor);
        noteColorTable.put(72, noteCColor);
        noteColorTable.put(84, noteCColor);
        noteColorTable.put(96, noteCColor);
        noteColorTable.put(108, noteCColor);
        
        noteColorTable.put(25, noteCsColor);
        noteColorTable.put(37, noteCsColor);
        noteColorTable.put(49, noteCsColor);
        noteColorTable.put(61, noteCsColor);
        noteColorTable.put(73, noteCsColor);
        noteColorTable.put(85, noteCsColor);
        noteColorTable.put(97, noteCsColor);
        
        noteColorTable.put(26, noteDColor);
        noteColorTable.put(38, noteDColor);
        noteColorTable.put(50, noteDColor);
        noteColorTable.put(62, noteDColor);
        noteColorTable.put(74, noteDColor);
        noteColorTable.put(86, noteDColor);
        noteColorTable.put(98, noteDColor);
        
        noteColorTable.put(27, noteDsColor);
        noteColorTable.put(39, noteDsColor);
        noteColorTable.put(51, noteDsColor);
        noteColorTable.put(63, noteDsColor);
        noteColorTable.put(75, noteDsColor);
        noteColorTable.put(87, noteDsColor);
        noteColorTable.put(99, noteDsColor);
        
        noteColorTable.put(28, noteEColor);
        noteColorTable.put(40, noteEColor);
        noteColorTable.put(52, noteEColor);
        noteColorTable.put(64, noteEColor);
        noteColorTable.put(76, noteEColor);
        noteColorTable.put(88, noteEColor);
        noteColorTable.put(100, noteEColor);
        
        noteColorTable.put(29, noteFColor);
        noteColorTable.put(41, noteFColor);
        noteColorTable.put(53, noteFColor);
        noteColorTable.put(65, noteFColor);
        noteColorTable.put(77, noteFColor);
        noteColorTable.put(89, noteFColor);
        noteColorTable.put(101, noteFColor);
        
        noteColorTable.put(30, noteFsColor);
        noteColorTable.put(42, noteFsColor);
        noteColorTable.put(54, noteFsColor);
        noteColorTable.put(66, noteFsColor);
        noteColorTable.put(78, noteFsColor);
        noteColorTable.put(90, noteFsColor);
        noteColorTable.put(102, noteFsColor);

        noteColorTable.put(31, noteGColor);
        noteColorTable.put(43, noteGColor);
        noteColorTable.put(55, noteGColor);
        noteColorTable.put(67, noteGColor);
        noteColorTable.put(79, noteGColor);
        noteColorTable.put(91, noteGColor);
        noteColorTable.put(103, noteGColor);
        
        noteColorTable.put(32, noteGsColor);
        noteColorTable.put(44, noteGsColor);
        noteColorTable.put(56, noteGsColor);
        noteColorTable.put(68, noteGsColor);
        noteColorTable.put(80, noteGsColor);
        noteColorTable.put(92, noteGsColor);
        noteColorTable.put(104, noteGsColor);
        */
    }
    
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        float i = 0.0f;
        float currentRowX = -1.5f;
        for (Note note : noteSequence) {
            mSquare = new Square();
            if (i % 4 == 0)
                currentRowX = -1.5f;
            else
                currentRowX += 1.0f;
            mSquare.setX(currentRowX);
            mSquare.setY(i * -1.0f);
            mSquare.setColor(noteColorTable.get(note.getNotevalue()));
            mSquares.add(mSquare);
            i++;
        }
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        for (Square square : mSquares) {
            // Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    
            square.setY(square.getY() + 0.035f);
            if (square.getY() > 3.0f) {
                // reset y position
                //square.setY(-1.0f);
                
                // change color to match next notevalue
                //noteColorTable.get(key)
            }
            
            Matrix.setIdentityM(mModelMatrix, 0);
            Matrix.translateM(mModelMatrix, 0, square.getX(), square.getY(), 0);
            Matrix.multiplyMM(mMVPMatrix, 0, mMVPMatrix, 0, mModelMatrix, 0);
            
            // Draw square
            square.draw(mMVPMatrix);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 0.5f, 7);
    }

    /**
     * Utility method for compiling an OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        //objColorLocation = GLES20.glGetUniformLocation(shader, "objColor");

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}