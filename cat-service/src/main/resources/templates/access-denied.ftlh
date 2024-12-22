<#import "blocks/template.ftlh" as t>
<@t.template user>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-body text-center">
                        <h1 class="display-4">403</h1>
                        <p class="lead">Доступ запрещен</p>
                        <p>У вас недостаточно прав для доступа к этой странице.</p>
                        <a href="/" class="btn btn-primary">Вернуться на главную</a>
                        <#if user.email??>
                            <a href="/profile" class="btn btn-secondary ms-2">Вернуться в профиль</a>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@t.template>