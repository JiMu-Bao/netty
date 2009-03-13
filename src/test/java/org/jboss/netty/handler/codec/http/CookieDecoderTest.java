/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005-2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.netty.handler.codec.http;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

/**
 * @author <a href="mailto:andy.taylor@jboss.org">Andy Taylor</a>
 */
public class CookieDecoderTest {
    @Test
    public void testDecodingSingleCookieV0() {
        String cookieString = "myCookie=myValue;expires=50;path=%2Fapathsomewhere;domain=%2Fadomainsomewhere;secure;";
        CookieDecoder cookieDecoder = new CookieDecoder();
        Map<String, Cookie> cookieMap = cookieDecoder.decode(cookieString);
        assertEquals(1, cookieMap.size());
        Cookie cookie = cookieMap.get("MyCookie");
        assertNotNull(cookie);
        assertEquals("myValue", cookie.getValue());
        assertNull(cookie.getComment());
        assertNull(cookie.getCommentURL());
        assertEquals("/adomainsomewhere", cookie.getDomain());
        assertFalse(cookie.isDiscard());
        assertEquals(50, cookie.getMaxAge());
        assertEquals("/apathsomewhere", cookie.getPath());
        assertTrue(cookie.getPorts().isEmpty());
        assertTrue(cookie.isSecure());
        assertEquals(0, cookie.getVersion());
    }

    @Test
    public void testDecodingSingleCookieV0ExtraParamsIgnored() {
        String cookieString = "myCookie=myValue;max-age=50;path=%2Fapathsomewhere;domain=%2Fadomainsomewhere;secure;comment=this%20is%20a%20comment;version=0;commentURL=http%2F%3Aaurl.com;port=80,8080;discard;";
        CookieDecoder cookieDecoder = new CookieDecoder();
        Map<String, Cookie> cookieMap = cookieDecoder.decode(cookieString);
        assertEquals(1, cookieMap.size());
        Cookie cookie = cookieMap.get("MyCookie");
        assertNotNull(cookie);
        assertEquals("myValue", cookie.getValue());
        assertNull(cookie.getComment());
        assertNull(cookie.getCommentURL());
        assertEquals("/adomainsomewhere", cookie.getDomain());
        assertFalse(cookie.isDiscard());
        assertEquals(50, cookie.getMaxAge());
        assertEquals("/apathsomewhere", cookie.getPath());
        assertTrue(cookie.getPorts().isEmpty());
        assertTrue(cookie.isSecure());
        assertEquals(0, cookie.getVersion());
    }
    @Test
    public void testDecodingSingleCookieV1() {
        String cookieString = "myCookie=myValue;max-age=50;path=%2Fapathsomewhere;domain=%2Fadomainsomewhere;secure;comment=this%20is%20a%20comment;version=1;";
        CookieDecoder cookieDecoder = new CookieDecoder();
        Map<String, Cookie> cookieMap = cookieDecoder.decode(cookieString);
        assertEquals(1, cookieMap.size());
        Cookie cookie = cookieMap.get("MyCookie");
        assertEquals("myValue", cookie.getValue());
        assertNotNull(cookie);
        assertEquals("this is a comment", cookie.getComment());
        assertNull(cookie.getCommentURL());
        assertEquals("/adomainsomewhere", cookie.getDomain());
        assertFalse(cookie.isDiscard());
        assertEquals(50, cookie.getMaxAge());
        assertEquals("/apathsomewhere", cookie.getPath());
        assertTrue(cookie.getPorts().isEmpty());
        assertTrue(cookie.isSecure());
        assertEquals(1, cookie.getVersion());
    }

