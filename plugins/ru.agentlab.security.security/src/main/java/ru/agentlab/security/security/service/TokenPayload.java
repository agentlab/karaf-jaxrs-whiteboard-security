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

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSub() {
        return sub;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, sub);
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
        return Objects.equals(email, other.email) && Objects.equals(groups, other.groups)
                && Objects.equals(sub, other.sub);
    }

}
