/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.rave.portal.web.controller;

import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.Region;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.service.PageService;
import org.apache.rave.portal.service.UserService;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.util.ModelKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Minimal Home Controller
 * 
 * @version $Id$
 */
@Controller
public class HomeController {
    private PageService pageService;
    private UserService userService;
    private WidgetService widgetService;

    @Autowired
    public HomeController(PageService pageService, UserService userService, WidgetService widgetService) {
        this.pageService = pageService;
        this.userService = userService;
        this.widgetService = widgetService;
    }

    @RequestMapping(value = {"/", "/index.html"})
    public String getHome(Model model) {
        User user = userService.getAuthenticatedUser();
        List<Page> pages = pageService.getAllPages(user.getUserId());
        for (Page page: pages){
            for (Region region: page.getRegions()){
                for (RegionWidget regionWidget: region.getRegionWidgets()){
                    regionWidget.setWidget(widgetService.getWidget(user, regionWidget.getId().toString(), regionWidget.getWidget()));
                }
            }
        }
        model.addAttribute(ModelKeys.PAGES, pages);
        return "home";
    }
}