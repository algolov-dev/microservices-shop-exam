<#macro template user>
    <!doctype html>
    <html lang="ru">
    <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit-no">
            <link rel="stylesheet" href="/static/css/style.css">

            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
                  integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                  crossorigin="anonymous">

            <title> Digital Cat Shop </title>
            <link rel="icon" type="image/x-icon" href="/static/favicon.ico">
        </head>
        <body>
        <nav class="navbar navbar-dark bg-primary">
            <div class="container-fluid">
                <a href="/">
                    <span class="navbar-brand mb-0 h1 ms-5"> Магазин цифровых изображений котов </span>
                </a>
                <#if user.email??>
                <div class="dropdown text-end">
                    <a href="/profile">
                        <img src="/static/images/avatar.png" alt="mdo"
                             width="32" height="32" class="rounded-circle">
                    </a>
                </div>
                <#else>
                <button class="btn btn-primary my-2 my-sm-0 fw-bold me-2" type="button"
                        onclick="window.location.href = '/login'">
                    Войти
                </button>
            </#if>
            </div>
        </nav>

        <div class="container mt-4">
            <#nested/>
        </div>

        <script src="https://code.jquery.com/jquery-3.7.1.slim.js"
                integrity="sha256-UgvvN8vBkgO0luPSUl2s8TIlOSYRoGFAX4jlCIm9Adc="
                crossorigin="anonymous"></script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
                crossorigin="anonymous"></script>
    </body>
    </html>
</#macro>

