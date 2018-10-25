/*
 * Copyright 2016 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsungxr.io.cursor3d;

import android.util.SparseArray;

import com.samsungxr.SXRContext;
import com.samsungxr.SXRSceneObject;
import com.samsungxr.animation.SXRAnimationEngine;
import com.samsungxr.animation.SXRAnimator;
import com.samsungxr.animation.SXRRepeatMode;
import com.samsungxr.utility.Log;

import java.io.IOException;

/**
 * Use this behavior to uniquely set an object to the {@link Cursor}.
 */
class ObjectCursorAsset extends CursorAsset {
    private static final String TAG = ObjectCursorAsset.class.getSimpleName();
    private final String assetName;
    private SparseArray<SXRSceneObject> objects;
    private SXRAnimationEngine animationEngine;
    private int LOOP_REPEAT_COUNT = -1;

    ObjectCursorAsset(SXRContext context, CursorType type, Action action, String assetName) {
        super(context, type, action);
        this.assetName = assetName;
        objects = new SparseArray<SXRSceneObject>();
        animationEngine = context.getAnimationEngine();
    }

    @Override
    void set(Cursor cursor) {
        super.set(cursor);
        SXRSceneObject modelSceneObject = objects.get(cursor.getId());

        if (modelSceneObject == null) {
            Log.e(TAG, "Model not found, should not happen");
            return;
        }
        modelSceneObject.setEnable(true);

        SXRAnimator animator = (SXRAnimator) modelSceneObject.getComponent(SXRAnimator.getComponentType());
        if (animator != null)
        {
            animator.setRepeatMode(SXRRepeatMode.REPEATED);
            animator.setRepeatCount(LOOP_REPEAT_COUNT);
            animator.start();
        }
    }

    private SXRSceneObject loadModelSceneObject() {
        SXRSceneObject modelSceneObject = null;
        try {
            modelSceneObject = context.getAssetLoader().loadModel(assetName);
        } catch (IOException e) {
            //should not happen
            Log.e(TAG, "Could not load model", e);
        }
        return modelSceneObject;
    }

    @Override
    void reset(Cursor cursor) {
        super.reset(cursor);

        SXRSceneObject modelSceneObject = objects.get(cursor.getId());

        modelSceneObject.setEnable(false);
        SXRAnimator animator = (SXRAnimator) modelSceneObject.getComponent(SXRAnimator.getComponentType());
        if (animator != null)
        {
            animator.setRepeatMode(SXRRepeatMode.ONCE);
            animator.setRepeatCount(0);
            animator.start();
        }
    }

    @Override
    void load(Cursor cursor) {
        Integer key = cursor.getId();
        SXRSceneObject modelSceneObject = objects.get(key);

        if (modelSceneObject == null) {
            modelSceneObject = loadModelSceneObject();
            modelSceneObject.setName( getAction().toString() + key.toString());
            objects.put(key, modelSceneObject);
        }
        cursor.addChildObject(modelSceneObject);
        modelSceneObject.setEnable(false);
    }

    @Override
    void unload(Cursor cursor) {
        SXRSceneObject assetSceneObject = objects.get(cursor.getId());
        cursor.removeChildObject(assetSceneObject);
        objects.remove(cursor.getId());
    }
}
