/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.webauthn4j.extras.fido.metadata.structure;

/**
 * Created by ynojima on 2017/09/08.
 */
public enum AuthenticatorStatus {
    FIDO_CERTIFIED,
    NOT_FIDO_CERTIFIED,
    USER_VERIFICATION_BYPASS,
    ATTESTATION_KEY_COMPROMISE,
    USER_KEY_REMOTE_COMPROMISE,
    USER_KEY_PHYSICAL_COMPROMISE,
    UPDATE_AVAILABLE,
    REVOKED
}
