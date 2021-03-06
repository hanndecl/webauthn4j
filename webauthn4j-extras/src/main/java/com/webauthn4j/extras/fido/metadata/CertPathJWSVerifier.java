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

package com.webauthn4j.extras.fido.metadata;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.util.Base64;
import com.webauthn4j.util.CertificateUtil;
import com.webauthn4j.util.WIP;
import com.webauthn4j.validator.exception.CertificateException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A JWSVerifier for CertPath based check
 */
@WIP
public class CertPathJWSVerifier implements JWSVerifier {

    private static final String DEFAULT_FIDO_METADATA_SERVICE_ROOT_CERTIFICATE_CLASSPATH = "classpath:metadata/certs/FIDOMetadataService.cer";

    //~ Instance fields ================================================================================================

    private Resource rootCertificate;

    /**
     * constructor
     *
     * @param resourceLoader resource loader
     */
    public CertPathJWSVerifier(ResourceLoader resourceLoader) {
        this.rootCertificate = resourceLoader.getResource(DEFAULT_FIDO_METADATA_SERVICE_ROOT_CERTIFICATE_CLASSPATH);
    }

    /**
     * {@inheritDoc}
     */
    public void verify(JWSObject jws) {
        //trust anchor
        X509Certificate x509rootCertificate = getX5c();
        Set<TrustAnchor> trustAnchor = new HashSet<>();
        trustAnchor.add(new TrustAnchor(x509rootCertificate, null));

        //certPath
        List<Certificate> certificates = getCertificatesFromJWSHeader(jws.getHeader());
        CertPath certPath = CertificateUtil.generateCertPath(certificates);

        CertPathValidator validator = CertificateUtil.createCertPathValidator();
        PKIXParameters certPathParameters = CertificateUtil.createPKIXParameters(trustAnchor);

        //Set PKIXRevocationChecker to enable CRL based revocation check, which is disabled by default.
        //Ref. http://docs.oracle.com/javase/7/docs/technotes/guides/security/certpath/CertPathProgGuide.html#AppB
        PKIXRevocationChecker pkixRevocationChecker = (PKIXRevocationChecker) validator.getRevocationChecker();
        pkixRevocationChecker.setOptions(EnumSet.of(PKIXRevocationChecker.Option.PREFER_CRLS));
        certPathParameters.addCertPathChecker(pkixRevocationChecker);

        try {
            validator.validate(certPath, certPathParameters);
        } catch (CertPathValidatorException e) {
            throw new CertificateException("Certificate validation failed", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Provides root metadata.certs {@link Resource}.
     *
     * @return root metadata.certs {@link Resource}
     */
    public Resource getRootCertificate() {
        return rootCertificate;
    }

    /**
     * Sets root metadata.certs {@link Resource}.
     *
     * @param rootCertificate root metadata.certs {@link Resource}
     */
    public void setRootCertificate(Resource rootCertificate) {
        this.rootCertificate = rootCertificate;
    }

    private X509Certificate getX5c() {
        try {
            Resource certificateResource = getRootCertificate();
            InputStream inputStream = certificateResource.getInputStream();
            return CertificateUtil.generateX509Certificate(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<Certificate> getCertificatesFromJWSHeader(JWSHeader header) {
        List<Base64> base64List = header.getX509CertChain();
        return base64List.stream().map(base64 -> CertificateUtil.generateX509Certificate(base64.decode())).collect(Collectors.toList());
    }
}
