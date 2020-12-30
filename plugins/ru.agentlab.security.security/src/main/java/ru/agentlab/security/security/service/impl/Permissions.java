/********************************************************************************
 * Copyright (c) 2020 Agentlab and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package ru.agentlab.security.security.service.impl;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;

import com.google.common.base.Preconditions;

public class Permissions {

    public final static Permission ALL_PERMISSIONS = new WildcardPermission("*");

//    public final static Permission READ_REPOSITORY_PERMISSION = new WildcardPermission("repository:read");
//
//    public final static Permission WRITE_REPOSITORY_PERMISSION = new WildcardPermission("repository:write");

    public static Permission readRepositoryPermission(String repositoryId) {
        Preconditions.checkArgument(repositoryId != null);
        return new WildcardPermission("repository:read:" + repositoryId);
    }
    
    public static Permission writeRepositoryPermission(String repositoryId) {
        Preconditions.checkArgument(repositoryId != null);
        return new WildcardPermission("repository:write:" + repositoryId);
    }

    private Permissions() {
    };

//    public final static Permission WRITE_REPOSITORY_PERMISSION = new WildcardPermission("repository:write");

}
