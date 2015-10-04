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

import com.greenhouseci.launchpad.dto.Project;
import com.greenhouseci.launchpad.dto.Projects;
import com.squareup.okhttp.ResponseBody;
import net.thecodersbreakfast.lp4j.api.*;
import net.thecodersbreakfast.lp4j.midi.MidiDeviceConfiguration;
import net.thecodersbreakfast.lp4j.midi.MidiLaunchpad;
import retrofit.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class    LaunchpadController {

    private CountDownLatch stop = new CountDownLatch(1);
    private LaunchpadClient launchpad;
    private HashMap<Pad, Timer> activeBuildingAnimationTimers = new HashMap<>();
    private Projects latestProjects;
    private HashMap<Pad, Project> padProjectMap = new HashMap<>();
    private GreenhouseCCMenuService ccMenuService;
    private int rowOffset = 0;

    private TextScroller textScroller = new TextScroller();

    public void start() throws Exception {
        Launchpad midiLaunchpad = new MidiLaunchpad(MidiDeviceConfiguration.autodetect());
        launchpad = midiLaunchpad.getClient();
        launchpad.reset();
        midiLaunchpad.setListener(new MyListener(launchpad));
        textScroller.setListener(() -> {
            System.out.println("onScrollFinished(). Restoring pad states");
            launchpad.setBuffers(Buffer.BUFFER_0, Buffer.BUFFER_0, false, false);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    retorePadState(Pad.at(i, j));
                }
            }
        });

        launchpad.setButtonLight(Button.STOP, Color.RED, BackBufferOperation.NONE);
        launchpad.setButtonLight(Button.UP, Color.GREEN, BackBufferOperation.NONE);
        launchpad.setButtonLight(Button.DOWN, Color.GREEN, BackBufferOperation.NONE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://app.greenhouseci.com/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        ccMenuService = retrofit.create(GreenhouseCCMenuService.class);
        final Call<Projects> response = ccMenuService.listRepos(GHLaunchpad.CCMENU_TOKEN);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Call<Projects> tempResponse = response.clone();
                tempResponse.enqueue(new Callback<Projects>() {
                    @Override
                    public void onResponse(Response<Projects> response) {
                        try {
                            if (response.isSuccess()) {
                                latestProjects = response.body();
                                parseProject(latestProjects.getProjects());
                            } else {
                                System.err.println("Can't make request: " + response.code() + ": " + response.errorBody().string());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
            }
        }, 0, 2000);

        stop.await();
        timer.cancel();
        activeBuildingAnimationTimers.values().forEach(java.util.Timer::cancel);
        launchpad.reset();

        midiLaunchpad.close();
        System.exit(0);
    }

    private synchronized void parseProject(List<Project> projects) {
        int row = 0;
        int column = 0;
        padProjectMap.clear();

        int skippedProjects = 0;
        for (Project project : projects) {
            if (rowOffset * 8 > skippedProjects) {
                skippedProjects++;
                continue;
            }

            Pad pad = Pad.at(column, row);
            padProjectMap.put(pad, project);
            setPadState(pad, project);
            column++;
            if (column > 7) {
                row++;
                column = 0;
            }
            if (row > 7) {
                break;
            }
        }
        // Clear rest of the pad
        if (column > 0) {
            for (; column < 8; column++) {
                Pad pad = Pad.at(column, row);
                launchpad.setPadLight(pad, Color.BLACK, BackBufferOperation.NONE);
            }
            row++;
        }

        for (; row < 8; row++) {
            for (column = 0; column < 8; column++) {
                Pad pad = Pad.at(column, row);
                launchpad.setPadLight(pad, Color.BLACK, BackBufferOperation.NONE);
            }
        }
    }

    private synchronized void buildProject(Pad pad) {
        try {
            if (padProjectMap.get(pad) == null) {
                return;
            }
            System.out.println("Building project: " + padProjectMap.get(pad).getProjectId());
            ccMenuService.buildProject(padProjectMap.get(pad).getProjectId(), GHLaunchpad.GIT_HOOK_TOKEN).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response) {
                    try {
                        if (response.isSuccess()) {
                            System.out.println("Request successful");
                        } else {
                            System.err.println("Can't make request: " + response.code() + ": " + response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private synchronized void retorePadState(Pad pad) {
        if (padProjectMap.containsKey(pad)) {
            setPadState(pad, padProjectMap.get(pad));
        } else {
            launchpad.setPadLight(pad, Color.BLACK, BackBufferOperation.NONE);
        }
    }

    private void setPadState(Pad pad, Project project) {
        if (textScroller.isScrolling()) {
            return;
        }
        Timer timer;
        switch (project.getLastBuildStatus()) {
            case "Success":
                timer = activeBuildingAnimationTimers.get(pad);
                if (timer != null) {
                    timer.cancel();
                    activeBuildingAnimationTimers.remove(timer);
                }
                launchpad.setPadLight(pad, Color.GREEN, BackBufferOperation.NONE);
                break;
            case "Failure":
                timer = activeBuildingAnimationTimers.get(pad);
                if (timer != null) {
                    timer.cancel();
                    activeBuildingAnimationTimers.remove(timer);
                }
                launchpad.setPadLight(pad, Color.RED, BackBufferOperation.NONE);
                break;
            case "Building":
                showBuildingAnimation(pad);
                break;
            default:
                System.err.println("Unhandled LastBuildStatus: " + project.getLastBuildStatus());
                break;
        }
    }

    private void showBuildingAnimation(final Pad pad) {

        Timer t;
        t = activeBuildingAnimationTimers.get(pad);
        if (t != null) {
            return;
        }

        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                launchpad.setPadLight(pad, Color.GREEN, BackBufferOperation.NONE);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                launchpad.setPadLight(pad, Color.BLACK, BackBufferOperation.NONE);
            }
        }, 0, 500);

        activeBuildingAnimationTimers.put(pad, t);
    }

    private void clearTimers() {
        for (Pad p : activeBuildingAnimationTimers.keySet()) {
            activeBuildingAnimationTimers.get(p).cancel();
        }
        activeBuildingAnimationTimers.clear();
    }

    private class MyListener extends LaunchpadListenerAdapter {

        private final LaunchpadClient client;
        private HashMap<Pad, Long> padDownTimes = new HashMap<>();

        public MyListener(LaunchpadClient client) {
            this.client = client;
        }

        @Override
        public void onPadPressed(Pad pad, long timestamp) {
            client.setPadLight(pad, Color.YELLOW, BackBufferOperation.NONE);
            padDownTimes.put(pad, timestamp);
        }

        @Override
        public void onPadReleased(Pad pad, long timestamp) {
            try {
                client.setPadLight(pad, Color.BLACK, BackBufferOperation.NONE);
                Long downTime = padDownTimes.get(pad);
                padDownTimes.remove(pad);
                if ((timestamp - downTime) > 400000) {
                    buildProject(pad);
                } else {
                    clearTimers();
                    textScroller.scrollText(padProjectMap.get(pad).getName(), launchpad);
                }
                retorePadState(pad);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onButtonReleased(Button button, long timestamp) {
            switch (button) {
                case STOP:
                    stop.countDown();
                    client.setButtonLight(button, Color.RED, BackBufferOperation.NONE);
                    break;
                case ARM:
                    textScroller.scrollText("GREENHOUSE", launchpad);
                    break;
                case DOWN:
                    client.setButtonLight(button, Color.GREEN, BackBufferOperation.NONE);
                    rowOffset++;
                    clearTimers();
                    parseProject(latestProjects.getProjects());
                    break;
                case UP:
                    client.setButtonLight(button, Color.GREEN, BackBufferOperation.NONE);
                    rowOffset--;
                    if (rowOffset < 0) {
                        rowOffset = 0;
                    }
                    clearTimers();
                    parseProject(latestProjects.getProjects());
                    break;
            }
        }

        @Override
        public void onButtonPressed(Button button, long timestamp) {
            super.onButtonPressed(button, timestamp);
            client.setButtonLight(button, Color.BLACK, BackBufferOperation.NONE);
        }
    }
}