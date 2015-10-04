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

package com.greenhouseci.launchpad.dto;


import com.squareup.okhttp.HttpUrl;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="Project")
public class Project {

    @Attribute
    private String webUrl;
    @Attribute
    private String name;
    @Attribute
    private String lastBuildLabel;
    @Attribute
    private String lastBuildTime;
    @Attribute
    private String lastBuildStatus;
    @Attribute
    private String activity;

    public String getWebUrl() {
        return webUrl;
    }

    public String getName() {
        return name;
    }

    public String getLastBuildLabel() {
        return lastBuildLabel;
    }

    public String getLastBuildTime() {
        return lastBuildTime;
    }

    public String getLastBuildStatus() {
        return lastBuildStatus;
    }

    public String getActivity() {
        return activity;
    }

    @Override
    public String toString() {
        return "Project{" +
                "webUrl='" + webUrl + '\'' +
                ", name='" + name + '\'' +
                ", lastBuildLabel='" + lastBuildLabel + '\'' +
                ", lastBuildTime='" + lastBuildTime + '\'' +
                ", lastBuildStatus='" + lastBuildStatus + '\'' +
                ", activity='" + activity + '\'' +
                '}';
    }

    public String getProjectId() {
        String fragment = HttpUrl.parse(getWebUrl()).fragment();
        return fragment.substring(fragment.lastIndexOf("/") + 1);
    }
}
