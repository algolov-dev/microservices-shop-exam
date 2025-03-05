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

    <!-- Секции управления тегами и категориями размещены рядом -->
    <div class="container mt-5">
        <div class="row">
            <!-- Управление тегами -->
            <div class="col-md-6">
                <h4 class="mb-4">Управление тегами</h4>
                <!-- Форма для добавления нового тега -->
                <form method="post" action="/admin/tags/add" class="mb-3">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <div class="input-group">
                        <input type="text" name="name" class="form-control" placeholder="Введите название тега" required>
                        <button class="btn btn-success" type="submit">Добавить тег</button>
                    </div>
                </form>
                <!-- Таблица со списком тегов -->
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead class="table-primary">
                        <tr>
                            <th>ID</th>
                            <th>Название</th>
                            <th>Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if tags?has_content>
                            <#list tags as tag>
                                <tr id="tag-row-${tag.id}">
                                    <td>${tag.id}</td>
                                    <td>
                                        <span class="tag-name-display">${tag.name}</span>
                                        <input type="text" class="form-control tag-name-input" style="display: none;" value="${tag.name}">
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-primary btn-sm edit-tag-btn" data-tag-id="${tag.id}">Редактировать</button>
                                        <button type="button" class="btn btn-success btn-sm save-tag-btn" style="display: none;" data-tag-id="${tag.id}">Сохранить</button>
                                        <form action="/admin/tags/delete/${tag.id}" method="post" class="d-inline">
                                            <input type="hidden" name="_csrf" value="${_csrf.token}">
                                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Удалить тег?');">Удалить</button>
                                        </form>
                                    </td>
                                </tr>
                            </#list>
                        <#else>
                            <tr>
                                <td colspan="3" class="text-center">Тегов нет</td>
                            </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Управление категориями -->
            <div class="col-md-6">
                <h4 class="mb-4">Управление категориями</h4>
                <!-- Форма для добавления новой категории -->
                <form method="post" action="/admin/categories/add" class="mb-3">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <div class="input-group">
                        <input type="text" name="category_name" class="form-control" placeholder="Введите название категории" required>
                        <button class="btn btn-success" type="submit">Добавить категорию</button>
                    </div>
                </form>
                <!-- Таблица со списком категорий -->
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead class="table-primary">
                        <tr>
                            <th>ID</th>
                            <th>Название категории</th>
                            <th>Действия</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#if categories?has_content>
                            <#list categories as category>
                                <tr id="category-row-${category.id}">
                                    <td>${category.id}</td>
                                    <td>
                                        <span class="category-name-display">${category.category_name}</span>
                                        <input type="text" class="form-control category-name-input" style="display:none;" value="${category.category_name}">
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-primary btn-sm edit-category-btn" data-category-id="${category.id}">Редактировать</button>
                                        <button type="button" class="btn btn-success btn-sm save-category-btn" style="display:none;" data-category-id="${category.id}">Сохранить</button>
                                        <form action="/admin/categories/delete/${category.id}" method="post" class="d-inline">
                                            <input type="hidden" name="_csrf" value="${_csrf.token}">
                                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Удалить категорию?');">Удалить</button>
                                        </form>
                                    </td>
                                </tr>
                            </#list>
                        <#else>
                            <tr>
                                <td colspan="3" class="text-center">Категорий нет</td>
                            </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script>
        // Обработчик для кнопок "Редактировать" тегов
        document.querySelectorAll('.edit-tag-btn').forEach(function(button) {
            button.addEventListener('click', function() {
                var tagId = button.getAttribute('data-tag-id');
                var row = document.getElementById('tag-row-' + tagId);
                row.querySelector('.tag-name-display').style.display = 'none';
                row.querySelector('.tag-name-input').style.display = 'inline-block';
                button.style.display = 'none';
                row.querySelector('.save-tag-btn').style.display = 'inline-block';
            });
        });

        // Обработчик для кнопок "Сохранить" тегов
        document.querySelectorAll('.save-tag-btn').forEach(function(button) {
            button.addEventListener('click', function() {
                var tagId = button.getAttribute('data-tag-id');
                var row = document.getElementById('tag-row-' + tagId);
                var newName = row.querySelector('.tag-name-input').value;
                var csrfToken = document.querySelector('input[name="_csrf"]').value;

                fetch('/admin/tags/edit', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'X-CSRF-TOKEN': csrfToken
                    },
                    body: new URLSearchParams({
                        'tagId': tagId,
                        'name': newName
                    })
                })
                    .then(function(response) {
                        if (response.ok) {
                            row.querySelector('.tag-name-display').textContent = newName;
                            row.querySelector('.tag-name-display').style.display = 'inline';
                            row.querySelector('.tag-name-input').style.display = 'none';
                            button.style.display = 'none';
                            row.querySelector('.edit-tag-btn').style.display = 'inline-block';
                        } else {
                            alert("Ошибка при обновлении тега");
                        }
                    })
                    .catch(function(error) {
                        console.error(error);
                        alert("Ошибка при обновлении тега");
                    });
            });
        });

        // Обработчики для редактирования категорий
        document.querySelectorAll('.edit-category-btn').forEach(function(button) {
            button.addEventListener('click', function() {
                var categoryId = button.getAttribute('data-category-id');
                var row = document.getElementById('category-row-' + categoryId);
                row.querySelector('.category-name-display').style.display = 'none';
                row.querySelector('.category-name-input').style.display = 'inline-block';
                button.style.display = 'none';
                row.querySelector('.save-category-btn').style.display = 'inline-block';
            });
        });

        document.querySelectorAll('.save-category-btn').forEach(function(button) {
            button.addEventListener('click', function() {
                var categoryId = button.getAttribute('data-category-id');
                var row = document.getElementById('category-row-' + categoryId);
                var newName = row.querySelector('.category-name-input').value;
                var csrfToken = document.querySelector('input[name="_csrf"]').value;

                fetch('/admin/categories/edit', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'X-CSRF-TOKEN': csrfToken
                    },
                    body: new URLSearchParams({
                        'categoryId': categoryId,
                        'category_name': newName
                    })
                })
                    .then(function(response) {
                        if (response.ok) {
                            row.querySelector('.category-name-display').textContent = newName;
                            row.querySelector('.category-name-display').style.display = 'inline';
                            row.querySelector('.category-name-input').style.display = 'none';
                            button.style.display = 'none';
                            row.querySelector('.edit-category-btn').style.display = 'inline-block';
                        } else {
                            alert("Ошибка при обновлении категории");
                        }
                    })
                    .catch(function(error) {
                        console.error(error);
                        alert("Ошибка при обновлении категории");
                    });
            });
        });
    </script>

</@t.template>
