/*
 * (C) Copyright 2007-2011 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 *
 * Contributors:
 *     Catalin Baican
 *     Florent Guillaume
 *
 * Some code extracted from Apache Tika ImageMetadataExtractor.
 */

package org.nuxeo.ecm.platform.picture;

import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_BY_LINE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_BY_LINE_TITLE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_CAPTION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_CATEGORY;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_CITY;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_COPYRIGHT_NOTICE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_COUNTRY_OR_PRIMARY_LOCATION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_CREDIT;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_DATE_CREATED;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_HEADLINE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_KEYWORDS;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_LANGUAGE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_OBJECT_NAME;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_ORIGINAL_TRANSMISSION_REFERENCE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_ORIGINATING_PROGRAM;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_PROVINCE_OR_STATE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_RECORD_VERSION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_RELEASE_DATE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_RELEASE_TIME;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_SOURCE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_SPECIAL_INSTRUCTIONS;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_SUPPLEMENTAL_CATEGORIES;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_TIME_CREATED;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_URGENCY;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_WRITER;

import java.util.Map;

import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.iptc.IptcDirectory;

public class IPTCHelper {

    public static void extract(Metadata md, Map<String, Object> metadata) throws MetadataException {

        IptcDirectory iptc = md.getDirectory(IptcDirectory.class);

        if (iptc == null) {
            return;
        }

        if (iptc.containsTag(IptcDirectory.TAG_BY_LINE)) {
            metadata.put(META_BY_LINE,
                    cleanupData(iptc.getString(IptcDirectory.TAG_BY_LINE)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_BY_LINE_TITLE)) {
            metadata.put(
                    META_BY_LINE_TITLE,
                    cleanupData(iptc.getString(IptcDirectory.TAG_BY_LINE_TITLE)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_CAPTION)) {
            metadata.put(META_CAPTION,
                    cleanupData(iptc.getString(IptcDirectory.TAG_CAPTION)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_CATEGORY)) {
            metadata.put(META_CATEGORY,
                    cleanupData(iptc.getString(IptcDirectory.TAG_CATEGORY)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_CITY)) {
            metadata.put(META_CITY,
                    cleanupData(iptc.getString(IptcDirectory.TAG_CITY)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_COPYRIGHT_NOTICE)) {
            metadata.put(
                    META_COPYRIGHT_NOTICE,
                    cleanupData(iptc.getString(IptcDirectory.TAG_COPYRIGHT_NOTICE)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME)) {
            metadata.put(
                    META_COUNTRY_OR_PRIMARY_LOCATION,
                    cleanupData(iptc.getString(IptcDirectory.TAG_COUNTRY_OR_PRIMARY_LOCATION_NAME)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_CREDIT)) {
            metadata.put(META_CREDIT,
                    cleanupData(iptc.getString(IptcDirectory.TAG_CREDIT)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_DATE_CREATED)) {
            metadata.put(META_DATE_CREATED,
                        iptc.getDate(IptcDirectory.TAG_DATE_CREATED));
        }

        if (iptc.containsTag(IptcDirectory.TAG_HEADLINE)) {
            metadata.put(META_HEADLINE,
                    cleanupData(iptc.getString(IptcDirectory.TAG_HEADLINE)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_KEYWORDS)) {
            metadata.put(META_KEYWORDS,
                    cleanupData(iptc.getString(IptcDirectory.TAG_KEYWORDS)));
        }

        if (iptc.containsTag(135)) {
            metadata.put(META_LANGUAGE, cleanupData(iptc.getString(135)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_OBJECT_NAME)) {
            metadata.put(META_OBJECT_NAME,
                    cleanupData(iptc.getString(IptcDirectory.TAG_OBJECT_NAME)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_ORIGINAL_TRANSMISSION_REFERENCE)) {
            metadata.put(
                    META_ORIGINAL_TRANSMISSION_REFERENCE,
                    cleanupData(iptc.getString(IptcDirectory.TAG_ORIGINAL_TRANSMISSION_REFERENCE)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_ORIGINATING_PROGRAM)) {
            metadata.put(
                    META_ORIGINATING_PROGRAM,
                    cleanupData(iptc.getString(IptcDirectory.TAG_ORIGINATING_PROGRAM)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_PROVINCE_OR_STATE)) {
            metadata.put(
                    META_PROVINCE_OR_STATE,
                    cleanupData(iptc.getString(IptcDirectory.TAG_PROVINCE_OR_STATE)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_PROVINCE_OR_STATE)) {
            metadata.put(
                    META_PROVINCE_OR_STATE,
                    cleanupData(iptc.getString(IptcDirectory.TAG_PROVINCE_OR_STATE)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_APPLICATION_RECORD_VERSION)) {
            metadata.put(
                    META_RECORD_VERSION,
                    cleanupData(iptc.getString(IptcDirectory.TAG_APPLICATION_RECORD_VERSION)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_RELEASE_DATE)) {
           metadata.put(
                   META_RELEASE_DATE,
                   iptc.getDate(IptcDirectory.TAG_RELEASE_DATE));
        }

        if (iptc.containsTag(IptcDirectory.TAG_RELEASE_TIME)) {
            metadata.put(META_RELEASE_TIME,
                    cleanupData(iptc.getString(IptcDirectory.TAG_RELEASE_TIME)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_SOURCE)) {
            metadata.put(META_SOURCE,
                    cleanupData(iptc.getString(IptcDirectory.TAG_SOURCE)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_SPECIAL_INSTRUCTIONS)) {
            metadata.put(
                    META_SPECIAL_INSTRUCTIONS,
                    cleanupData(iptc.getString(IptcDirectory.TAG_SPECIAL_INSTRUCTIONS)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_SUPPLEMENTAL_CATEGORIES)) {
            metadata.put(
                    META_SUPPLEMENTAL_CATEGORIES,
                    cleanupData(iptc.getString(IptcDirectory.TAG_SUPPLEMENTAL_CATEGORIES)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_TIME_CREATED)) {
            metadata.put(META_TIME_CREATED,
                    cleanupData(iptc.getString(IptcDirectory.TAG_TIME_CREATED)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_URGENCY)) {
            metadata.put(META_URGENCY,
                    cleanupData(iptc.getString(IptcDirectory.TAG_URGENCY)));
        }

        if (iptc.containsTag(IptcDirectory.TAG_CAPTION_WRITER)) {
            metadata.put(META_WRITER,
                    cleanupData(iptc.getString(IptcDirectory.TAG_CAPTION_WRITER)));
        }
    }

    // http://jira.nuxeo.org/browse/NXP-4297
    public static String cleanupData(String data) {
        if (data != null) {
            return data.replace("\u0000", "");
        }

        return null;
    }

}
