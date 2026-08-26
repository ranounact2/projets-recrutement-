<head class="head-section">
    <!-- Global site tag (gtag.js) - Google Analytics -->
    <script
    async
    src="https://www.googletagmanager.com/gtag/js?id=UA-109408277-24"
    ></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag() {
        dataLayer.push(arguments);
        }
        gtag("js", new Date());

        gtag("config", "UA-109408277-24");
    </script>
    <meta
    name="google-site-verification"
    content="CNZuYBkUWFxSCV1elog1ZOiqKJbfKR8DZkP1Vk8-cHM"
    />

    <meta name="msvalidate.01" content="E7A413B3796995B13B212D62ABE23180" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta
    name="viewport"
    content="width=device-width, initial-scale=1, shrink-to-fit=no"
    />
    <meta http-equiv="cache-control" content="public,max-age:86400" />
    <meta name="author" content="Centoria" />

    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link
    rel="icon"
    type="image/png"
    sizes="16x16"
    href="/assets/img/logo.png"
    />

    <#-- Récupération des valeurs depuis zones -->
    <#if zones??>
        <#list zones as zonePage>
            <#if zonePage.zones??>
                <#list zonePage.zones?keys as zoneKey>
                    <#assign currentZone = zonePage.zones[zoneKey]>
                    <#if currentZone.values??>
                    <#-- Title -->
                    <#if currentZone.values.title??>
                        <title>${currentZone.values.title.content!currentZone.values.title.value!''}</title>
                    </#if>
                    <#-- Meta description -->
                    <#if currentZone.values.meta_description??>
                    <meta name="description" content="${currentZone.values.meta_description.content!currentZone.values.meta_description.value!''}">
                    </#if>
                    <#-- Meta keywords -->
                    <#if currentZone.values.meta_keyword??>
                    <meta name="keywords" content="${currentZone.values.meta_keyword.content!currentZone.values.meta_keyword.value!''}">
                    </#if>
                    <#-- JSON-LD -->
                    <#if currentZone.values['json-ld']??>
                        <script type="application/ld+json">
${currentZone.values['json-ld'].content!currentZone.values['json-ld'].value!''}
                            </script>
                    </#if>
                    </#if>
                </#list>
            </#if>
        </#list>
    </#if>


</head>