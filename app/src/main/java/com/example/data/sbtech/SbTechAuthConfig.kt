package com.example.data.sbtech

data class SbTechCredentials(
    val nafathAuthCode: String = "NFT_AUTH_LIVE_982341",
    val nafathClientId: String = "sbtech-sa-prod-client",
    val wathqApiKey: String = "wq_live_9a87fbc31010776412",
    val zatcaCsidToken: String = "CSID_ZATCA_FA_310582910400003",
    val qiwaBearerToken: String = "qiwa_oauth_token_7_882193",
    val baladyApiKey: String = "balady_lic_sec_4409923812",
    val webhookEndpoint: String = "https://sbtech.sa/api/v1/saudi-gov/auth-callback",
    val isLiveProduction: Boolean = true,
    val lastHandshakeTime: Long = System.currentTimeMillis()
)

object SbTechEmbedGenerator {
    fun generateHtmlEmbedSnippet(credentials: SbTechCredentials): String {
        return """
<!-- ================================================================= -->
<!-- SBTech.sa - Saudi Digital Government Unified Gateway Embed Snippet -->
<!-- Insert this inside your sbtech.sa HTML <head> or before </body>  -->
<!-- ================================================================= -->
<div id="sbtech-saudi-gov-widget" 
     data-cr="1010776412" 
     data-vat="310582910400003"
     data-nafath-auth="${credentials.nafathAuthCode}"
     data-client-id="${credentials.nafathClientId}"
     data-wathq-key="${credentials.wathqApiKey}"
     data-zatca-csid="${credentials.zatcaCsidToken}"
     data-qiwa-token="${credentials.qiwaBearerToken}"
     data-endpoint="${credentials.webhookEndpoint}"
     style="position: fixed; bottom: 20px; right: 20px; z-index: 99999; font-family: 'Segoe UI', Tahoma, sans-serif;">
</div>

<script>
(function() {
    window.SBTECH_GOV_CONFIG = {
        website: "sbtech.sa",
        companyName: "SBTech Saudi Arabia",
        crNumber: "1010776412",
        vatNumber: "310582910400003",
        authCode: "${credentials.nafathAuthCode}",
        clientId: "${credentials.nafathClientId}",
        wathqApiKey: "${credentials.wathqApiKey}",
        zatcaToken: "${credentials.zatcaCsidToken}",
        qiwaToken: "${credentials.qiwaBearerToken}",
        callbackUrl: "${credentials.webhookEndpoint}",
        isProduction: ${credentials.isLiveProduction}
    };

    console.log("[SBTech Gov Gateway] Authenticated with KSA Gateways. Auth Code: " + window.SBTECH_GOV_CONFIG.authCode);
})();
</script>
<script src="https://sbtech.sa/static/js/saudi-gov-gateway.min.js" async></script>
""".trimIndent()
    }
}
