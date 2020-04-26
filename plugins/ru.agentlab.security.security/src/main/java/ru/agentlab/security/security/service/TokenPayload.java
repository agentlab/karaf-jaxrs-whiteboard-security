package ru.agentlab.security.security.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenPayload {

    private final String sub;

    private Optional<List<String>> groups = Optional.empty();

    private Optional<String> email = Optional.empty();

    public TokenPayload(@JsonProperty("sub") String sub) {
        this.sub = sub;
    }

    public Optional<List<String>> getGroups() {
        return groups;
    }

    public void setGroups(Optional<List<String>> groups) {
        this.groups = groups;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
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
