<#import "blocks/template.ftlh" as t>
<@t.template user>
    <div class="container">
        <h1 class="text-center">Редактировать кота</h1>
        <form method="post" action="/product/update/${product.id}" enctype="multipart/form-data">
            <div class="row">
                <div class="col-md-6">
                    <div class="mb-3">
                        <label for="title" class="form-label">Имя кота</label>
                        <input type="text" id="title" name="title" class="form-control" value="${product.title}" required>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label">Описание кота</label>
                        <textarea id="description" name="description" class="form-control" rows="4">${product.description}</textarea>
                    </div>

                    <div class="mb-3">
                        <label for="price" class="form-label">Цена (₽)</label>
                        <input type="number" id="price" name="price" class="form-control" value="${product.price}" required>
                    </div>

                    <div class="mb-3">
                        <label for="city" class="form-label">Город</label>
                        <select id="city" name="city" class="form-control">
                            <#list cities as city>
                                <option value="${city.name}" <#if city.name == product.city>selected</#if>>${city.name}</option>
                            </#list>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="files" class="form-label">Изображения кота</label>
                        <input type="file" id="files" name="files" class="form-control" multiple>
                    </div>

                    <div class="mb-3">
                        <button type="submit" class="btn btn-primary">Сохранить изменения</button>
                    </div>
                </div>

                <div class="col-md-6">
                    <h3>Текущие изображения</h3>
                    <div class="carousel slide" id="imageCarousel" data-bs-ride="carousel">
                        <div class="carousel-inner">
                            <#list product.images as image>
                                <div class="carousel-item <#if image.id == product.previewImageId>active</#if>">
                                    <img src="/images/${image.id}" class="d-block w-100" alt="Изображение товара" style="object-fit: contain;">
                                    <div class="carousel-caption d-none d-md-block">
                                        <button type="button" class="btn btn-danger" onclick="deleteImage(${image.id})">Удалить изображение</button>
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
        function deleteImage(imageId) {
            if (confirm("Вы уверены, что хотите удалить это изображение?")) {
                // AJAX request or form submission to delete the image
                window.location.href = '/product/deleteImage/${product.id}/' + imageId;
            }
        }
    </script>
</@t.template>
