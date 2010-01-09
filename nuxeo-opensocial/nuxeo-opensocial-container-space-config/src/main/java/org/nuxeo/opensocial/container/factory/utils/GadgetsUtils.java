package org.nuxeo.opensocial.container.factory.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shindig.gadgets.GadgetSpecFactory;
import org.apache.shindig.gadgets.spec.GadgetSpec;
import org.apache.shindig.gadgets.spec.UserPref;
import org.nuxeo.opensocial.container.factory.mapping.GadgetMapper;
import org.nuxeo.opensocial.gadgets.service.api.GadgetDeclaration;
import org.nuxeo.opensocial.gadgets.service.api.GadgetService;
import org.nuxeo.opensocial.service.api.OpenSocialService;
import org.nuxeo.runtime.api.Framework;

public class GadgetsUtils {

    public static final Log log = LogFactory.getLog(GadgetsUtils.class);

    public static GadgetSpec getGadgetSpec(GadgetMapper gadget)
            throws Exception {
        OpenSocialService service;
        service = Framework.getService(OpenSocialService.class);
        String gadgetDef = UrlBuilder.getGadgetDef(gadget.getName());
        GadgetSpecFactory gadgetSpecFactory = service.getGadgetSpecFactory();
        NxGadgetContext context = new NxGadgetContext(gadgetDef,
                gadget.getViewer(), gadget.getOwner());

        return gadgetSpecFactory.getGadgetSpec(context);
    }

    /**
     * Return the gadget title retrieved from the registered gadget. Title is
     * retrieve from userpref if it exists and modulepref otherwise.
     * 
     * @param gadgetName
     * @param defaultValue to be return if any issue
     * @return
     */
    public static String getGadgetTitle(String gadgetName, String defaultValue)
            throws Exception {
        GadgetSpec gadgetspec = createGadgetSpecFromRegisteredGadget(gadgetName);
        return getGadgetTitle(gadgetspec, gadgetName);
    }

    /**
     * Create a gadgetSpec using a the gadget name: use the one registered in
     * the gadget service
     * 
     * @param gadgetName
     * @return
     * @throws Exception
     */
    public static GadgetSpec createGadgetSpecFromRegisteredGadget(
            String gadgetName) throws Exception {
        GadgetService gadgetService = Framework.getService(GadgetService.class);

        GadgetDeclaration gadget = gadgetService.getGadget(gadgetName);

        InputStream stream = gadgetService.getGadgetResource(gadgetName,
                gadget.getEntryPoint());

        return createGadgetSpec(stream);
    }

    /**
     * Create a gadgetSpec from the local input.
     * 
     * Warning: using GadgetSpec(url, xml) constructor with null as url.
     * 
     * @param input
     * @return
     * @throws Exception
     */
    public static GadgetSpec createGadgetSpec(InputStream input)
            throws Exception {
        StringWriter writer = new StringWriter();
        InputStreamReader streamReader = new InputStreamReader(input);

        BufferedReader buffer = new BufferedReader(streamReader);
        String line = "";
        while (null != (line = buffer.readLine())) {
            writer.write(line);
        }
        buffer.close();
        streamReader.close();
        writer.close();

        return new GadgetSpec(null, writer.toString());
    }

    /**
     * Choose the correct title from the gadgetSpec. Check in the user
     * preference first, than in the module ones. use defaultValue if nothing
     * has been found or if any error occured.
     * 
     * @param spec
     * @param defaultValue
     * @return
     */
    public static String getGadgetTitle(GadgetSpec spec, String defaultValue) {
        try {
            List<UserPref> userPrefs = spec.getUserPrefs();

            for (UserPref userPref : userPrefs) {
                if ("title".equals(userPref.getName())) {
                    return userPref.getDefaultValue();
                }
            }
            String moduleTitle = spec.getModulePrefs().getTitle();
            if (moduleTitle != null) {
                return moduleTitle;
            }
        } catch (Exception e) {
            log.warn("Couldn't get a nice title from the gadget spec. Use the default value instead: "
                    + defaultValue);
        }
        return defaultValue;
    }

}
