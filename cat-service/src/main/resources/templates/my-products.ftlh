<#import "blocks/template.ftlh" as t>
<@t.template user>
    <h1 style="text-align: center">Мои Изображения</h1>
    <hr>
    <a class="btn btn-primary mt-3 mb-3" style="text-align: center; width: 100%"
       data-bs-toggle="collapse" href="#collapseExample" role="button"
       aria-expanded="true" aria-controls="collapseExample">
        Добавить
    </a>
    <div class="collapse mb-3" id="collapseExample">
        <form action="/product/create" method="post" enctype="multipart/form-data" id="productForm">
            <div class="form-group">
                <label for="productName">Название</label>
                <input type="text" required class="form-control" id="productName" name="title">
            </div>
            <div class="form-group mt-3">
                <label for="price">Цена</label>
                <div class="input-group mb-3">
                    <input type="number" required class="form-control" id="price" name="price">
                    <div class="input-group-append">
                        <span class="input-group-text">₽</span>
                    </div>
                </div>
            </div>
            <div class="form-group col-md-2">
                <label for="searchCategory">Категория</label>
                <select name="searchCategory" id="searchCategory" class="form-control">
                    <option value="" <#if !searchCategory?? || searchCategory == "">selected</#if>>Выберите категорию</option>
                    <#list categories as category>
                        <option value="${category.category_name}" <#if searchCategory == category.category_name>selected</#if>>${category.category_name}</option>
                    </#list>
                </select>
            </div>
            <div class="form-group mt-3">
                <label for="description">Описание</label>
                <textarea required class="form-control" id="description" name="description"></textarea>
            </div>
            <div class="form-group mt-3">
                <label for="files">Изображения (от 1 до 10)</label>
                <input type="file" id="files" name="files" class="form-control" accept="image/*" multiple required>
                <small class="form-text text-muted">Вы можете загрузить до 10 изображений.</small>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>

            <div class="form-group mt-3">
                <label for="tagInput">Добавить тег</label>
                <div class="input-group">
                    <input type="text" id="tagInput" class="form-control" placeholder="Введите тег">
                    <button type="button" id="addTagBtn" class="btn btn-outline-secondary">Добавить тег</button>
                </div>
                <div id="tagList" class="mt-2">

                </div>
            </div>

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
                    </div>
                </div>
            </a>
        <#else>
            <h1 style="text-align: center">Не найдено</h1>
        </#list>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const tagInput = document.getElementById('tagInput');
            const addTagBtn = document.getElementById('addTagBtn');
            const tagList = document.getElementById('tagList');
            const productForm = document.getElementById('productForm');

            function getCurrentTags() {
                const hiddenInputs = document.querySelectorAll('#tagList input[name="tagNames[]"]');
                let tags = [];
                hiddenInputs.forEach(input => {
                    tags.push(input.value);
                });
                return tags;
            }

            addTagBtn.addEventListener('click', function () {
                const tagValue = tagInput.value.trim();
                if (tagValue !== '') {
                    const tagContainer = document.createElement('span');
                    tagContainer.className = "badge bg-secondary me-1";
                    tagContainer.style.cursor = "pointer";
                    tagContainer.style.padding = "5px";
                    tagContainer.style.fontSize = "0.9em";
                    tagContainer.textContent = tagValue;

                    const hiddenInput = document.createElement('input');
                    hiddenInput.type = 'hidden';
                    hiddenInput.name = 'tagNames[]';
                    hiddenInput.value = tagValue;
                    tagContainer.appendChild(hiddenInput);

                    tagContainer.addEventListener('click', function () {
                        tagContainer.remove();
                    });

                    tagList.appendChild(tagContainer);
                    tagInput.value = '';

                    console.log('Тег добавлен, текущие теги:', getCurrentTags());
                }
            });

            function validateFiles() {
                const filesInput = document.getElementById('files');
                if (filesInput.files.length < 1 || filesInput.files.length > 10) {
                    alert("Выберите от 1 до 10 изображений.");
                    return false;
                }

                for (let i = 0; i < filesInput.files.length; i++) {
                    if (!filesInput.files[i].type.startsWith("image/")) {
                        alert("Можно загружать только изображения.");
                        return false;
                    }
                }
                return true;
            }

            productForm.addEventListener('submit', function (event) {
                event.preventDefault();

                if (!validateFiles()) {
                    return;
                }

                const formData = new FormData();

                formData.append('title', document.getElementById('productName').value);
                formData.append('price', document.getElementById('price').value);
                formData.append('searchCategory', document.getElementById('searchCategory').value);
                formData.append('description', document.getElementById('description').value);

                const filesInput = document.getElementById('files');
                if (filesInput.files.length > 0) {
                    formData.append('files', filesInput.files[0]);
                }

                const tagInputs = document.querySelectorAll('#tagList input[name="tagNames[]"]');
                const tagValues = Array.from(tagInputs).map(input => input.value);
                formData.append('tagNames', JSON.stringify(tagValues, null, 2));


                formData.append('_csrf', document.querySelector('input[name="_csrf"]').value);

                const formValues = {};
                for (const [key, value] of formData.entries()) {
                    formValues[key] = value;
                }
                console.log("Данные формы перед отправкой:", formValues);

                fetch('/product/create', {
                    method: 'POST',
                    body: formData,
                })
                    .then(response => {
                        if (response.ok) {
                            window.location.href = '/my/products';
                        } else {
                            console.error('Ошибка при отправке формы:', response);
                            alert('Ошибка при создании продукта. Пожалуйста, попробуйте позже.');
                        }
                    })
                    .catch(error => {
                        console.error('Ошибка сети:', error);
                        alert('Ошибка сети. Пожалуйста, проверьте ваше соединение с интернетом.');
                    });
            });
        });
    </script>
</@t.template>