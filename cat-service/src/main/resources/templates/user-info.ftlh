<#import "blocks/template.ftlh" as t>
<@t.template userByPrincipal>
    <h1 style="text-align: center">Информация о пользователе</h1>
    <hr>
    <div style="text-align: center">
        <img src="/static/images/avatar.png"
             style="bcat:2px solid black; bcat-radius: 20px" height="120">
        <br>
        <br>
        <h2>${user.name}</h2>
        <br>
        Номер телефона: <b>${user.phone_number}</b>
    </div>
    <br>
    <hr>
    <h1 style="text-align: center">Коты пользователя ${user.name}</h1>
    <br>
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