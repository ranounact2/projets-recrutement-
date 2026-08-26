<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/common-new/head.ftl" />
    <link rel="stylesheet" href="${assetsPath}/css-min/bo-index.min.css" defer/>
</head>
<body>
<header>
    <#include "/common-new/header.ftl" />
</header>

<!-- bread -->
<div class="bread">
    <div class="titre">
        <h2>Mes offres d'emploi</h2>
        <div class="breadcrumbs" id="breadcrumbs">
            <a class="breadcrumbs__link" href="/">Accueil</a>
            <span> > </span>
            <br>
            <a class="breadcrumbs__link" href="/a-propos-emplois-maroc">A propos</a>
            <span> > </span>
            <span>Mes offres d'emplois</span>
        </div>
    </div>
</div>

<section class="offres">
    <#assign boFilterQs><#if statusFilter != 'all'>&filter=${statusFilter}</#if></#assign>
    <nav class="bo-status-filter" aria-label="Filtrer par statut">
        <a href="?page=1&size=${adsPerPage}"<#if statusFilter == 'all'> class="is-active"</#if>>Toutes</a>
        <a href="?page=1&size=${adsPerPage}&filter=new"<#if statusFilter == 'new'> class="is-active"</#if>>Nouvelles</a>
        <a href="?page=1&size=${adsPerPage}&filter=modified"<#if statusFilter == 'modified'> class="is-active"</#if>>Modifiées</a>
    </nav>
    <div class="slide">
        <#if !ads?has_content>
            <div class="col-md-12">
                <div class="site-heading">
                    <div class="notfound-msg">${utils.get('no-offer-bo')}</div>
                    <br>
                </div>
            </div>
        </#if>
        <form id="boactionform" action="/rtsystems_admin_8965_hXtwWUCzukqwPyEuWxcE" method="post" class="d-none">
            <input type="hidden" name="key">
            <input type="hidden" name="action">
            <input type="hidden" name="page" value="${pageNumber}">
            <input type="hidden" name="size" value="${adsPerPage}">
            <#if statusFilter != 'all'><input type="hidden" name="filter" value="${statusFilter}"></#if>
        </form>
        <#list ads as job>
            <div class="item">
                <img src="/assets/img/save.png" class="save" alt="" srcset="">
                <div class="heading">
                    <#if job.img??>
                        <img src="/assets/img/${job.img}" alt="company logo">
                    <#else>
                        <img src="/assets/img/company-1.png" alt="company logo">
                    </#if>
                    <div class="div2">
                        <h2>${job.title}</h2>
                        <p class="open-icon">
                            <i class="far fa-clock"></i>
                            <span class="creation-date">${job.creationDate?string("yyyy-MM-dd HH:mm:ss")}</span>
                        </p>
                        <img src="/assets/img/business_center.png" alt="domain">
                        <span>${job.domain}</span>
                        <img src="/assets/img/place.png" alt="city">
                        <span>${job.city}</span>
                    </div>
                </div>
                <#if job.type == 'stage'>
                    <span class="badge danger">${job.type}</span>
                <#else>
                    <span class="badge success">${job.type}</span>
                </#if>
                <div class="cl1">
                        <span class="orange">
                            <#if job.announcetype == 0>Scrapy
                            <#elseif job.announcetype == 1>Scrapy with email
                            <#elseif job.announcetype == 2>Star
                            <#else>Normal
                            </#if>
                        </span>
                    <span class="orange">
                            <#if job.nbrDePostes??>Nombre de poste: ${job.nbrDePostes}</#if>
                        </span>

                    <div>
                        <b>Détails</b><br/>
                        <p>${job.content}</p>
                    </div>
                    <div class="btn-group">
                        <#list ["new", "updated", "new_verified", "disabled", "updated_verified"] as state>
                            <#if job.state == state>
                                <button type="button" class="jobmaroc-btn-validate btnaction" data-key="${job.key}"
                                        action="validate">
                                    <#if state == "disabled">Activer<#else>Valider</#if>
                                </button>
                            </#if>
                        </#list>

                        <button type="button" class="jobmaroc-btn-delete btnaction-delete" data-key="${job.key}"
                                action="delete">
                            Supprimer
                        </button>
                        <button type="button" class="jobmaroc-btn-disabled btnaction-disabled" data-key="${job.key}"
                                action="disabled">
                            Fermer
                        </button>
                    </div>
                </div>

            </div>
        </#list>
    </div>
    <div class="pagination">
        <#if pageNumber gt 1>
            <a href="?page=${pageNumber -1}&size=${adsPerPage}${boFilterQs}" class="pagination-link">Previous</a>
        </#if>
        <#if pageNumber gt 1>
            <span class="pagination-link"><a href="?page=${pageNumber -1}&size=${adsPerPage}${boFilterQs}"
                                             class="pagination-link">${pageNumber-1}</a></span>
        </#if>
        <span class="pagination-link selected current-page"><a href="?page=${pageNumber}&size=${adsPerPage}${boFilterQs}"
                                                               class="pagination-link">${pageNumber}</a></span>
        <#if pageNumber lt totalPages>
        <span class="pagination-link"><a href="?page=${pageNumber +1}&size=${adsPerPage}${boFilterQs}"
                                         class="pagination-link">${pageNumber+1}</a></span>
            <a href="?page=${pageNumber +1}&size=${adsPerPage}${boFilterQs}" class="pagination-link">Next</a>
        </#if>
    </div>
</section>

<script>
    const ready = (callback) => {
        if (document.readyState !== 'loading') {
            callback();
        } else if (document.addEventListener) {
            document.addEventListener('DOMContentLoaded', callback);
        } else {
            document.attachEvent('onreadystatechange', function () {
                if (document.readyState === 'complete') {
                    callback();
                }
            });
        }
    };

    const handleClick = (event) => {
        const action = event.target.getAttribute("action");
        const key = event.target.getAttribute("data-key");
        document.querySelector("input[name='key']").value = key;
        document.querySelector("input[name='action']").value = action;
        document.querySelector("#boactionform").submit();
    };

    const addButtonClickListener = (selector) => {
        const buttons = document.querySelectorAll(selector);
        buttons.forEach(button => {
            button.addEventListener('click', handleClick);
        });
    };

    ready(() => {
        const buttonSelectors = [
            ".btnaction-new-verified",
            ".btnaction-disabled",
            ".btnaction-updated-verified",
            ".btnaction",
            ".btnaction-updated",
            ".btnaction-delete"
        ];

        buttonSelectors.forEach(selector => addButtonClickListener(selector));
    });

    function toggleContent(key) {
        var contentElement = document.getElementById('job-content-' + key);
        if (contentElement.style.display === 'none') {
            contentElement.style.display = 'block';
        } else {
            contentElement.style.display = 'none';
        }
    }
</script>

</body>
<footer>
    <#include "/common-new/footer.ftl" />
</footer>
</html>
