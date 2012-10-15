package org.apache.rave.portal.repository.impl;

import com.google.common.collect.Lists;
import org.apache.rave.portal.model.*;
import org.apache.rave.portal.model.impl.*;
import org.apache.rave.portal.repository.PageLayoutRepository;
import org.apache.rave.portal.repository.PageRepository;
import org.apache.rave.portal.repository.UserRepository;
import org.apache.rave.portal.repository.WidgetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: mfranklin
 * Date: 10/14/12
 * Time: 8:14 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml"})
public class MongoDbPageRepositoryTest {

    @Autowired
    PageRepository repository;

    @Autowired
    WidgetRepository widgetRepository;

    @Autowired
    PageLayoutRepository pageLayoutRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void MongoTest() {
        Page page = new PageImpl();

        User user1 = new MongoDbUser(12345L);
        user1.setDisplayName("GEORGE DOE");
        userRepository.save(user1);

        User user2 = new MongoDbUser(12345L);
        user2.setDisplayName("JANE DOE");
        userRepository.save(user2);

        PageUser p = new PageUserImpl(user1, page);
        page.setName("PAGE NAME");
        page.setMembers(Lists.<PageUser>newLinkedList());
        page.getMembers().add(p);
        page.setOwner(user2);
        page.setRegions(Lists.<Region>newLinkedList());

        Region region = new RegionImpl();
        region.setPage(page);
        region.setRegionWidgets(Lists.<RegionWidget>newLinkedList());
        page.getRegions().add(region);

        RegionWidget regionWidget = new RegionWidgetImpl();
        regionWidget.setRegion(region);
        regionWidget.setPreferences(Lists.<RegionWidgetPreference>newLinkedList());
        region.getRegionWidgets().add(regionWidget);

        RegionWidgetPreference preference = new RegionWidgetPreferenceImpl();
        preference.setName("PREF NAME");
        preference.setValue("PREF_VALUE");
        regionWidget.getPreferences().add(preference);

        Widget widget = new MongoDbWidget(13223L);
        widget.setAuthor("FOO");
        widget.setDescription("BAR");
        widgetRepository.save(widget);

        regionWidget.setWidget(widget);

        page.setPageType(PageType.USER);
        PageLayout layout = new MongoDbPageLayout("LAYOUT");
        page.setPageLayout(layout);
        layout.setNumberOfRegions(24L);
        pageLayoutRepository.save(layout);

        Page saved = repository.save(page);
        assertThat(saved, instanceOf(MongoDbPage.class));

        Page fromDb = repository.get(saved.getId());
        assertThat(fromDb.getMembers().get(0).getUser(), is(equalTo(saved.getMembers().get(0).getUser())));
        assertThat(fromDb, is(sameInstance(fromDb.getMembers().get(0).getPage())));
        assertThat(fromDb.getPageLayout(), is(equalTo(saved.getPageLayout())));
        assertThat(fromDb.getRegions().get(0), is(equalTo(saved.getRegions().get(0))));
        assertThat(fromDb.getRegions().get(0).getRegionWidgets().get(0), is(equalTo(saved.getRegions().get(0).getRegionWidgets().get(0))));
        assertThat(fromDb.getRegions().get(0).getRegionWidgets().get(0).getWidget(), is(equalTo(saved.getRegions().get(0).getRegionWidgets().get(0).getWidget())));
        assertThat(fromDb.getRegions().get(0).getRegionWidgets().get(0).getPreferences().get(0), is(equalTo(saved.getRegions().get(0).getRegionWidgets().get(0).getPreferences().get(0))));
    }
}
