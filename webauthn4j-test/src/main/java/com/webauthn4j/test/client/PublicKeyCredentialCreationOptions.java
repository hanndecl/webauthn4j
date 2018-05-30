package com.webauthn4j.test.client;

import com.webauthn4j.client.challenge.Challenge;
import com.webauthn4j.extension.ExtensionIdentifier;
import com.webauthn4j.util.WIP;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@WIP
public class PublicKeyCredentialCreationOptions {
    private PublicKeyCredentialRpEntity rp;
    private PublicKeyCredentialUserEntity user;

    private Challenge challenge;
    private List<PublicKeyCredentialParameters> pubKeyCredParams = Collections.emptyList();
    private BigInteger timeout;
    private List<PublicKeyCredentialDescriptor> excludeCredentials = Collections.emptyList();
    private AuthenticatorSelectionCriteria authenticatorSelection;
    private AttestationConveyancePreference attestation;
    private Map<ExtensionIdentifier, ExtensionInput> extentions;


    public PublicKeyCredentialRpEntity getRp() {
        return rp;
    }

    public void setRp(PublicKeyCredentialRpEntity rp) {
        this.rp = rp;
    }

    public PublicKeyCredentialUserEntity getUser() {
        return user;
    }

    public void setUser(PublicKeyCredentialUserEntity user) {
        this.user = user;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public List<PublicKeyCredentialParameters> getPubKeyCredParams() {
        return pubKeyCredParams;
    }

    public void setPubKeyCredParams(List<PublicKeyCredentialParameters> pubKeyCredParams) {
        this.pubKeyCredParams = pubKeyCredParams;
    }

    public BigInteger getTimeout() {
        return timeout;
    }

    public void setTimeout(BigInteger timeout) {
        this.timeout = timeout;
    }

    public List<PublicKeyCredentialDescriptor> getExcludeCredentials() {
        return excludeCredentials;
    }

    public void setExcludeCredentials(List<PublicKeyCredentialDescriptor> excludeCredentials) {
        this.excludeCredentials = excludeCredentials;
    }

    public AuthenticatorSelectionCriteria getAuthenticatorSelection() {
        return authenticatorSelection;
    }

    public void setAuthenticatorSelection(AuthenticatorSelectionCriteria authenticatorSelection) {
        this.authenticatorSelection = authenticatorSelection;
    }

    public AttestationConveyancePreference getAttestation() {
        return attestation;
    }

    public void setAttestation(AttestationConveyancePreference attestation) {
        this.attestation = attestation;
    }

    public Map<ExtensionIdentifier, ExtensionInput> getExtentions() {
        return extentions;
    }

    public void setExtentions(Map<ExtensionIdentifier, ExtensionInput> extentions) {
        this.extentions = extentions;
    }
}