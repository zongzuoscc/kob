<template>
     <div class = "playground">
        <GameMap/>
    </div>
</template>
<script>
import GameMap from '@/components/GameMap.vue';
export default {
    components:{
        GameMap,
    }
}
</script>
<style scoped>
div.playground{
    width: 60vw;
    height: 70vh;
    /* background-color: white; */
    margin: 80px auto;/* 水平居中 */
    border-radius: 10px;/* 圆角效果 */
    /* box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); 添加阴影效果 */
    /* margin-top: auto;顶部外边距 */
}
</style>