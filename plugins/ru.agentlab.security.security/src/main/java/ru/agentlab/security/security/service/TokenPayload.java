/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package ru.agentlab.security.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenPayload {

    private final String sub;

    private List<String> groups = new ArrayList<>();

    private String name;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    private String email;

    public TokenPayload(@JsonProperty("sub") String sub) {
        this.sub = sub;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getGivenName() {
        return Optional.ofNullable(givenName);
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Optional<String> getFamilyName() {
        return Optional.ofNullable(familyName);
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSub() {
        return sub;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TokenPayload)) {
            return false;
        }
        TokenPayload other = (TokenPayload) obj;
        return Objects.equals(email, other.email) && Objects.equals(familyName, other.familyName)
                && Objects.equals(givenName, other.givenName) && Objects.equals(groups, other.groups)
                && Objects.equals(name, other.name) && Objects.equals(sub, other.sub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, familyName, givenName, groups, name, sub);
    }
}
