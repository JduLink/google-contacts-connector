/**
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.google.contact.automation.testcases;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.google.contact.automation.RegressionTests;
import org.mule.modules.google.contact.automation.SmokeTests;
import org.mule.modules.google.contact.wrappers.GoogleContactEntry;
import org.mule.modules.tests.ConnectorTestUtils;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DownloadPhotoTestCases extends GoogleContactsTestParent {

    @Before
    public void setUp() throws Exception {
        initializeTestRunMessage("downloadPhoto");

        GoogleContactEntry contactEntry = runFlowAndGetPayload("insert-contact");
        InputStream imageStream = loadResource((String) getTestRunMessageValue("imagePath"));

        upsertOnTestRunMessage("contactRef", contactEntry);
        upsertOnTestRunMessage("image", imageStream);
        upsertOnTestRunMessage("id", extractEntryId(contactEntry));

        runFlowAndGetPayload("update-contact-photo");
    }

    @Category({RegressionTests.class, SmokeTests.class})
    @Test
    public void testDownloadPhoto() {
        try {
            // Wait for the remote google object to be refreshed
            Thread.sleep(5000);

            GoogleContactEntry contactEntry = runFlowAndGetPayload("get-contact-by-id");
            upsertOnTestRunMessage("contactRef", contactEntry);

            // Wait for the remote google object to be refreshed
            Thread.sleep(5000);

            InputStream response = runFlowAndGetPayload("download-photo");

            assertTrue(response != null);

        } catch (Exception e) {
            fail(ConnectorTestUtils.getStackTrace(e));
        }
    }

    @After
    public void tearDown() throws Exception {
        // Wait for the remote google object to be refreshed
        Thread.sleep(5000);
        runFlowAndGetPayload("delete-contact-by-id");
    }
}