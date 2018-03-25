package net.sharplab.springframework.security.webauthn.sample.app.web.helper;

import com.webauthn4j.webauthn.attestation.WebAuthnAttestationObject;
import com.webauthn4j.webauthn.attestation.authenticator.WebAuthnAuthenticatorData;
import com.webauthn4j.webauthn.context.WebAuthnRegistrationContext;
import com.webauthn4j.webauthn.exception.BadChallengeException;
import net.sharplab.springframework.security.webauthn.context.provider.WebAuthnRegistrationContextProvider;
import net.sharplab.springframework.security.webauthn.context.validator.WebAuthnRegistrationContextValidator;
import net.sharplab.springframework.security.webauthn.sample.app.web.AuthenticatorCreateForm;
import net.sharplab.springframework.security.webauthn.sample.app.web.AuthenticatorUpdateForm;
import net.sharplab.springframework.security.webauthn.sample.domain.constant.MessageCodes;
import net.sharplab.springframework.security.webauthn.sample.domain.model.Authenticator;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.terasoluna.gfw.common.message.ResultMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class AuthenticatorHelper {

    private WebAuthnRegistrationContextProvider registrationContextProvider;
    private WebAuthnRegistrationContextValidator registrationContextValidator;

    public AuthenticatorHelper(
            WebAuthnRegistrationContextProvider registrationContextProvider,
            WebAuthnRegistrationContextValidator registrationContextValidator){

        this.registrationContextProvider = registrationContextProvider;
        this.registrationContextValidator = registrationContextValidator;
    }

    /**
     * returns true if validation success
     *
     * @param model              model
     * @param request            request
     * @param response           reponse
     * @param authenticatorCreateForms authenticator form list
     * @return true if validation success
     */
    public boolean validateAuthenticators(Model model, HttpServletRequest request, HttpServletResponse response, List<AuthenticatorCreateForm> authenticatorCreateForms) {
        if (authenticatorCreateForms == null) {
            return true;
        }
        return authenticatorCreateForms.stream().allMatch(authenticator -> {

            try {
                WebAuthnRegistrationContext registrationContext = registrationContextProvider.provide(
                        request,
                        response,
                        authenticator.getClientData().getClientDataBase64(),
                        authenticator.getAttestationObject().getAttestationObjectBase64());
                registrationContextValidator.validate(registrationContext);
                return true;
            } catch (BadChallengeException e) {
                model.addAttribute(ResultMessages.error().add(MessageCodes.Error.User.BAD_CHALLENGE));
                return false;
            }
        });
    }


    public Authenticator mapForUpdate(AuthenticatorCreateForm source, Authenticator destination){
        if (source == null) {
            return null;
        }
        if (destination == null) {
            destination = new Authenticator();
        }
        destination.setName(source.getName());
        if(source.getAttestationObject() != null){
            WebAuthnAttestationObject attestationObject = source.getAttestationObject().getAttestationObject();
            WebAuthnAuthenticatorData authnAuthenticatorData = attestationObject.getAuthenticatorData();

            destination.setRpIdHash(authnAuthenticatorData.getRpIdHash());
            destination.setCounter(authnAuthenticatorData.getCounter());
            destination.setAttestedCredentialData(authnAuthenticatorData.getAttestationData());
            destination.setAttestationStatement(attestationObject.getAttestationStatement());
        }
        return destination;
    }

    public Authenticator mapForUpdate(AuthenticatorUpdateForm source, Authenticator destination){
        if (source == null) {
            return null;
        }
        if (destination == null) {
            destination = new Authenticator();
        }
        destination.setId(source.getId());
        destination.setName(source.getName());
        return destination;
    }

    public AuthenticatorUpdateForm mapForUpdate(Authenticator source, AuthenticatorUpdateForm destination) {
        if (source == null) {
            return null;
        }
        if (destination == null) {
            destination = new AuthenticatorUpdateForm();
        }
        destination.setId(source.getId());
        destination.setName(source.getName());
        return destination;
    }
}
