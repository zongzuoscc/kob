<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="confirmedpassword" class="form-label">确认密码</label>
                        <input v-model="confirmedpassword" type="password" class="form-control" id="confirmedpassword" placeholder="请确认密码">
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
import { ref } from 'vue';
// import { useStore } from 'vuex';
import router from '@/router/index';
import $ from 'jquery';


export default {
  components: {
    ContentField,
  },
  setup(){
    // const store = useStore();
    let username = ref('');
    let password = ref('');
    let confirmedpassword = ref('');
    let error_message = ref('');

    const register = () =>{
      $.ajax({
        url: "http://127.0.0.1:3000/api/user/account/register",
        type: "POST",
        data: {
          username: username.value,
          password: password.value,
          confirmedPassword: confirmedpassword.value,
        },
        success(resp){
          console.log(resp);
          if(resp.success){
            router.push({ name: 'user_account_login' });
          }else{
            error_message.value = resp.error_message;
          }
        },
        error(resp){
          console.log(resp);
        }
      })
    }

    return {
        username,
        password,
        confirmedpassword,
        error_message,
        register,
    }
  }
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
