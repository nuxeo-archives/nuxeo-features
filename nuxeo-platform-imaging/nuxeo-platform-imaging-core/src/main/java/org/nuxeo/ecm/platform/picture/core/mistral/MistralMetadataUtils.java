/*
 * (C) Copyright 2006-2007 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 * $Id: JOOoConvertPluginImpl.java 18651 2007-05-13 20:28:53Z sfermigier $
 */

package org.nuxeo.ecm.platform.picture.core.mistral;

import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_COLORSPACE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_COMMENT;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_COPYRIGHT;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_DESCRIPTION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_EQUIPMENT;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_EXPOSURE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_FNUMBER;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_FOCALLENGTH;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_HRESOLUTION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_ICCPROFILE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_ISOSPEED;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_ORIENTATION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_ORIGINALDATE;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_PIXEL_XDIMENSION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_PIXEL_YDIMENSION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_VRESOLUTION;
import static org.nuxeo.ecm.platform.picture.api.MetadataConstants.META_WHITEBALANCE;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.platform.picture.IPTCHelper;
import org.nuxeo.ecm.platform.picture.core.MetadataUtils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.Rational;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDescriptor;
import com.drew.metadata.exif.ExifSubIFDDirectory;

/**
 *
 * @author Max Stepanov
 * @author <a href="mailto:cbaican@nuxeo.com">Catalin Baican</a>
 *
 */
public class MistralMetadataUtils implements MetadataUtils {

    private static final Log log = LogFactory.getLog(MistralMetadataUtils.class);

    @Override
    public Map<String, Object> getImageMetadata(Blob blob) {

        Map<String, Object> metadata = new HashMap<String, Object>();

        Metadata imageMetadata = null;

        try {
            imageMetadata = ImageMetadataReader.readMetadata(new BufferedInputStream(blob.getStream()), false);
        } catch (IOException | ImageProcessingException e) {
            log.error("Failed to read metadata for the file:"
                            + blob.getFilename(), e);
        }

        if (imageMetadata == null) {
            return metadata;
        }

        /* EXIF */
        try {
            metadata.putAll(extractEXIF(imageMetadata));
        } catch (MetadataException e) {
            log.error("Failed to get EXIF metadata for the file:"
                            + blob.getFilename(), e);
        }

        /* IPTC */
        try {
            metadata.putAll(extractIPTC(imageMetadata));
        } catch (MetadataException e) {
            log.error("Failed to get IPTC metadata for the file:"
                    + blob.getFilename(), e);
        }

        return metadata;
    }


