<#import "blocks/template.ftlh" as t>
<@t.template user>
    <div class="container">
        <div class="row">
            <div class="col-md-6">
                <div id="myCarousel" class="carousel slide">
                    <div class="carousel-inner">
                        <#list images as image>
                            <div class="carousel-item <#if image.previewImage>active</#if>" data-bs-slide-to="${image_index}">
                                <div class="carousel-item-wrapper" style="position: relative; width: 100%; height: 0; padding-bottom: 56.25%;">
                                    <img src="/images/${image.id}" class="d-block w-100 h-100" alt="Изображение товара" style="object-fit: contain; position: absolute; top: 0; left: 0;">
                                </div>
                            </div>
                        </#list>
                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#myCarousel" data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Предыдущий</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#myCarousel" data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Следующий</span>
                    </button>
                </div>

                <div class="row mt-4">
                    <#list images as image>
                        <div class="col mb-2">
                            <img src="/images/${image.id}" data-bs-target="#myCarousel" data-bs-slide-to="${image_index}" class="img-fluid preview-image max-image-size" alt="Предпросмотр изображения" style="object-fit: cover; cursor: pointer;">
                        </div>
                    </#list>
                </div>

                <div class="text-center" style="margin: 0;">
                    <#if product.tags?has_content>
                        <#list product.tags as tag>
                            <span class="badge bg-secondary me-1">${tag.name}</span>
                        </#list>
                    <#else>
                        <span class="text-muted">Нет тегов</span>
                    </#if>
                </div>

            </div>

            <div class="col-md-6">
                <hr>
                <h1 class="text-center">${product.title} | ${product.price} ₽</h1>
                <p class="text-center">${product.description}</p>

                <#if user.isActive()>
                    <#if user.isAdmin() || (user?? && product.user.id == user.id)>
                        <div class="text-center mt-3">
                            <form method="post" action="/product/delete/${product.id}" class="d-inline">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <button type="submit" class="btn btn-danger btn-sm">Удалить</button>
                            </form>
                            <a href="/product/edit/${product.id}" class="btn btn-warning btn-sm d-inline ms-2">Изменить</a>
                        </div>
                    </#if>
                </#if>

                <hr>
                <p class="text-center">
                    <a href="/user/${authorProduct.id}" class="d-inline-flex align-items-center text-decoration-none">
                        <img src="/static/images/avatar.png" alt="${authorProduct.name}" width="32" height="32" class="rounded-circle me-2">
                        ${authorProduct.name}
                    </a>
                </p>

                <#if user?? && user.isActive()>
                    <form method="post" action="/cat/create">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <input type="hidden" name="productId" value="${product.id}" />
                        <div class="text-center mt-4">
                            <button type="submit" class="btn btn-primary">Связаться с продавцом</button>
                        </div>
                    </form>
                </#if>
            </div>
        </div>
    </div>

    <script>
        const previewImages = document.querySelectorAll('.preview-image');
        previewImages.forEach(image => {
            image.addEventListener('click', function() {
                const slideIndex = this.getAttribute('data-bs-slide-to');
                const carousel = new bootstrap.Carousel(document.getElementById('myCarousel'));
                carousel.to(slideIndex);
            });
        });
    </script>
</@t.template>
