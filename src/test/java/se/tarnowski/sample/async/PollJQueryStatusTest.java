/*
 * Copyright (c) 2012, Alexander Tarnowski
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package se.tarnowski.sample.async;

import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import se.tarnowski.sample.page.TestPage;

/**
 * This test saves the result of the jQuery.active variable in a div and prints the contents of that div.
 * It uses the naive approach to sleeping for better verbosity. Note that the jQuery.active variable
 * is used for more than signalling ongoing Ajax calls.
 */
public class PollJQueryStatusTest {

    WebDriver webDriver = new HtmlUnitDriver(true);
    TestPage page = new TestPage(webDriver);

    @Test
    public void run() throws InterruptedException {
        webDriver.get("http://localhost:8080/index_jquery.html");
        page.clickDelayedTextButton();

        for (int seconds = 0; seconds < 5; seconds++) {
            printContainerDivAndStatus(seconds);
            Thread.sleep(1000);
        }
    }

    private void printContainerDivAndStatus(int seconds) {
        long jQueryStatus = (Long) ((JavascriptExecutor) webDriver).executeScript("return window.jQuery.active;");
        System.err.println("After " + seconds + " seconds: ");
        System.err.println("container='" + page.getContainerDiv() + "', status=" + jQueryStatus);
    }
}
