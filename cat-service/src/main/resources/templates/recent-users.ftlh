<#import "blocks/template.ftlh" as t>
<@t.template user>

    <h1 style="text-align: center">Последние зарегистрированные пользователи</h1>
    <hr>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>#</th>
            <th>Email</th>
            <th>Имя</th>
            <th>Дата регистрации</th>
            <th>Последнее обновление</th>
        </tr>
        </thead>
        <tbody>
        <#list recentUsers as user>
            <tr>
                <td>${user.id}</td>
                <td>${user.email}</td>
                <td>${user.name!'-'}</td>
                <td>${user.registeredAt}</td>
                <td>${user.updatedAt}</td>
            </tr>
        </#list>
        </tbody>
    </table>

    <a href="/" class="btn btn-primary">Назад</a>

</@t.template>
