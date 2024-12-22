<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Регистрация</h1>
    <form action="/registration" method="post">
        <div class="form-group mt-3">
            <label for="name">Имя пользователя</label>
            <input type="text" class="form-control" required id="name" name="name">
        </div>
        <div class="form-group mt-3">
            <label for="email">Электронная почта</label>
            <input type="email" class="form-control" id="email" required name="email">
        </div>
        <div class="form-group mt-3">
            <label for="phone_number">Номер телефона</label>
            <input type="text" class="form-control" id="phone_number" required name="phone_number">
        </div>
        <div class="form-group  mt-3">
            <label for="password">Пароль</label>
            <input type="password" class="form-control" id="password" required name="password">
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" style="width: 100%" class="btn btn-primary mt-3">Зарегистрироваться</button>
    </form>
    <#if errorMessage??>
        <h2 style="color:red">${errorMessage}</h2>
    </#if>
</@t.template>