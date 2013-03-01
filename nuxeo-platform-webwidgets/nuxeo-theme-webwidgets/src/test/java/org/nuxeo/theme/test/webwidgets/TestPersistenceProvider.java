package org.nuxeo.theme.test.webwidgets;

import org.nuxeo.theme.webwidgets.providers.PersistentProvider;

public class TestPersistenceProvider extends AbstractTestPersistentProvider {

    @Override
    protected PersistentProvider newProvider() {
        return new PersistentProvider();
    }

}
