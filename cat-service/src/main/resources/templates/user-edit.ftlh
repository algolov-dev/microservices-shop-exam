<#import "blocks/template.ftlh" as t>
<@t.template userByPrincipal>
    <h1 style="text-align: center">Редактирование пользователя ${user.email}</h1>
    <hr>
    <form action="/admin/user/edit" method="post">

        <div class="form-group">
            <label for="name">Имя:</label>
            <input type="text" id="name" name="name" class="form-control" value="${user.name}">
        </div>

        <div class="form-group">
            <label for="phoneNumber">Номер телефона:</label>
            <input type="text" id="phone_number" name="phone_number" class="form-control" value="${user.phone_number}">
        </div>

        <div class="form-group">
            <label>Роли:</label>
            <#list roles as role>
                <div>
                    <label>
                        <input type="radio" name="role" value="${role}" <#if user.roles?seq_contains(role)>checked</#if>> ${role}
                    </label>
                </div>
            </#list>
        </div>

        <input type="hidden" value="${user.id}" name="userId">
        <input type="hidden" value="${_csrf.token}" name="_csrf">

        <button type="submit" class="btn btn-dark">Сохранить</button>
    </form>

</@t.template>
