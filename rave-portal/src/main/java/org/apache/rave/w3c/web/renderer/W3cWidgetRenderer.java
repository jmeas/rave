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

package org.apache.rave.w3c.web.renderer;

import static org.apache.rave.w3c.Constants.WIDGET_TYPE;

import org.apache.rave.exception.NotSupportedException;
import org.apache.rave.portal.model.RegionWidget;
import org.apache.rave.portal.model.Widget;
import org.apache.rave.portal.service.WidgetService;
import org.apache.rave.portal.web.renderer.RegionWidgetRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Renders W3C widgets via the injected Wookie service
 */
@Component
public class W3cWidgetRenderer implements RegionWidgetRenderer {


    private static final String IFRAME_MARKUP = "<script type=\"text/javascript\">" +
                                                    "widgets.push({type: '%1$s'," +
                                                                 " regionWidgetId: %2$s," +
                                                                 " widgetUrl: '%3$s'});" +
                                                "</script>";

    private static final String INLINE_MARKUP = "";

    private final WidgetService widgetService;

    @Autowired
    public W3cWidgetRenderer(@Qualifier("wookieWidgetService") WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @Override
    public String getSupportedContext() {
        return WIDGET_TYPE;
    }

    @Override
    public String render(RegionWidget item) {
        Widget widget = item.getWidget();
        if(!WIDGET_TYPE.equals(widget.getType())) {
            throw new NotSupportedException("Invalid widget type passed to renderer: " + widget.getType());
        }
        Widget contextualizedWidget = widgetService.getWidget(null, null, widget);
        String url = contextualizedWidget == null ? null : contextualizedWidget.getUrl();
        return String.format(IFRAME_MARKUP, WIDGET_TYPE, item.getId(), url);
    }
}
