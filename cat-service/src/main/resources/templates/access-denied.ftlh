<#import "blocks/template.ftlh" as t>
<@t.template user>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card mb-4">
                    <div class="card-body text-center">
                        <a href="/" class="btn btn-primary">Вернуться на главную</a>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        Последние пользователи(Обновлено ${recentUsers[0].updatedAt})
                    </div>
                    <div class="card-body">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>User ID</th>
                                <th>Email</th>
                                <th>Имя</th>
                            </tr>
                            </thead>
                            <tbody>
                                <#list recentUsers as recentUser>
                                    <tr>
                                        <td>${recentUser.id}</td>
                                        <td>${recentUser.userId}</td>
                                        <td>${recentUser.email}</td>
                                        <td>${recentUser.name!'-'}</td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@t.template>
