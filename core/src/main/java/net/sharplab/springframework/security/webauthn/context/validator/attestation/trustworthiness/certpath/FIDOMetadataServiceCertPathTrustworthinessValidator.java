package net.sharplab.springframework.security.webauthn.context.validator.attestation.trustworthiness.certpath;

import net.sharplab.springframework.security.fido.metadata.Metadata;
import net.sharplab.springframework.security.webauthn.anchor.FIDOMetadataServiceTrustAnchorService;
import net.sharplab.springframework.security.webauthn.attestation.statement.FIDOU2FAttestationStatement;
import net.sharplab.springframework.security.webauthn.attestation.statement.WebAuthnAttestationStatement;
import net.sharplab.springframework.security.webauthn.util.CertificateUtil;

import java.security.InvalidAlgorithmParameterException;
import java.security.cert.*;
import java.util.Map;
import java.util.Set;

/**
 * Created by ynojima on 2017/09/24.
 */
public class FIDOMetadataServiceCertPathTrustworthinessValidator implements CertPathTrustworthinessValidator {

    private FIDOMetadataServiceTrustAnchorService fidoMetadataServiceTrustAnchorService;

    public FIDOMetadataServiceCertPathTrustworthinessValidator(FIDOMetadataServiceTrustAnchorService fidoMetadataServiceTrustAnchorService){
        this.fidoMetadataServiceTrustAnchorService = fidoMetadataServiceTrustAnchorService;
    }

    @Override
    public void validate(WebAuthnAttestationStatement attestationStatement) {

        Metadata metadata = fidoMetadataServiceTrustAnchorService.findMetadata(attestationStatement);
        if(metadata == null){
            throw new RuntimeException(); //TODO
        }
        metadata.getStatusReports().forEach(report -> {
            switch (report.getStatus()){
                case FIDO_CERTIFIED:
                case UPDATE_AVAILABLE:
                case NOT_FIDO_CERTIFIED:
                    return;
                case ATTESTATION_KEY_COMPROMISE:
                case USER_VERIFICATION_BYPASS:
                case USER_KEY_REMOTE_COMPROMISE:
                case USER_KEY_PHYSICAL_COMPROMISE:
                case REVOKED:
                default:
                    throw new RuntimeException(); //TODO
            }
        });
    }

}