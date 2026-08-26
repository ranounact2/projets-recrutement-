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
    <#if zp?has_content && zp.zones.head.values.title?has_content>
        <title>${zp.zones.head.values.title.content}</title>
    </#if>

    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link
    rel="icon"
    type="image/png"
    sizes="16x16"
    href="/assets/img/logo.png"
    />
    <#if zp?has_content && zp.zones?has_content && zp.zones.head?has_content && zp.zones.head.values?has_content && zp.zones.head.values.meta_description?has_content && zp.zones.head.values.meta_description.content?has_content>
        <meta name="description" content="${zp.zones.head.values.meta_description.content}">
    </#if>

    <#if zp?has_content && zp.zones?has_content && zp.zones.head.values.meta_keyword?has_content && zp.zones.head.values.meta_keyword.content?has_content>
        <meta name="keyword" content="${zp.zones.head.values.meta_keyword.content}">
    </#if>


</head>