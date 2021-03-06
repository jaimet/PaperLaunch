/*
 * Copyright 2015 Devmil Solutions
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
package de.devmil.paperlaunch.utils;

import android.graphics.Rect;

public abstract class ActivationIndicatorHelper {
    private ActivationIndicatorHelper() {
    }

    public static Rect calculateActivationIndicatorSize(
            int sensitivity,
            int offsetPosition,
            int offsetSize,
            boolean isOnRightSide,
            Rect availableRect) {

        int top = availableRect.top;
        int left = availableRect.left;
        int right = availableRect.right;
        int bottom = availableRect.bottom;

        if(isOnRightSide) {
            left = right - sensitivity;
        } else {
            right = left + sensitivity;
        }

        int height = availableRect.height() - offsetSize;
        top = top + offsetPosition + (offsetSize / 2);
        bottom = top + height;

        Rect result = new Rect(left, top, right, bottom);

        if(!result.intersect(availableRect)) {
            return availableRect;
        }

        return result;
    }
}
