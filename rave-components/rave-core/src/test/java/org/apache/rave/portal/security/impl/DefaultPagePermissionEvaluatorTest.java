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

package org.apache.rave.portal.security.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.rave.portal.model.Page;
import org.apache.rave.portal.model.User;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.security.ModelPermissionEvaluator.Permission;
import org.apache.rave.portal.security.util.AuthenticationUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

/**
 *
 * @author carlucci
 */
public class DefaultPagePermissionEvaluatorTest {
    private DefaultPagePermissionEvaluator defaultPagePermissionEvaluator;
    private PageRepository mockPageRepository;
    private Authentication mockAuthentication;
    private Page page;
    private User user, user2;    
    private List<GrantedAuthority> grantedAuthoritiesList;
    
    private final Long VALID_PAGE_ID = 3L;
    private final String VALID_USERNAME = "john.doe";
    private final String VALID_USERNAME2 = "jane.doe";
    
    @Before
    public void setUp() {               
        mockPageRepository = createMock(PageRepository.class);
        mockAuthentication = createMock(Authentication.class);
        defaultPagePermissionEvaluator = new DefaultPagePermissionEvaluator(mockPageRepository);
        
        user = new User();
        user.setUsername(VALID_USERNAME);
        user2 = new User();
        user2.setUsername(VALID_USERNAME2);
        page = new Page();
        page.setEntityId(VALID_PAGE_ID);
        page.setOwner(user);
        grantedAuthoritiesList = new ArrayList<GrantedAuthority>();
        grantedAuthoritiesList.add(new GrantedAuthorityImpl("ROLE_USER"));
    }
 
   
    @Test
    public void testGetType() throws ClassNotFoundException {            
        assertThat(defaultPagePermissionEvaluator.getType().getName(), is(Page.class.getName()));
    }
  
    @Test
    public void testHasPermission_3args_administer() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);              
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.ADMINISTER), is(false));        
        verify(mockAuthentication);
    }
    
    @Test
    public void testHasPermission_3args_administer_hasAdminRole() {                             
        grantedAuthoritiesList.add(new GrantedAuthorityImpl(AuthenticationUtils.ROLE_ADMIN));
        
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);              
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.ADMINISTER), is(true));        
        verify(mockAuthentication);
    }    
    
    @Test
    public void testHasPermission_3args_create() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);      
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.CREATE), is(true));        
        verify(mockAuthentication);
    }    
    
    @Test
    public void testHasPermission_3args_delete_isPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.DELETE), is(true));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }        
    
    @Test
    public void testHasPermission_3args_delete_notPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.DELETE), is(false));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }       
    
    @Test
    public void testHasPermission_3args_read_isPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.READ), is(true));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }       
    
    @Test
    public void testHasPermission_3args_read_notPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.READ), is(false));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }      
    
    @Test
    public void testHasPermission_3args_update_isPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.UPDATE), is(true));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }   
    
    @Test
    public void testHasPermission_3args_update_notPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, page, Permission.UPDATE), is(false));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }         
    
    @Test
    public void testHasPermission_4args_administer() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);              
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.ADMINISTER), is(false));        
        verify(mockAuthentication);
    }    
    
    @Test
    public void testHasPermission_4args_create() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        replay(mockAuthentication);      
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.CREATE), is(true));        
        verify(mockAuthentication);
    }     
    
    @Test
    public void testHasPermission_4args_delete_isPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.DELETE), is(true));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }        
        
    @Test
    public void testHasPermission_4args_delete_notPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.DELETE), is(false));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }       
    
    @Test
    public void testHasPermission_4args_read_isPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.READ), is(true));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }       
    
    @Test
    public void testHasPermission_4args_read_notPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.READ), is(false));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }      
    
    @Test
    public void testHasPermission_4args_update_isPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.UPDATE), is(true));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }   
    
    @Test
    public void testHasPermission_4args_update_notPageOwner() {                             
        expect(mockAuthentication.getAuthorities()).andReturn(grantedAuthoritiesList);
        expect(mockAuthentication.getPrincipal()).andReturn(user2);
        expect(mockPageRepository.get(VALID_PAGE_ID)).andReturn(page);
        replay(mockAuthentication);      
        replay(mockPageRepository);
        assertThat(defaultPagePermissionEvaluator.hasPermission(mockAuthentication, VALID_PAGE_ID, Page.class.getName(), Permission.UPDATE), is(false));        
        verify(mockAuthentication);
        verify(mockPageRepository);
    }         
}