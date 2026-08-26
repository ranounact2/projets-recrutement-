<!DOCTYPE html>
<html lang="en">

    <div class="bread">
        <div class="titre m-1">
            <div class="breadcrumbs" id="breadcrumbs">
                <#if linkDto??>
                    <#list linkDto as url>
                        <a class="breadcrumbs__link" href="${url.link}">${url.label?cap_first}</a>
                        <#if url_index < (linkDto?size - 1)>
                            <span> &gt; </span>
                        </#if>
                        <br>
                    </#list>
                </#if>

                    <#if breadCrumb??>
                        <#list breadCrumb as url>
                            <a class="breadcrumbs__link" href="${url.link}">${url.label?cap_first}</a>
                            <#if url_index < (breadCrumb?size - 1)>
                                <span> &gt; </span>
                            </#if>
                            <br>
                        </#list>
                    </#if>
                    <#if breadCrumbResultat??>
                        <#list breadCrumbResultat as url>
                            <a class="breadcrumbs__link" href="${url.link}">${url.label?cap_first}</a>
                            <#if url_index < (breadCrumbResultat?size - 1)>
                                <span> &gt; </span>
                            </#if>
                            <br>
                        </#list>
                    </#if>
            </div>
        </div>
    </div>


</html>