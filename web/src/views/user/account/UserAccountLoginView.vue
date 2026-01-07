<template>
    <ContentField v-if="!$store.state.user.pulling_info">
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="error_message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import { useStore } from 'vuex';
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';

export default {
    components: {
        ContentField,
    },
    setup() {
        const store = useStore();
        const router = useRouter();
        const route = useRoute();

        let username = ref('');
        let password = ref('');
        let error_message = ref('');

        const jwt_token = localStorage.getItem("jwt_token");
        if (jwt_token) {
            store.commit("updateToken", jwt_token);
            store.dispatch("getinfo", {
                success() {
                    // 自动登录成功跳转
                    if (route.query.redirect) {
                        router.push(route.query.redirect);
                    } else {
                        router.push({ name: 'home' });
                    }
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        } else {
            store.commit("updatePullingInfo", false);
        }

        const login = () => {
            error_message.value = "";
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                // 修改点：去掉了未使用的 resp 参数，避免 ESLint 报错
                success: () => {
                    store.dispatch("getinfo", {
                        success() {
                            // 手动登录成功跳转
                            if (route.query.redirect) {
                                router.push(route.query.redirect);
                            } else {
                                router.push({ name: 'home' });
                            }
                        },
                        // 修改点：补充 error 回调，防止 getinfo 失败时 store 报错
                        error() {
                            error_message.value = "系统异常，请稍后重试";
                        }
                    });
                },
                error: () => {
                    error_message.value = "用户名或密码错误";
                }
            })
        }

        return {
            username,
            password,
            error_message,
            login,
        }
    },
};
</script>

<style scoped>
button {
    width: 100%;
}

div.error_message {
    color: red;
    margin-bottom: 10px;
}
</style>