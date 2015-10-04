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


import com.greenhouseci.launchpad.dto.Point;
import net.thecodersbreakfast.lp4j.api.*;

import java.util.*;

public class TextScroller {

    public static HashMap<Character, List<Pad>> charPixelMap = new HashMap<>();

    static {
        addCharPixels('0', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(3, 4), Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(1, 3), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6));
        addCharPixels('1', Pad.at(2, 1), Pad.at(2, 2), Pad.at(2, 3), Pad.at(1, 2), Pad.at(2, 4), Pad.at(2, 5), Pad.at(3, 6), Pad.at(0, 3), Pad.at(2, 6), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('2', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(2, 4), Pad.at(0, 2), Pad.at(3, 6), Pad.at(0, 3), Pad.at(2, 6), Pad.at(1, 5), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('3', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(1, 4), Pad.at(2, 6), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('4', Pad.at(3, 2), Pad.at(2, 2), Pad.at(3, 3), Pad.at(3, 4), Pad.at(2, 4), Pad.at(3, 5), Pad.at(1, 3), Pad.at(3, 6), Pad.at(1, 4), Pad.at(0, 4), Pad.at(3, 1));
        addCharPixels('5', Pad.at(2, 1), Pad.at(1, 1), Pad.at(3, 4), Pad.at(0, 1), Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(1, 3), Pad.at(3, 6), Pad.at(0, 3), Pad.at(2, 6), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('6', Pad.at(2, 1), Pad.at(1, 1), Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(3, 6), Pad.at(0, 3), Pad.at(1, 4), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6));
        addCharPixels('7', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(0, 1), Pad.at(2, 4), Pad.at(1, 5), Pad.at(0, 6), Pad.at(3, 1));
        addCharPixels('8', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(2, 3), Pad.at(3, 4), Pad.at(0, 1), Pad.at(3, 5), Pad.at(0, 2), Pad.at(1, 3), Pad.at(3, 6), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('9', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(3, 4), Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(3, 6), Pad.at(0, 3), Pad.at(1, 4), Pad.at(3, 1));
        addCharPixels('A', Pad.at(2, 1), Pad.at(3, 2), Pad.at(4, 3), Pad.at(4, 4), Pad.at(3, 4), Pad.at(4, 5), Pad.at(1, 2), Pad.at(2, 4), Pad.at(4, 6), Pad.at(0, 3), Pad.at(1, 4), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('B', Pad.at(2, 1), Pad.at(3, 2), Pad.at(1, 1), Pad.at(2, 3), Pad.at(3, 4), Pad.at(0, 1), Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(1, 3), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('C', Pad.at(2, 1), Pad.at(1, 1), Pad.at(0, 2), Pad.at(3, 6), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(3, 1));
        addCharPixels('D', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(3, 4), Pad.at(0, 1), Pad.at(3, 5), Pad.at(0, 2), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('E', Pad.at(2, 1), Pad.at(3, 3), Pad.at(1, 1), Pad.at(2, 3), Pad.at(0, 1), Pad.at(0, 2), Pad.at(1, 3), Pad.at(3, 6), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(0, 6), Pad.at(3, 1));
        addCharPixels('F', Pad.at(2, 1), Pad.at(3, 3), Pad.at(1, 1), Pad.at(2, 3), Pad.at(0, 1), Pad.at(0, 2), Pad.at(1, 3), Pad.at(0, 3), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6), Pad.at(3, 1));
        addCharPixels('G', Pad.at(2, 1), Pad.at(1, 1), Pad.at(3, 4), Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(3, 6), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(3, 1));
        addCharPixels('H', Pad.at(3, 2), Pad.at(3, 3), Pad.at(2, 3), Pad.at(3, 4), Pad.at(0, 1), Pad.at(3, 5), Pad.at(0, 2), Pad.at(1, 3), Pad.at(3, 6), Pad.at(0, 3), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6), Pad.at(3, 1));
        addCharPixels('I', Pad.at(2, 1), Pad.at(1, 1), Pad.at(0, 1), Pad.at(1, 2), Pad.at(1, 3), Pad.at(1, 4), Pad.at(2, 6), Pad.at(1, 5), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('J', Pad.at(2, 1), Pad.at(2, 2), Pad.at(1, 1), Pad.at(2, 3), Pad.at(0, 1), Pad.at(2, 4), Pad.at(2, 5), Pad.at(2, 6), Pad.at(0, 5), Pad.at(1, 6), Pad.at(3, 1));
        addCharPixels('K', Pad.at(2, 2), Pad.at(0, 1), Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(1, 3), Pad.at(3, 6), Pad.at(0, 3), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('L', Pad.at(0, 1), Pad.at(0, 2), Pad.at(3, 6), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('M', Pad.at(3, 2), Pad.at(4, 3), Pad.at(4, 4), Pad.at(2, 3), Pad.at(4, 5), Pad.at(0, 1), Pad.at(1, 2), Pad.at(4, 6), Pad.at(0, 2), Pad.at(0, 3), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6), Pad.at(4, 1), Pad.at(4, 2));
        addCharPixels('N', Pad.at(3, 2), Pad.at(3, 3), Pad.at(2, 3), Pad.at(3, 4), Pad.at(0, 1), Pad.at(1, 2), Pad.at(3, 5), Pad.at(0, 2), Pad.at(3, 6), Pad.at(0, 3), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6), Pad.at(3, 1));
        addCharPixels('O', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(3, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6));
        addCharPixels('P', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(0, 1), Pad.at(2, 4), Pad.at(0, 2), Pad.at(0, 3), Pad.at(1, 4), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('Q', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(3, 4), Pad.at(3, 5), Pad.at(4, 6), Pad.at(0, 2), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6));
        addCharPixels('R', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(0, 1), Pad.at(2, 4), Pad.at(0, 2), Pad.at(2, 5), Pad.at(3, 6), Pad.at(0, 3), Pad.at(1, 4), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('S', Pad.at(2, 1), Pad.at(1, 1), Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(0, 3), Pad.at(1, 4), Pad.at(2, 6), Pad.at(1, 6), Pad.at(0, 6), Pad.at(3, 1));
        addCharPixels('T', Pad.at(2, 1), Pad.at(1, 1), Pad.at(0, 1), Pad.at(1, 2), Pad.at(1, 3), Pad.at(1, 4), Pad.at(1, 5), Pad.at(1, 6), Pad.at(3, 1));
        addCharPixels('U', Pad.at(3, 2), Pad.at(3, 3), Pad.at(3, 4), Pad.at(0, 1), Pad.at(3, 5), Pad.at(0, 2), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(3, 1));
        addCharPixels('V', Pad.at(3, 2), Pad.at(3, 3), Pad.at(3, 4), Pad.at(0, 1), Pad.at(3, 5), Pad.at(0, 2), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(1, 5), Pad.at(3, 1));
        addCharPixels('W', Pad.at(4, 3), Pad.at(4, 4), Pad.at(4, 5), Pad.at(0, 1), Pad.at(2, 4), Pad.at(3, 5), Pad.at(4, 6), Pad.at(0, 2), Pad.at(0, 3), Pad.at(0, 4), Pad.at(1, 5), Pad.at(0, 5), Pad.at(0, 6), Pad.at(4, 1), Pad.at(4, 2));
        addCharPixels('X', Pad.at(3, 2), Pad.at(2, 3), Pad.at(0, 1), Pad.at(1, 2), Pad.at(2, 4), Pad.at(3, 5), Pad.at(4, 6), Pad.at(1, 5), Pad.at(0, 6), Pad.at(4, 1));
        addCharPixels('Y', Pad.at(3, 3), Pad.at(0, 1), Pad.at(2, 4), Pad.at(0, 2), Pad.at(1, 3), Pad.at(2, 5), Pad.at(2, 6), Pad.at(4, 1), Pad.at(4, 2));
        addCharPixels('Z', Pad.at(2, 1), Pad.at(3, 2), Pad.at(1, 1), Pad.at(2, 3), Pad.at(0, 1), Pad.at(3, 6), Pad.at(1, 4), Pad.at(2, 6), Pad.at(0, 5), Pad.at(1, 6), Pad.at(0, 6), Pad.at(3, 1));
        addCharPixels('a', Pad.at(3, 2), Pad.at(4, 3), Pad.at(2, 2), Pad.at(4, 4), Pad.at(4, 5), Pad.at(1, 2), Pad.at(3, 5), Pad.at(4, 6), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6));
        addCharPixels('b', Pad.at(2, 3), Pad.at(3, 4), Pad.at(0, 1), Pad.at(3, 5), Pad.at(0, 2), Pad.at(1, 3), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('c', Pad.at(2, 2), Pad.at(3, 3), Pad.at(1, 2), Pad.at(3, 5), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6));
        addCharPixels('d', Pad.at(3, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(2, 3), Pad.at(3, 4), Pad.at(3, 5), Pad.at(1, 3), Pad.at(3, 6), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(3, 1));
        addCharPixels('e', Pad.at(3, 2), Pad.at(2, 2), Pad.at(3, 3), Pad.at(1, 2), Pad.at(2, 4), Pad.at(3, 6), Pad.at(0, 3), Pad.at(1, 4), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6));
        addCharPixels('f', Pad.at(3, 2), Pad.at(2, 2), Pad.at(3, 4), Pad.at(1, 2), Pad.at(2, 4), Pad.at(0, 3), Pad.at(1, 4), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('g', Pad.at(2, 1), Pad.at(3, 2), Pad.at(3, 3), Pad.at(1, 1), Pad.at(2, 3), Pad.at(3, 4), Pad.at(3, 5), Pad.at(0, 2), Pad.at(1, 3), Pad.at(2, 6), Pad.at(0, 5), Pad.at(1, 6));
        addCharPixels('h', Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 1), Pad.at(0, 2), Pad.at(3, 6), Pad.at(0, 3), Pad.at(1, 4), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('i', Pad.at(0, 1), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6));
        addCharPixels('j', Pad.at(3, 3), Pad.at(3, 4), Pad.at(3, 5), Pad.at(2, 6), Pad.at(0, 5), Pad.at(1, 6), Pad.at(3, 1));
        addCharPixels('k', Pad.at(2, 4), Pad.at(0, 1), Pad.at(0, 2), Pad.at(2, 6), Pad.at(0, 3), Pad.at(1, 5), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('l', Pad.at(0, 2), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(0, 1));
        addCharPixels('m', Pad.at(4, 3), Pad.at(3, 3), Pad.at(4, 4), Pad.at(4, 5), Pad.at(2, 4), Pad.at(4, 6), Pad.at(0, 2), Pad.at(1, 3), Pad.at(0, 3), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('n', Pad.at(3, 3), Pad.at(3, 4), Pad.at(3, 5), Pad.at(2, 5), Pad.at(3, 6), Pad.at(0, 3), Pad.at(1, 4), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('o', Pad.at(2, 2), Pad.at(3, 3), Pad.at(3, 4), Pad.at(1, 2), Pad.at(3, 5), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6));
        addCharPixels('p', Pad.at(2, 3), Pad.at(1, 2), Pad.at(0, 2), Pad.at(0, 3), Pad.at(1, 4), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('q', Pad.at(3, 2), Pad.at(2, 2), Pad.at(3, 3), Pad.at(3, 4), Pad.at(1, 2), Pad.at(2, 4), Pad.at(3, 5), Pad.at(3, 6), Pad.at(0, 3), Pad.at(1, 4));
        addCharPixels('r', Pad.at(3, 2), Pad.at(2, 2), Pad.at(0, 2), Pad.at(1, 3), Pad.at(0, 3), Pad.at(0, 4), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('s', Pad.at(3, 2), Pad.at(2, 2), Pad.at(1, 2), Pad.at(2, 4), Pad.at(3, 5), Pad.at(0, 3), Pad.at(1, 4), Pad.at(2, 6), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('t', Pad.at(3, 3), Pad.at(2, 3), Pad.at(1, 2), Pad.at(1, 3), Pad.at(0, 3), Pad.at(1, 4), Pad.at(2, 6), Pad.at(1, 5));
        addCharPixels('u', Pad.at(3, 3), Pad.at(3, 4), Pad.at(3, 5), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(0, 5), Pad.at(1, 6), Pad.at(3, 6));
        addCharPixels('v', Pad.at(3, 3), Pad.at(3, 4), Pad.at(3, 5), Pad.at(0, 3), Pad.at(2, 6), Pad.at(0, 4), Pad.at(1, 5));
        addCharPixels('w', Pad.at(4, 3), Pad.at(4, 4), Pad.at(4, 5), Pad.at(2, 4), Pad.at(3, 5), Pad.at(4, 6), Pad.at(0, 3), Pad.at(0, 4), Pad.at(1, 5), Pad.at(0, 5), Pad.at(0, 6));
        addCharPixels('x', Pad.at(3, 3), Pad.at(2, 4), Pad.at(3, 5), Pad.at(4, 6), Pad.at(0, 2), Pad.at(1, 3), Pad.at(1, 5), Pad.at(0, 6), Pad.at(4, 2));
        addCharPixels('y', Pad.at(3, 2), Pad.at(3, 3), Pad.at(3, 4), Pad.at(0, 2), Pad.at(2, 5), Pad.at(0, 3), Pad.at(1, 4), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('z', Pad.at(3, 2), Pad.at(2, 2), Pad.at(3, 3), Pad.at(1, 2), Pad.at(2, 4), Pad.at(0, 2), Pad.at(3, 6), Pad.at(2, 6), Pad.at(1, 5), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels(' ', (Pad) null);
        addCharPixels('!', Pad.at(0, 0), Pad.at(1, 0), Pad.at(0, 1), Pad.at(0, 2), Pad.at(0, 3), Pad.at(0, 5), Pad.at(0, 6), Pad.at(1, 1), Pad.at(1, 2), Pad.at(1, 3), Pad.at(1, 5), Pad.at(1, 6));
        addCharPixels('.', Pad.at(1, 5), Pad.at(0, 5), Pad.at(1, 6), Pad.at(0, 6));
        addCharPixels('-', Pad.at(0, 4), Pad.at(1, 4), Pad.at(2, 4));
        addCharPixels('<', Pad.at(2, 2), Pad.at(1, 3), Pad.at(2, 5), Pad.at(3, 6), Pad.at(1, 4), Pad.at(0, 4), Pad.at(3, 1));

    }

    private Timer timer = new Timer();
    private TextScrollListener listener;
    private boolean isScrolling = false;

    private static void addCharPixels(char c, Pad pad) {
        addCharPixels(c, new Pad[]{pad});
    }

    private static void addCharPixels(char c, Pad... pad) {
        charPixelMap.put(c, Arrays.asList(pad));
    }

    public synchronized void scrollText(String text, LaunchpadClient launchpad) {
        List<Point> charPositions = getCharPositions(text);
        List<Point> visibleCharPositions = new ArrayList<>();
        List<Point> oldVisiblePositions = new ArrayList<>();

        int stringWidth2 = 0;
        for (Point point : charPositions) {
            stringWidth2 = Math.max(stringWidth2, point.x);
        }
        final int stringWidth = stringWidth2 + 1;

        timer.cancel();
        timer = new Timer();
        isScrolling = true;

        launchpad.setBuffers(Buffer.BUFFER_0, Buffer.BUFFER_1, true, false);
        clearPads(launchpad);
        timer.scheduleAtFixedRate(new TimerTask() {
            boolean writeToBuf1 = true;
            int i = 8;

            @Override
            public void run() {
                if (i >= -stringWidth) {
                    oldVisiblePositions.clear();
                    oldVisiblePositions.addAll(visibleCharPositions);
                    fillVisiblePositions(visibleCharPositions, charPositions, i);
                    clearPreviousPositions(oldVisiblePositions, visibleCharPositions, launchpad);
                    showCharacters(visibleCharPositions, launchpad);

                    launchpad.setBuffers(writeToBuf1 ? Buffer.BUFFER_1 : Buffer.BUFFER_0, writeToBuf1 ? Buffer.BUFFER_0 : Buffer.BUFFER_1, true, false);
                    writeToBuf1 = !writeToBuf1;
                } else {
                    timer.cancel();
                    isScrolling = false;
                    if (listener != null) {
                        listener.onScrollFinished();
                    }
                }
                i--;
            }
        }, 0, 100);
    }

    private void clearPads(LaunchpadClient launchpad) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                launchpad.setPadLight(Pad.at(i, j), Color.BLACK, BackBufferOperation.NONE);
            }
        }
    }

    public synchronized boolean isScrolling() {
        return isScrolling;
    }

    private void fillVisiblePositions(List<Point> visibleCharPositions, List<Point> charPositions, int i) {
        visibleCharPositions.clear();
        for (Point position : charPositions) {
            int xOffset = position.x + i;
            if (xOffset >= 0 && xOffset < 8 && position.y >= 0 && position.y < 8) {
                visibleCharPositions.add(new Point(xOffset, position.y));
            }
        }
    }

    private void showCharacters(List<Point> charPositions, LaunchpadClient launchpad) {
        for (Point position : charPositions) {
            launchpad.setPadLight(Pad.at(position.x, position.y), Color.GREEN, BackBufferOperation.NONE);
        }
    }

    private List<Point> getCharPositions(String text) {
        List<Point> list = new ArrayList<>();
        int characterOffset = 0;
        for (char c : text.toCharArray()) {
            List<Pad> pads = charPixelMap.get(c);
            int charWidth = 0;
            for (Pad pad : pads) {
                if (pad == null) {
                    continue;
                }
                list.add(new Point(pad.getX() + characterOffset, pad.getY()));
                charWidth = Math.max(charWidth, pad.getX() + 1);
            }
            characterOffset += charWidth + 1;
        }
        return list;
    }

    private void clearPreviousPositions(List<Point> oldVisiblePositions, List<Point> visiblePositions, LaunchpadClient launchpad) {
        oldVisiblePositions.stream().filter(pair -> !visiblePositions.contains(pair)).forEach(pair -> launchpad.setPadLight(Pad.at(pair.x, pair.y), Color.BLACK, BackBufferOperation.NONE));
    }

    public void setListener(TextScrollListener listener) {
        this.listener = listener;
    }

    public interface TextScrollListener {
        void onScrollFinished();
    }
}
