/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.msf4j.delegates;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;

/**
 * Trimmed MSF4J implementation of javax.ws.rs.core.Response.
 */
public class MSF4JResponse extends Response {

    private Object entity;
    private int status;
    private MultivaluedMap<String, String> headers;
    private Map<String, NewCookie> cookies = new HashMap<>();
    private MediaType type;
    private static final ThreadLocal<URI> baseUriThreadLocal = new ThreadLocal<>();

    /**
     * Set the {@code baseUri} of the actual request into the {@link InheritableThreadLocal}.
     * <p>
     * The {@code baseUri} will be used for absolutizing the location header
     * content in case that only a relative URI is provided.
     * </p>
     * <p>
     * After resource method invocation when the value is not needed
     * any more to be stored in {@code ThreadLocal} {@link #clearBaseUri() clearBaseUri()} should be
     * called for cleanup in order to prevent possible memory leaks.
     * </p>
     *
     * @param baseUri - baseUri of the actual request
     * @see Builder#location(URI)
     * @since 2.1.1
     */
    public static void setBaseUri(URI baseUri) {
        baseUriThreadLocal.set(baseUri);
    }

    /**
     * Return request baseUri previously set by {@link #setBaseUri(java.net.URI)}.
     *
     * Returned {@link URI} is used for absolutization of the location header in case that only a relative
     * {@code URI} was provided.
     *
     * @return baseUri of the actual request
     * @see Builder#location(URI)
     * @since 2.1.1
     */
    private static URI getBaseUri() {
        return baseUriThreadLocal.get();
    }

    /**
     * Remove the current thread's value for baseUri thread-local variable (set by {@link #setBaseUri(java.net.URI)}).
     *
     * Should be called after resource method invocation for cleanup.
     *
     * @see Builder#location(URI)
     * @since 2.1.1
     */
    public static void clearBaseUri() {
        baseUriThreadLocal.remove();
    }

    public void setHeaders(MultivaluedMap<String, String> headers) {
        this.headers = headers;
    }

    public void setCookies(Map<String, NewCookie> cookies) {
        this.cookies = cookies;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public StatusType getStatusInfo() {
        return new Response.StatusType() {

            public Status.Family getFamily() {
                return Response.Status.Family.familyOf(MSF4JResponse.this.status);
            }

            public String getReasonPhrase() {
                Response.Status statusEnum = Response.Status.fromStatusCode(MSF4JResponse.this.status);
                return statusEnum != null ? statusEnum.getReasonPhrase() : "";
            }

            public int getStatusCode() {
                return MSF4JResponse.this.status;
            }

        };
    }

    @Override
    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    @Override
    public <T> T readEntity(Class<T> entityType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T readEntity(Class<T> entityType, Annotation[] annotations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasEntity() {
        return entity != null;
    }

    @Override
    public boolean bufferEntity() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public MediaType getMediaType() {
        return type;
    }

    @Override
    public Locale getLanguage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLength() {
        if (headers != null) {
            String length = headers.getFirst(HttpHeaders.CONTENT_LENGTH);
            return length == null ? -1 : Integer.parseInt(length);
        }
        return -1;
    }

    @Override
    public Set<String> getAllowedMethods() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, NewCookie> getCookies() {
        return cookies;
    }

    @Override
    public EntityTag getEntityTag() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getLastModified() {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getLocation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Link> getLinks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasLink(String relation) {
        return false;
    }

    @Override
    public Link getLink(String relation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Link.Builder getLinkBuilder(String relation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultivaluedMap<String, Object> getMetadata() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MultivaluedMap<String, String> getStringHeaders() {
        return headers;
    }

    @Override
    public String getHeaderString(String name) {
        throw new UnsupportedOperationException();
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    /**
     * Trimmed Convenient builder for MSF4JResponse instances.
     */
    public class Builder extends ResponseBuilder {

        private Object entity;
        private int status;
        private MultivaluedMap<String, String> headers;
        private MediaType type;

        @Override
        public Response build() {
            MSF4JResponse msf4jResponse = new MSF4JResponse();
            msf4jResponse.setStatus(status);
            msf4jResponse.setEntity(entity);
            msf4jResponse.setHeaders(headers);
            msf4jResponse.setType(type);
            msf4jResponse.setCookies(cookies);
            return msf4jResponse;
        }

        @Override
        public ResponseBuilder clone() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder status(int status) {
            this.status = status;
            return this;
        }

        @Override
        public ResponseBuilder entity(Object entity) {
            this.entity = entity;
            return this;
        }

        @Override
        public ResponseBuilder entity(Object entity, Annotation[] annotations) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder allow(String... methods) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder allow(Set<String> methods) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder cacheControl(CacheControl cacheControl) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder encoding(String encoding) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder header(String name, Object value) {
            if (headers == null) {
                headers = new MultivaluedHashMap<>();
            }
            if (value instanceof List) {
                List values = (List) value;
                for (Object valueElement : values) {
                    headers.add(name, valueElement.toString());
                }
            } else {
                headers.add(name, value == null ? null : value.toString());
            }
            return this;
        }

        @Override
        public ResponseBuilder replaceAll(MultivaluedMap<String, Object> headers) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder language(String language) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder language(Locale language) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder type(MediaType type) {
            this.type = type;
            return this;
        }

        @Override
        public ResponseBuilder type(String type) {
            this.type = MediaType.valueOf(type);
            return this;
        }

        @Override
        public ResponseBuilder variant(Variant variant) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder contentLocation(URI location) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder cookie(NewCookie... cookies) {
            Arrays.asList(cookies).forEach(cookie -> MSF4JResponse.this.cookies.put(cookie.getName(), cookie));
            return this;
        }

        @Override
        public ResponseBuilder expires(Date expires) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder lastModified(Date lastModified) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder location(URI location) {
            URI locationUri = location;
            if (locationUri != null && !locationUri.isAbsolute()) {
                URI baseUri = getBaseUri();
                if (baseUri != null) {
                    locationUri = baseUri.resolve(location);
                }
            }
            header(HttpHeaders.LOCATION, locationUri);
            return this;
        }

        @Override
        public ResponseBuilder tag(EntityTag tag) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder tag(String tag) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder variants(Variant... variants) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder variants(List<Variant> variants) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder links(Link... links) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder link(URI uri, String rel) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ResponseBuilder link(String uri, String rel) {
            throw new UnsupportedOperationException();
        }
    }
}
