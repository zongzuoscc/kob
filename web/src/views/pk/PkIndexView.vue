<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'"/>
    <MatchGround v-if="$store.state.pk.status === 'matching'"/>
</template>

<script>
import PlayGround from '@/components/PlayGround.vue';
import { onMounted,onUnmounted } from 'vue';
import { useStore } from 'vuex';
import MatchGround from '@/components/MatchGround.vue';
export default {
    components: {
        MatchGround,
        PlayGround,
    },
    setup() {
        const store = useStore();

        const socketUrl = `ws://localhost:3000/websocket/${store.state.user.token}`;

        let socket = null;
        //当当前组件被挂载的时候
        onMounted(() => {
            store.commit("updateOpponent",{
                username: "待战强敌",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })

            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connected!");
                store.commit("updateSocket",socket);
            };

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if(data.event === "start-matching"){ //匹配成功
                    store.commit("updateOpponent",{
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    })
                    setTimeout(()=>{
                        alert("匹配成功！对手：" + data.opponent_username);
                        store.commit("updatePkStatus","playing");
                    },1000);
                }
            };
            socket.onclose = () =>{
                console.log("disconnected!");
            }
        });

        onUnmounted(()=>{
            socket.close();
            store.commit("updateStatus","matching");
        })
    },
};
</script>

<style scoped>

</style>