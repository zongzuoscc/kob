<template>
    <div class="container content-field">
        <div class="card">
            <div class="card-body">
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "ContentField"
}
</script>

<style scoped>
div.content-field {
    margin-top: 20px;
}

/* ⬇️ 修复点2：直接在这里定义全局的黑色卡片风格 */
div.card {
    /* 统一设为黑色半透明背景 */
    background-color: #212529; 
    /* 统一文字颜色为白色 */
    color: white;
    /* 稍微调整边框颜色，让它和黑底更搭 */
    border: 1px solid #444;
}
</style>