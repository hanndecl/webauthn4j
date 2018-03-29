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

package com.webauthn4j.webauthn.context.validator;

import com.webauthn4j.webauthn.client.CollectedClientData;
import com.webauthn4j.webauthn.client.challenge.Challenge;
import com.webauthn4j.webauthn.client.challenge.DefaultChallenge;
import com.webauthn4j.webauthn.context.RelyingParty;
import com.webauthn4j.webauthn.context.validator.ChallengeValidator;
import com.webauthn4j.webauthn.exception.BadChallengeException;
import com.webauthn4j.webauthn.exception.MissingChallengeException;
import org.junit.Test;

/**
 * Test for ChallengeValidator
 */
public class ChallengeValidatorTest {

    private ChallengeValidator target = new ChallengeValidator();

    @Test
    public void verifyChallenge_test1() {

        Challenge challengeA = new DefaultChallenge(new byte[]{0x00});
        Challenge challengeB = new DefaultChallenge(new byte[]{0x00});

        CollectedClientData collectedClientData = new CollectedClientData();
        collectedClientData.setChallenge(challengeA);
        RelyingParty relyingParty = new RelyingParty(null, null, challengeB);

        //When
        target.validate(collectedClientData, relyingParty);
    }

    @Test(expected = BadChallengeException.class)
    public void verifyChallenge_test_with_different_challenge() {

        Challenge challengeA = new DefaultChallenge(new byte[]{0x00});
        Challenge challengeB = new DefaultChallenge(new byte[]{0x01});

        CollectedClientData collectedClientData = new CollectedClientData();
        collectedClientData.setChallenge(challengeA);
        RelyingParty relyingParty = new RelyingParty(null, null, challengeB);

        //When
        target.validate(collectedClientData, relyingParty);
    }

    @Test(expected = MissingChallengeException.class)
    public void verifyChallenge_test_without_saved_challenge() {

        Challenge challengeA = new DefaultChallenge(new byte[]{0x00});
        Challenge challengeB = null;

        CollectedClientData collectedClientData = new CollectedClientData();
        collectedClientData.setChallenge(challengeA);
        RelyingParty relyingParty = new RelyingParty(null, null, challengeB);

        //When
        target.validate(collectedClientData, relyingParty);
    }
}