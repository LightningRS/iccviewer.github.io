/* Copyright 2016 Braden Farmer
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

package com.farmerbb.taskbar.util;

public class MenuHelper {

    private boolean startMenuOpen = false;
    private boolean contextMenuOpen = false;

    private static MenuHelper theInstance;

    private MenuHelper() {}

    public static MenuHelper getInstance() {
        if(theInstance == null) theInstance = new MenuHelper();

        return theInstance;
    }

    public boolean isStartMenuOpen() {
        return startMenuOpen;
    }

    public void setStartMenuOpen(boolean value) {
        startMenuOpen = value;
    }

    public boolean isContextMenuOpen() {
        return contextMenuOpen;
    }

    public void setContextMenuOpen(boolean value) {
        contextMenuOpen = value;
    }
}