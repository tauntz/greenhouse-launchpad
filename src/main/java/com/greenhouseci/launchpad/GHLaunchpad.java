/*
 * Copyright 2015 Tauno Talimaa
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

package com.greenhouseci.launchpad;


import java.io.File;

public class GHLaunchpad {

    public static String CCMENU_TOKEN;
    public static String GIT_HOOK_TOKEN;

    public static void main(String[] args) {

        if (args.length == 2) {
            CCMENU_TOKEN = args[0];
            GIT_HOOK_TOKEN = args[1];
        } else {
            System.out.println("Usage: " +
                    new File(GHLaunchpad.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName() +
                    " <CCMENU_TOKEN> <GIT_HOOK_TOKEN>");
            System.exit(0);
        }

        LaunchpadController ghLaunchpad = new LaunchpadController();
        try {
            ghLaunchpad.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
