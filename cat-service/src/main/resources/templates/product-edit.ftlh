<#import "blocks/template.ftlh" as t>
<@t.template user>
    <div class="container">
        <h1 class="text-center">Редактировать</h1>
        <form method="post" action="/product/update/${product.id}" enctype="multipart/form-data" id="productForm">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <div class="row">
                <!-- Левая колонка с формой редактирования -->
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="title" class="form-label">Название</label>
                        <input type="text" id="title" name="title" class="form-control" value="${product.title}" required>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Описание</label>
                        <textarea id="description" name="description" class="form-control" rows="4">${product.description}</textarea>
                    </div>

                    <div class="mb-3">
                        <label for="price" class="form-label">Цена (₽)</label>
                        <input type="number" id="price" name="price" class="form-control" value="${product.price}" required>
                    </div>

                    <div class="mb-3">
                        <label for="category" class="form-label">Категория</label>
                        <select id="category" name="category" class="form-control">
                            <#list categories as category>
                                <option value="${category.category_name}" <#if category.category_name == product.category>selected</#if>>${category.category_name}</option>
                            </#list>
                        </select>
                    </div>

                    <!-- Объединённый блок для старых и новых тегов -->
                    <div class="mb-3">
                        <label>Теги:</label>
                        <div class="input-group">
                            <input type="text" id="tagInput" class="form-control" placeholder="Введите тег">
                            <button type="button" id="addTagBtn" class="btn btn-outline-secondary">Добавить тег</button>
                        </div>
                        <div id="tagList" class="mt-2">
                            <#if product.tags?has_content>
                                <#list product.tags as tag>
                                    <span class="badge bg-secondary me-1" style="cursor: pointer; padding: 5px; font-size: 0.9em;">
                                        ${tag.name}
                                        <input type="hidden" name="tagNames[]" value="${tag.name}">
                                    </span>
                                </#list>
                            </#if>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="files" class="form-label">Изображения</label>
                        <input type="file" id="files" name="files" class="form-control" multiple>
                    </div>

                    <div class="mb-3">
                        <button type="submit" class="btn btn-primary">Сохранить изменения</button>
                    </div>
                </div>

                <!-- Правая колонка с просмотром изображений -->
                <div class="col-md-6">
                    <h3>Текущие изображения</h3>
                    <div id="imageCarousel" class="carousel slide" data-bs-ride="carousel">
                        <div class="carousel-inner">
                            <#list product.images as image>
                                <div class="carousel-item <#if image?index == 0>active</#if>" data-bs-slide-to="${image?index}">
                                    <div class="carousel-item-wrapper" style="position: relative; width: 100%; height: 0; padding-bottom: 50%;">
                                        <img src="/images/${image.id}" class="d-block w-100 h-100" alt="Изображение товара"
                                             style="object-fit: contain; position: absolute; top: 0; left: 0;">
                                        <#if product.images?size gt 1>
                                            <div style="position: absolute; bottom: 10px; left: 50%; transform: translateX(-50%);">
                                                <button type="button" class="btn btn-danger btn-sm" onclick="deleteImage(${image.id})">Удалить</button>
                                            </div>
                                        </#if>
                                    </div>
                                </div>
                            </#list>
                        </div>
                        <button class="carousel-control-prev" type="button" data-bs-target="#imageCarousel" data-bs-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Предыдущее</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#imageCarousel" data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Следующее</span>
                        </button>
                    </div>

                </div>
            </div>
        </form>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const tagInput = document.getElementById('tagInput');
            const addTagBtn = document.getElementById('addTagBtn');
            const tagList = document.getElementById('tagList');
            const productForm = document.getElementById('productForm');

            // Назначаем обработчик для уже существующих тегов
            document.querySelectorAll('#tagList span').forEach(function(tagContainer) {
                tagContainer.addEventListener('click', function () {
                    tagContainer.remove();
                });
            });

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

                    // Обработчик удаления тега по клику
                    tagContainer.addEventListener('click', function () {
                        tagContainer.remove();
                    });

                    tagList.appendChild(tagContainer);
                    tagInput.value = '';

                    console.log('Тег добавлен, текущие теги:', getCurrentTags());
                }
            });

            function getCurrentTags() {
                const hiddenInputs = document.querySelectorAll('#tagList input[name="tagNames[]"]');
                let tags = [];
                hiddenInputs.forEach(input => {
                    tags.push(input.value);
                });
                return tags;
            }

            productForm.addEventListener('submit', function (event) {
                event.preventDefault();

                const formData = new FormData(productForm);

                // Собираем значения тегов и преобразуем в JSON-строку
                const tagInputs = document.querySelectorAll('#tagList input[name="tagNames[]"]');
                const tagValues = Array.from(tagInputs).map(input => input.value);
                formData.set('tagNames', JSON.stringify(tagValues, null, 2));

                // Выводим данные формы в консоль для отладки
                const formValues = {};
                for (const [key, value] of formData.entries()) {
                    formValues[key] = value;
                }
                console.log("Данные формы перед отправкой:", formValues);

                fetch(productForm.action, {
                    method: 'POST',
                    body: formData
                })
                    .then(response => {
                        if (response.ok) {
                            window.location.href = '/my/products';
                        } else {
                            console.error('Ошибка при отправке формы:', response);
                            alert('Ошибка при обновлении продукта. Пожалуйста, попробуйте позже.');
                        }
                    })
                    .catch(error => {
                        console.error('Ошибка сети:', error);
                        alert('Ошибка сети. Пожалуйста, проверьте ваше соединение с интернетом.');
                    });
            });

        });

        function deleteImage(imageId) {
            if (confirm("Вы уверены, что хотите удалить это изображение?")) {
                window.location.href = '/product/deleteImage/${product.id}/' + imageId;
            }
        }

        const previewImages = document.querySelectorAll('.preview-image');
        previewImages.forEach(image => {
            image.addEventListener('click', function() {
                const slideIndex = this.getAttribute('data-bs-slide-to');
                const carousel = new bootstrap.Carousel(document.getElementById('imageCarousel'));
                carousel.to(slideIndex);
            });
        });
    </script>
</@t.template>
