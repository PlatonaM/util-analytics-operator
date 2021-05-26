/*
 * Copyright 2021 InfAI (CC SES)
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
 */


package org.infai.ses.platonam.util;


import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.infai.ses.platonam.util.IO.stringFromStream;

public class HttpRequest {
    private static String httpRequest(HttpUriRequest request) throws HttpRequestException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpclient.execute(request);
            try {
                if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() <= 206) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        return stringFromStream(entity.getContent());
                    } else {
                        throw new HttpRequestException("empty response");
                    }
                } else {
                    throw new HttpRequestException(request.getMethod() + ": " + request.getURI() + " - " + response.getStatusLine().getStatusCode());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            throw new HttpRequestException(e.getMessage());
        }

    }

    public static String httpGet(String url, String contentType) throws HttpRequestException {
        HttpGet request = new HttpGet(url);
        request.addHeader("content-type", contentType);
        return httpRequest(request);
    }

    public static String httpPost(String url, String contentType, String data) throws HttpRequestException {
        HttpPost request = new HttpPost(url);
        request.addHeader("content-type", contentType);
        try {
            request.setEntity(new StringEntity(data));
            return httpRequest(request);
        } catch (UnsupportedEncodingException e) {
            throw new HttpRequestException(e.getMessage());
        }
    }

    public static class HttpRequestException extends Exception {
        public HttpRequestException(String errorMessage) {
            super(errorMessage);
        }
    }
}