    @Test
    public void testDecodingSingleCookieV1ExtraParamsIgnored() {
        String cookieString = "myCookie=myValue;max-age=50;path=%2Fapathsomewhere;domain=%2Fadomainsomewhere;secure;comment=this%20is%20a%20comment;version=1;commentURL=http%2F%3Aaurl.com;port=80,8080;discard;";
        CookieDecoder cookieDecoder = new CookieDecoder();
        Map<String, Cookie> cookieMap = cookieDecoder.decode(cookieString);
        assertEquals(1, cookieMap.size());
        Cookie cookie = cookieMap.get("MyCookie");
        assertNotNull(cookie);
        assertEquals("myValue", cookie.getValue());
        assertEquals("this is a comment", cookie.getComment());
        assertNull(cookie.getCommentURL());
        assertEquals("/adomainsomewhere", cookie.getDomain());
        assertFalse(cookie.isDiscard());
        assertEquals(50, cookie.getMaxAge());
        assertEquals("/apathsomewhere", cookie.getPath());
        assertTrue(cookie.getPorts().isEmpty());
        assertTrue(cookie.isSecure());
        assertEquals(1, cookie.getVersion());
    }
    @Test
    public void testDecodingSingleCookieV2() {
        String cookieString = "myCookie=myValue;max-age=50;path=%2Fapathsomewhere;domain=%2Fadomainsomewhere;secure;comment=this%20is%20a%20comment;version=2;commentURL=http%2F%3Aaurl.com;port=80,8080;discard;";
        CookieDecoder cookieDecoder = new CookieDecoder();
        Map<String, Cookie> cookieMap = cookieDecoder.decode(cookieString);
        assertEquals(1, cookieMap.size());
        Cookie cookie = cookieMap.get("MyCookie");
        assertNotNull(cookie);
        assertEquals("myValue", cookie.getValue());
        assertEquals("this is a comment", cookie.getComment());
        assertEquals("http/:aurl.com", cookie.getCommentURL());
        assertEquals("/adomainsomewhere", cookie.getDomain());
        assertTrue(cookie.isDiscard());
        assertEquals(50, cookie.getMaxAge());
        assertEquals("/apathsomewhere", cookie.getPath());
        assertEquals(2, cookie.getPorts().size());
        assertTrue(cookie.getPorts().contains(80));
        assertTrue(cookie.getPorts().contains(8080));
        assertTrue(cookie.isSecure());
        assertEquals(2, cookie.getVersion());
    }


    @Test
    public void testDecodingMultipleCookies() {
        String c1 = "myCookie=myValue;max-age=50;path=%2Fapathsomewhere;domain=%2Fadomainsomewhere;secure;comment=this%20is%20a%20comment;version=2;commentURL=http%2F%3Aaurl.com;port=80,8080;discard;";
        String c2 = "myCookie2=myValue2;max-age=0;path=%2Fanotherpathsomewhere;domain=%2Fanotherdomainsomewhere;comment=this%20is%20another%20comment;version=2;commentURL=http%2F%3Aanotherurl.com;";
        String c3 = "myCookie3=myValue3;max-age=0;version=2;";
        CookieDecoder decoder = new CookieDecoder();

        Map<String, Cookie> cookieMap = decoder.decode(c1 + c2 + c3);
        assertEquals(3, cookieMap.size());
        Cookie cookie = cookieMap.get("MyCookie");
        assertNotNull(cookie);
        assertEquals("myValue", cookie.getValue());
        assertEquals("this is a comment", cookie.getComment());
        assertEquals("http/:aurl.com", cookie.getCommentURL());
        assertEquals("/adomainsomewhere", cookie.getDomain());
        assertTrue(cookie.isDiscard());
        assertEquals(50, cookie.getMaxAge());
        assertEquals("/apathsomewhere", cookie.getPath());
        assertEquals(2, cookie.getPorts().size());
        assertTrue(cookie.getPorts().contains(80));
        assertTrue(cookie.getPorts().contains(8080));
        assertTrue(cookie.isSecure());
        assertEquals(2, cookie.getVersion());
        cookie = cookieMap.get("MyCookie2");
        assertNotNull(cookie);
        assertEquals("myValue2", cookie.getValue());
        assertEquals("this is another comment", cookie.getComment());
        assertEquals("http/:anotherurl.com", cookie.getCommentURL());
        assertEquals("/anotherdomainsomewhere", cookie.getDomain());
        assertFalse(cookie.isDiscard());
        assertEquals(0, cookie.getMaxAge());
        assertEquals("/anotherpathsomewhere", cookie.getPath());
        assertTrue(cookie.getPorts().isEmpty());
        assertFalse(cookie.isSecure());
        assertEquals(2, cookie.getVersion());
        cookie = cookieMap.get("MyCookie3");
        assertNotNull(cookie);
        assertEquals("myValue3", cookie.getValue());
        assertNull( cookie.getComment());
        assertNull(cookie.getCommentURL());
        assertNull(cookie.getDomain());
        assertFalse(cookie.isDiscard());
        assertEquals(0, cookie.getMaxAge());
        assertNull(cookie.getPath());
        assertTrue(cookie.getPorts().isEmpty());
        assertFalse(cookie.isSecure());
        assertEquals(2, cookie.getVersion());
    }
}
