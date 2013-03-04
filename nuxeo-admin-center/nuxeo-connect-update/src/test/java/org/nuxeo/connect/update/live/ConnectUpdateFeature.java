package org.nuxeo.connect.update.live;

import org.nuxeo.connect.update.standalone.PackageUpdateFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.RuntimeFeature;
import org.nuxeo.runtime.test.runner.SimpleFeature;

@Features({PackageUpdateFeature.class, RuntimeFeature.class})
@Deploy({ "org.nuxeo.connect.client", "org.nuxeo.connect.client.wrapper",
        "org.nuxeo.connect.update", "org.nuxeo.runtime.reload" })
public class ConnectUpdateFeature extends SimpleFeature{

}
