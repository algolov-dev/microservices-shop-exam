<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Мои Котики</h1>
    <hr>
    <a class="btn btn-primary mt-3 mb-3" style="text-align: center; width: 100%"
       data-bs-toggle="collapse" href="#collapseExample" role="button"
       aria-expanded="true" aria-controls="collapseExample">
        Добавить нового кота
    </a>
    <div class="collapse mb-3" id="collapseExample" style="">
        <form action="/product/create" method="post" enctype="multipart/form-data" onsubmit="return validateFiles()">
            <div class="form-group">
                <label for="productName">Имя кота</label>
                <input type="text" required class="form-control"
                       id="productName" name="title">
            </div>
            <div class="form-group mt-3">
                <label for="price">Цена кота</label>
                <div class="input-group mb-3">
                    <input type="number" required class="form-control" id="price" name="price">
                    <div class="input-group-append">
                        <span class="input-group-text">₽</span>
                    </div>
                </div>
            </div>
            <div class="form-group col-md-2">
                <label for="searchCity">Город</label>
                <select name="searchCity" id="searchCity" class="form-control">
                    <option value="" <#if !searchCity?? || searchCity == "">selected</#if>>Любой город</option>
                    <#list cities as city>
                        <option value="${city.city_name}" <#if searchCity == city.city_name>selected</#if>>${city.city_name}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group mt-3">
                <label for="description">Описание кота</label>
                <textarea required class="form-control" id="description" name="description"></textarea>
            </div>
            <div class="form-group mt-3">
                <label for="files">Фотографии кота (от 1 до 10)</label>
                <input type="file" id="files" name="files" class="form-control" accept="image/*" multiple required>
                <small class="form-text text-muted">Вы можете загрузить до 10 фотографий.</small>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>

            <button type="submit" style="width: 100%" class="btn btn-primary mt-3">Добавить</button>
        </form>
    </div>
    <div class="card-columns">
        <#list products as product>
            <a href="/product/${product.id}" class="text-decoration-none m-2 scale-on-hover" style="max-width: 200px;">
                <div class="card h-100 d-flex flex-column bcat-0">
                    <img src="/images/${product.previewImageId}" class="card-img-top product-img-small" alt="${product.title}">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${product.title}</h5>
                        <p class="card-text">${product.price} ₽</p>
                        <span class="mt-auto">${product.city}</span>
                    </div>
                </div>
            </a>
        <#else>
            <h1 style="text-align: center">Не найдено</h1>
        </#list>
    </div>
</@t.template>