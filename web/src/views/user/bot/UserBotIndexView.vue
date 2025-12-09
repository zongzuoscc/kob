<template>
    <ContentField>
        <h1>我的Bot</h1>
        <p>在这里你可以查看和管理你所有的Bot。</p>
    </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
import { useStore } from 'vuex';
export default {
    components: {
        ContentField,
    },
    setup(){
        const store = useStore();
        // $ .ajax({
        //     url: 'http://localhost:3000/user/bot/add',
        //     type: 'POST',
        //     data:{
        //         title: "bot的标题",
        //         description: "bot的描述",
        //         content: "bot的代码",
        //     },
        //     headers: {
        //         'Authorization': 'Bearer ' + store.state.user.token,
        //     },
        //     success: (resp) => {
        //         console.log(resp);
        //     },
        //     error: (resp) => {
        //         console.log(resp);
        //     }
        // })

        // $ .ajax({
        //     url: 'http://localhost:3000/user/bot/remove',
        //     type: 'POST',
        //     data:{
        //         bot_id: 1, 
        //     },
        //     headers: {
        //         'Authorization': 'Bearer ' + store.state.user.token,
        //     },
        //     success: (resp) => {
        //         console.log(resp);
        //     },
        //     error: (resp) => {
        //         console.log(resp);
        //     }
        // })
        // $ .ajax({
        //     url: 'http://localhost:3000/user/bot/update',
        //     type: 'POST',
        //     data:{
        //         bot_id: 1, 
        //         title: "bot标题",
        //         description: "bot描述",
        //         content: "bot代码",
        //     },
        //     headers: {
        //         'Authorization': 'Bearer ' + store.state.user.token,
        //     },
        //     success: (resp) => {
        //         console.log(resp);
        //     },
        //     error: (resp) => {
        //         console.log(resp);
        //     }
        // })
        $ .ajax({
            url: 'http://localhost:3000/user/bot/getlist',
            type: 'get',
            headers: {
                'Authorization': 'Bearer ' + store.state.user.token,
            },
            success: (resp) => {
                console.log(resp);
            },
            error: (resp) => {
                console.log(resp);
            }
        })
    }
};
</script>

<style scoped>

</style>