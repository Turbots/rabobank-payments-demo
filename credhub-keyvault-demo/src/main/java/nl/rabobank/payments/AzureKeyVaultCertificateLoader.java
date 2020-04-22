package nl.rabobank.payments;

import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.certificates.CertificateClient;
import com.azure.security.keyvault.certificates.CertificateClientBuilder;
import com.azure.security.keyvault.certificates.models.CertificateProperties;
import com.azure.security.keyvault.certificates.models.KeyVaultCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Component
public class AzureKeyVaultCertificateLoader {

    @Value("${azure.keyvault.uri}")
    private String keyVaultUri;

    @Value("${azure.keyvault.client-id}")
    private String keyVaultClientId;

    @Value("${azure.keyvault.client-key}")
    private String keyVaultClientSecret;

    @Value("${azure.keyvault.tenant-id}")
    private String keyVaultTenantId;

    @Value("${user.home}")
    private String homeDirectory;

    private static final Logger LOGGER = LoggerFactory.getLogger(AzureKeyVaultCertificateLoader.class);

    @PostConstruct
    public void load() {
        LOGGER.info("Current Truststore is located at {}", System.getenv("javax.net.ssl.trustStore"));

        CertificateClient client = buildClient();
        loadCertificates(client);
    }

    private CertificateClient buildClient() {
        return new CertificateClientBuilder()
                .vaultUrl(keyVaultUri)
                .credential(new ClientSecretCredentialBuilder()
                        .clientId(keyVaultClientId)
                        .clientSecret(keyVaultClientSecret)
                        .tenantId(keyVaultTenantId)
                        .build())
                .buildClient();
    }

    private void loadCertificates(CertificateClient client) {
        LOGGER.info("[Azure KeyVault] Get Certificates for [{}] at [{}]", keyVaultClientId, keyVaultUri);
        for (CertificateProperties properties : client.listPropertiesOfCertificates()) {
            KeyVaultCertificate certificate = client.getCertificateVersion(properties.getName(), properties.getVersion());
            LOGGER.info("Received certificate with name {} and secret id {}", certificate.getProperties().getName(), certificate.getSecretId());

            try {
                writeCertificateToDisk(certificate);
            } catch (Exception e) {
                LOGGER.error("Could not load certificate [{}]", certificate.getName(), e);
            }
        }
    }

    private void writeCertificateToDisk(KeyVaultCertificate certificate) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException {
        Path file = Paths.get(homeDirectory + File.separator + "certificates" + File.separator + certificate.getName() + ".pkcs12");

        Files.deleteIfExists(file);
        Path certFile = Files.createFile(file);

        try (FileOutputStream fos = new FileOutputStream(certFile.toFile())) {
            fos.write(certificate.getCer());
            LOGGER.info("Written certificate to " + certFile.toString());
        }
    }
}
