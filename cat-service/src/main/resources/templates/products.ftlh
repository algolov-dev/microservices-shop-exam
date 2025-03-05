<#import "blocks/template.ftlh" as t>
<@t.template user>

    <h1 style="text-align: center">Поиск</h1>
    <hr>
    <form method="get" action="/">
        <div class="form-row">
            <div class="form-group col-md-2">
                <label for="searchCategory">Категории</label>
                <select name="searchCategory" id="searchCategory" class="form-control">
                    <option value="" <#if !searchCategory?has_content>selected</#if>>Все категории</option>
                    <#list categories as category>
                        <option value="${category.category_name}" <#if searchCategory == category.category_name>selected</#if>>${category.category_name}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group col-md-10 mt-3">
                <label for="searchWord">Поиск</label>
                <input type="text" name="searchWord"
                       value="${searchWord!}"
                       class="form-control" id="searchWord" placeholder="Введите название">
            </div>
            <div class="form-group col-md-10 mt-3">
                <label>Популярные теги:</label>
                <div>
                    <#if topTags?has_content>
                        <#list topTags as tag>
                            <a href="/?tag=${tag.name}" class="btn btn-outline-secondary btn-sm m-1">${tag.name}</a>
                        </#list>
                    <#else>
                        <span class="text-muted">Нет данных</span>
                    </#if>
                </div>
            </div>
            <button style="width: 100%" type="submit" class="btn btn-primary mt-3">Поиск</button>
        </div>
    </form>
    <hr>
    <div class="card-columns" style="overflow: visible;">
        <#if productPage.content?has_content>
            <#list productPage.content as product>
                <div class="card-fixed-container" style="position: relative; width: 200px; height: 400px; margin: 10px; overflow: visible;">
                    <a href="/product/${product.id}" class="text-decoration-none scale-on-hover" style="position: absolute; top: 0; left: 0; width: 100%;">
                        <div class="card h-100 d-flex flex-column bcat-0">
                            <div style="position: relative;">
                                <img src="/images/${product.previewImageId}" class="card-img-top product-img-small" alt="${product.title}">
                                <#if product.images?size gt 1>
                                    <div style="position: absolute; bottom: 0; left: 0; background: rgba(255,255,255,0.7); padding: 5px; font-weight: bold;">
                                        Пак
                                    </div>
                                </#if>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">${product.title}</h5>
                                <p class="card-text">${product.price} ₽</p>
                                <div class="text-center" style="margin: 0;">
                                    <#if product.tags?has_content>
                                        <#list product.tags as tag>
                                            <span class="badge bg-secondary me-1">${tag.name}</span>
                                        </#list>
                                    <#else>
                                        <span class="text-muted">Нет тегов</span>
                                    </#if>
                                </div>
                            </div>
                        </div>
                    </a>
                </div>
            </#list>
        </#if>
    </div>

    <nav aria-label="Page navigation example" <#if !productPage.content?has_content>class="d-none"</#if>>
        <ul class="pagination justify-content-center mt-5">
            <#if productPage.hasPrevious()>
                <li class="page-item">
                    <a class="page-link" href="?tag=${selectedTag!}&searchWord=${searchWord!}&searchCategory=${searchCategory!}&page=${productPage.number - 1}&size=${productPage.size}" aria-label="Previous">
                        <span aria-hidden="true">«</span>
                    </a>
                </li>
            </#if>
            <#list 0..productPage.totalPages - 1 as i>
                <li class="page-item <#if i == productPage.number>active</#if>">
                    <a class="page-link" href="?tag=${selectedTag!}&searchWord=${searchWord!}&searchCategory=${searchCategory!}&page=${i}&size=${productPage.size}">${i + 1}</a>
                </li>
            </#list>
            <#if productPage.hasNext()>
                <li class="page-item">
                    <a class="page-link" href="?tag=${selectedTag!}&searchWord=${searchWord!}&searchCategory=${searchCategory!}&page=${productPage.number + 1}&size=${productPage.size}" aria-label="Next">
                        <span aria-hidden="true">»</span>
                    </a>
                </li>
            </#if>
        </ul>
    </nav>

    <#if !productPage.content?has_content>
        <h3 style="text-align: center">Не найдено</h3>
    </#if>

    <script>
        document.getElementById('searchCategory').addEventListener('change', function() {
            this.form.submit();
        });
    </script>

</@t.template>