    protected Map<String, Object> extractEXIF(Metadata imageMetadata) throws MetadataException {

        Map<String, Object> metadata = new HashMap<String, Object>();

        ExifIFD0Directory exifIFD0 = imageMetadata.getDirectory(ExifIFD0Directory.class);

        if (exifIFD0 != null) {

            // Description
            if (exifIFD0.containsTag(ExifIFD0Directory.TAG_IMAGE_DESCRIPTION)) {
                String description = exifIFD0.getString(ExifIFD0Directory.TAG_IMAGE_DESCRIPTION).trim();
                if (description.length() > 0) {
                    metadata.put(META_DESCRIPTION, description);
                }
            }

            // Make and model
            if (exifIFD0.containsTag(ExifIFD0Directory.TAG_MAKE) || exifIFD0.containsTag(ExifIFD0Directory.TAG_MODEL)) {
                String equipment = (exifIFD0.getString(ExifIFD0Directory.TAG_MAKE) + " " + exifIFD0.getString(ExifIFD0Directory.TAG_MODEL)).trim();
                if (equipment.length() > 0) {
                    metadata.put(META_EQUIPMENT, equipment);
                }
            }

            // Date
            if (exifIFD0.containsTag(ExifIFD0Directory.TAG_DATETIME)) {
                metadata.put(META_ORIGINALDATE,
                        exifIFD0.getDate(ExifIFD0Directory.TAG_DATETIME));
            }

            // Resolution
            if (exifIFD0.containsTag(ExifIFD0Directory.TAG_X_RESOLUTION) && exifIFD0.containsTag(ExifIFD0Directory.TAG_Y_RESOLUTION)) {
                metadata.put(META_HRESOLUTION, exifIFD0.getInt(ExifIFD0Directory.TAG_X_RESOLUTION));
                metadata.put(META_VRESOLUTION, exifIFD0.getInt(ExifIFD0Directory.TAG_Y_RESOLUTION));
            }

            // Copyright
            if (exifIFD0.containsTag(ExifIFD0Directory.TAG_COPYRIGHT)) {
                String copyright = exifIFD0.getString(ExifIFD0Directory.TAG_COPYRIGHT).trim();
                if (copyright.length() > 0) {
                    metadata.put(META_COPYRIGHT, copyright);
                }
            }

            if (exifIFD0.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                metadata.put(META_ORIENTATION, exifIFD0.getString(ExifIFD0Directory.TAG_ORIENTATION));
            }

        }

        /* EXIF from the SubIFD directory */

        ExifSubIFDDirectory exifSubIFD = imageMetadata.getDirectory(ExifSubIFDDirectory.class);

        if (exifSubIFD != null) {

            ExifSubIFDDescriptor descriptor = new ExifSubIFDDescriptor(exifSubIFD);

            // User comment
            String comment = descriptor.getUserCommentDescription();
            if (comment != null) {
                metadata.put(META_COMMENT, comment);
            }

            // Dimensions
            if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH) && exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT)) {
                metadata.put(META_PIXEL_XDIMENSION, exifSubIFD.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
                metadata.put(META_PIXEL_YDIMENSION, exifSubIFD.getInt(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
            }

            // Exposure
            if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_EXPOSURE_TIME)) {
                Rational exposure = exifSubIFD.getRational(ExifSubIFDDirectory.TAG_EXPOSURE_TIME);
                long n = exposure.getNumerator();
                long d = exposure.getDenominator();
                if (d >= n && d % n == 0) {
                    exposure = new Rational(1, d / n);
                }
                metadata.put(META_EXPOSURE, exposure.toString());
            }

            // ISO
            String iso = descriptor.getIsoEquivalentDescription();
            if (iso != null) {
                metadata.put(META_ISOSPEED, "ISO-" + iso);
            }

            // Focal length
            if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_FOCAL_LENGTH)) {
                metadata.put(META_FOCALLENGTH,
                        exifSubIFD.getRational(ExifSubIFDDirectory.TAG_FOCAL_LENGTH).doubleValue());
            }

            // Color space
            String colorSpace = descriptor.getColorSpaceDescription();
            if (colorSpace != null) {
                metadata.put(META_COLORSPACE, colorSpace);
            }

            // White balance mode
            String whiteBalanceMode = descriptor.getWhiteBalanceModeDescription();
            if (whiteBalanceMode != null) {
                metadata.put(META_WHITEBALANCE, whiteBalanceMode);
            }


            // ICC profile
            if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_INTER_COLOR_PROFILE)) {
                metadata.put(META_ICCPROFILE, exifSubIFD.getString(ExifSubIFDDirectory.TAG_INTER_COLOR_PROFILE));
            }

            // F-stop
            if (exifSubIFD.containsTag(ExifSubIFDDirectory.TAG_FNUMBER)) {
                metadata.put(META_FNUMBER,
                        exifSubIFD.getRational(ExifSubIFDDirectory.TAG_FNUMBER).doubleValue());
            }
        }

        return metadata;
    }

    protected Map<String, Object> extractIPTC(Metadata imageMetadata) throws MetadataException {
        Map<String, Object> metadata = new HashMap<String, Object>();
        IPTCHelper.extract(imageMetadata, metadata);
        return metadata;
    }

}
