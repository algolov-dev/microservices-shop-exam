<#import "blocks/template.ftlh" as t>
<@t.template user>
    <div class="container mt-5">
        <h4 class="mb-4 text-center">Панель администратора</h4>
        <div class="table-responsive">
            <table class="table table-striped table-hover table-bcated">
                <thead class="table-primary">
                <tr>
                    <th scope="col">Email</th>
                    <th scope="col">Номер телефона</th>
                    <th scope="col">Активность</th>
                    <th scope="col">Роли</th>
                    <th scope="col">Бан</th>
                    <th scope="col">Редактирование</th>
                    <th scope="col">Подробная информация</th>
                </tr>
                </thead>
                <tbody>
                <#if users?has_content>
                    <#list users as user>
                        <tr>
                            <td>${user.email}</td>
                            <td>${user.phone_number}</td>
                            <td>
                                    <span class="badge ${user.active?string('bg-success', 'bg-secondary')}">
                                        ${user.active?string("Активен", "Неактивен")}
                                    </span>
                            </td>
                            <td>
                                <#list user.roles as role>
                                    <span class="badge bg-info text-dark me-1">${role}</span>
                                </#list>
                            </td>
                            <td>
                                <#if user.active>
                                    <form action="/admin/user/ban/${user.id}" method="post" class="d-inline">
                                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Вы уверены, что хотите забанить этого пользователя?');">Бан</button>
                                    </form>
                                <#else>
                                    <form action="/admin/user/ban/${user.id}" method="post" class="d-inline">
                                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                                        <button type="submit" class="btn btn-success btn-sm" onclick="return confirm('Вы уверены, что хотите разбанить этого пользователя?');">Разбанить</button>
                                    </form>
                                </#if>
                            </td>
                            <td>
                                <a href="/admin/user/edit/${user.id}" class="btn btn-primary btn-sm">Редактировать</a>
                            </td>
                            <td>
                                <a href="/user/${user.id}" class="btn btn-secondary btn-sm">Подробнее</a>
                            </td>
                        </tr>
                    </#list>
                <#else>
                    <tr>
                        <td colspan="7" class="text-center">Пользователей нет</td>
                    </tr>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</@t.template>
