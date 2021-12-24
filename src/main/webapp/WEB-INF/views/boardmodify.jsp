<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <title>게시물 작성폼</title>
</head>

<style>

    .layout {
        width : 500px;
        margin : 0 auto;
        margin-top : 40px;
    }

    .layout input {
        width : 100%;
        box-sizing : border-box
    }

    .layout textarea {
        width : 100%;
        margin-top : 10px;
        min-height : 300px;
    }

</style>

<body>
<div class="layout">
    <form th:action="@{/board/update/{postId}}" method="post">
        <input name="title" type="text" th:value="${board.title}">
        <textarea name="content" th:text="${board.content}"></textarea>
        <button type="submit">수정</button>
    </form>
</div>
</body>
</html>