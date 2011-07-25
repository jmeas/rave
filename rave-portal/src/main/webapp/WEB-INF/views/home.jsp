<%--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  
  $Id$

--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="portal" uri="http://www.apache.org/rave/tags" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="rave"%>
<jsp:useBean id="pages" type="java.util.List<org.apache.rave.portal.model.Page>" scope="request"/>
<jsp:useBean id="openSocialEnv" scope="request" type="org.apache.rave.provider.opensocial.config.OpenSocialEnvironment"/>
<c:set var="opensocial_engine_url" value="${openSocialEnv.engineProtocol}://${openSocialEnv.engineRoot}${openSocialEnv.engineGadgetPath}"/>

<rave:rave_generic_page pageTitle="Home - Rave">
    <c:set var="defaultPage" value="${pages[0]}"/>
    <div id="header">
        <div class="header-a">
            <a href="<spring:url value="/j_spring_security_logout" htmlEscape="true" />">Logout</a>
        </div>
        <div class="widget-a">
            <a href="<spring:url value="/app/store?referringPageId=${defaultPage.id}" />">Widget Store</a>
        </div>
        <h1>Hello ${defaultPage.owner.username}, welcome to Rave!</h1>
    </div>
    <div id="content">
        <c:forEach var="region" items="${defaultPage.regions}">
            <div class="region" id="region-${region.id}-id">
                <c:forEach var="regionWidget" items="${region.regionWidgets}">
                    <div class="widget-wrapper" id="widget-${regionWidget.id}-wrapper">
                        <div class="widget-title-bar">
                            <span id="widget-${regionWidget.id}-title">${regionWidget.widget.title}</span>
                            <!-- These are toolbar buttons -->
                            <div id="widget-${regionWidget.id}-toolbar" style="float:right;">
                                <button id="widget-${regionWidget.id}-prefs"
                                        class="widget-toolbar-btn widget-toolbar-btn-prefs">
                                </button>
                                <button id="widget-${regionWidget.id}-max"
                                        class="widget-toolbar-btn">
                                </button>
                                <button id="widget-${regionWidget.id}-remove"
                                        class="widget-toolbar-btn">
                                </button>
                            </div>
                        </div>
                        <div class="widget-prefs" id="widget-${regionWidget.id}-prefs-content"></div>
                        <div class="widget" id="widget-${regionWidget.id}-body">
                                <%-- Widget will be rendered here --%>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
        <div class="clear-float">&nbsp;</div>
    </div>
    <script src="//cdnjs.cloudflare.com/ajax/libs/json2/20110223/json2.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
    <script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.8.13/jquery-ui.min.js"></script>
    <script src="${opensocial_engine_url}/js/container.js?c=1&container=default&debug=1"></script>
    <script src="<spring:url value="/script/rave.js"/>"></script>
    <script src="<spring:url value="/script/rave_api.js"/>"></script>
    <script src="<spring:url value="/script/rave_opensocial.js"/>"></script>
    <script src="<spring:url value="/script/rave_wookie.js"/>"></script>
    <script>
        //Define the global widgets variable
        //This array will be populated by RegionWidgetRender providers.
        var widgets = [];
        <%--
           Among other things, the render-widget tag will populate the widgets[] array.
           See the markup text in OpenSocialWidgetRenderer.java, for example.
        --%>
        <c:forEach var="region" items="${defaultPage.regions}">
        <c:forEach var="regionWidget" items="${region.regionWidgets}">
        <portal:render-widget regionWidget="${regionWidget}" />
        </c:forEach>
        </c:forEach>

        $(function() {
            rave.setContext("<spring:url value="/app/" />");
            rave.initProviders();
            rave.initWidgets(widgets);
            rave.initUI();
        });
    </script>
</rave:rave_generic_page>
