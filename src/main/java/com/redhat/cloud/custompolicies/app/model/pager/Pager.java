/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.cloud.custompolicies.app.model.pager;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Pager {

    @Getter
    @Builder.Default
    private int page = 0;
    @Getter
    @Builder.Default
    private int itemsPerPage = 10;

    public Pager(int page, int itemsPerPage) {
        this.page = page;
        this.itemsPerPage = itemsPerPage;
    }

}
