<#import "blocks/template.ftlh" as t>
<@t.template user>

    <h1 style="text-align: center">Поиск котов</h1>
    <hr>
    <form method="get" action="/">
        <div class="form-row">
            <div class="form-group col-md-2">
                <label for="searchCity">Город</label>
                <select name="searchCity" id="searchCity" class="form-control">
                    <option value="" <#if !searchCity?has_content>selected</#if>>Все города</option>
                    <#list cities as city>
                        <option value="${city.city_name}" <#if searchCity == city.city_name>selected</#if>>${city.city_name}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group col-md-10 mt-3">
                <label for="searchWord">Поиск по объявлениям</label>
                <input type="text" name="searchWord"
                       value="${searchWord!}"
                       class="form-control" id="searchWord" placeholder="Введите имя кота">
            </div>
            <button style="width: 100%" type="submit" class="btn btn-primary mt-3">Поиск</button>
        </div>
    </form>
    <hr>
    <div class="card-columns">
        <#if productPage.content?has_content>
            <#list productPage.content as product>
                <a href="/product/${product.id}" class="text-decoration-none m-2 scale-on-hover" style="max-width: 200px;">
                    <div class="card h-100 d-flex flex-column bcat-0">
                        <img src="/images/${product.previewImageId}" class="card-img-top product-img-small" alt="${product.title}">
                        <div class="card-body">
                            <h5 class="card-title">${product.title}</h5>
                            <p class="card-text">${product.price} ₽</p>
                            <span class="mt-auto">${product.city}</span>
                        </div>
                    </div>
                </a>
            </#list>
        </#if>
    </div>

    <nav aria-label="Page navigation example" <#if !productPage.content?has_content>class="d-none"</#if>>
        <ul class="pagination justify-content-center mt-3">
            <#if productPage.hasPrevious()>
                <li class="page-item">
                    <a class="page-link" href="?searchWord=${searchWord!}&searchCity=${searchCity!}&page=${productPage.number - 1}&size=${productPage.size}" aria-label="Previous">
                        <span aria-hidden="true">«</span>
                    </a>
                </li>
            </#if>
            <#list 0..productPage.totalPages - 1 as i>
                <li class="page-item <#if i == productPage.number>active</#if>">
                    <a class="page-link" href="?searchWord=${searchWord!}&searchCity=${searchCity!}&page=${i}&size=${productPage.size}">${i + 1}</a>
                </li>
            </#list>
            <#if productPage.hasNext()>
                <li class="page-item">
                    <a class="page-link" href="?searchWord=${searchWord!}&searchCity=${searchCity!}&page=${productPage.number + 1}&size=${productPage.size}" aria-label="Next">
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
        document.getElementById('searchCity').addEventListener('change', function() {
            this.form.submit();
        });
    </script>

</@t.template>