/*
 * Copyright (c) 2019 Connor Goulding
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
package com.monadiccloud.bindingz.contract.registry.dao;

import com.monadiccloud.bindingz.contract.registry.entity.ContractEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Connor Goulding
 */
@Repository
public interface JpaContractDao extends CrudRepository<ContractEntity, ContractEntity.ContractId> {
    List<ContractEntity> findByContractIdClientIdentifier(String clientIdentifier);
}
