<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Авторизация</h1>
    <hr>
    <#if error??>
        <div class="alert alert-danger" role="alert">
            Неверный email и/или пароль
        </div>
    </#if>
    <form action="/login" method="post">
        <div class="form-group">
            <label for="email">Электронная почта</label>
            <input type="email" class="form-control <#if emailError??>is-invalid</#if>" id="email" name="username" value="${username!}">
        </div>
        <div class="form-group mt-3">
            <label for="password">Пароль</label>
            <input type="password" class="form-control <#if passwordError??>is-invalid</#if>" id="password" name="password">
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" style="width: 100%" class="btn btn-primary mt-3">Войти</button>
    </form>
    <hr>
    <h2><a href="/registration">Зарегистрироваться</a></h2>
</@t.template>